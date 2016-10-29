package algorithms;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import core.AlgoDominant;
import core.S_MIS;
import core.UDGraph;
import steiner.AlgoSteiner;

public class DefaultTeam {
	public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
		ArrayList<Point> result = null;
		ArrayList<Point> resultBlack;
		// Gen.genTestBeds(edgeThreshold);
		// lancerTests(edgeThreshold);
		System.out.println("point plus connectes ==> " + UDGraph.neighbors(UDGraph.getPPC(points, edgeThreshold),points, edgeThreshold).size());
		S_MIS mis = new S_MIS(edgeThreshold);
		resultBlack = (ArrayList<Point>) mis.mis(points).stream().filter(p -> p.getC() == Color.BLACK)
				.map(p -> (Point) p).collect(Collectors.toList());
		result = mis.constructCDS(points);
		System.out.println("is connexe " + UDGraph.isConnexe(result, edgeThreshold));
		System.out.println(" result " + result.size());
		return result;
	}

	public void testSMIS(int edgeThreshold) {
		final String fichier = "testbeds/input";
		S_MIS mis = new S_MIS(edgeThreshold);
		long time = 0;
		List<Point> pointsMis = new ArrayList<>();
		List<Point> testsSMIS = new ArrayList<>();
		List<Point> points;
		for (int i = 0; i < 100; i++) {
			points = readFromFile(fichier + i + ".points");
			time = System.currentTimeMillis();
			pointsMis = mis.constructCDS(points);
			time = System.currentTimeMillis() - time;
			testsSMIS.add(new Point(pointsMis.size(), (int) time));
			System.out.println(fichier + i + " >>> fin teste  smis " + pointsMis.size());
		}
		saveToFile("test_resultat/smis", testsSMIS);
	}

	public List<Point> algoNaive(List<Point> points, int edgeThreshold) {
		List<Point> pointsSteiner = new ArrayList<>();
		List<Point> dom;

		AlgoDominant algo = new AlgoDominant(edgeThreshold);
		AlgoSteiner algoSteiner = new AlgoSteiner();

		dom = algo.calculDominatingSet((ArrayList<Point>) points);
		pointsSteiner = algoSteiner.calculSteiner(points, edgeThreshold, dom).getList();
		pointsSteiner.addAll(dom);

		return pointsSteiner.stream().distinct().collect(Collectors.toList());
	}

	public void testAlgoNaive(int edgeThreshold) {
		final String fichier = "testbeds/input";
		long time = 0;
		List<Point> testsSteiner = new ArrayList<>();
		List<Point> pointsSteiner;
		List<Point> points;
		for (int i = 0; i < 100; i++) {
			points = readFromFile(fichier + i + ".points");
			time = System.currentTimeMillis();
			pointsSteiner = algoNaive(points, edgeThreshold);
			time = System.currentTimeMillis() - time;

			testsSteiner.add(new Point(pointsSteiner.size(), (int) time));
			System.out.println(fichier + i + " >>> fin teste  steiner >>> " + pointsSteiner.size());
		}
		saveToFile("test_resultat/steiner", testsSteiner);
	}

	public void lancerTests(int edgeThreshold) {
		testAlgoNaive(edgeThreshold);
		testSMIS(edgeThreshold);
		System.out.println(" fin total tests >>>>>> ");

	}

	// FILE PRINTER
	public static void saveToFile(String filename, List<Point> result) {
		int index = 0;
		try {
			while (true) {
				BufferedReader input = new BufferedReader(
						new InputStreamReader(new FileInputStream(filename + ".points")));
				try {
					input.close();
				} catch (IOException e) {
					System.err.println(
							"I/O exception: unable to close " + filename + Integer.toString(index) + ".points");
				}
				index++;
			}
		} catch (FileNotFoundException e) {
			printToFile(filename + ".points", result);
		}
	}

	public static void saveToFile2(String filename, List<String> result) {
		int index = 0;
		try {
			while (true) {
				BufferedReader input = new BufferedReader(
						new InputStreamReader(new FileInputStream(filename + ".points")));
				try {
					input.close();
				} catch (IOException e) {
					System.err.println(
							"I/O exception: unable to close " + filename + Integer.toString(index) + ".points");
				}
				index++;
			}
		} catch (FileNotFoundException e) {
			printToFile2(filename + ".points", result);
		}
	}

	public static void printToFile(String filename, List<Point> points) {
		try {
			PrintStream output = new PrintStream(new FileOutputStream(filename));
			int x, y;
			for (Point p : points)
				output.println(Integer.toString((int) p.getX()) + " " + Integer.toString((int) p.getY()));
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("I/O exception: unable to create " + filename);
		}
	}

	public static void printToFile2(String filename, List<String> points) {
		try {
			PrintStream output = new PrintStream(new FileOutputStream(filename));
			for (String p : points)
				output.println(p);
			output.close();
		} catch (FileNotFoundException e) {
			System.err.println("I/O exception: unable to create " + filename);
		}
	}

	// FILE LOADER
	public List<Point> readFromFile(String filename) {
		String line;
		String[] coordinates;
		ArrayList<Point> points = new ArrayList<Point>();
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			try {
				while ((line = input.readLine()) != null) {
					coordinates = line.split("\\s+");
					points.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
				}
			} catch (IOException e) {
				System.err.println("Exception: interrupted I/O.");
			} finally {
				try {
					input.close();
				} catch (IOException e) {
					System.err.println("I/O exception: unable to close " + filename);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Input file not found.");
		}
		return points;
	}
}
