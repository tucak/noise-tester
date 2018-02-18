package instrumentation.concurpairs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import instrumentation.Metric;
import instrumentation.inject.Injector;
import instrumentation.objectid.ObjectBehaviour;
import instrumentation.objectid.ObjectRenamer;
import instrumentation.opcode.Instrumentation;
import instrumentation.threadid.ThreadBehaviour;
import instrumentation.threadid.ThreadRenamer;

public class ConcurPairsMetric implements Injector, Metric<ConcurPairsTask> {

	private static Map<Integer, Boolean> switched;
	private static Map<Integer, Long> last;

	private static ThreadLocal<Integer> lastPlace = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return null;
		};
	};

	public static Queue<ConcurPairsTask> q = new LinkedBlockingQueue<>();

	public static int amount;

	public static int current = 0;

	public static void init(int amount) {
		new ConcurPairsMetric().reset();
	}

	public static void cover(int pos) {
		long id = Thread.currentThread().getId();

		synchronized (q) {
			if (lastPlace.get() != null && last.get(lastPlace.get()) != id) {
				switched.put(pos, true);
			}
			if (lastPlace.get() != null) {
				q.add(new ConcurPairsTask(lastPlace.get(), pos, last.get(lastPlace.get()) != id));
			}
			last.put(pos, id);
			lastPlace.set(pos);
		}
	}

	@Override
	public void inject(MethodVisitor mv) {
		mv.visitIntInsn(Opcodes.SIPUSH, current);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "instrumentation/concurpairs/ConcurPairsMetric", "cover", "(I)V",
				false);
		current += 1;
	}

	public void setAsInjector(MethodVisitor mv) {
		while (mv instanceof Instrumentation) {
			Instrumentation i = (Instrumentation) mv;
			i.setInjector(this);
			mv = i.getParentMethodVisitor();
		}
	}

	@Override
	public void reset() {
		ConcurPairsMetric.switched = new HashMap<Integer, Boolean>();
		ConcurPairsMetric.last = new HashMap<Integer, Long>();
		ConcurPairsMetric.q.clear();
		lastPlace.set(null);
	}

	@Override
	public Set<ConcurPairsTask> getTranslatedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm, ObjectRenamer or,
			Map<Integer, ObjectBehaviour> om) {
		return new HashSet<ConcurPairsTask>(q);
	}
}
