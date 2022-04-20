package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cvrp.Solution;

public class Logger {

	private String file;
	private StringBuilder string_to_save;
	private int counter;
	
	public Logger(String file) {
		this.file = file;
		this.string_to_save = new StringBuilder();
		this.counter = 0;
	}
	
	public void add_pop(Solution[] pop, int pop_size) {
		double best = pop[0].evaluation, worst = pop[0].evaluation, sum = pop[0].evaluation;
		
		
		for (int i = 1; i < pop_size; i++) {
			if (pop[i].evaluation < best) {
				best = pop[i].evaluation;
			}
			
			if (pop[i].evaluation > worst) {
				worst = pop[i].evaluation;
			}
			
			sum += pop[i].evaluation;
		}
		
		double avg = sum / pop_size;
		
		this.string_to_save.append(this.counter++);
		this.string_to_save.append(";");
		this.string_to_save.append(best);
		this.string_to_save.append(";");
		this.string_to_save.append(avg);
		this.string_to_save.append(";");
		this.string_to_save.append(worst);
		
//		for (int i = 0; i < pop_size; i++) {
//			this.string_to_save.append(";");
//			this.string_to_save.append(pop[i].solution);
//			this.string_to_save.append(";");
//			this.string_to_save.append(pop[i].evaluation);
//		}
		
		this.string_to_save.append("\n");
	}
	
	public void add_selection(Solution[] pop, int pop_size, Solution p1, Solution p2) {
		List<Solution> pop_copy = new ArrayList<Solution>();
		
		for (int i = 0; i < pop_size; i++) {
			pop_copy.add(new Solution(
					pop[i].solution,
					pop[i].evaluation));
		}
		
		Collections.sort(pop_copy);
		
		int p1_index = pop_copy.indexOf(p1);
		int p2_index = pop_copy.indexOf(p2);
		
		this.string_to_save.append(p1_index)
			.append(";")
			.append(p2_index)
			.append("\n");
	}
	
	public void add_neighbor(Solution best_solution, Solution current, double T) {
		this.string_to_save.append(current.evaluation)
			.append(";")
			.append(best_solution.evaluation)
			.append(";")
			.append(T)
//			.append(";")
//			.append(current.solution)
//			.append(";")
//			.append(best_solution.solution)
			.append("\n");
	}
	
	public void sa_debug(Solution current, Solution best, double T) {
		this.string_to_save.append(current.evaluation)
			.append(";")
			.append(best.evaluation)
			.append(";")
			.append(T)
			.append(";")
			.append(best.evaluation - current.evaluation)
			.append(";")
			.append(Math.exp((best.evaluation - current.evaluation) / T))
			.append("\n");
	}
	
	public void add_time(List<Long> times) {
		long best = times.get(0), worst = times.get(0), sum = times.get(0);
		
		for (int i = 1; i < times.size(); i++) {
			if (times.get(i) < best) {
				best = times.get(i);
			}
			
			if (times.get(i) > worst) {
				worst = times.get(i);
			}
			
			sum += times.get(i);
		}
		
		double avg = (double) sum / times.size();
		double std = 0.;
		
		for (int i = 0; i < times.size(); i++) {
			std += Math.pow((((double) times.get(i)) - avg), 2);
		}
		
		std = Math.sqrt(std / times.size());
		
		this.string_to_save.append(best);
		this.string_to_save.append(";");
		this.string_to_save.append(avg);
		this.string_to_save.append(";");
		this.string_to_save.append(worst);
		this.string_to_save.append(";");
		this.string_to_save.append(std);
		this.string_to_save.append("\n");
	}
	
	public void add_neighbors(List<Solution> neighbors, Solution best_solution) {
		double best = neighbors.get(0).evaluation, worst = neighbors.get(0).evaluation, sum = neighbors.get(0).evaluation;
		
		
		for (int i = 1; i < neighbors.size(); i++) {
			if (neighbors.get(i).evaluation < best) {
				best = neighbors.get(i).evaluation;
			}
			
			if (neighbors.get(i).evaluation > worst) {
				worst = neighbors.get(i).evaluation;
			}
			
			sum += neighbors.get(i).evaluation;
		}
		
		double avg = sum / neighbors.size();
		
		this.string_to_save.append(this.counter++);
		this.string_to_save.append(";");
		this.string_to_save.append(best);
		this.string_to_save.append(";");
		this.string_to_save.append(avg);
		this.string_to_save.append(";");
		this.string_to_save.append(worst);
		this.string_to_save.append(";");
		this.string_to_save.append(best_solution.evaluation);
		
//		for (int i = 0; i < neighbors.size(); i++) {
//			this.string_to_save.append(";");
//			this.string_to_save.append(neighbors.get(i).solution);
//			this.string_to_save.append(";");
//			this.string_to_save.append(neighbors.get(i).evaluation);
//		}
		
		this.string_to_save.append("\n");
	}
	
	public void save_to_file() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this.file)));
			writer.write(string_to_save.toString());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
