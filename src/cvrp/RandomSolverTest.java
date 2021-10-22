package cvrp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RandomSolverTest {

	@Test
	void testIfSolutionsAreCorrect() {
		CVRP cvrp = new CVRP();
		try {
			cvrp.loadProblem("problems/toy.vrp");
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
		}
		
		RandomSolver randomSolver = new RandomSolver(cvrp);
		
		for (int i = 0; i < 10000; i++) {
			assertTrue(cvrp.check_if_correct_solution(randomSolver.find_solution()));
		}
	}

}
