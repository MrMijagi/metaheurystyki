package main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cvrp.Solution;

public class SolutionMap {
	private Map<Integer, Double> map;
	
	public SolutionMap(int size) {
		this.map = new LinkedHashMap<Integer, Double>(size) {
			protected boolean removeEldestEntry(Map.Entry eldest) {
				return size() > size;
			}
		};
	}
	
	private List<Integer> cleanSolution(Solution solution) {
		List<Integer> cleaned = new ArrayList<Integer>();
		
		if (solution.solution.get(0) > 0) {
			cleaned.add(solution.solution.get(0));
		}
		
		for (int i = 1; i < solution.solution.size(); i++) {
			if (solution.solution.get(i) > 0) {
				cleaned.add(solution.solution.get(i));
			} else if (solution.solution.get(i - 1) > 0) {
				cleaned.add(0);
			}
		}
		
		return cleaned;
	}
	
	public void put(Solution solution, Double value) {
		List<Integer> key = this.cleanSolution(solution);
		
		this.map.put(key.hashCode(), value);
	}
	
	public boolean check(Solution solution) {
		List<Integer> key = this.cleanSolution(solution);
		
		return this.map.containsKey(key.hashCode());
	}
	
	public Double get(Solution solution) {
		List<Integer> key = this.cleanSolution(solution);
		
		return this.map.get(key.hashCode());
	}
}
