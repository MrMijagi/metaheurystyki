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
		Logger debug_logger = new Logger("stats/debug.csv");
		
		// initialize best solution
		Solution best_solution = this.random_solution();
		//Solution best_solution = this.greedy_solution();
		this.cvrp.calculateCost(best_solution);
		
		// create current solution
		Solution current = new Solution(best_solution.solution);
		this.cvrp.calculateCost(current);
		
		Solution neighbor;
		List<Solution> neighbors = new ArrayList<Solution>();
		
		int t = 0;
		double T = this.t_start;
		double t_decrease_value = (this.t_start - this.t_end) / this.iterations * 1.01;
		
		while (t < this.iterations) {

			for (int i = 0; i < this.n_size; i++) {
				// get random neighbor
				neighbor = this.random_neighbor.random_neighbor(current);
				this.cvrp.calculateCost(neighbor);
//				this.cvrp.calculateCostDifference(current, neighbor);
				
				if (neighbor.evaluation <= current.evaluation) {
					current = neighbor;
				} else {
					// debug
					//debug_logger.sa_debug(neighbor, current, T);
					
					if (ThreadLocalRandom.current().nextDouble()
							< Math.exp((current.evaluation - neighbor.evaluation) / T)) {
						
						current = neighbor;
					}
				}
			}
			
			T = this.t_method.method(this.t_start, this.t_end, t, this.iterations);
			//T -= t_decrease_value;
			
			if (T < this.t_end) T = this.t_end;
			
			if (current.evaluation < best_solution.evaluation) {
				best_solution = current;
			}
			
			logger.add_neighbor(best_solution, current, T);
			
			t++;
		}
		
		logger.save_to_file();
		debug_logger.save_to_file();
		
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
						this.iterations = (int) Double.parseDouble(parts[1].trim());
						break;
					case "N_SIZE":
						this.n_size = (int) Double.parseDouble(parts[1].trim());
						break;
					case "T_START":
						this.t_start = Double.parseDouble(parts[1].trim());
						break;
					case "T_END":
						this.t_end = Double.parseDouble(parts[1].trim());
						break;
					case "T_METHOD":
						if (parts[1].trim().equals("LINEAR")) {
							this.t_method = TemperatureMethods::linear;
						} else if (parts[1].trim().equals("SQUARE_ROOT")) {
							this.t_method = TemperatureMethods::square_root;
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
