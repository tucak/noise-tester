package instrumentation.goodlock;

public class GoodLockTask {
	public int pl1;
	public int pl2;
	public int l1;
	public int l2;
	public long t;
	
	public GoodLockTask(int pl1, int pl2, int l1, int l2, long t) {
		super();
		this.pl1 = pl1;
		this.pl2 = pl2;
		this.l1 = l1;
		this.l2 = l2;
		this.t = t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + l1;
		result = prime * result + l2;
		result = prime * result + pl1;
		result = prime * result + pl2;
		result = prime * result + (int) (t ^ (t >>> 32));
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
		GoodLockTask other = (GoodLockTask) obj;
		if (l1 != other.l1)
			return false;
		if (l2 != other.l2)
			return false;
		if (pl1 != other.pl1)
			return false;
		if (pl2 != other.pl2)
			return false;
		if (t != other.t)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GoodLockTask [pl1=" + pl1 + ", pl2=" + pl2 + ", l1=" + l1 + ", l2=" + l2 + ", t=" + t + "]";
	}
	
}
