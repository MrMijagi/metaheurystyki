package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import cvrp.CVRP;
import cvrp.Solution;
import cvrp.WrongInputFileFormat;
import solvers.GeneticAlgorithmSolver;
import solvers.GreedySolver;
import solvers.RandomSolver;
import solvers.SimulatedAnnealingSolver;
import solvers.Solver;
import solvers.TabuSearchSolver;

public class Main {
	
	// returns datetime prefix - useful for creating original files
	public static String getDatetimePrefix() {
		return new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss-SSS.").format(new Date());
	}
	
	public static void saveStatistics(double[] values, int array_size, String filename) {
		double sum = values[0], best = values[0], worst = values[0];
		
		for (int i = 1; i < array_size; i++) {
			sum += values[i];
			if (values[i] < best) best = values[i];
			if (values[i] > worst) worst = values[i];
		}
		
		double avg = sum / array_size;
		double std = 0.;
		
		for (int i = 0; i < array_size; i++) {
			std += Math.pow((values[i] - avg), 2);
		}
		
		std = Math.sqrt(std / array_size);
		
		// save to file
		StringBuilder string_to_save = new StringBuilder();
		
		string_to_save.append("best;worst;average;std\n")
			.append(best)
			.append(";")
			.append(worst)
			.append(";")
			.append(avg)
			.append(";")
			.append(std)
			.append("\n");
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));
			writer.write(string_to_save.toString());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getRandomSolverStatistics(String file, String file_to_save, int how_many_solutions) {
		CVRP cvrp = new CVRP();
		
		try {
			cvrp.loadProblem("problems/" + file);
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
			System.out.println("Couldn't load the problem.");
		}
		
		Solver solver = new RandomSolver(cvrp);
		System.out.println("\nProblem loaded.");
		
		double[] evaluations = new double[how_many_solutions];
		Solution solution;
		
		for (int i = 0; i < how_many_solutions; i++) {
			solution = solver.find_solution();
			evaluations[i] = cvrp.calculateCost(solution);
		}
		
		saveStatistics(evaluations, how_many_solutions, file_to_save);
	}

	public static void getGreedySolverStatistics(String file, String file_to_save) {
		CVRP cvrp = new CVRP();
		
		try {
			cvrp.loadProblem("problems/" + file);
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
			System.out.println("Couldn't load the problem.");
		}
		
		Solver solver;
		System.out.println("\nProblem loaded.");
		
		double[] evaluations = new double[cvrp.getDimension()];
		Solution solution;
		
		for (int i = 0; i < cvrp.getDimension(); i++) {
			solver = new GreedySolver(cvrp, i);
			solution = solver.find_solution();
			evaluations[i] = cvrp.calculateCost(solution);
		}
		
		saveStatistics(evaluations, cvrp.getDimension(), file_to_save);
	}

	public static void getGeneticAlgorithmSolverStatistics(String file, String config_file, String file_to_save, int how_many_solutions) {
		CVRP cvrp = new CVRP();
		
		try {
			cvrp.loadProblem("problems/" + file);
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
			System.out.println("Couldn't load the problem.");
		}
		
		Solver solver;
		System.out.println("\nProblem loaded.");
		
		double[] evaluations = new double[how_many_solutions];
		Solution solution;
		
		for (int i = 0; i < how_many_solutions; i++) {
			solver = new GeneticAlgorithmSolver(cvrp);
			solver.load_configuration(config_file);
			
			do {
				solution = solver.find_solution();
				System.out.println(cvrp.checkIfCorrectSolution(solution));
			} while(!cvrp.checkIfCorrectSolution(solution));
			
			evaluations[i] = cvrp.calculateCost(solution);
		}
		
		saveStatistics(evaluations, how_many_solutions, file_to_save);
	}

	public static void getTabuSearchSolverStatistics(String file, String config_file, String file_to_save, int how_many_solutions) {
		CVRP cvrp = new CVRP();
		
		try {
			cvrp.loadProblem("problems/" + file);
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
			System.out.println("Couldn't load the problem.");
		}
		
		Solver solver;
		System.out.println("\nProblem loaded.");
		
		double[] evaluations = new double[how_many_solutions];
		Solution solution;
		
		for (int i = 0; i < how_many_solutions; i++) {
			solver = new TabuSearchSolver(cvrp);
			solver.load_configuration(config_file);
			
			do {
				solution = solver.find_solution();
				System.out.println(cvrp.checkIfCorrectSolution(solution));
			} while(!cvrp.checkIfCorrectSolution(solution));
			
			evaluations[i] = cvrp.calculateCost(solution);
		}
		
		saveStatistics(evaluations, how_many_solutions, file_to_save);
	}

	public static void getSimulatedAnnealingStatistics(String file, String config_file, String file_to_save, int how_many_solutions) {
		CVRP cvrp = new CVRP();
		
		try {
			cvrp.loadProblem("problems/" + file);
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
			System.out.println("Couldn't load the problem.");
		}
		
		Solver solver;
		System.out.println("\nProblem loaded.");
		
		double[] evaluations = new double[how_many_solutions];
		Solution solution;
		
		for (int i = 0; i < how_many_solutions; i++) {
			solver = new SimulatedAnnealingSolver(cvrp);
			solver.load_configuration(config_file);
			
			do {
				solution = solver.find_solution();
				System.out.println(cvrp.checkIfCorrectSolution(solution));
			} while(!cvrp.checkIfCorrectSolution(solution));
			
			evaluations[i] = cvrp.calculateCost(solution);
		}
		
		saveStatistics(evaluations, how_many_solutions, file_to_save);
	}
	
	public static void solveProblem(String file, CVRP cvrp, Solver solver) {
		try {
			cvrp.loadProblem("problems/" + file);
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
			System.out.println("Couldn't load the problem.");
		}
		
		System.out.println("\nProblem loaded.");
		
		Solution solution = solver.find_solution();
		
		System.out.println("\nFound solution. Is it correct?");
		System.out.println(cvrp.checkIfCorrectSolution(solution));
		
		String file_out = getDatetimePrefix() + file;
		
		System.out.println("\nSaving solution to: " + file_out);
		cvrp.saveResult("solutions/" + file_out, solution);
	}
	
	public static void testingGrounds() {
		String[] all_problems = {
				"toy.vrp",
				"A-n32-k5.vrp",
				"A-n37-k6.vrp",
				"A-n39-k5.vrp",
				"A-n45-k6.vrp",
				"A-n48-k7.vrp",
				"A-n54-k7.vrp",
				"A-n60-k9.vrp"
		};
		int all_problems_size = 8;
		
		CVRP cvrp = new CVRP();
		
		Solver gaSolver = new GeneticAlgorithmSolver(cvrp);
		gaSolver.load_configuration("configs/ga.txt");
		
		Solver randomSolver = new RandomSolver(cvrp);
		
		Solver greedySolver = new GreedySolver(cvrp);
		greedySolver.load_configuration("configs/greedy.txt");
		
		Solver tsSolver = new TabuSearchSolver(cvrp);
		tsSolver.load_configuration("configs/ts.txt");
		
		Solver saSolver = new SimulatedAnnealingSolver(cvrp);
		saSolver.load_configuration("configs/sa.txt");
		
		for (int i = 1; i < 2; i++) {
			//solveProblem(all_problems[i], cvrp, randomSolver);
			//solveProblem(all_problems[i], cvrp, greedySolver);
			//solveProblem(all_problems[i], cvrp, gaSolver);
			//solveProblem(all_problems[i], cvrp, tsSolver);
			solveProblem(all_problems[i], cvrp, saSolver);
		}
	}

	public static void main(String[] args) {
		String[] all_problems = {
				"toy.vrp",
				"A-n32-k5.vrp",
				"A-n37-k6.vrp",
				"A-n39-k5.vrp",
				"A-n45-k6.vrp",
				"A-n48-k7.vrp",
				"A-n54-k7.vrp",
				"A-n60-k9.vrp"
		};
		
		testingGrounds();
//		
//		CVRP cvrp = new CVRP();
//		
//		try {
//			cvrp.loadProblem("problems/toy.vrp");
//		} catch (WrongInputFileFormat e) {
//			e.printStackTrace();
//		}
//		
//		Solution solution = new Solution(Arrays.asList(
//			1, 4, -1, 3, 2, 5, -4, -4, -4, -4
//		));
//		
//		System.out.println("calculated cost: " + cvrp.calculateCost(solution));
		
		for (int i = 1; i < 8; i++) {
			//getRandomSolverStatistics(all_problems[i], "stats/randomStats" + all_problems[i] + ".csv", 10000);
			//getGreedySolverStatistics(all_problems[i], "stats/greedyStats" + all_problems[i] + ".csv");
			//getGeneticAlgorithmSolverStatistics(all_problems[i], "configs/ga.txt", "stats/gaStats" + all_problems[i] + ".csv", 10);
			//getTabuSearchSolverStatistics(all_problems[i], "configs/ts.txt", "stats/tsStats" + all_problems[i] + ".csv", 10);
			//getSimulatedAnnealingStatistics(all_problems[i], "configs/sa.txt", "stats/saStats" + all_problems[i] + ".csv", 10);
			//System.out.println(i);
		}
	}
}
