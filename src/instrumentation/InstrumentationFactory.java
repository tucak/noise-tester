package instrumentation;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import instrumentation.inject.Injector;
import instrumentation.inject.LongerSleeper;
import instrumentation.opcode.FieldInstInstrumentation;
import instrumentation.opcode.InstInstrumentation;
import instrumentation.opcode.Instrumentation;
import instrumentation.opcode.MethodInstInstrumentation;
import instrumentation.opcode.NamedFieldInstInstrumentation;
import instrumentation.opcode.PatternInstrumentation;

public class InstrumentationFactory {
	public MethodVisitor monitorEnter(MethodVisitor mv) {
		return new InstInstrumentation(mv, Opcodes.MONITORENTER);
	}

	public MethodVisitor monitorEnter(MethodVisitor mv, boolean before, boolean after) {
		return new InstInstrumentation(mv, Opcodes.MONITORENTER, before, after);
	}

	public MethodVisitor monitorEnter(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new InstInstrumentation(mv, Opcodes.MONITORENTER, injector, before, after);
	}

	public MethodVisitor monitorExit(MethodVisitor mv) {
		return new InstInstrumentation(mv, Opcodes.MONITOREXIT);
	}

	public MethodVisitor monitorExit(MethodVisitor mv, boolean before, boolean after) {
		return new InstInstrumentation(mv, Opcodes.MONITOREXIT, before, after);
	}

	public MethodVisitor monitorExit(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new InstInstrumentation(mv, Opcodes.MONITOREXIT, injector, before, after);
	}

	public MethodVisitor arrayLoad(MethodVisitor mv) {
		return this.arrayLoad(mv, true, false);
	}

	public MethodVisitor arrayLoad(MethodVisitor mv, boolean before, boolean after) {
		return this.arrayLoad(mv, null, before, after);
	}

	public MethodVisitor arrayLoad(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		MethodVisitor current = mv;
		int[] opcodes = new int[] { Opcodes.IALOAD, Opcodes.LALOAD, Opcodes.FALOAD, Opcodes.DALOAD, Opcodes.AALOAD,
				Opcodes.BALOAD, Opcodes.CALOAD, Opcodes.SALOAD };
		for (int opcode : opcodes) {
			if (injector != null) {
				current = new InstInstrumentation(current, opcode, injector, before, after);
			} else {
				current = new InstInstrumentation(current, opcode, before, after);
			}
		}
		return current;
	}

	public MethodVisitor arrayStore(MethodVisitor mv) {
		return this.arrayStore(mv, true, false);
	}

	public MethodVisitor arrayStore(MethodVisitor mv, boolean before, boolean after) {
		MethodVisitor current = mv;
		int[] opcodes = new int[] { Opcodes.IASTORE, Opcodes.LASTORE, Opcodes.FASTORE, Opcodes.DASTORE, Opcodes.AASTORE,
				Opcodes.BASTORE, Opcodes.CASTORE, Opcodes.SASTORE };
		for (int opcode : opcodes) {
			current = new InstInstrumentation(current, opcode, before, after);
		}
		return current;
	}

	public MethodVisitor arrayStore(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		MethodVisitor current = mv;
		int[] opcodes = new int[] { Opcodes.IASTORE, Opcodes.LASTORE, Opcodes.FASTORE, Opcodes.DASTORE, Opcodes.AASTORE,
				Opcodes.BASTORE, Opcodes.CASTORE, Opcodes.SASTORE };
		for (int opcode : opcodes) {
			if (injector != null) {
				current = new InstInstrumentation(current, opcode, injector, before, after);
			} else {
				current = new InstInstrumentation(current, opcode, before, after);
			}
		}
		return current;
	}

	public MethodVisitor getStatic(MethodVisitor mv) {
		return new FieldInstInstrumentation(mv, Opcodes.GETSTATIC);
	}

	public MethodVisitor getStatic(MethodVisitor mv, boolean before, boolean after) {
		return new FieldInstInstrumentation(mv, Opcodes.GETSTATIC, before, after);
	}

	public MethodVisitor getStatic(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new FieldInstInstrumentation(mv, Opcodes.GETSTATIC, injector, before, after);
	}

	public MethodVisitor putStatic(MethodVisitor mv) {
		return new FieldInstInstrumentation(mv, Opcodes.PUTSTATIC);
	}

