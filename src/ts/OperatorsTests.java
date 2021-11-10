package ts;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import cvrp.Solution;

class OperatorsTests {

	@Test
	void testGetNeighbors() {
		Solution solution = new Solution(Arrays.asList(
			1, 2, -1, 3, -2, -3
		));
		
		List<Solution> neighbors = NeighborsOperators.getNeighbors(solution, 7);
		
		List<Solution> correct_neighbors = Arrays.asList(
			new Solution(Arrays.asList(2, 1, -1, 3, -2, -3)),
			new Solution(Arrays.asList(-1, 2, 1, 3, -2, -3)),
			new Solution(Arrays.asList(3, 2, -1, 1, -2, -3)),
			new Solution(Arrays.asList(-2, 2, -1, 3, 1, -3)),
			new Solution(Arrays.asList(-3, 2, -1, 3, -2, 1)),
			new Solution(Arrays.asList(1, -1, 2, 3, -2, -3)),
			new Solution(Arrays.asList(1, 3, -1, 2, -2, -3))
		);
		
		for (int i = 0; i < neighbors.size(); i++) {
			assertEquals(neighbors.get(i).solution, correct_neighbors.get(i).solution);
		}
	}

	@Test
	void testGetRandomNeighbors() {
		Solution solution = new Solution(Arrays.asList(
			1, 2, -1, 3, -2, -3
		));
		
		List<Solution> neighbors = NeighborsOperators.getRandomNeighbors(solution, 7);
		
		System.out.println("\ngetRandomNeighbors:");
		for (Solution neighbor: neighbors) {
			System.out.println(neighbor.solution.toString());
		}
	}

}
