package cvrp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Location {
	
	private List<Integer> coords;
	private int demand;
	
	public Location() {
		this.coords = new ArrayList<Integer>();
		this.demand = 0;
	}
	
	public Location(List<Integer> coords, int demand) {
		this.coords = new ArrayList<Integer>(coords);
		this.demand = demand;
	}
	
	public List<Integer> getCoords() {
		return coords;
	}

	public void setCoords(List<Integer> coords) {
		this.coords = coords;
	}

	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}

	@Override
	public int hashCode() {
		return Objects.hash(coords, demand);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(coords, other.coords) && demand == other.demand;
	}
	
	
	
}
