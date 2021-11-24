package ga;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class RandomCollection<E> {
	
	private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
	private double total = 0;
	
	public void add(double weight, E element) {
		if (weight <= 0) throw new IllegalArgumentException("weight < 0");
		
		this.total += weight;
		this.map.put(this.total, element);
	}
	
	public E next() {
		double value = ThreadLocalRandom.current().nextDouble() * this.total;
		return this.map.higherEntry(value).getValue();
	}
}
