package instrumentation.avio;

public class LastAccess {
	public int pl;
	public AccessType type;
	public long t;
	public LastAccess read;
	
	public LastAccess(int pl, AccessType type, long t) {
		super();
		this.pl = pl;
		this.type = type;
		this.t = t;
	}

	@Override
	public String toString() {
		return String.format("LastAccess [pl=%s, type=%s, t=%s, read=%s]", pl,
				type, t, read);
	}
}
