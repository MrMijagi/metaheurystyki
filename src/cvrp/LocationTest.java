package cvrp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class LocationTest {

	@Test
	void testCoords() {
		List<Integer> coords = Arrays.asList(1, 2, 3);
		Location location = new Location();
		
		location.setCoords(coords);
		assertEquals(coords, location.getCoords());
	}
	
	@Test
	void testDemand() {
		int demand = 1;
		Location location = new Location();
		
		location.setDemand(demand);
		assertEquals(demand, location.getDemand());
	}
	
	@Test
	void testEquals() {
		Location l0 = new Location();
		l0.setDemand(1);
		
		Location l1 = l0;
		
		Location l2 = new Location();
		l2.setDemand(1);
		
		Location l3 = new Location();
		l3.setDemand(1);
		l3.setCoords(Arrays.asList(1, 2, 3));
		
		Location l4 = new Location();
		l4.setCoords(Arrays.asList(1, 2, 3));
		
		Location l5 = new Location();
		l5.setCoords(Arrays.asList(1, 2, 3));
		
		assertTrue(l0.equals(l1));
		assertTrue(l0.equals(l2));
		assertTrue(l1.equals(l2));
		assertTrue(l4.equals(l5));
		
		assertFalse(l2.equals(l3));
		assertFalse(l3.equals(l4));
	}

}
