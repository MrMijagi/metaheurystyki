package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;

public class CrossoverOperators {
	public static List<Solution> crossover(Solution p1, Solution p2) {
		List<Solution> children = new ArrayList<Solution>();
		Solution child = new Solution();
		
		// copy of second parent to eliminate values from range
		List<Integer> p2_route_copy = new ArrayList<Integer>(p2.solution);
		
		// get range to copy
		int from_index = ThreadLocalRandom.current().nextInt(0, p1.solution.size());
		int to_index = ThreadLocalRandom.current().nextInt(0, p1.solution.size());
		
		// make sure from_index is smaller than to_index
		if (from_index > to_index) {
			int tmp = from_index;
			from_index = to_index;
			to_index = tmp;
		}
		
		// eliminate all values from the range from p2 copy
		List<Integer> p1_sub_list = p1.solution.subList(from_index, to_index);
		p2_route_copy.removeAll(p1_sub_list);
		
		for (int i = from_index; i < to_index; i++) {
			child.solution.add(p1.solution.get(i));
		}
		
		// fill child with remaining values from the other parent
		for (int i = 0; i < from_index; i++) {
			child.solution.add(i, p2_route_copy.remove(0));
		}
		
		for (int i = to_index; i < p1.solution.size(); i++) {
			child.solution.add(p2_route_copy.remove(0));
		}
		
		children.add(child);
		
		return children;
	}
}
