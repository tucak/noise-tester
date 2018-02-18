package instrumentation.goodlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import instrumentation.Metric;
import instrumentation.objectid.ObjectBehaviour;
import instrumentation.objectid.ObjectRenamer;
import instrumentation.threadid.ThreadBehaviour;
import instrumentation.threadid.ThreadRenamer;

public class GoodLockMetric implements Metric<GoodLockTask> {
	public static Queue<GoodLockTask> tasks = new LinkedBlockingQueue<>();

	private static ThreadLocal<List<Integer>> myLocks = new ThreadLocal<List<Integer>>() {
		protected List<Integer> initialValue() {
			return new ArrayList<>();
		};
	};
	private static ThreadLocal<List<Integer>> myPlaces = new ThreadLocal<List<Integer>>() {
		protected List<Integer> initialValue() {
			return new ArrayList<>();
		};
	};

	public static void clear() {
		myPlaces.remove();
		myLocks.remove();
		tasks.clear();
	}

	private static int h(Object o) {
		return System.identityHashCode(o);
	}

	public static void afterMonitorEnter(Object monitor, int place) {
		if (myLocks.get().size() > 0) {
			tasks.add(new GoodLockTask(myPlaces.get().get(myPlaces.get().size() - 1), place,
					myLocks.get().get(myLocks.get().size() - 1), h(monitor), Thread.currentThread().getId()));
		}
		myLocks.get().add(h(monitor));
		myPlaces.get().add(place);
	}

	public static void beforeMonitorExit(Object monitor, int place) {
		int i = myLocks.get().lastIndexOf(h(monitor));
		myLocks.get().remove(i);
		myPlaces.get().remove(i);
	}

	public static Set<GoodLockTask> getTranslatedTasksStatic(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm,
			ObjectRenamer or, Map<Integer, ObjectBehaviour> om) {
		Set<GoodLockTask> result = tr.translateWithMapping(tm,
				(translate, t) -> new GoodLockTask(t.pl1, t.pl2, t.l1, t.l2, translate.apply(t.t)), tasks);
		result = or.translateWithMapping(om,
				(translate, t) -> new GoodLockTask(t.pl1, t.pl2, translate.apply(t.l1), translate.apply(t.l2), t.t),
				result);
		return result;
	}

	@Override
	public void reset() {
		GoodLockMetric.clear();
	}

	@Override
	public Set<GoodLockTask> getTranslatedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm, ObjectRenamer or,
			Map<Integer, ObjectBehaviour> om) {
		return GoodLockMetric.getTranslatedTasksStatic(tr, tm, or, om);
	}

}
