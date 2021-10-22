package cvrp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CVRPTest {

	@Test
	void testLoadProblem() {
		CVRP cvrp = new CVRP();
		
		try {
			cvrp.loadProblem("problems/toy.vrp");
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
		}
		
		Map<String, String> description = Map.of(
			    "CAPACITY", "30",
			    "EDGE_WEIGHT_TYPE", "EUC_2D",
			    "DIMENSION", "6",
			    "TYPE", "CVRP",
			    "COMMENT", "toy instance>",
			    "NAME", "toy.vrp"
		);
		
		assertEquals(description, cvrp.getDescription());
		
		Location[] locations = new Location[] {
				new Location(new ArrayList<Integer>(Arrays.asList(38, 46)), 0),
				new Location(new ArrayList<Integer>(Arrays.asList(59, 46)), 16),
				new Location(new ArrayList<Integer>(Arrays.asList(96, 42)), 18),
				new Location(new ArrayList<Integer>(Arrays.asList(47, 61)), 1),
				new Location(new ArrayList<Integer>(Arrays.asList(26, 15)), 13),
				new Location(new ArrayList<Integer>(Arrays.asList(66, 6)), 8)
		};
		
		for (int i = 0; i < 6; i++) {
			assertEquals(locations[i], cvrp.getLocation(i));
		}
	}
	
	@Test
	void testGetDistanceFromCoords() {
		CVRP cvrp = new CVRP();
		
		try {
			cvrp.loadProblem("problems/toy.vrp");
		} catch (WrongInputFileFormat e) {
			e.printStackTrace();
		}
				
		double[] distance_matrix = new double[] {
			Math.sqrt(0.),
			Math.sqrt(441.),
			Math.sqrt(3380.),
			Math.sqrt(306.),
			Math.sqrt(1105.),
			Math.sqrt(2384.),
		};
		
		for (int i = 0; i < 6; i++) {
			assertEquals(cvrp.getDistance(0, i), distance_matrix[i]);
		}
	}

}
