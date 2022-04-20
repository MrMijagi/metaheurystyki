package ga;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RandomCollectionTest {

	@Test
	void test() {
		RandomCollection<Integer> collection = new RandomCollection<Integer>();
		
		for (int i = 1; i <= 10; i++) {
			collection.add(i*10, i);
		}
		
		for (int i = 0; i < 10; i++) {
			System.out.println(collection.next());
		}
	}

}
