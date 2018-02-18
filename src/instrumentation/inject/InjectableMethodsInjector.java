package instrumentation.inject;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InjectableMethodsInjector {
	public static void injectRandomYield(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, InjectableMethods.class.getName().replace('.', '/'), "randomYield",
				"()V", false);
	}

	public static void injectRandomWait(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, InjectableMethods.class.getName().replace('.', '/'), "randomWait",
				"()V", false);
	}

	public static void injectSynchronizedYield(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, InjectableMethods.class.getName().replace('.', '/'),
				"synchronizedYield", "()V", false);
	}

	public static void injectBusywait(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, InjectableMethods.class.getName().replace('.', '/'), "busywait", "()V",
				false);
	}

	public static void injectRandomSleep(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, InjectableMethods.class.getName().replace('.', '/'), "randomSleep",
				"()V", false);
	}

	public static void injectSemaphoreOne(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, InjectableMethods.class.getName().replace('.', '/'), "semaphoreOne",
				"()V", false);
	}
}
