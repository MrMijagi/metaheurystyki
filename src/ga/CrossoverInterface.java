package ga;

import java.util.List;

import cvrp.Solution;

@FunctionalInterface
public interface CrossoverInterface {
	List<Solution> crossover(Solution p1, Solution p2);
}
