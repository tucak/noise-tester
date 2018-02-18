package instrumentation.agent;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import instrumentation.FactoryClassAdapter;
import instrumentation.InstrumentationFactory;
import instrumentation.avio.AvioInstrumentation;
import instrumentation.concurpairs.ConcurPairsMetric;
import instrumentation.dupairs.DUPairsInstrumentation;
import instrumentation.ereaser.EreaserInstrumentation;
import instrumentation.goodlock.GoodLockInstrumentation;
import instrumentation.inject.InjectableMethodsInjector;
import instrumentation.inject.Injector;
import instrumentation.objectid.ObjectIdInstrumentation;
import instrumentation.sync.SyncInstrumentation;
import instrumentation.threadid.ThreadInstrumentation;

public class Agent {
	public static List<Rule> rules = new ArrayList<>();

	public static void premain(String agentArgs, Instrumentation inst) throws IOException {
		if (agentArgs == null) {
			throw new IllegalArgumentException("No rulefile specified in agentArgs!");
		}

		loadRules(agentArgs);

		inst.addTransformer((ClassLoader loader, String className, Class<?> classBeingRedefined,
				ProtectionDomain protectionDomain, byte[] classfileBuffer) -> {
			if (className == null)
				return classfileBuffer;
			for (Rule rule : rules) {
				if (className.matches(rule.getKey())) {
					ClassReader cr = new ClassReader(classfileBuffer);
					ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
					ClassVisitor cv = new FactoryClassAdapter(cw, rule.getMethodVisitor());
					cr.accept(cv, 0);
					classfileBuffer = cw.toByteArray();
				}
			}
			return classfileBuffer;

		});
	}

	public static void main(final String args[]) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: Agent [rulefile] [classfiles]");
		}

		loadRules(args[0]);

		for (int i = 1; i < args.length; ++i) {
			FileInputStream is = new FileInputStream(args[i]);
			byte[] b;

			for (Rule rule : rules) {
				if (args[i].replaceFirst("[.][^.]+$", "").matches(rule.getKey())) {
					ClassReader cr = new ClassReader(is);
					ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
					ClassVisitor cv = new FactoryClassAdapter(cw, rule.getMethodVisitor());
					cr.accept(cv, 0);
					b = cw.toByteArray();

					FileOutputStream fos = new FileOutputStream(args[i]);
					fos.write(b);
					fos.close();
				}
			}
		}
	}

	private static void loadRules(String filePath) throws IOException {
		// Load instrumentation rules.
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			stream.forEach(Agent::parseLine);
		}
	}

	private static void parseLine(String line) {
		line = line.trim();

		// Empty or comment
		if (line.isEmpty() || line.charAt(0) == '#') {
			return;
		}

		String[] parts = line.split("%");

		if (parts.length != 2) {
			throw new RuntimeException("Malformed line: too many/few parts '" + line + "'");
		}

		String pattern = parts[0].trim();

		String description = parts[1].trim();

		if (description.isEmpty()) {
			return;
		}

		rules.add(new Rule(pattern, parseDescription(description.split(","))));
	}

	private static Function<MethodVisitor, MethodVisitor> parseDescription(String[] parts) {
		if (parts.length < 1) {
			throw new IllegalArgumentException("Empty description!");
		}
		Function<MethodVisitor, MethodVisitor> result = null;
		for (String description : parts) {
			String method = null;
			String injector = null;
			boolean before = true;
			boolean after = false;

			String[] options = description.trim().split(":");
			method = options[0].trim();
			if (options.length > 1) {
				injector = options[1].trim();
			}
			if (options.length > 2) {
				switch (options[2].trim()) {
				case "":
					break;
				case "before":
					before = true;
					after = false;
					break;
				case "after":
					before = false;
					after = true;
					break;
				case "both":
					before = true;
					after = true;
					break;
				default:
					throw new IllegalArgumentException("Unknown position " + options[2].trim());
				}
			}
			Function<MethodVisitor, MethodVisitor> factory = getFactory(method, injector, before, after);
			result = result == null ? factory : factory.compose(result);
		}
		return result;
	}

	private static Function<MethodVisitor, MethodVisitor> getFactory(String method, String injector, boolean before,
			boolean after) {

		if (injector == null) {
			switch (method) {
			case "threadid":
				return ThreadInstrumentation::new;
			case "objectid":
				return ObjectIdInstrumentation::new;
			case "avio":
				return AvioInstrumentation::new;
			case "concurpairs":
				throw new IllegalArgumentException("concurpairs used without position!");
			case "dupairs":
				return DUPairsInstrumentation::new;
			case "ereaser":
				return EreaserInstrumentation::new;
			case "goodlock":
				return GoodLockInstrumentation::new;
			case "sync":
				return SyncInstrumentation::new;
			default:
				throw new IllegalArgumentException("Unknown method or missing position! " + injector);
			}
		}

		final Injector inject;
		switch (injector) {
		case "concurpairs":
			inject = new ConcurPairsMetric();
			break;
		case "randomyield":
			inject = InjectableMethodsInjector::injectRandomYield;
			break;
		case "randomwait":
			inject = InjectableMethodsInjector::injectRandomWait;
			break;
		case "syncyield":
			inject = InjectableMethodsInjector::injectSynchronizedYield;
			break;
		case "busywait":
			inject = InjectableMethodsInjector::injectBusywait;
			break;
		case "randomsleep":
			inject = InjectableMethodsInjector::injectRandomSleep;
			break;
		case "semaphore":
			inject = InjectableMethodsInjector::injectSemaphoreOne;
			break;
		default:
			throw new IllegalArgumentException("Unknown injector: " + injector);
		}

		InstrumentationFactory factory = new InstrumentationFactory();
		switch (method) {
		case "monitorenter":
			return (mv) -> factory.monitorEnter(mv, inject, before, after);
		case "monitorexit":
			return (mv) -> factory.monitorExit(mv, inject, before, after);
		case "arrayload":
			return (mv) -> factory.arrayLoad(mv, inject, before, after);
		case "arraystore":
			return (mv) -> factory.arrayStore(mv, inject, before, after);
		case "getstatic":
			return (mv) -> factory.getStatic(mv, inject, before, after);
		case "putstatic":
			return (mv) -> factory.putStatic(mv, inject, before, after);
		case "getfield":
			return (mv) -> factory.getField(mv, inject, before, after);
		case "putfield":
			return (mv) -> factory.putField(mv, inject, before, after);
		case "notify":
			return (mv) -> factory.notify(mv, inject, before, after);
		case "notifyall":
			return (mv) -> factory.notifyAll(mv, inject, before, after);
		case "wait":
			return (mv) -> factory.wait(mv, inject, before, after);
		case "threadstart":
			return (mv) -> factory.threadStart(mv, inject, before, after);
		case "threadjoin":
			return (mv) -> factory.threadJoin(mv, inject, before, after);
		case "variable":
			return (mv) -> factory.sharedVariableVisitor(mv, inject, before, after);
		default:
			throw new IllegalArgumentException("Unknown method: " + method);
		}
	}
}
