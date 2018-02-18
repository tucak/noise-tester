package instrumentation.ereaser;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import instrumentation.Metric;
import instrumentation.objectid.ObjectBehaviour;
import instrumentation.objectid.ObjectRenamer;
import instrumentation.threadid.ThreadBehaviour;
import instrumentation.threadid.ThreadRenamer;

public class EreaserMetric implements Metric<EreaserTask>{
	private static ThreadLocal<WeakHashMap<Object, AtomicInteger>> locks = new ThreadLocal<WeakHashMap<Object, AtomicInteger>>() {
		protected WeakHashMap<Object, AtomicInteger> initialValue() {
			return new WeakHashMap<>();
		};
	};

	public static Queue<EreaserTask> tasks = new LinkedBlockingQueue<>();
	private static Map<Integer, EreaserVariable> var = new ConcurrentHashMap<>();

	private static int h(Object o) {
		return System.identityHashCode(o);
	}

	public static void afterMonitorEnter(Object monitor) {
		if (locks.get().containsKey(monitor)) {
			locks.get().get(monitor).incrementAndGet();
		} else {
			locks.get().put(monitor, new AtomicInteger(1));
		}
	}

	public static void beforeMonitorExit(Object monitor) {
		if (locks.get().containsKey(monitor)) {
			int remaining = locks.get().get(monitor).decrementAndGet();
			if (remaining <= 0) {
				locks.get().remove(monitor);
			}
		}
	}

	public static void readAccess(int position, int variable) {
		if (!var.containsKey(variable)) {
			var.put(variable, new EreaserVariable());
		}
		
		EreaserVariable v = var.get(variable);
		v.read(id(), locks.get().keySet().stream().map(o -> h(o)).collect(Collectors.toList()));
		tasks.add(new EreaserTask(position, variable, v.state, v.getLockset(), id()));
	}

	public static void writeAccess(int position, int variable) {
		if (!var.containsKey(variable)) {
			var.put(variable, new EreaserVariable());
		}
		EreaserVariable v = var.get(variable);
		v.write(id(), locks.get().keySet().stream().map(o -> h(o)).collect(Collectors.toList()));
		tasks.add(new EreaserTask(position, variable, v.state, v.getLockset(), id()));
	}

	private static long id() {
		return Thread.currentThread().getId();
	}

	public static Set<EreaserTask> getTranslatedTasksStatic(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm, ObjectRenamer or,
			Map<Integer, ObjectBehaviour> om) {
		Set<EreaserTask> result = tr.translateWithMapping(tm,
				(translate, t) -> new EreaserTask(t.pl, t.var, t.state, t.lockset, translate.apply(t.thread)), tasks);
		result = or.translateWithMapping(om,
				(translate, t) -> new EreaserTask(t.pl, t.var, t.state,
						t.lockset == null ? null
								: t.lockset.stream().map(i -> translate.apply(i)).collect(Collectors.toSet()),
						t.thread),
				result);
		return result;
	}

	@Override
	public void reset() {
		EreaserMetric.var.clear();
		EreaserMetric.tasks.clear();
		EreaserMetric.locks.remove();
	}

	@Override
	public Set<EreaserTask> getTranslatedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm, ObjectRenamer or,
			Map<Integer, ObjectBehaviour> om) {
		return EreaserMetric.getTranslatedTasksStatic(tr, tm, or, om);
	}
}
