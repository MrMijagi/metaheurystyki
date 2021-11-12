package cvrp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class SolutionTest {

	@Test
	void testCopyConstructor() {
		List<Integer> route1 = Arrays.asList(
			1, 2, 3, 4, 5, -1, -2, -3, -4, -5
		);
			
		Solution p1 = new Solution(route1);
		Solution p2 = new Solution(p1.solution);
		
		assertFalse(p1 == p2);
		assertFalse(p1.solution == p2.solution);
		assertTrue(p1.solution.equals(p2.solution));
	}
	
	@Test
	void testSwapFromIndices() {
		Solution solution = new Solution(Arrays.asList(
			1, 2, 3
		));
		solution.swapFromIndices(0, 2);
		
		List<Integer> swapped = Arrays.asList(3, 2, 1);
		
		assertEquals(solution.solution, swapped);
	}
	
	@Test
	void testInverseFromIndices() {
		Solution solution = new Solution(Arrays.asList(
			1, 2, 3, 4, 5, 6, 7
		));
		solution.inverseFromIndices(1, 4);
		
		List<Integer> inverse0 = Arrays.asList(1, 5, 4, 3, 2, 6, 7);
		
		System.out.println(solution.solution);
		assertEquals(solution.solution, inverse0);
		
		solution = new Solution(Arrays.asList(
			1, 2, 3, 4, 5, 6, 7
		));
		solution.inverseFromIndices(1, 3);
		
		List<Integer> inverse1 = Arrays.asList(1, 4, 3, 2, 5, 6, 7);
		
		assertEquals(solution.solution, inverse1);
	}
}
