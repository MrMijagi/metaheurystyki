package ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;

public class CrossoverOperators {
	public static List<Solution> OX(Solution p1, Solution p2) {
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

	public static List<Solution> PMX(Solution p1, Solution p2) {
		List<Solution> children = new ArrayList<Solution>();
		
		// get range to copy
		int from_index = ThreadLocalRandom.current().nextInt(0, p1.solution.size());
		int to_index = ThreadLocalRandom.current().nextInt(0, p1.solution.size() - 1);		
		
		// make sure from != to
		if (from_index == to_index) to_index = p1.solution.size() - 1;
		
		// make sure from_index is smaller than to_index
		if (from_index > to_index) {
			int tmp = from_index;
			from_index = to_index;
			to_index = tmp;
		}
		
		children.add(PMXHelper(p1, p2, from_index, to_index));
		children.add(PMXHelper(p2, p1, from_index, to_index));
		
		return children;
	}
	
	private static Solution PMXHelper(Solution p1, Solution p2, int from, int to) {
		Solution child = new Solution(new ArrayList<Integer>(Collections.nCopies(p1.solution.size(), 0)));
		
		for (int i = from; i <= to; i++) {
			// copy elements from p1
			child.solution.set(i, p1.solution.get(i));
			// copy mapped elements from p2
			int free_index = find_free_index(p1, p2, i, from, to);
			
			if (free_index != -1) {
				child.solution.set(free_index, p2.solution.get(i));
			}
		}
		
		// replace remaining zeros with p2 elements
		for (int i = 0; i < p1.solution.size(); i++) {
			if (child.solution.get(i) == 0) {
				child.solution.set(i, p2.solution.get(i));
			}
		}
		
		return child;
	}
	
	private static int find_free_index(Solution p1, Solution p2, int i, int from, int to) {
		int p1_index = p1.solution.indexOf(p2.solution.get(i));
		
		if (from <= p1_index && p1_index <= to) return -1;
		
		do {
			i = p2.solution.indexOf(p1.solution.get(i));
		} while (from <= i && i <= to);
		
		return i;
	}
}
