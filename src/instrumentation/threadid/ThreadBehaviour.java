package instrumentation.threadid;

import java.util.LinkedList;
import java.util.List;

public class ThreadBehaviour {
	public int order;
	public List<Integer> methods;

	public ThreadBehaviour(int order) {
		super();
		this.order = order;
		this.methods = new LinkedList<>();
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", order, methods);
	}

}
