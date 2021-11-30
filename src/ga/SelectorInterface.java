package ga;

import cvrp.Solution;

@FunctionalInterface
public interface SelectorInterface {
	Solution selection(Solution[] pop, int pop_size, int selection_param);
}
