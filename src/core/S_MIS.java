package core;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class S_MIS {

	private int edgeThreshold;

	public S_MIS(int edgeThreshold) {
		super();
		this.edgeThreshold = edgeThreshold;
	}

	/**
	 * first step of the algo S-MIS
	 * 
	 * @param points
	 * @return MIS
	 */
	public List<PointMIS> mis(List<Point> points) {
		if(!UDGraph.isConnexe(points, edgeThreshold))
			System.out.println(" erreur graphe n'est pas connexe ");
		// init
		ArrayList<PointMIS> psMIS = (ArrayList<PointMIS>) points.stream().map(p -> new PointMIS(p, Color.WHITE, false))
				.collect(Collectors.toList());
		psMIS.forEach(p -> {
			p.setNeighbors(neighbors(p, psMIS, edgeThreshold));
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

		}

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
	public ArrayList<Point> constructCDS(List<Point> points) {

		return toPoints(algorithmA(mis(points)));
	}

	public List<PointMIS> algorithmA(List<PointMIS> psMIS) {
		List<PointMIS> psBlack = new ArrayList<>(); List<PointMIS> psBlue = new ArrayList<>();
		PointMIS grayToBlue = null;
		int smallestID; int id = 1; int voisinID;
		for (PointMIS p : psMIS) {
			if (p.getC() == Color.BLACK) {
				psBlack.add(p);
				p.setId(id);
				id++;
			} else {
				p.setC(Color.GRAY);
			}
			p.setNeighbors(neighbors(p, psMIS, edgeThreshold));
		}
		for (int i = 5; i >= 2; i--) {
			while ((grayToBlue = grayConnectKBlack(psBlack, psMIS, i)) != null) {
				grayToBlue.setC(Color.BLUE);
				psBlue.add(grayToBlue);
				smallestID = grayToBlue.getNeighbors().stream().filter(n -> n.getC() == Color.BLACK).map(p -> p.getId())
						.min((u, v) -> Integer.compare(u, v)).get();
				grayToBlue.setId(smallestID);

				// update
				for (PointMIS voisin : grayToBlue.getNeighbors()) {
					if (voisin.getC() != Color.BLACK)
						continue;
					voisinID = voisin.getId();
					for (PointMIS pb : psBlack) {
						if (pb.getId() == voisinID) {
							pb.setId(smallestID);
						}
					}
					voisin.setId(smallestID);

				}

			}

		}
		psBlue.addAll(psBlack);
		return psBlue;

	}

	public static PointMIS grayConnectKBlack(List<PointMIS> psBlack, List<PointMIS> psMIS, int k) {

		for (PointMIS p : psMIS) {
			if (p.getC() != Color.GRAY) {
				continue;
			}
			long iblack = p.getNeighbors().stream().filter(node -> node.getC() == Color.BLACK)
					.map(node -> node.getId()).distinct().count();
			if (k == iblack) {
				return p;
			}
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
