package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UDGraph<T extends Point> {
	private int edgeThreshold;

	public UDGraph(int threshold) {
		this.edgeThreshold = threshold;
	}

	public List<T> neighbors(T point, List<T> vertices) {
		List<T> neighbors = new ArrayList<T>();
		for (T p : vertices) {
			if (!point.equals(p) && point.distance(p) <= this.edgeThreshold)
				neighbors.add(p);
		}
		return neighbors;
	}

	public Point mostPowerful(List<T> vertices) {
		T most = vertices.get(0);
		for (T p : vertices) {
			if (!p.equals(most) && neighbors(most, vertices).size() < neighbors(p, vertices).size())
				most = p;
		}
		return most;
	}

	public List<T> getFreeVertices(List<T> vertices) {
		return vertices.stream().filter(p -> (neighbors(p, vertices)).size() == 0).collect(Collectors.toList());
	}

	public boolean isValid(List<T> ds, List<T> vertices) {
		List<T> points = new ArrayList<>(vertices);
		for(T p : ds){
			points.removeAll(neighbors(p, points));
			points.remove(p);
		}
		return points.isEmpty();
	}
}
