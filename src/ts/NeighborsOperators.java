package ts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;
import main.RandomRange;

public class NeighborsOperators {
	public static List<Solution> getNeighborsSwap(Solution solution, int size) {
		List<Solution> neighbors = new ArrayList<Solution>();
		Solution swapped;
		RandomRange range;
		
		while (neighbors.size() < size) {
			range = new RandomRange(solution.solution.size());
			
			swapped = new Solution(solution.solution);
			swapped.swapFromIndices(range.from, range.to);
			
			neighbors.add(swapped);
		}
		
		return neighbors;
	}
	
	public static List<Solution> getNeighborsInverse(Solution solution, int size) {
		List<Solution> neighbors = new ArrayList<Solution>();
		Solution swapped;
		RandomRange range;
		
		while (neighbors.size() < size) {
			range = new RandomRange(solution.solution.size());
			
			swapped = new Solution(solution.solution);
			swapped.inverseFromIndices(range.from, range.to);
			
			neighbors.add(swapped);
		}
		
		return neighbors;
	}
}
