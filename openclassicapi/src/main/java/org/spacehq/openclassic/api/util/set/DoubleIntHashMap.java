package org.spacehq.openclassic.api.util.set;

import java.util.HashMap;

/**
 * A HashMap that uses a key of 3 integers.
 */
public class DoubleIntHashMap<T> extends HashMap<String, T> {

	private static final long serialVersionUID = 1L;

	public DoubleIntHashMap() {
		super(100);
	}

	public DoubleIntHashMap(int capacity) {
		super(capacity);
	}

	public T put(int key1, int key2, T value) {
		return super.put(key1 + "_" + key2, value);
	}

	public boolean containsKey(int key1, int key2) {
		return super.containsKey(key1 + "_" + key2);
	}

	public T remove(int key1, int key2) {
		return super.remove(key1 + "_" + key2);
	}
	
	public T get(int key1, int key2) {
		return super.get(key1 + "_" + key2);
	}

	public static final int key1(String key) {
		return Integer.parseInt(key.split("_")[0]);
	}

	public static final int key2(String key) {
		return Integer.parseInt(key.split("_")[1]);
	}
	
}
