package cvrp;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	public List<Integer> solution;
	public double evaluation;
	
	public Solution() {
		this.solution = new ArrayList<Integer>();
	}
	
	public Solution(List<Integer> solution) {
		this.solution = new ArrayList<Integer>(solution);
	}
	
	public void swapFromIndices(int i, int j) {
		if (i == j) {
			throw new IllegalArgumentException("i is equal to j");
		}
		
		int tmp = this.solution.get(i);
		this.solution.set(i, this.solution.get(j));
		this.solution.set(j, tmp);
	}
}
