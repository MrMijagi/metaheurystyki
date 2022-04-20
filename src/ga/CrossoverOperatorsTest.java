package ga;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import cvrp.Solution;

class CrossoverOperatorsTest {

	@Test
	void testOX() {
		Solution p1 = new Solution(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		Solution p2 = new Solution(Arrays.asList(5, 7, 4, 9, 1, 3, 6, 2, 8));
		Solution child = new Solution(Arrays.asList(7, 9, 3, 4, 5, 6, 1, 2, 8));
		
		assertEquals(child.solution, CrossoverOperators.OXHelper(p1, p2, 2, 5).solution);
		
		child = new Solution(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		
		assertEquals(child.solution, CrossoverOperators.OXHelper(p1, p2, 0, 8).solution);
	}
	
	@Test
	void textPMX() {
		Solution p1 = new Solution(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		Solution p2 = new Solution(Arrays.asList(4, 3, 1, 2, 8, 7, 5, 6, 9));
		Solution child = new Solution(Arrays.asList(1, 4, 3, 2, 8, 7, 6, 5, 9));
		
		assertEquals(child.solution, CrossoverOperators.PMXHelper(p2, p1, 3, 5).solution);
		
		child = new Solution(Arrays.asList(2, 3, 1, 4, 5, 6, 8, 7, 9));
		
		assertEquals(child.solution, CrossoverOperators.PMXHelper(p1, p2, 3, 5).solution);
		
		child = new Solution(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		
		assertEquals(child.solution, CrossoverOperators.PMXHelper(p1, p2, 0, 8).solution);
		
		child = new Solution(Arrays.asList(4, 3, 1, 2, 8, 7, 5, 6, 9));
		
		assertEquals(child.solution, CrossoverOperators.PMXHelper(p2, p1, 0, 8).solution);
	}

}
