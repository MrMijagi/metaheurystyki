package cvrp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	
	public void inverseFromIndices(int from_index, int to_index) {
		if (from_index == to_index) {
			throw new IllegalArgumentException("from_index is equal to to_index");
		}
		
		// make sure from_index is smaller than to_index
		if (from_index > to_index) {
			int tmp = from_index;
			from_index = to_index;
			to_index = tmp;
		}
		
		for (int i = 0; i < ((to_index - from_index) / 2) + 1; i++) {
			int tmp = this.solution.get(from_index + i);
			this.solution.set(from_index + i, this.solution.get(to_index - i));
			this.solution.set(to_index - i, tmp);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(solution);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solution other = (Solution) obj;
		return Objects.equals(solution, other.solution);
	}
}
