package instrumentation.inject;

import org.objectweb.asm.MethodVisitor;

public interface Injector {

	public abstract void inject(MethodVisitor mv);
}