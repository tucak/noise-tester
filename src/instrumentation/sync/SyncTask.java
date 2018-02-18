package instrumentation.sync;

public class SyncTask {
	int pl; // Program location
	SyncEvent type;

	public SyncTask(int pl, SyncEvent type) {
		super();
		this.pl = pl;
		this.type = type;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", pl, type);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pl;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		SyncTask other = (SyncTask) obj;
		if (pl != other.pl)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
