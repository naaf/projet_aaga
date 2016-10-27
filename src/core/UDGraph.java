package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class UDGraph {

	public static <T extends Point> List<T> neighbors(T point, List<T> vertices, int edgeThreshold) {
		List<T> neighbors = new ArrayList<T>();
		for (T p : vertices) {
			if (!point.equals(p) && point.distance(p) <= edgeThreshold)
				neighbors.add(p);
		}
		return neighbors;
	}
	
	public static boolean isConnexe(List<Point> points, int edgeThreshold) {
		List<Point> f = new ArrayList<Point>();
		List<Point> marques = new ArrayList<Point>();
		f.add(points.get(0));
		Point s = null;

		while (!f.isEmpty()) {
			s = f.remove(0);
			for (Point p : UDGraph.neighbors(s, points, edgeThreshold)) {
				if (!marques.contains(p)) {
					f.add(p);
					marques.add(p);
				}
			}
		}

		return marques.stream().distinct().count() == points.stream().distinct().count();
	}
	
}
