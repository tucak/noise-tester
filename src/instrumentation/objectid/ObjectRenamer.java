package instrumentation.objectid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectRenamer {
	private List<ObjectBehaviour> types = new ArrayList<ObjectBehaviour>();
	private Map<Integer, ObjectBehaviour> mapping;

	private Integer translate(Integer o) {
		ObjectBehaviour id = mapping.get(o);
		for (int i = 0; i < types.size(); ++i) {
			if (types.get(i).places.equals(id.places) && types.get(i).className == id.className
					&& types.get(i).i == id.i) {
				return i;
			}
		}
		types.add(id);
		return types.size() - 1;
	}

	public <T> Set<T> translateWithMapping(Map<Integer, ObjectBehaviour> mapping,
			BiFunction<Function<Integer, Integer>, T, T> body, Collection<T> tasks) {
		this.mapping = mapping;
		Set<T> result = tasks.stream().map((t) -> body.apply(this::translate, t)).collect(Collectors.toSet());
		this.mapping = null;
		return result;
	}

	public List<ObjectBehaviour> getTypes() {
		return this.types;
	}
}
