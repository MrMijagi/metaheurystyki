package sa;

import cvrp.Solution;
import main.RandomRange;

public class RandomNeighborOperators {

	public static Solution random_neighbor_swap(Solution solution) {
		RandomRange range = new RandomRange(solution.solution.size());
		
		Solution new_solution = new Solution(solution.solution);
		new_solution.swapFromIndices(range.from, range.to);
		
		return new_solution;
	}
	
	public static Solution random_neighbor_inverse(Solution solution) {
		RandomRange range = new RandomRange(solution.solution.size());
		
		Solution new_solution = new Solution(solution.solution);
		new_solution.inverseFromIndices(range.from, range.to);
		
		return new_solution;
	}

}
