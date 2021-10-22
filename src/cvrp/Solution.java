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
		this.solution = solution;
	}
}
