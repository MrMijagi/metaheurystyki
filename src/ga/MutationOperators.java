package ga;

import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;

public class MutationOperators {
	public static void mutation(Solution o1, double probability) {
		for (int i = 0; i < o1.solution.size(); i++) {
			// swap
			double random = ThreadLocalRandom.current().nextDouble();
			
			if (random < probability) {
				int random_index = ThreadLocalRandom.current().nextInt(0, o1.solution.size());
				
				if (random_index != i) {      // make sure not to swap the same element
					int tmp = o1.solution.get(i);
					o1.solution.set(i, o1.solution.get(random_index));
					o1.solution.set(random_index, tmp);
				}
			}
		}
	}
}
