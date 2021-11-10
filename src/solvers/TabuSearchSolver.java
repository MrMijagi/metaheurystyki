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
import ga.CrossoverOperators;
import ga.MutationOperators;
import ga.MutatorInterface;
import ga.SelectionOperators;
import ts.NeighborsInterface;
import ts.NeighborsOperators;

public class TabuSearchSolver extends Solver {
	
	private int iterations, n_size, tabu_size;
	
	private NeighborsInterface get_neighbors;
	
	private String logger_file;
	
	public TabuSearchSolver(CVRP cvrp, String logger_file) {
		super(cvrp);
		this.logger_file = logger_file;
	}

	public TabuSearchSolver(CVRP cvrp) {
		this(cvrp, "stats/default.csv");
	}

	@Override
	public Solution find_solution() {
		// initialise lists
		List<Solution> neighbors;
		List<Solution> tabu_list = new ArrayList<Solution>();
		
		// initialise best solution
		Solution best_solution = this.random_solution();
		best_solution.evaluation = this.cvrp.calculateCost(best_solution);
		
		// create current solution to get neighbors
		Solution neighbor = new Solution(best_solution.solution);
		
		int t = 0;
		Solution best_neighbor;
		
		while (t < this.iterations) {
			// get neighbors
			neighbors = this.get_neighbors.getNeighbors(neighbor, this.n_size);
			
			// evaluate neighbors, find the best one
			best_neighbor = neighbors.get(0);
			best_neighbor.evaluation = this.cvrp.calculateCost(best_neighbor);
			
			for (int i = 0; i < neighbors.size(); i++) {
				neighbor = neighbors.get(i);
				neighbor.evaluation = this.cvrp.calculateCost(neighbor);
				
				if (!tabu_list.contains(neighbor) && neighbor.evaluation < best_neighbor.evaluation) {
					best_neighbor = neighbor;
				}
			}
			
			if (best_neighbor.evaluation < best_solution.evaluation) {
				best_solution = best_neighbor;
			}
			
			tabu_list.add(best_neighbor);
			
			if (tabu_list.size() > this.tabu_size) {
				tabu_list.remove(0);
			}
			
			t++;
		}
		
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
					case "ITERATIONS":
						this.iterations = Integer.parseInt(parts[1].trim());
						break;
					case "N_SIZE":
						this.n_size = Integer.parseInt(parts[1].trim());
						break;
					case "TABU_SIZE":
						this.tabu_size = Integer.parseInt(parts[1].trim());
						break;
					case "NEIGHBORS_TYPE":
						if (parts[1].trim().equals("GREEDY")) {
							this.get_neighbors = NeighborsOperators::getNeighbors; 
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
