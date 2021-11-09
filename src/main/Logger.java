package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
