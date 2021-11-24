package ga;

import java.util.Arrays;
import java.util.List;

import cvrp.Solution;

public class OperatorsTests {
	private static int distanceBetweenRoutes(Solution p1, Solution p2) {
		int sum = 0;
		
		for (int i = 0; i < p1.solution.size(); i++) {
			if (p1.solution.get(i) != p2.solution.get(i)) {
				sum++;
			}
		}
		
		return sum;
	}
	
	public static void main(String[] args) {
		List<Integer> route1 = Arrays.asList(
			1, 2, 3, 4, 5, -1, -2, -3, -4, -5
		);
		
		List<Integer> route2 = Arrays.asList(
			-1, -2, -3, -4, -5, 1, 2, 3, 4, 5
		);
		
		Solution p1 = new Solution(route1);
		Solution p2 = new Solution(route1);
		
		System.out.println("p1: " + p1.solution);
		
		for (double i = 0.; i < 1.1; i += 0.1) {
			MutationOperators.swap(p1, i);
			System.out.println("\nswap " + i + ":" + p1.solution);
			System.out.println("distance between p1 and p2: " + OperatorsTests.distanceBetweenRoutes(p1, p2));
			p1 = new Solution(route1);
		}
		
		for (double i = 0; i < 1.1; i += 0.1) {
			MutationOperators.inverse(p1, i);
			System.out.println("\ninverse " + i + ":" + p1.solution);
			System.out.println("distance between p1 and p2: " + OperatorsTests.distanceBetweenRoutes(p1, p2));
			p1 = new Solution(route1);
		}
		
		p2 = new Solution(route2);
		
		System.out.println("\np1: " + p1.solution);
		System.out.println("p2: " + p2.solution);
		
		List<Solution> children = CrossoverOperators.crossover(p1, p2);
		
		System.out.println("\nchildren of p1 x p2:");
		
		for (Solution solution: children) {
			System.out.println(solution.solution);
		}
	}
}
