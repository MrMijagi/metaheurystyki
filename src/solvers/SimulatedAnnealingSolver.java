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
import main.Logger;
import sa.RandomNeighborInterface;
import sa.RandomNeighborOperators;
import sa.TemperatureMethodInterface;
import sa.TemperatureMethods;
import ts.NeighborsOperators;

public class SimulatedAnnealingSolver extends Solver {
	
	private int iterations, n_size;
	private double t_param, t_start, t_end;
	
	private RandomNeighborInterface random_neighbor;
	private TemperatureMethodInterface t_method;
	
	private String logger_file;
	
	public SimulatedAnnealingSolver(CVRP cvrp, String logger_file) {
		super(cvrp);
		this.logger_file = logger_file;
	}

	public SimulatedAnnealingSolver(CVRP cvrp) {
		this(cvrp, "stats/default.csv");
	}

	@Override
	public Solution find_solution() {
		Logger logger = new Logger(this.logger_file);
		
		// initialize best solution
		Solution best_solution = this.random_solution();
		//Solution best_solution = this.greedy_solution();
		best_solution.evaluation = this.cvrp.calculateCost(best_solution);
		
		// create current solution
		Solution neighbor = new Solution(best_solution.solution);
		List<Solution> neighbors = new ArrayList<Solution>();
		
		int t = 0;
		double T = this.t_start;
		
		while (t < this.iterations) {
			
			// alternative loop
			neighbors = NeighborsOperators.getNeighbors(best_solution, n_size);
			
			for (int i = 0; i < neighbors.size(); i++) {
				neighbor = neighbors.get(i);
				neighbor.evaluation = cvrp.calculateCost(neighbor);
				
				if (neighbor.evaluation < best_solution.evaluation) {
					best_solution = neighbor;
				} else {
					System.out.println(best_solution.evaluation - neighbor.evaluation);
					System.out.println(Math.exp((best_solution.evaluation - neighbor.evaluation) / T));
					System.out.println(T);
					System.out.println("\n");
					if (ThreadLocalRandom.current().nextDouble()
							< Math.exp((best_solution.evaluation - neighbor.evaluation) / T)) {
						
						best_solution = neighbor;
					}
				}
			}
			
			T = this.t_method.linear(T, t, this.t_param);
			
			logger.add_neighbor(neighbor, best_solution, T);
			
			t++;
		}
		
		logger.save_to_file();
		
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
					case "T_START":
						this.t_start = Double.parseDouble(parts[1].trim());
						break;
					case "T_END":
						this.t_end = Double.parseDouble(parts[1].trim());
						break;
					case "T_PARAM":
						this.t_param = Double.parseDouble(parts[1].trim());
					case "T_METHOD":
						if (parts[1].trim().equals("LINEAR")) {
							this.t_method = TemperatureMethods::linear;
						}
					case "RANDOM_NEIGHBOR":
						if (parts[1].trim().equals("SWAP")) {
							this.random_neighbor = RandomNeighborOperators::random_neighbor_swap; 
						} else if (parts[1].trim().equals("INVERSE")) {
							this.random_neighbor = RandomNeighborOperators::random_neighbor_inverse;
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
