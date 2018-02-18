package instrumentation.goodlock;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import instrumentation.opcode.Instrumentation;

public class GoodLockInstrumentation extends Instrumentation {
	private static int nextPos = 0;

	public GoodLockInstrumentation(MethodVisitor mv) {
		super(mv, Opcodes.NOP);
	}

	private int nextPosition() {
		int t = GoodLockInstrumentation.nextPos;
		++GoodLockInstrumentation.nextPos;
		return t;
	}

	private void injectBefore() {
		mv.visitInsn(Opcodes.DUP);
	}

	private void injectAfter() {
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, GoodLockMetric.class.getName().replace('.', '/'), "afterMonitorEnter",
				"(Ljava/lang/Object;I)V", false);
	}

	private void injectExit() {
		mv.visitInsn(Opcodes.DUP);
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition());
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, GoodLockMetric.class.getName().replace('.', '/'), "beforeMonitorExit",
				"(Ljava/lang/Object;I)V", false);
	}

	@Override
	public void visitInsn(int opcode) {
		if (Opcodes.MONITORENTER == opcode) {
			injectBefore();
		} else if (Opcodes.MONITOREXIT == opcode) {
			injectExit();
		}
		super.visitInsn(opcode);
		if (Opcodes.MONITORENTER == opcode) {
			injectAfter();
		}
	}

	public static void reset() {
		nextPos = 0;
	}
}