	public MethodVisitor putStatic(MethodVisitor mv, boolean before, boolean after) {
		return new FieldInstInstrumentation(mv, Opcodes.PUTSTATIC, before, after);
	}

	public MethodVisitor putStatic(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new FieldInstInstrumentation(mv, Opcodes.PUTSTATIC, injector, before, after);
	}

	public MethodVisitor getField(MethodVisitor mv) {
		return new FieldInstInstrumentation(mv, Opcodes.GETFIELD);
	}

	public MethodVisitor getField(MethodVisitor mv, boolean before, boolean after) {
		return new FieldInstInstrumentation(mv, Opcodes.GETFIELD, before, after);
	}

	public MethodVisitor getField(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new FieldInstInstrumentation(mv, Opcodes.GETFIELD, injector, before, after);
	}

	public MethodVisitor putField(MethodVisitor mv) {
		return new FieldInstInstrumentation(mv, Opcodes.PUTFIELD);
	}

	public MethodVisitor putField(MethodVisitor mv, boolean before, boolean after) {
		return new FieldInstInstrumentation(mv, Opcodes.PUTFIELD, before, after);
	}

	public MethodVisitor putField(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new FieldInstInstrumentation(mv, Opcodes.PUTFIELD, injector, before, after);
	}

	public MethodVisitor notify(MethodVisitor mv) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "notify");
	}

	public MethodVisitor notify(MethodVisitor mv, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "notify", before, after);
	}

	public MethodVisitor notify(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "notify", injector, before,
				after);
	}

	public MethodVisitor notifyAll(MethodVisitor mv) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "notifyAll");
	}

	public MethodVisitor notifyAll(MethodVisitor mv, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "notifyAll", before, after);
	}

	public MethodVisitor notifyAll(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "notifyAll", injector,
				before, after);
	}

	public MethodVisitor wait(MethodVisitor mv) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "wait");
	}

	public MethodVisitor wait(MethodVisitor mv, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "wait", before, after);
	}

	public MethodVisitor wait(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Object", "wait", injector, before,
				after);
	}

	public MethodVisitor threadStart(MethodVisitor mv) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "start", false, true);
	}

	public MethodVisitor threadStart(MethodVisitor mv, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "start", before, after);
	}

	public MethodVisitor threadStart(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "start", injector, before,
				after);
	}

	public MethodVisitor threadJoin(MethodVisitor mv) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "join");
	}

	public MethodVisitor threadJoin(MethodVisitor mv, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "join", before, after);
	}

	public MethodVisitor threadJoin(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return new MethodInstInstrumentation(mv, Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "join", injector, before,
				after);
	}

	public MethodVisitor defaultVisitor(MethodVisitor mv) {
		return monitorEnter(monitorExit(
				getStatic(putStatic(getField(putField(notify(notifyAll(wait(threadStart(threadJoin(mv)))))))))));
	}

	public MethodVisitor sharedVariableVisitor(MethodVisitor mv) {
		return getStatic(putStatic(getField(putField(mv))));
	}

	public MethodVisitor sharedVariableVisitor(MethodVisitor mv, Injector injector, boolean before, boolean after) {
		return getStatic(putStatic(getField(putField(mv, injector, before, after), injector, before, after), injector,
				before, after), injector, before, after);
	}


	public MethodVisitor namedVariable(MethodVisitor mv, String owner, String name) {
		return new NamedFieldInstInstrumentation(new NamedFieldInstInstrumentation(new NamedFieldInstInstrumentation(
				new NamedFieldInstInstrumentation(mv, name, owner, Opcodes.GETFIELD), name, owner, Opcodes.PUTFIELD),
				name, owner, Opcodes.GETSTATIC), name, owner, Opcodes.PUTSTATIC);
	}

	public MethodVisitor readWriteVisitor(MethodVisitor mv, String owner, String name) {
		Injector longerSleep = new LongerSleeper();
		Instrumentation read = new FieldInstInstrumentation(mv, Opcodes.GETSTATIC, longerSleep, true, false);
		read = new FieldInstInstrumentation(read, Opcodes.GETFIELD, longerSleep, true, false);
		read.setInjector(longerSleep);

		return putField(putStatic(read));
	}

	public MethodVisitor patterVisitor(MethodVisitor mv) {
		return new PatternInstrumentation(mv);
	}

}
