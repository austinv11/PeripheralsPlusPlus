package miscperipherals.util;

import java.util.Random;

public class RandomExt extends Random {
	private long seed;
	
	@Override
	public void setSeed(long seed) {
		super.setSeed(seed);
		this.seed = seed;
	}
	
	public long getSeed() {
		return seed;
	}
	
	/**
	 * http://stackoverflow.com/questions/12772939/java-storing-two-ints-in-a-long
	 */
	public long nextLong() {
		return Math.abs((((long)nextInt()) << 32) | (nextInt() & 0xFFFFFFFFL));
	}
	
	public long nextULong() {
		return (((long)nextInt()) << 32) | (nextInt() & 0xFFFFFFFFL);
	}
}
