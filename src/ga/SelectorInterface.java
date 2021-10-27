package ga;

import cvrp.Solution;

@FunctionalInterface
public interface SelectorInterface {
	Solution selection(Solution[] pop, int tournament_size);
}
