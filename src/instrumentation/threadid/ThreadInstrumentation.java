package instrumentation.threadid;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import instrumentation.opcode.Instrumentation;

public class ThreadInstrumentation extends Instrumentation {

	private static int next = 0;

	public ThreadInstrumentation(MethodVisitor mv) {
		super(mv, Opcodes.NOP);
	}

	@Override
	public void visitCode() {
		mv.visitCode();
		mv.visitIntInsn(Opcodes.SIPUSH, next);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, ThreadIdentifier.class.getName().replace('.', '/'), "atMethodEnter",
				"(I)V", false);
		next++;
	}

}
