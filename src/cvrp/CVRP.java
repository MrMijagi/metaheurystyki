package cvrp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CVRP {
	
	private Map<String, String> problem_description;
	private Location[] locations;
	private double[][] distance_matrix;
	
	private int capacity;
	
	// returns cost of passed solution
	public double calculateCost(Solution solution) {
		double cost = 0.;
		int capacity = 0;
		int curr_location = 0;    // depot
		
		for (int i = 0; i < solution.solution.size(); i++) {
			int next_location = solution.solution.get(i) < 0 ? 0 : solution.solution.get(i);
			
			if (next_location == 0) {         // go back to depot
				capacity = 0;
			} else {
				capacity += this.getLocation(next_location).getDemand();
			}
			
			// check if capacity was reached
			if (capacity > this.getCapacity()) {
				// punishment
				cost += this.getDistance(curr_location, 0);
				cost += this.getDistance(0, next_location);
			} else {
				cost += this.getDistance(curr_location, next_location);
			}
			
			curr_location = next_location;
		}
		
		// come back to depot
		cost += this.getDistance(curr_location, 0);
		
		// check if solution is correct - add punishment value
		// punishment = maximum distance between any two locations * 2 * multiplier
		//cost += 1 * this.max_distance * this.getPunishmentMultiplier(solution);
		
		return cost;
	}
	
	// returns true only if solution is correct
	public boolean checkIfCorrectSolution(Solution solution) {
		int capacity = 0;
		final int max_capacity = Integer.parseInt(this.problem_description.get("CAPACITY"));
		int curr_location = 0;
		
		// set for checking if there duplicated locations
		Set<Integer> duplicates = new HashSet<Integer>();
				
		for (int i = 0; i < solution.solution.size(); i++) {
			curr_location = solution.solution.get(i);
			
			if (curr_location <= 0) {              // go back to depot - reset capacity
				capacity = 0;
			} else {                               // normal location - add capacity
				capacity += this.locations[curr_location].getDemand();
			}
						
			if (capacity > max_capacity) {         // solution is incorrect
				return false;
			}
			
			// check for duplicates
			if (!duplicates.add(curr_location)) {
				return false;
			}
		}
		
		return true;
	}
	
	// calculates distance in any dimension
	private double getDistanceFromCoords(int i, int j) {
		double sum = 0.;
		List<Integer> x = this.locations[i].getCoords();
		List<Integer> y = this.locations[j].getCoords();
		
		for (int k = 0; k < x.size(); k++) {
			sum += Math.pow(x.get(k) - y.get(k), 2);
		}
		
		return Math.sqrt(sum);
	}
	
	// calculates distances between each pair of locations
	public void calculateDistanceMatrix() {
		int dimension = Integer.parseInt(problem_description.get("DIMENSION"));
		this.distance_matrix = new double[dimension][dimension];
		double curr_distance;
		
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				curr_distance = this.getDistanceFromCoords(i, j);
				this.distance_matrix[i][j] = curr_distance;
			}
		}
	}
	
	// loads problem from given file
	public void loadProblem(String in_file) throws WrongInputFileFormat {
		this.problem_description = new HashMap<String, String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(in_file));
			String line = br.readLine();
			
			while (line != null) {
				if (line.contains(":")) {
					String[] parts = line.split(":");
					
					problem_description.put(parts[0].trim(), parts[1].trim());
					
					// if dimension key then create array of locations
					if (parts[0].trim().equals("DIMENSION")) this.locations = new Location[Integer.parseInt(parts[1].trim())];
				} else {
					if (!problem_description.containsKey("DIMENSION")) {
						throw new WrongInputFileFormat("DIMENSION key does not exist!");
					}
					
					int dimension = Integer.parseInt(problem_description.get("DIMENSION"));
					
					switch(line.trim()) {
					case "NODE_COORD_SECTION":
						for (int i = 0; i < dimension; i++) {
							// create new location
							this.locations[i] = new Location();
							// prepare coordinates
							List<Integer> coords = new ArrayList<Integer>();
							
							line = br.readLine();
							
							Scanner scanner = new Scanner(line);
							// skip index
							scanner.nextInt();
							
							for (int j = 0; j < 2; j++) {
								coords.add(scanner.nextInt());
							}
							
							// set coordinates to location
							this.locations[i].setCoords(coords);
							
							scanner.close();
						}
						break;
					case "DEMAND_SECTION":
						for (int i = 0; i < dimension; i++) {
							line = br.readLine();
							
							Scanner scanner = new Scanner(line);
							// skip index
							scanner.nextInt();
							
							// set coordinates to location
							this.locations[i].setDemand(scanner.nextInt());
							
							scanner.close();
						}
					case "DEPOT_SECTION":
						for (int i = 0; i < 2; i++) br.readLine();
						break;
					}
				}
				line = br.readLine();
			}
			br.close();
			
			// calculate distance matrix after locations have been loaded
			this.calculateDistanceMatrix();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// test
		this.capacity = Integer.parseInt(this.problem_description.get("CAPACITY"));
	}
	
	// saves passed solution to a file
	public void saveResult(String out_file, Solution solution) {
		// build string first
		StringBuilder string_to_save = new StringBuilder("Route #1:");
		int counter = 2;
		int last_location = 0;
		
		// create a copy of solution list
		List<Integer> path = new ArrayList<Integer>(solution.solution);
		
		while (path.size() > 0) {
			int location = path.remove(0);
			
			if (location > 0) {
				if (last_location < 0) {     // new route
					string_to_save.append("\nRoute #");
					string_to_save.append(counter++);
					string_to_save.append(":");
				}
				
				string_to_save.append(" ");
				string_to_save.append(location);
			}
			
			// update last_location
			last_location = location;
		}
		
		// append cost
		string_to_save.append("\n\nCost: ");
		string_to_save.append(this.calculateCost(solution));
		string_to_save.append("\n");
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(out_file)));
			writer.write(string_to_save.toString());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getDescription() {
		return problem_description;
	}
	
	public Location getLocation(int index) {
		return this.locations[index];
	}
	
	// get distance between the two locations pointed by indices
	public double getDistance(int i, int j) {
		return this.distance_matrix[i][j];
	}
	
	public int getDimension() {
		return Integer.parseInt(this.problem_description.get("DIMENSION"));
	}
	
	public int getCapacity() {
		return this.capacity;
	}
}
