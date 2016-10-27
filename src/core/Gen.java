package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import algorithms.DefaultTeam;

public class Gen {

	private static int MAX_X = 1153;
	private static int MAX_Y = 842;

	public static void genTestBeds(int edgeThreshold) {
		List<Point> list = null;

		for (int i = 0; i < 100; i++) {
			list = Gen.getPointGen(edgeThreshold);
			DefaultTeam.saveToFile("testbeds/input" + i, list);
			System.out.println("fichier " + i + " >>>  genere ...");
		}
	}

	public static List<Point> getPointGen(int edgeThreshold) {
		List<Point> result = genListPoints();
		if (UDGraph.isConnexe(result, edgeThreshold)) {
			return result;
		}
		System.out.println(" gen list non connexe ...");
		return getPointGen(edgeThreshold);
	}

	private static List<Point> genListPoints() {
		Random rand = new Random((new GregorianCalendar()).getTimeInMillis());
		List<Point> result = new ArrayList<>();
		Point s = null;
		boolean cont = true;
		for (int i = 0; i < 1000; i++) {
			do {
				s = new Point(rand.nextInt(MAX_X), rand.nextInt(MAX_Y));
				cont = result.contains(s);
			} while (cont);
			result.add(s);
			cont = true;
		}
		return result;

	}
}
