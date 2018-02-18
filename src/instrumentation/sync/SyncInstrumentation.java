package instrumentation.sync;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import instrumentation.opcode.Instrumentation;

public class SyncInstrumentation extends Instrumentation {
	private static int nextPosition = 0;

	public SyncInstrumentation(MethodVisitor mv) {
		super(mv, Opcodes.NOP);
	}

	private void injectBefore() {
		mv.visitInsn(Opcodes.DUP);
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition);
		nextPosition += 1;
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, SyncMetric.class.getName().replace('.', '/'), "beforeMonitorEnter",
				"(Ljava/lang/Object;I)V", false);
		mv.visitInsn(Opcodes.DUP);
	}

	private void injectAfter() {
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition);
		nextPosition += 1;
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, SyncMetric.class.getName().replace('.', '/'), "afterMonitorEnter",
				"(Ljava/lang/Object;I)V", false);
	}

	private void injectExit() {
		mv.visitInsn(Opcodes.DUP);
		mv.visitIntInsn(Opcodes.SIPUSH, nextPosition);
		nextPosition += 1;
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, SyncMetric.class.getName().replace('.', '/'), "beforeMonitorExit",
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

}
