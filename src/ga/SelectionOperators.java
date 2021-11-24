package ga;

import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;

public class SelectionOperators {
	public static Solution tournament(Solution[] pop, int selection_param) {
		int random_index = ThreadLocalRandom.current().nextInt(0, selection_param + 1);
		Solution best_solution = pop[random_index];
		
		for (int i = 0; i < selection_param - 1; i++) {
			random_index = ThreadLocalRandom.current().nextInt(0, selection_param + 1);
			
			if (pop[random_index].evaluation < best_solution.evaluation) {
				best_solution = pop[random_index];
			}
		}
		
		return best_solution;
	}
	
	public static Solution roulette(Solution[] pop, int selection_param) {
		RandomCollection<Solution> random_collection = new RandomCollection<Solution>();
	}
}
