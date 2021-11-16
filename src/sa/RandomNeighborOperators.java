package sa;

import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;

public class RandomNeighborOperators {

	public static Solution random_neighbor_swap(Solution solution) {
		int from = ThreadLocalRandom.current().nextInt(0, solution.solution.size());
		int to = ThreadLocalRandom.current().nextInt(0, solution.solution.size() - 1);
		
		if (from == to) to = solution.solution.size() - 1;
		
		Solution new_solution = new Solution(solution.solution);
		new_solution.swapFromIndices(from, to);
		
		return new_solution;
	}
	
	public static Solution random_neighbor_inverse(Solution solution) {
		int from = ThreadLocalRandom.current().nextInt(0, solution.solution.size());
		int to = ThreadLocalRandom.current().nextInt(0, solution.solution.size() - 1);
		
		if (from == to) to = solution.solution.size() - 1;
		
		Solution new_solution = new Solution(solution.solution);
		new_solution.inverseFromIndices(from, to);
		
		return new_solution;
	}

}
