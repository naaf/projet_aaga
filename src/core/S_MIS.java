package core;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
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

	public ArrayList<PointMIS> mis(ArrayList<Point> points, int edgeThreshold) {
		// init
		UDGraph< PointMIS> g = new UDGraph<>(edgeThreshold);
		ArrayList<PointMIS> psMIS = (ArrayList<PointMIS>) points.stream().map(p -> new PointMIS(p, Color.WHITE, false))
				.collect(Collectors.toList());
		psMIS.forEach(p -> {
			p.setNeighbors(g.neighbors(p, psMIS));
		});

		Random r = new Random();

		// We also designate a host as the leader.
		PointMIS pointDom = psMIS.get(r.nextInt(psMIS.size()));
		pointDom.setC(Color.BLACK);

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
			System.out.println("blanc " + psMIS.stream().filter(p -> p.getC() == Color.WHITE).count());
			pointDom = psMIS.stream().filter(p -> p.getC() == Color.WHITE && p.isActive())
					.max((u, v) -> Long.compare(u.degree(), v.degree())).get();
			pointDom.setC(Color.BLACK);

		}
		// in a unit disk graph, every node is adjacent to at most
		// five independent nodes
		psMIS.stream().forEach(p -> {
			long l = p.getNeighbors().stream().filter(dom -> dom.getC() == Color.BLACK).count();
			if (l <= 5) {
				System.out.println("invariant valide ==> mis " + l);
			} else {
				System.out.println("invariant invalide ==> mis " + l);
			}
		});

		return psMIS;
	}

	public static ArrayList<Point> toPoints(ArrayList<PointMIS> list) {
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
	public List<Point> constructCDS(List<Point> points) {

		return points;
	}

	public void algorithmA(List<PointMIS> psMIS) {

		List<PointMIS> psDom = psMIS.stream()
				.filter(p -> p.getC() == Color.BLACK)
				.collect(Collectors.toList());
		for (int i = 5; i >= 2; i--) {

			while (true) {
				if (psMIS.stream().noneMatch(p -> p.getC() == Color.GRAY)) {
					break;
				}

			}

		}
	}

	public void algoS_MIS() {

	}
}
