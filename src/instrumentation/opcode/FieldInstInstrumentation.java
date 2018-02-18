package instrumentation.opcode;

import org.objectweb.asm.MethodVisitor;

import instrumentation.inject.Injector;

public class FieldInstInstrumentation extends Instrumentation {

	public FieldInstInstrumentation(MethodVisitor mv, int opcode,
			boolean before, boolean after) {
		super(mv, opcode, before, after);
	}

	public FieldInstInstrumentation(MethodVisitor mv, int opcode) {
		super(mv, opcode);
	}


	public FieldInstInstrumentation(MethodVisitor mv, int opcode, Injector injector, boolean before, boolean after) {
		super(mv, opcode, injector, before, after);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name,
			String desc) {
		if (opcode == this.getOpcode() && this.isBefore()) {
			this.inject(mv);
		}
		super.visitFieldInsn(opcode, owner, name, desc);
		if (opcode == this.getOpcode() && this.isAfter()) {
			this.inject(mv);
		}
	}
}
