package ga;

import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;

public class MutationOperators {
	public static void swap(Solution o1, double probability) {
		for (int i = 0; i < o1.solution.size(); i++) {
			// swap
			double random = ThreadLocalRandom.current().nextDouble();
			
			if (random < probability) {
				int random_index = ThreadLocalRandom.current().nextInt(0, o1.solution.size());
				
				if (random_index != i) {      // make sure not to swap the same element
					o1.swapFromIndices(i, random_index);
				}
			}
		}
	}
	
	public static void inverse(Solution o1, double probability) {
		double random = ThreadLocalRandom.current().nextDouble();
		
		if (random < probability) {
			int from_index = ThreadLocalRandom.current().nextInt(0, o1.solution.size());
			int to_index = ThreadLocalRandom.current().nextInt(0, o1.solution.size() - 1);
			
			if (to_index == from_index) {
				to_index = o1.solution.size() - 1;
			}
			
			o1.inverseFromIndices(from_index, to_index);
		}
	}
}
