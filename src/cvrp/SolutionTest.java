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

}
