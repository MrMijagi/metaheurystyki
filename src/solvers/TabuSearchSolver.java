package solvers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cvrp.CVRP;
import cvrp.Solution;

public class TabuSearchSolver extends Solver {
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load_configuration(String conf_file) {
		// TODO Auto-generated method stub

	}
	
	private List<Solution> getNeighbors(Solution solution) {
		List<Solution> neighbors = new ArrayList<Solution>();
		Solution swapped;
		
		for (int i = 0; i < solution.solution.size(); i++) {
			for (int j = 0; j < solution.solution.size(); j++) {
				if (i != j) {
					// copy the original one
					swapped = new Solution(solution.solution);
					// swap
					swapped.swapFromIndices(i, j);
					// add to list
					neighbors.add(swapped);
				}
			}
		}
		
		return neighbors;
	}
}
