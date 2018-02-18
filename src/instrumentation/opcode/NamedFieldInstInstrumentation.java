package instrumentation.opcode;

import org.objectweb.asm.MethodVisitor;

import instrumentation.inject.Injector;

public class NamedFieldInstInstrumentation extends Instrumentation {

	private String name;
	private String owner;

	public NamedFieldInstInstrumentation(MethodVisitor mv, String name, String owner, int opcode, boolean before,
			boolean after) {
		super(mv, opcode, before, after);
		this.name = name;
		this.owner = owner;
	}

	public NamedFieldInstInstrumentation(MethodVisitor mv, String name, String owner, int opcode) {
		super(mv, opcode);
		this.name = name;
		this.owner = owner;
	}

	public NamedFieldInstInstrumentation(MethodVisitor mv, int opcode, String name, String owner, Injector injector,
			boolean before, boolean after) {
		super(mv, opcode, injector, before, after);
		this.name = name;
		this.owner = owner;
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		if (owner.equals(this.owner) && name.equals(this.name) && opcode == this.getOpcode() && this.isBefore()) {
			this.inject(mv);
		}
		super.visitFieldInsn(opcode, owner, name, desc);
		if (owner.equals(this.owner) && name.equals(this.name) && opcode == this.getOpcode() && this.isAfter()) {
			this.inject(mv);
		}
	}
}
