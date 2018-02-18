package instrumentation.objectid;

import java.util.ArrayList;
import java.util.List;

public class ObjectBehaviour {
	String className;
	int i;
	List<Integer> places = new ArrayList<>();

	@Override
	public String toString() {
		return "ObjectBehaviour [className=" + className + ", places=" + places + ", i="+ i +"]";
	}
}
