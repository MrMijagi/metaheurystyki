package ga;

import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;

public class SelectionOperators {
	public static Solution tournament(Solution[] pop, int pop_size, int selection_param) {
		int random_index = ThreadLocalRandom.current().nextInt(0, pop_size);
		Solution best_solution = pop[random_index];
		
		for (int i = 0; i < selection_param - 1; i++) {
			random_index = ThreadLocalRandom.current().nextInt(0, pop_size);
			
			if (pop[random_index].evaluation < best_solution.evaluation) {
				best_solution = pop[random_index];
			}
		}
		
		return best_solution;
	}
	
	public static Solution roulette(Solution[] pop, int pop_size, int selection_param) {
		RandomCollection<Integer> random_collection = new RandomCollection<Integer>();
		
		for (int i = 0; i < pop_size; i++) {
			random_collection.add(pop[i].evaluation, i);
		}
		
		return pop[random_collection.next()];
	}
}
