package cvrp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class GeneticAlgorithmSolver extends Solver {
	
	private int pop_size, evolutions;
	private double mutation_prob, cross_prob;
	
	public GeneticAlgorithmSolver(CVRP cvrp) {
		super(cvrp);
	}

	@Override
	Solution find_solution() {
		Logger logger = new Logger("stats.csv");
		int t = 0;
		
		Solution[] pop = this.initialise_pop();
		this.evaluate_pop(pop);
		
		// save stats
		logger.add_pop(pop, this.pop_size);
		
		// find best solution
		Solution best_solution = pop[0]; 
		
		for (int i = 1; i < this.pop_size; i++) {
			if (pop[i].evaluation > best_solution.evaluation) {
				best_solution = pop[i];
			}
		}
		
		Solution p1, p2, o1;
		
		while (t < this.evolutions) {
			List<Solution> new_pop = new ArrayList<Solution>();
			
			while (new_pop.size() < this.pop_size) {
				p1 = this.selection(pop, 5);
				p2 = this.selection(pop, 5);
				
				// crossover
				double random = ThreadLocalRandom.current().nextDouble();
				
				if (random < this.cross_prob) {
					o1 = this.crossover(p1, p2);
				} else {
					o1 = new Solution(p1.solution);
				}
				
				// mutation
				o1 = this.mutation(o1);
				
				o1.evaluation = this.cvrp.calculate_cost(o1);
				new_pop.add(o1);
				
				if (o1.evaluation > best_solution.evaluation) {
					best_solution = o1;
				}
			}
			
			pop = new Solution[new_pop.size()];
			new_pop.toArray(pop);
			
			// save stats
			logger.add_pop(pop, this.pop_size);
			
			t++;
		}
		
		logger.save_to_file();
		
		return best_solution;
	}
	
	@Override
	void load_configuration(String conf_file) {
		// no configuration for random solver
		System.out.println("Configuraiton loaded");
	}

	void load_configuration(int pop_size, int evolutions, double mutation_prob, double cross_prob) {
		this.pop_size = pop_size;
		this.evolutions = evolutions;
		this.mutation_prob = mutation_prob;
		this.cross_prob = cross_prob;
	}
	
	private Solution crossover(Solution p1, Solution p2) {
		return p1;
	}
	
	private Solution mutation(Solution o1) {
		for (int i = 0; i < o1.solution.size(); i++) {
			// swap
			double random = ThreadLocalRandom.current().nextDouble();
			
			if (random < this.mutation_prob) {
				int random_index = ThreadLocalRandom.current().nextInt(0, o1.solution.size());
				
				if (random_index != i) {      // make sure not to swap the same element
					int tmp = o1.solution.get(i);
					o1.solution.set(i, o1.solution.get(random_index));
					o1.solution.set(random_index, tmp);
				}
			}
		}
		
		o1 = this.fix_solution(o1);
		
		return o1;
	}
	
	private Solution selection(Solution[] pop, int tournament_size) {
		int random_index = ThreadLocalRandom.current().nextInt(0, tournament_size + 1);
		Solution best_solution = pop[random_index];
		
		for (int i = 0; i < tournament_size - 1; i++) {
			random_index = ThreadLocalRandom.current().nextInt(0, tournament_size + 1);
			
			if (pop[random_index].evaluation > best_solution.evaluation) {
				best_solution = pop[random_index];
			}
		}
		
		return best_solution;
	}
	
	private void evaluate_pop(Solution[] pop) {
		for (int i = 0; i < this.pop_size; i++) {
			pop[i].evaluation = cvrp.calculate_cost(pop[i]);
		}
	}
	
	private Solution[] initialise_pop() {
		Solution[] new_pop = new Solution[this.pop_size];
		
		for (int i = 0; i < this.pop_size; i++) {
			new_pop[i] = this.fix_solution(this.random_solution());
		}
		
		return new_pop;
	}
	
	// random solution for initialization purposes
	private Solution random_solution() {
		// prepare lists for populating the solution
		int dimension = this.cvrp.getDimension();
		
		List<Integer> locations = new ArrayList<Integer>(dimension);
		
		for (int i = 1; i < dimension; i++) {
			locations.add(i);
			locations.add(-i);
		}
		
		Collections.shuffle(locations);
		
		return new Solution(locations);
	}
	
	// removes duplicated locations and puts missing ones instead
	private Solution fix_duplicates(Solution solution) {
		int dimension = this.cvrp.getDimension();
		
		Set<Integer> all_locations = new HashSet<Integer>();
		
		for (int i = 1; i < dimension; i++) {
			all_locations.add(i);
			all_locations.add(-i);
		}
		
		// find missing values
		all_locations.removeAll(solution.solution);
		
		// go through solution and fill duplicates with missing values
		Set<Integer> duplicates = new HashSet<Integer>();
		Iterator<Integer> missing_locations = all_locations.iterator();
		
		for (int i = 0; i < solution.solution.size(); i++) {
			int location = solution.solution.get(i);
			
			if (!duplicates.add(location)) {
				solution.solution.set(i, missing_locations.next());
			}
		}
		
		return solution;
	}
	
	// when capacity is crossed, this function finds next value for returning to depot
	// returns -1 if no fitting index is found
	private int getNextDepotReturnIndex(Solution solution) {
		// first check for returns to depot in beginning
		if (solution.solution.get(0) < 0) {
			return 0;
		} else {
			// find two consecutive returns to depot (two or more negative values)
			int last_value = solution.solution.get(0);
			int value;
			
			for (int i = 1; i < solution.solution.size(); i++) {
				value = solution.solution.get(i);
				
				if (value < 0 && last_value < 0) {
					return i;
				}
				
				last_value = value;
			}
		}
		
		return -1;
	}
	
	// fixes solution - removes duplicates and fixes order
	private Solution fix_solution(Solution solution) {		
		// fix order by checking capacity
		int capacity = 0;
		final int max_capacity = this.cvrp.getCapacity();
		
		for (int i = 0; i < solution.solution.size(); i++) {
			int location = solution.solution.get(i);
			
			if (location < 0) {
				capacity = 0;
			} else {
				// check if capacity is crossed
				int demand = this.cvrp.getLocation(location).getDemand();
				
				if (capacity + demand > max_capacity) {
					// put return to depot here
					int index = this.getNextDepotReturnIndex(solution);
					location = solution.solution.remove(index);
					solution.solution.add(i, location);
					
					// update capacity
					capacity = 0;
				} else {
					// update capacity
					capacity += demand;
				}
			}
		}
		
		return solution;
	}

}
