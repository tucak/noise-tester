package instrumentation.avio;

public class AvioTask {
	public int pl1;
	public int pl2;
	public int pl3;

	public long t1;
	public long t2;

	public AvioTask(int pl1, int pl2, int pl3, long t1, long t2) {
		super();
		this.pl1 = pl1;
		this.pl2 = pl2;
		this.pl3 = pl3;
		this.t1 = t1;
		this.t2 = t2;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s, %s, t1=%s, t2=%s)", pl1, pl2, pl3, t1,
				t2);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pl1;
		result = prime * result + pl2;
		result = prime * result + pl3;
		result = prime * result + (int) (t1 ^ (t1 >>> 32));
		result = prime * result + (int) (t2 ^ (t2 >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AvioTask other = (AvioTask) obj;
		if (pl1 != other.pl1)
			return false;
		if (pl2 != other.pl2)
			return false;
		if (pl3 != other.pl3)
			return false;
		if (t1 != other.t1)
			return false;
		if (t2 != other.t2)
			return false;
		return true;
	}
}
