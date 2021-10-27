package ga;

import cvrp.Solution;

@FunctionalInterface
public interface MutatorInterface {
	void mutation(Solution o1, double probability);
}
