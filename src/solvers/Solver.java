package solvers;

import cvrp.CVRP;
import cvrp.Solution;

public abstract class Solver {
	
	protected CVRP cvrp;
	
	public Solver(CVRP cvrp) {
		this.setCvrp(cvrp);
	}
	
	public abstract Solution find_solution();
	
	public abstract void load_configuration(String conf_file);

	public CVRP getCvrp() {
		return cvrp;
	}

	public void setCvrp(CVRP cvrp) {
		this.cvrp = cvrp;
	}
}
