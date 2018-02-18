package instrumentation.concurpairs;

public class ConcurPairsTask {
	public int p1;
	public int p2;
	public boolean switched;

	public ConcurPairsTask(int p1, int p2, boolean switched) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.switched = switched;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d, %b)", p1, p2, switched);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + p1;
		result = prime * result + p2;
		result = prime * result + (switched ? 1231 : 1237);
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
		ConcurPairsTask other = (ConcurPairsTask) obj;
		if (p1 != other.p1)
			return false;
		if (p2 != other.p2)
			return false;
		if (switched != other.switched)
			return false;
		return true;
	}
}