package instrumentation.opcode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import instrumentation.inject.InjectableMethodsInjector;
import instrumentation.inject.Injector;

public abstract class Instrumentation extends MethodVisitor {
	private static Injector defaultInjector = InjectableMethodsInjector::injectRandomSleep;

	private boolean before = true;
	private boolean after = false;
	private int opcode;
	private Injector injector = defaultInjector;
	private final MethodVisitor parent;

	public Instrumentation(final MethodVisitor mv, int opcode) {
		super(Opcodes.ASM5, mv);
		this.setOpcode(opcode);
		this.parent = mv;
	}

	public Instrumentation(final MethodVisitor mv, int opcode, boolean before, boolean after) {
		super(Opcodes.ASM5, mv);
		this.setOpcode(opcode);
		this.before = before;
		this.after = after;
		this.parent = mv;
	}

	public Instrumentation(final MethodVisitor mv, int opcode, Injector injector, boolean before, boolean after) {
		super(Opcodes.ASM5, mv);
		this.setOpcode(opcode);
		this.before = before;
		this.after = after;
		this.parent = mv;
		this.injector = injector;
	}

	protected boolean isBefore() {
		return before;
	}

	protected void setBefore(boolean before) {
		this.before = before;
	}

	protected boolean isAfter() {
		return after;
	}

	protected void setAfter(boolean after) {
		this.after = after;
	}

	protected int getOpcode() {
		return opcode;
	}

	protected void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	public Injector getInjector() {
		return this.injector;
	}

	protected void inject(MethodVisitor mv) {
		injector.inject(mv);
	}

	public MethodVisitor getParentMethodVisitor() {
		return this.parent;
	}
}