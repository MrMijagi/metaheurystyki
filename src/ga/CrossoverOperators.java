package ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import cvrp.Solution;
import main.RandomRange;

public class CrossoverOperators {
	public static List<Solution> OX(Solution p1, Solution p2) {
		List<Solution> children = new ArrayList<Solution>();
		
		RandomRange range = new RandomRange(p1.solution.size()).sort();
		
		children.add(OXHelper(p1, p2, range.from, range.to));
		
		return children;
	}
	
	public static Solution OXHelper(Solution p1, Solution p2, int from_index, int to_index) {
		Solution child = new Solution();
		
		// copy of second parent to eliminate values from range
		List<Integer> p2_route_copy = new ArrayList<Integer>(p2.solution);
		
		// eliminate all values from the range from p2 copy
		List<Integer> p1_sub_list = p1.solution.subList(from_index, to_index + 1);
		p2_route_copy.removeAll(p1_sub_list);
		
		for (int i = from_index; i <= to_index; i++) {
			child.solution.add(p1.solution.get(i));
		}
		
		// fill child with remaining values from the other parent
		for (int i = 0; i < from_index; i++) {
			child.solution.add(i, p2_route_copy.remove(0));
		}
		
		for (int i = to_index + 1; i < p1.solution.size(); i++) {
			child.solution.add(p2_route_copy.remove(0));
		}
		
		return child;
	}

	public static List<Solution> PMX(Solution p1, Solution p2) {
		List<Solution> children = new ArrayList<Solution>();
		
		RandomRange range = new RandomRange(p1.solution.size()).sort();
		
		children.add(PMXHelper(p1, p2, range.from, range.to));
		children.add(PMXHelper(p2, p1, range.from, range.to));
		
		return children;
	}
	
	public static Solution PMXHelper(Solution p1, Solution p2, int from, int to) {
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
