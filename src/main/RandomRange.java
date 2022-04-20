package main;

import java.util.concurrent.ThreadLocalRandom;

public class RandomRange {
	
	public int from, to;
	
	public RandomRange(int size) {
		// get range to copy
		this.from = ThreadLocalRandom.current().nextInt(0, size);
		this.to = ThreadLocalRandom.current().nextInt(0, size - 1);		
		
		// make sure from != to
		if (this.from == this.to) this.to = size - 1;
	}
	
	public RandomRange sort() {
		if (this.from > this.to) {
			int tmp = this.from;
			this.from = this.to;
			this.to = tmp;
		}
		
		return this;
	}
}
