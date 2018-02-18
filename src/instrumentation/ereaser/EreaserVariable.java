package instrumentation.ereaser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class EreaserVariable {
	EreaserState state = EreaserState.VIRGIN;
	long owner;
	private static HashSet<Integer> EMPTY = new HashSet<Integer>();
	Set<Integer> lockset = EMPTY;

	private Set<Integer> intersect(Collection<Integer> a, Collection<Integer> b) {
		if (a.isEmpty() || b.isEmpty()) {
			return EMPTY;
		}
		Set<Integer> r = new HashSet<Integer>(a);
		r.retainAll(b);
		return r;
	}

	public void read(long thread, Collection<Integer> locks) {
		locks = (locks == null || locks.isEmpty()) ? EMPTY : locks;
		switch (state) {
		case VIRGIN:
			owner = thread;
			lockset = new HashSet<Integer>(locks);
			state = EreaserState.EXCLUSIVE;
			break;
		case EXCLUSIVE:
			if (owner != thread) {
				state = EreaserState.SHARED;
				lockset = intersect(lockset, locks);
			}
			break;
		case EXCLUSIVE_MODIFIED:
			if (owner != thread) {
				state = EreaserState.SHARED_MODIFIED;
				lockset = intersect(lockset, locks);
			}
			break;
		case SHARED:
			lockset = intersect(lockset, locks);
			break;
		case SHARED_MODIFIED:
			lockset = intersect(lockset, locks);
			break;
		}
	}

	public void write(long thread, Collection<Integer> locks) {
		locks = (locks == null || locks.isEmpty()) ? EMPTY : locks;
		switch (state) {
		case VIRGIN:
			owner = thread;
			lockset = new HashSet<Integer>(locks);
			state = EreaserState.EXCLUSIVE_MODIFIED;
			break;
		case EXCLUSIVE:
			if (owner == thread) {
				state = EreaserState.EXCLUSIVE_MODIFIED;
			} else {
				state = EreaserState.SHARED_MODIFIED;
				lockset = intersect(lockset, locks);
			}
			break;
		case EXCLUSIVE_MODIFIED:
			if (owner != thread) {
				state = EreaserState.SHARED_MODIFIED;
				lockset = intersect(lockset, locks);
			}
			break;
		case SHARED:
			lockset = intersect(lockset, locks);
			state = EreaserState.SHARED_MODIFIED;
			break;
		case SHARED_MODIFIED:
			lockset = intersect(lockset, locks);
			break;
		}
	}

	public Set<Integer> getLockset() {
		return (state == EreaserState.SHARED || state == EreaserState.SHARED_MODIFIED) ? lockset : null;
	}
}
