package instrumentation.agent;

import java.util.function.Function;

import org.objectweb.asm.MethodVisitor;

public class Rule {
	private String key;
	private Function<MethodVisitor, MethodVisitor> mv;

	public Rule(String key, Function<MethodVisitor, MethodVisitor> mv) {
		this.key = key;
		this.mv = mv;
	}
	
	public String getKey() {
		return key;
	}
	
	public Function<MethodVisitor, MethodVisitor> getMethodVisitor() {
		return mv;
	}
}
