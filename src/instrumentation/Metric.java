package instrumentation;

import java.util.Map;
import java.util.Set;

import instrumentation.objectid.ObjectBehaviour;
import instrumentation.objectid.ObjectRenamer;
import instrumentation.threadid.ThreadBehaviour;
import instrumentation.threadid.ThreadRenamer;

public interface Metric<T> {
	 public void reset();
	 public Set<T> getTranslatedTasks(ThreadRenamer tr, Map<Long, ThreadBehaviour> tm, ObjectRenamer or,
				Map<Integer, ObjectBehaviour> om);
}
