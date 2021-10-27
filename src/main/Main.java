package main;

import java.text.SimpleDateFormat;
import java.util.Date;

import cvrp.CVRP;
import cvrp.Solution;
import cvrp.WrongInputFileFormat;
import solvers.GeneticAlgorithmSolver;
import solvers.GreedySolver;
import solvers.RandomSolver;
import solvers.Solver;

public class Main {
	
	// returns datetime prefix - useful for creating original files
	public static String getDatetimePrefix() {
		return new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss-SSS.").format(new Date());
	}
	
	public static void getRandomSolverStatistics(String file, CVRP cvrp, Solver solver) {
		try {
			cvrp.loadProblem("problems/" + file);
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
			System.out.println("Couldn't load the problem.");
		}
		
		System.out.println("\nProblem loaded.");
		
		for (int i = 0; i < 10; i++) {
			Solution solution = solver.find_solution();
			System.out.println("\nFound solution. Adding it to list.");
		}
		
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
		int all_problems_size = 8;
		
		CVRP cvrp = new CVRP();
		
		Solver gaSolver = new GeneticAlgorithmSolver(cvrp);
		gaSolver.load_configuration("configs/ga.txt");
		
		Solver randomSolver = new RandomSolver(cvrp);
		
		Solver greedySolver = new GreedySolver(cvrp);
		greedySolver.load_configuration("configs/greedy.txt");
		
		for (int i = 1; i < 2; i++) {
			solveProblem(all_problems[i], cvrp, randomSolver);
			//solveProblem(all_problems[i], cvrp, greedySolver);
			//solveProblem(all_problems[i], cvrp, gaSolver);
		}
	}
}
