package core;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class S_MIS {

	/**
	 * first step of the algo S-MIS
	 * 
	 * @param points
	 * @return MIS
	 */

	public List<PointMIS> mis(List<Point> points, int edgeThreshold) {
		// init
		ArrayList<PointMIS> psMIS = (ArrayList<PointMIS>) points.stream().map(p -> new PointMIS(p, Color.WHITE, false))
				.collect(Collectors.toList());
		psMIS.forEach(p -> {
			p.setNeighbors(neighbors(p, psMIS, edgeThreshold));
		});
		int id = 1;
		Random r = new Random();

		// We also designate a host as the leader.
		PointMIS pointDom = psMIS.get(r.nextInt(psMIS.size()));
		pointDom.setC(Color.BLACK);
		pointDom.setId(id++);
		while (true) {
			if (psMIS.stream().noneMatch(p -> p.getC() == Color.WHITE)) {
				break;
			}

			// black broadcasts message DOMINATOR
			for (PointMIS p : pointDom.getNeighbors()) {
				if (p.getC() == Color.WHITE) {
					p.setC(Color.GRAY);
					p.setDom(pointDom);

					// white broadcasts message DOMINATEE.
					p.getNeighbors().stream().filter(q -> q.getC() == Color.WHITE).forEach(q -> {
						q.setActive(true);
					});
				}
			}

			// An active white host with highest (d*, id)
			// among all of its active white neighbors will color itself black
			
			pointDom = psMIS.stream().filter(p -> p.getC() == Color.WHITE && p.isActive())
					.max((u, v) -> Long.compare(u.degree(), v.degree())).get();
			pointDom.setC(Color.BLACK);
			pointDom.setId(id++);

		}
		// in a unit disk graph, every node is adjacent to at most
		// five independent nodes
		

		return psMIS;
	}

	public static ArrayList<Point> toPoints(List<PointMIS> list) {
		ArrayList<Point> points = new ArrayList<>();
		for (PointMIS p : list) {
			points.add(p);
		}
		return points;
	}

	/**
	 * second step of the algo S-MIS from CMSD
	 * 
	 * @param pointsMIS
	 * @return points CDS would have size bounded by (4.8 + ln 5)opt + 1:2.
	 */
	public ArrayList<Point> constructCDS(List<Point> points, int edgeThreshold) {

		return toPoints(algorithmA(mis(points, edgeThreshold)));
	}

	public List<PointMIS> algorithmA(List<PointMIS> psMIS) {

		List<PointMIS> psBlack = psMIS.stream().filter(p -> p.getC() == Color.BLACK).collect(Collectors.toList());
		PointMIS grayToBlue = null;
		List<PointMIS> psBlue = new ArrayList<>();
		for (int i = 5; i >= 2; i--) {

			while ((grayToBlue = grayConnectKBlack(psBlack, psMIS, i)) != null) {
				grayToBlue.setC(Color.BLUE);
				psBlue.add(grayToBlue);
				
			}

		}
		 psBlue.addAll(psBlack);
		 return psBlue;
		
	}

	private PointMIS grayConnectKBlack(List<PointMIS> psBlack, List<PointMIS> psMIS, int k) {
		HashSet<Integer> ids = new HashSet<Integer>();
		for (PointMIS p : psMIS) {
			if (p.getC() != Color.GRAY) {
				continue;
			}
			p.getNeighbors().stream().filter(node -> node.getC() == Color.BLACK).forEach( q->{
				ids.add(q.getId());
			});
			if (k == ids.size())
				return p;
		}
		return null;
	}

	List<PointMIS> neighbors(PointMIS p, List<PointMIS> vertices, int edgeThreshold) {
		ArrayList<PointMIS> result = new ArrayList<PointMIS>();

		for (PointMIS point : vertices)
			if (point.distance(p) <= edgeThreshold && !point.equals(p))
				result.add(point);

		return result;
	}
	
}
