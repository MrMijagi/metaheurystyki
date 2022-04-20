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

public class HybridTwoSolver extends Solver {
	
	private int pop_size, evolutions, selection_param;
	private double mutation_prob, cross_prob;
	
	private MutatorInterface mutator;
	private SelectorInterface selector;
	private CrossoverInterface crossover;
	
	private String logger_file;

	public HybridTwoSolver(CVRP cvrp, String logger_file) {
		super(cvrp);
		this.logger_file = logger_file;
	}

	public HybridTwoSolver(CVRP cvrp) {
		this(cvrp, "stats/default.csv");
	}

	@Override
	public Solution find_solution() {
		Logger logger = new Logger(this.logger_file);
		Logger selection_logger = new Logger("stats/selection.csv");
		int t = 0;
		
		Solution[] pop = this.initialise_pop();
		this.evaluate_pop(pop);
		
		// save stats
		logger.add_pop(pop, this.pop_size);
		
		// find best solution
		Solution best_solution = pop[0]; 
		
		for (int i = 1; i < this.pop_size; i++) {
			if (pop[i].evaluation < best_solution.evaluation) {
				best_solution = pop[i];
			}
		}
		
		Solution p1, p2;
		List<Solution> children = new ArrayList<Solution>();
		
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
				p2 = this.hybrid_selector(pop, this.pop_size, this.selection_param, p1);
				//p2 = this.selector.selection(pop, this.pop_size, this.selection_param);
				
				//selection_logger.add_selection(pop, this.pop_size, p1, p2);
				
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
			
			// save stats
			logger.add_pop(pop, this.pop_size);
			
			t++;
		}
		
		logger.save_to_file();
		selection_logger.save_to_file();
		
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
						this.pop_size = Integer.parseInt(parts[1].trim());
						break;
					case "EVOLUTIONS":
						this.evolutions = Integer.parseInt(parts[1].trim());
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
						this.selection_param = Integer.parseInt(parts[1].trim());
						break;
					case "CROSSOVER_TYPE":
						if (parts[1].trim().equals("OX")) {
							this.crossover = CrossoverOperators::OX;
						} else if (parts[1].trim().equals("PMX")) {
							this.crossover = CrossoverOperators::PMX;
						}
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

	private Solution hybrid_selector(Solution[] pop, int pop_size, int selection_param, Solution p1) {
		int random_index = ThreadLocalRandom.current().nextInt(0, pop_size);
		Solution p2 = pop[random_index];
		
		for (int i = 0; i < selection_param - 1; i++) {
			random_index = ThreadLocalRandom.current().nextInt(0, pop_size);
			
			if (Math.abs(p1.evaluation - pop[random_index].evaluation) > Math.abs(p1.evaluation - p2.evaluation)) {
				p2 = pop[random_index];
			}
		}
		
		return p2;
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
