package instrumentation.avio;

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

public class AvioMetric implements Metric<AvioTask> {

	private static ThreadLocal<Map<Integer, LastAccess>> localAccessTable = new ThreadLocal<Map<Integer, LastAccess>>() {
		protected Map<Integer, LastAccess> initialValue() {
			return new HashMap<>();
		};
	};

	public static Map<Integer, LastAccess> globalWriteTable = new HashMap<>();
	public static Queue<AvioTask> tasks = new LinkedBlockingQueue<AvioTask>();

	public static void readAccess(int position, int variable) {
		synchronized (globalWriteTable) {
			LastAccess global = globalWriteTable.get(variable);
			long t = Thread.currentThread().getId();

			if (global != null && global.type == AccessType.WRITE && global.t != t) {
				global.read = new LastAccess(position, AccessType.READ, t);

				if (localAccessTable.get().get(variable) != null) {
					tasks.add(new AvioTask(localAccessTable.get().get(variable).pl, position, global.pl, t, global.t));
				}
			}
			localAccessTable.get().put(variable, new LastAccess(position, AccessType.READ, t));
		}
	}

	public static void writeAccess(int position, int variable) {
		synchronized (globalWriteTable) {
			LastAccess global = globalWriteTable.get(variable);
			LastAccess local = localAccessTable.get().get(variable);
			long t = Thread.currentThread().getId();

			if (global != null && local != null && local.type == AccessType.READ && global.t != t) {
				tasks.add(new AvioTask(local.pl, position, global.pl, t, global.t));
			}

			if (local != null && local.type == AccessType.WRITE && local.read != null) {
				tasks.add(new AvioTask(local.pl, position, local.read.pl, t, local.read.t));
			}

			LastAccess current = new LastAccess(position, AccessType.WRITE, t);
			localAccessTable.get().put(variable, current);
			globalWriteTable.put(variable, current);
		}
	}

	public static Set<AvioTask> getRenamedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm) {
		return tr.translateWithMapping(tm,
				(translate, a) -> new AvioTask(a.pl1, a.pl2, a.pl3, translate.apply(a.t1), translate.apply(a.t2)),
				tasks);
	}

	public static void clear() {
		localAccessTable.remove();
		globalWriteTable.clear();
		tasks.clear();
	}

	public static void dump() {
		System.err.println(tasks);
	}

	@Override
	public void reset() {
		AvioMetric.clear();
	}

	@Override
	public Set<AvioTask> getTranslatedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm, ObjectRenamer or,
			Map<Integer, ObjectBehaviour> om) {
		return AvioMetric.getRenamedTasks(tr, tm);
	}
}
