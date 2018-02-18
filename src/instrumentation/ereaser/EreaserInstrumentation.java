package instrumentation.ereaser;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import instrumentation.opcode.Instrumentation;

public class EreaserInstrumentation extends Instrumentation {
	private static Map<String, Integer> variables = new HashMap<>();
	private static int nextPosition = 0;
	private static int nextVariable = 0;

	public EreaserInstrumentation(MethodVisitor mv) {
		super(mv, Opcodes.NOP);
	}

	private void injectRead(String owner, String name) {
		String id = owner + "." + name;
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition);
		nextPosition += 1;
		mv.visitIntInsn(Opcodes.SIPUSH, getMapping(id));
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, EreaserMetric.class.getName().replace('.', '/'), "readAccess", "(II)V",
				false);
	}

	private void injectWrite(String owner, String name) {
		String id = owner + "." + name;
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition);
		nextPosition += 1;
		mv.visitIntInsn(Opcodes.SIPUSH, getMapping(id));
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, EreaserMetric.class.getName().replace('.', '/'), "writeAccess",
				"(II)V", false);
	}

	private int getMapping(String id) {
		int v;
		if (variables.containsKey(id)) {
			v = variables.get(id);
		} else {
			v = nextVariable;
			nextVariable += 1;
			variables.put(id, v);
		}
		return v;
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		if (opcode == Opcodes.PUTFIELD || opcode == Opcodes.PUTSTATIC) {
			injectWrite(owner, name);
		}

		if (opcode == Opcodes.GETFIELD || opcode == Opcodes.GETSTATIC) {
			injectRead(owner, name);
		}

		super.visitFieldInsn(opcode, owner, name, desc);
	}

	private void injectDuplicate() {
		mv.visitInsn(Opcodes.DUP);
	}

	private void injectEnter() {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, EreaserMetric.class.getName().replace('.', '/'), "afterMonitorEnter",
				"(Ljava/lang/Object;)V", false);
	}

	private void injectExit() {
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, EreaserMetric.class.getName().replace('.', '/'), "beforeMonitorExit",
				"(Ljava/lang/Object;)V", false);
	}

	@Override
	public void visitInsn(int opcode) {
		if (Opcodes.MONITORENTER == opcode) {
			injectDuplicate();
		} else if (Opcodes.MONITOREXIT == opcode) {
			injectExit();
		}
		super.visitInsn(opcode);
		if (Opcodes.MONITORENTER == opcode) {
			injectEnter();
		}
	}

	public static void reset() {
		variables.clear();
		nextPosition = 0;
		nextVariable = 0;
	}
}
