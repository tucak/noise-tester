package instrumentation.ereaser;

import java.util.Set;

public class EreaserTask {
	public int pl;
	public int var;
	public EreaserState state;
	public Set<Integer> lockset;
	public long thread;
	
	public EreaserTask(int pl, int var, EreaserState state,
			Set<Integer> lockset, long thread) {
		super();
		this.pl = pl;
		this.var = var;
		this.state = state;
		this.lockset = lockset;
		this.thread = thread;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lockset == null) ? 0 : lockset.hashCode());
		result = prime * result + pl;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + (int) (thread ^ (thread >>> 32));
		result = prime * result + var;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EreaserTask other = (EreaserTask) obj;
		if (lockset == null) {
			if (other.lockset != null)
				return false;
		} else if (!lockset.equals(other.lockset))
			return false;
		if (pl != other.pl)
			return false;
		if (state != other.state)
			return false;
		if (thread != other.thread)
			return false;
		if (var != other.var)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EreaserTask [pl=" + pl + ", var=" + var + ", state=" + state + ", lockset=" + lockset + ", thread="
				+ thread + "]";
	}
	
	
}
