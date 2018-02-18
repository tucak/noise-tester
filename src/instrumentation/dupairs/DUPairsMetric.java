package instrumentation.dupairs;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import instrumentation.Metric;
import instrumentation.objectid.ObjectBehaviour;
import instrumentation.objectid.ObjectRenamer;
import instrumentation.threadid.ThreadBehaviour;
import instrumentation.threadid.ThreadRenamer;

public class DUPairsMetric implements Metric<DUPairsTask> {
	public static Map<Integer, Integer> lastWritePosition = new HashMap<>();
	public static Map<Integer, Long> lastWriteThread = new HashMap<>();
	public static Queue<DUPairsTask> tasks = new LinkedBlockingQueue<DUPairsTask>();

	public static void readAccess(int position, int variable) {
		synchronized (lastWritePosition) {
			if (lastWriteThread.get(variable) != null
					&& lastWriteThread.get(variable) != Thread.currentThread().getId()) {
				tasks.add(new DUPairsTask(variable, lastWritePosition.get(variable), position, lastWriteThread.get(variable),
						Thread.currentThread().getId()));
			}
		}
	}

	public static void writeAccess(int position, int variable) {
		synchronized (lastWritePosition) {
			lastWritePosition.put(variable, position);
			lastWriteThread.put(variable, Thread.currentThread().getId());
		}
	}

	public static Set<DUPairsTask> getTranslatedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> mapping) {
		return tr.translateWithMapping(mapping,
				(translate, t) -> new DUPairsTask(t.v, t.p1, t.p2, translate.apply(t.t1), translate.apply(t.t2)),
				tasks);
	}

	@Override
	public void reset() {
		lastWritePosition.clear();
		lastWriteThread.clear();
		tasks.clear();
	}

	@Override
	public Set<DUPairsTask> getTranslatedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm, ObjectRenamer or,
			Map<Integer, ObjectBehaviour> om) {
		return DUPairsMetric.getTranslatedTasks(tr, tm);
	}
}
