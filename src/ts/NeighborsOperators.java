package ts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;

public class NeighborsOperators {
	public static List<Solution> getNeighbors(Solution solution, int size) {
		List<Solution> neighbors = new ArrayList<Solution>();
		Solution swapped;
		
		for (int i = 0; i < solution.solution.size(); i++) {			
			for (int j = i + 1; j < solution.solution.size(); j++) {
				if (solution.solution.get(i) > 0 || solution.solution.get(j) > 0) {
					// copy the original one
					swapped = new Solution(solution.solution);
					// swap
					swapped.swapFromIndices(i, j);
					// add to list
					neighbors.add(swapped);
					
					// maximum size reached
					if (neighbors.size() == size) {
						return neighbors;
					}
				}
			}
		}
		
		return neighbors;
	}
	
	private static boolean checkIfSimilarNeighbor(List<Solution> neighbors, Solution swapped) {
		for (Solution neighbor: neighbors) {
			if (neighbor.solution == swapped.solution) {
				return true;
			}
		}
		
		return false;
	}
	
	public static List<Solution> getRandomNeighbors(Solution solution, int size) {
		List<Solution> neighbors = new ArrayList<Solution>();
		Solution swapped;
		
		while (neighbors.size() < size) {
			int from_index = ThreadLocalRandom.current().nextInt(0, solution.solution.size());
			int to_index = ThreadLocalRandom.current().nextInt(0, solution.solution.size());
			
			if (from_index != to_index) {
				// copy the original one
				swapped = new Solution(solution.solution);
				// swap
				//swapped.swapFromIndices(from_index, to_index);
				// inverse
				swapped.inverseFromIndices(from_index, to_index);
				// add to list
				neighbors.add(swapped);
				// add to list if there are no similar neighbors already
				//if (!checkIfSimilarNeighbor(neighbors, swapped)) {
				//	neighbors.add(swapped);
				//}
			}
		}
		
		return neighbors;
	}
}
