package ts;

import java.util.List;

import cvrp.Solution;

@FunctionalInterface
public interface NeighborsInterface {
	List<Solution> getNeighbors(Solution solution, int size);
}
