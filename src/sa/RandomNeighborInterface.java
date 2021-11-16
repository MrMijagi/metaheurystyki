package sa;

import cvrp.Solution;

@FunctionalInterface
public interface RandomNeighborInterface {
	Solution random_neighbor(Solution solution);
}
