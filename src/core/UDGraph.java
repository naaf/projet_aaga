package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
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
	
	/**
	 * 
	 * @param points
	 * @param edgeThreshold
	 * @return point the node most connected
	 */
	public static <T extends Point> T getPPC(List<T> points,  int edgeThreshold) {
		final Comparator<T> comp = (p1, p2) -> Integer.compare(neighbors(p1, points, edgeThreshold).size(),
				neighbors(p2, points, edgeThreshold).size());
		return points.stream().max(comp).get();
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
