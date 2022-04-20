package solvers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cvrp.CVRP;
import cvrp.Solution;
import ga.CrossoverInterface;
import ga.CrossoverOperators;
import ga.MutationOperators;
import ga.MutatorInterface;
import ga.SelectionOperators;
import ga.SelectorInterface;
import main.Logger;
import sa.RandomNeighborOperators;

public class HybridOneSolver extends Solver {
	
	private int pop_size, evolutions, selection_param, hybrid_n_size, hybrid_iterations;
	private double mutation_prob, cross_prob, hybrid_percent;
	
	private MutatorInterface mutator;
	private SelectorInterface selector;
	private CrossoverInterface crossover;
	
	private String logger_file, logger_hybrid_file;
	
	public HybridOneSolver(CVRP cvrp, String logger_file, String logger_hybrid_file) {
		super(cvrp);
		this.logger_file = logger_file;
		this.logger_hybrid_file = logger_hybrid_file;
	}

	public HybridOneSolver(CVRP cvrp) {
		this(cvrp, "stats/default.csv", "stats/default_hybrid.csv");
	}

	@Override
	public Solution find_solution() {
		Logger logger = new Logger(this.logger_file);
		Logger logger_hybrid = new Logger(this.logger_hybrid_file);
		int t = 0;
		
		Solution[] pop = this.initialise_pop();
		this.evaluate_pop(pop);
		
		// save stats
		logger.add_pop(pop, this.pop_size);
		//logger_hybrid.add_pop(pop, this.pop_size);
		
		// find best solution
		Solution best_solution = pop[0]; 
		
		for (int i = 1; i < this.pop_size; i++) {
			if (pop[i].evaluation < best_solution.evaluation) {
				best_solution = pop[i];
			}
		}
		
		Solution p1, p2;
		List<Solution> children = new ArrayList<Solution>();
		
		// how many solutions to run hill climbing on
		int hill_climbing_amount = (int) (this.hybrid_percent * this.pop_size);
		
		while (t < this.evolutions) {
			List<Solution> new_pop = new ArrayList<Solution>();
			
			// take the best solution from previous pop
			double min_evaluation = pop[0].evaluation;
			int min_index = 0;
			for (int i = 0; i < this.pop_size; i++) {
				if (pop[i].evaluation < min_evaluation) {
					min_evaluation = pop[i].evaluation;
					min_index = i;
				}
			}
			
			//Solution first = new Solution(best_solution.solution);
			Solution first = new Solution(pop[min_index].solution);
			cvrp.calculateCost(first);
			new_pop.add(first);
			
			while (new_pop.size() < this.pop_size) {
				p1 = this.selector.selection(pop, this.pop_size, this.selection_param);
				p2 = this.selector.selection(pop, this.pop_size, this.selection_param);
				
				// crossover
				double random = ThreadLocalRandom.current().nextDouble();
				
				if (random < this.cross_prob) {
					children = this.crossover.crossover(p1, p2);
				} else {
					children.add(new Solution(p1.solution));
				}
				
				// mutation
				for (Solution child: children) {
					this.mutator.mutation(child, this.mutation_prob);
					
					// before adding it to next pop, check if it's not a clone		
					if (!this.check_if_clone(new_pop, child)) {
						this.cvrp.calculateCost(child);
						new_pop.add(child);
						
						if (child.evaluation < best_solution.evaluation) {
							best_solution = child;
						}
					}
				}
				
				// clear children list
				children.clear();
			}
			
			pop = new Solution[new_pop.size()];
			new_pop.toArray(pop);
			
			// save stats before hill climbing
			//logger.add_pop(pop, this.pop_size);
			
			// hybrid add - take hybrid_percentage% of population and run hill climbing algorithm
			for (int i = 0; i < hill_climbing_amount; i++) {
				int solution_index = ThreadLocalRandom.current().nextInt(0, this.pop_size);
				pop[solution_index] = this.hill_climbing(pop[solution_index]);
			}
			
			// save stats
			logger_hybrid.add_pop(pop, this.pop_size);
			
			t++;
		}
		
		logger.save_to_file();
		logger_hybrid.save_to_file();
		
		return best_solution;
	}

	@Override
	public void load_configuration(String conf_file) {
		try {			
			BufferedReader br = new BufferedReader(new FileReader(conf_file));
			String line = br.readLine();
			
			while (line != null) {
				if (line.contains(":")) {
					String[] parts = line.split(":");
										
					switch(parts[0].trim()) {
					case "POP_SIZE":
						this.pop_size = (int) Double.parseDouble(parts[1].trim());
						break;
					case "EVOLUTIONS":
						this.evolutions = (int) Double.parseDouble(parts[1].trim());
						break;
					case "MUTATION_PROB":
						this.mutation_prob = Double.parseDouble(parts[1].trim());
						break;
					case "CROSS_PROB":
						this.cross_prob = Double.parseDouble(parts[1].trim());
						break;
					case "MUTATION_TYPE":
						if (parts[1].trim().equals("SWAP")) {
							this.mutator = MutationOperators::swap; 
						} else if (parts[1].trim().equals("INVERSE")) {
							this.mutator = MutationOperators::inverse;
						}
						break;
					case "SELECTION_TYPE":
						if (parts[1].trim().equals("TOURNAMENT")) {
							this.selector = SelectionOperators::tournament;
						} else if (parts[1].trim().equals("ROULETTE")) {
							this.selector = SelectionOperators::roulette;
						}
						break;
					case "SELECTION_PARAM":
						this.selection_param = (int) Double.parseDouble(parts[1].trim());
						break;
					case "CROSSOVER_TYPE":
						if (parts[1].trim().equals("OX")) {
							this.crossover = CrossoverOperators::OX;
						} else if (parts[1].trim().equals("PMX")) {
							this.crossover = CrossoverOperators::PMX;
						}
						break;
					case "HYBRID_ITERATIONS":
						this.hybrid_iterations = (int) Double.parseDouble(parts[1].trim());
						break;
					case "HYBRID_N_SIZE":
						this.hybrid_n_size = (int) Double.parseDouble(parts[1].trim());
						break;
					case "HYBRID_PERCENT":
						this.hybrid_percent = Double.parseDouble(parts[1].trim());
						break;
					}
				}
				
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Solution hill_climbing(Solution solution) {
		Solution neighbor;
		
		for (int j = 0; j < this.hybrid_iterations; j++) {
			for (int i = 0; i < this.hybrid_n_size; i++) {
				// get random neighbor
				neighbor = RandomNeighborOperators.random_neighbor_swap(solution);
//				this.cvrp.calculateCost(neighbor);
				this.cvrp.calculateCostDifference(solution, neighbor);
				
				if (neighbor.evaluation <= solution.evaluation) {
					solution = neighbor;
				}
			}
		}
		
		return solution;
	}
	
	private boolean check_if_clone(List<Solution> pop, Solution solution) {
		for (Solution individual: pop) {
			if (solution.solution.equals(individual.solution)) return true;
		}
		
		return false;
	}
	
	private void evaluate_pop(Solution[] pop) {
		for (int i = 0; i < this.pop_size; i++) {
			cvrp.calculateCost(pop[i]);
			pop[i].evaluation = pop[i].evaluation;
		}
	}
	
	private Solution[] initialise_pop() {
		Solution[] new_pop = new Solution[this.pop_size];
		
		for (int i = 0; i < this.pop_size; i++) {
			new_pop[i] = this.random_solution();
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
}
