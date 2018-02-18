package instrumentation.threadid;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadIdentifier {
	private static int N = 100;

	private static ThreadLocal<ThreadBehaviour> id = ThreadLocal.<ThreadBehaviour>withInitial(() -> null);
	public static Map<Long, ThreadBehaviour> threadMapping = new HashMap<>();
	private static AtomicInteger next = new AtomicInteger(0);

	public static void atMethodEnter(int method) {
		if (id.get() == null || !threadMapping.containsKey(Thread.currentThread().getId())) {
			id.set(new ThreadBehaviour(next.getAndIncrement()));
			threadMapping.put(Thread.currentThread().getId(), id.get());
		}
		if (id.get().methods.size() > ThreadIdentifier.N) {
			return;
		}
		id.get().methods.add(method);
	}

	public static void clear() {
		threadMapping.clear();
		next.set(0);
		id.remove();
	}

	public static void dump() {
		System.err.println(threadMapping);
	}

}
