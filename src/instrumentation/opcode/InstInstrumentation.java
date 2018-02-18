package instrumentation.opcode;

import org.objectweb.asm.MethodVisitor;

import instrumentation.inject.Injector;

public class InstInstrumentation extends Instrumentation {

	public InstInstrumentation(MethodVisitor mv, int opcode, boolean before,
			boolean after) {
		super(mv, opcode, before, after);
	}

	public InstInstrumentation(MethodVisitor mv, int opcode) {
		super(mv, opcode);
	}
	
	public InstInstrumentation(MethodVisitor mv, int opcode, Injector injector, boolean before, boolean after) {
		super(mv, opcode, injector, before, after);
	}

	@Override
	public void visitInsn(int opcode) {
		if (opcode == this.getOpcode() && this.isBefore()) {
			this.inject(mv);
		}
		super.visitInsn(opcode);
		if (opcode == this.getOpcode() && this.isAfter()) {
			this.inject(mv);
		}
	}

}
