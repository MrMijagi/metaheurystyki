package cvrp;

public abstract class Solver {
	
	protected CVRP cvrp;
	
	public Solver(CVRP cvrp) {
		this.setCvrp(cvrp);
	}
	
	abstract Solution find_solution();
	
	abstract void load_configuration(String conf_file);

	public CVRP getCvrp() {
		return cvrp;
	}

	public void setCvrp(CVRP cvrp) {
		this.cvrp = cvrp;
	}
}
