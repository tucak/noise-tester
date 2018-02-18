package instrumentation.dupairs;

public class DUPairsTask {
	public int v; // variable
	public int p1; // write location
	public int p2; // read location
	public long t1; // thread 1
	public long t2; // thread 2

	public DUPairsTask(int v, int p1, int p2, long t1, long t2) {
		super();
		this.v = v;
		this.p1 = p1;
		this.p2 = p2;
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s, %s, t1=%s, t2=%s)", v, p1, p2, t1, t2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + p1;
		result = prime * result + p2;
		result = prime * result + (int) (t1 ^ (t1 >>> 32));
		result = prime * result + (int) (t2 ^ (t2 >>> 32));
		result = prime * result + v;
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
		DUPairsTask other = (DUPairsTask) obj;
		if (p1 != other.p1)
			return false;
		if (p2 != other.p2)
			return false;
		if (t1 != other.t1)
			return false;
		if (t2 != other.t2)
			return false;
		if (v != other.v)
			return false;
		return true;
	}

}
