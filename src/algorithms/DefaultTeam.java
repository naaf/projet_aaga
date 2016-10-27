package algorithms;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import steiner.AlgoSteiner;
import steiner.Tree2D;
import core.AlgoDominant;
import core.S_MIS;

public class DefaultTeam {
	public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {

		lancerTests(edgeThreshold);
		ArrayList<Point> result;
		S_MIS mis = new S_MIS(edgeThreshold);
		result = mis.constructCDS(points);
		return result;
	}

//	public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeThreshold) {
//		System.out.println("seuil " + edgeThreshold);
//		ArrayList<Point> result = new ArrayList<Point>();
//		long time = System.currentTimeMillis();
//		AlgoDominant algo = new AlgoDominant(edgeThreshold);
//		ArrayList<Point> dom = algo.calculDominatingSet(points);
//		AlgoSteiner algoSteiner = new AlgoSteiner();
//		Tree2D resultSteiner = algoSteiner.calculSteiner(points, edgeThreshold, dom);
//		time = System.currentTimeMillis()-time;
//		System.out.println(">>>>>>> time " + time);
//		result.addAll(dom);
//		result.addAll(resultSteiner.getList());
//		Set<Point> s = new HashSet<Point>(result);
//
//		return new ArrayList<Point>(s);
//
//	}
	
	public void lancerTests(int edgeThreshold){
		final String fichier = "testbeds/input";
		List<Point> points = null;
		S_MIS mis = new S_MIS(edgeThreshold);
		AlgoSteiner algoSteiner ;
		AlgoDominant algo = new AlgoDominant(edgeThreshold);;
		long time = 0;
		// point.x ==> size of mis ,  point.y ==> time execution
		List<Point> pointsMis = new ArrayList<>();
		List<Point> pointsSteiner = new ArrayList<>();
		List<Point> testsSMIS = new ArrayList<>();
		List<Point> testsSteiner = new ArrayList<>();
		List<Point> dom;
		Tree2D resultSteiner;
		Set<Point> s ;
		for (int i = 0; i < 100; i++) {
			points = readFromFile( fichier + i + ".points");
			System.out.println(" debut test smis" + i + " size >>" + points.size());
			algoSteiner = new AlgoSteiner();
			
			// smis
//			time = System.currentTimeMillis();
//			pointsMis = mis.constructCDS(points);
//			time = System.currentTimeMillis()-time;
//			testsSMIS.add(new Point(pointsMis.size(),(int) time));
//			System.out.println(fichier + i + " >>> fin teste  smis...");
			
			// steiner
			time = System.currentTimeMillis();
			dom = algo.calculDominatingSet((ArrayList<Point>) points);
			resultSteiner = algoSteiner.calculSteiner(points, edgeThreshold, dom);
			pointsSteiner.addAll(dom);
			pointsSteiner.addAll(resultSteiner.getList());
			s = new HashSet<Point>(pointsSteiner);
			pointsSteiner = new  ArrayList<Point>(s);
			time = System.currentTimeMillis()-time;
			testsSteiner.add(new Point(pointsSteiner.size(),(int) time));
			System.out.println(fichier + i + " >>> fin teste  steiner...");
		}
		System.out.println(" fin total tests >>>>>> ");
		saveToFile("test_resultat/steiner", testsSteiner);
//		saveToFile("test_resultat/smis", testsSteiner);
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
