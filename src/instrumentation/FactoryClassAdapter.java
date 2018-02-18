package instrumentation;

import java.util.function.Function;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FactoryClassAdapter extends ClassVisitor {
	private Function<MethodVisitor, MethodVisitor> visitorFactory;

	public FactoryClassAdapter(final ClassVisitor cv, Function<MethodVisitor, MethodVisitor> visitorFactory) {
		super(Opcodes.ASM5, cv);
		this.visitorFactory = visitorFactory;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		try {
			return mv == null ? null : visitorFactory.apply(mv);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
