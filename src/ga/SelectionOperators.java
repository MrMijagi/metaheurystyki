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
		
		// find maximum
		double max = pop[0].evaluation;
		
		for (int i = 1; i < pop_size; i++) {
			if (pop[i].evaluation > max) {
				max = pop[i].evaluation;
			}
		}
		
		max += 1;   // eliminate possibility for weight to be zero
		
		for (int i = 0; i < pop_size; i++) {
			double weight = Math.pow(max - pop[i].evaluation, selection_param);
			random_collection.add(weight, i);
		}
		
		return pop[random_collection.next()];
	}
}
