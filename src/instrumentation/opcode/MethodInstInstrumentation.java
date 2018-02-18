package instrumentation.opcode;

import org.objectweb.asm.MethodVisitor;

import instrumentation.inject.Injector;

public class MethodInstInstrumentation extends Instrumentation {

	private String owner;
	private String name;

	public MethodInstInstrumentation(MethodVisitor mv, int opcode, String owner, String name, boolean before,
			boolean after) {
		super(mv, opcode, before, after);
		this.owner = owner;
		this.name = name;
	}

	public MethodInstInstrumentation(MethodVisitor mv, int opcode, String owner, String name) {
		super(mv, opcode);
		this.owner = owner;
		this.name = name;
	}

	public MethodInstInstrumentation(MethodVisitor mv, int opcode, String owner, String name, Injector injector,
			boolean before, boolean after) {
		super(mv, opcode, injector, before, after);
		this.owner = owner;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.MethodVisitor#visitMethodInsn(int, java.lang.String,
	 * java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		if (this.isBefore() && opcode == this.getOpcode() && this.owner.equals(owner) && this.name.equals(name)) {
			this.inject(mv);
		}
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		if (this.isAfter() && opcode == this.getOpcode() && this.owner.equals(owner) && this.name.equals(name)) {
			this.inject(mv);
		}
	}

}
