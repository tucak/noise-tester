package instrumentation.objectid;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import instrumentation.opcode.Instrumentation;

public class ObjectIdInstrumentation extends Instrumentation {
	public ObjectIdInstrumentation(MethodVisitor mv) {
		super(mv, Opcodes.NOP);
	}

	public int next = 0;

	private int nextPosition() {
		int i = next;
		next += 1;
		return i;
	}

	private void beforeEnter() {
		mv.visitInsn(Opcodes.DUP);
	}

	private void injectEnter() {
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, ObjectIdIdentifier.class.getName().replace('.', '/'), "enter",
				"(Ljava/lang/Object;I)V", false);
	}

	private void injectExit() {
		mv.visitInsn(Opcodes.DUP);
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, ObjectIdIdentifier.class.getName().replace('.', '/'), "exit",
				"(Ljava/lang/Object;I)V", false);
	}

	@Override
	public void visitInsn(int opcode) {
		if (Opcodes.MONITORENTER == opcode) {
			beforeEnter();
		}
		if (Opcodes.MONITOREXIT == opcode) {
			injectExit();
		}
		super.visitInsn(opcode);
		if (Opcodes.MONITORENTER == opcode) {
			injectEnter();
		}
	}
}
