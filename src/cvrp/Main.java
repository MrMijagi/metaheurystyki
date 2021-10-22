package cvrp;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
	
	// returns datetime prefix - useful for creating original files
	public static String get_datetime_prefix() {
		return new SimpleDateFormat("yyyy-MM-dd.HH:mm.").format(new Date());
	}

	public static void main(String[] args) {
		CVRP cvrp = new CVRP();
		
		try {
			//cvrp.loadProblem("problems/A-n32-k5.vrp");
			cvrp.loadProblem("problems/toy.vrp");
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
		}
		
		System.out.println("Loaded");
		
		//List<Integer> path = Arrays.asList(1, 4, -1, 3, 2, 5, -2, -3, -4, -5);
		//Solution solution = new Solution(path);
		
		//RandomSolver randomSolver = new RandomSolver(cvrp);
		//Solution solution = randomSolver.find_solution();
		
		//GreedySolver greedySolver = new GreedySolver(cvrp);
		//Solution solution = greedySolver.find_solution();
		
		GeneticAlgorithmSolver gaSolver = new GeneticAlgorithmSolver(cvrp);
		gaSolver.load_configuration(100, 100, 0.1, 0);
		Solution solution = gaSolver.find_solution();
		
		System.out.println("\nIs correct solution?");
		System.out.println(cvrp.check_if_correct_solution(solution));
		
		
		System.out.println("\nSolution cost:");
		System.out.println(cvrp.calculate_cost(solution));
		
		System.out.println("\nSaving solution.");
		cvrp.saveResult("solutions/toy.vrp", solution);
	}
}
