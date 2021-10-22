package cvrp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GreedySolver extends Solver {

	public GreedySolver(CVRP cvrp) {
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
		
		// put the closest location until running out of locations
		// make sure capacity doesn't go over maximum
		final int max_capacity = Integer.parseInt(this.cvrp.getDescription().get("CAPACITY"));
		int capacity = 0;
		
		int closest_location, location, demand;
		int last_location = 0;
		double min_distance, distance;
		
		while (!locations.isEmpty()) {
			// find the closest location
			closest_location = locations.get(0);
			min_distance = cvrp.getDistance(closest_location, last_location);
			
			for (int i = 1; i < locations.size(); i++) {
				location = locations.get(i);
				distance = cvrp.getDistance(location, last_location);
				
				if (distance < min_distance) {
					min_distance = distance;
					closest_location = location;
				}
			}
			
			// return to depot if necessary
			demand = this.cvrp.getLocation(closest_location).getDemand();
			
			if (capacity + demand > max_capacity) {     // return to depot
				// update capacity
				capacity = 0;
				// add depot
				solution.solution.add(returns_to_depot.remove(0));
				// remember last location
				last_location = 0;
			} else {                                    // go to closest location
				// update capacity
				capacity += this.cvrp.getLocation(closest_location).getDemand();
				// add the location
				solution.solution.add(closest_location);
				// remove by value from list of locations
				locations.remove(Integer.valueOf(closest_location));
				// remember last location
				last_location = closest_location;
			}
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
