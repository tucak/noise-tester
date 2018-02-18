package instrumentation.sync;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import instrumentation.Metric;
import instrumentation.objectid.ObjectBehaviour;
import instrumentation.objectid.ObjectRenamer;
import instrumentation.threadid.ThreadBehaviour;
import instrumentation.threadid.ThreadRenamer;

public class SyncMetric implements Metric<SyncTask> {

	public static Map<Object, Integer> locks = new WeakHashMap<>();
	public static Queue<SyncTask> tasks = new LinkedBlockingQueue<SyncTask>();

	public static void beforeMonitorEnter(Object monitor, int place) {
		synchronized (locks) {
			tasks.add(new SyncTask(place, SyncEvent.VISITED));
			if (locks.containsKey(monitor)) {
				tasks.add(new SyncTask(place, SyncEvent.BLOCKED));
				tasks.add(new SyncTask(locks.get(monitor), SyncEvent.BLOCKING));
			}
		}
	}

	public static void afterMonitorEnter(Object monitor, int place) {
		synchronized (locks) {
			locks.put(monitor, place);
		}
	}

	public static void beforeMonitorExit(Object monitor, int place) {
		synchronized (locks) {
			tasks.add(new SyncTask(place, SyncEvent.VISITED));
			locks.remove(monitor);
		}
	}

	@Override
	public void reset() {
		locks.clear();
		tasks.clear();
	}

	@Override
	public Set<SyncTask> getTranslatedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm, ObjectRenamer or,
			Map<Integer, ObjectBehaviour> om) {
		return new HashSet<SyncTask>(tasks);
	}

}
