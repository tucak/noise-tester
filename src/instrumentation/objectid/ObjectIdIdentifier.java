package instrumentation.objectid;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectIdIdentifier {
	public static Map<Integer, ObjectBehaviour> map = new HashMap<Integer, ObjectBehaviour>();
	public static Map<String, AtomicInteger> r = new HashMap<String, AtomicInteger>();

	public static void enter(Object o, int place) {
		synchronized (map) {
			if (!map.containsKey(h(o))) {
				ObjectBehaviour i = new ObjectBehaviour();
				i.className = o.getClass().getCanonicalName();

				if (!r.containsKey(i.className)) {
					r.put(i.className, new AtomicInteger(0));
				}
				i.i = r.get(i.className).incrementAndGet();
				map.put(h(o), i);
			}
			ObjectBehaviour i = map.get(h(o));
			i.places.add(place);
		}
	}

	public static void exit(Object o, int place) {
		synchronized (map) {
			ObjectBehaviour i = map.get(h(o));
			i.places.add(place);
		}
	}

	private static int h(Object o) {
		return System.identityHashCode(o);
	}
	
	public static void clear() {
		map.clear();
		r.clear();
	}
}
