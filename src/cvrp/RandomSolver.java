package cvrp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomSolver extends Solver {
	
	public RandomSolver(CVRP cvrp) {
		super(cvrp);
	}

	@Override
	Solution find_solution() {
		Solution solution = new Solution();
		
		// prepare lists for populating the solution
		int dimension = Integer.parseInt(this.cvrp.getDescription().get("DIMENSION"));
		
		List<Integer> locations = new ArrayList<Integer>(dimension);
		List<Integer> returns_to_depot = new ArrayList<Integer>(dimension);
		
		for (int i = 1; i < dimension; i++) {
			locations.add(i);
			returns_to_depot.add(-i);
		}
		
		// put random locations to solution while making sure capacity isn't crossed
		final int max_capacity = Integer.parseInt(this.cvrp.getDescription().get("CAPACITY"));
		int capacity = 0;
		
		while (!locations.isEmpty()) {
			// get random index from remaining locations
			int random_index = ThreadLocalRandom.current().nextInt(0, locations.size());
			int random_location = locations.remove(random_index);
			
			// update capacity
			capacity += this.cvrp.getLocation(random_location).getDemand();
			
			// return to depot if necessary
			if (capacity > max_capacity) {
				solution.solution.add(returns_to_depot.remove(0));
				capacity = this.cvrp.getLocation(random_location).getDemand();
			}
			
			// add the location
			solution.solution.add(random_location);
		}
		
		// concat the rest or returns_to_depot to solution if any exist
		if (!returns_to_depot.isEmpty()) {
			solution.solution.addAll(returns_to_depot);
		}
		
		return solution;
	}

	@Override
	void load_configuration(String conf_file) {
		// no configuration for random solver
		System.out.println("Configuraiton loaded");
	}

}
