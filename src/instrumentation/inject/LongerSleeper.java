package instrumentation.inject;

import java.util.Random;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LongerSleeper implements Injector {
	private static Random r = new Random(System.currentTimeMillis());

	public static void randomSleep() {
		try {
			Thread.sleep((long) (r.nextFloat() * 6));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * instrumentation.inject.Injector#inject(org.objectweb.asm.MethodVisitor)
	 */
	@Override
	public void inject(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC,
				LongerSleeper.class.getName().replace('.', '/'), "randomSleep", "()V", false);
	}
}
