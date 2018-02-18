package instrumentation.threadid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ThreadRenamer {
	private List<ThreadBehaviour> types = new ArrayList<>();

	private Map<Long, ThreadBehaviour> mapping;

	private Long translate(Long t) {
		ThreadBehaviour ti = mapping.get(t);
		for (int i = 0; i < types.size(); ++i) {
			if (types.get(i).methods.equals(ti.methods) && types.get(i).order == ti.order) {
				return Long.valueOf(i);
			}
		}
		types.add(ti);
		return Long.valueOf(types.size() - 1);
	}

	public <T> Set<T> translateWithMapping(Map<Long, ThreadBehaviour> mapping,
			BiFunction<Function<Long, Long>, T, T> body, Collection<T> tasks) {
		this.mapping = mapping;
		Set<T> result = tasks.stream().map(t -> body.apply(this::translate, t)).collect(Collectors.toSet());
		this.mapping = null;
		return result;
	}

	public List<ThreadBehaviour> gettypes() {
		return types;
	}
}
