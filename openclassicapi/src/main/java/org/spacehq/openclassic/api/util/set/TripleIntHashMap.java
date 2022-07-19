package org.spacehq.openclassic.api.util.set;

import java.util.HashMap;

/**
 * A HashMap that uses a key of 3 integers.
 */
public class TripleIntHashMap<T> extends HashMap<String, T> {

	private static final long serialVersionUID = 1L;

	public TripleIntHashMap() {
		super(100);
	}

	public TripleIntHashMap(int capacity) {
		super(capacity);
	}

	public T put(int key1, int key2, int key3, T value) {
		return super.put(key1 + "_" + key2 + "_" + key3, value);
	}

	public boolean containsKey(int key1, int key2, int key3) {
		return super.containsKey(key1 + "_" + key2 + "_" + key3);
	}

	public T remove(int key1, int key2, int key3) {
		return super.remove(key1 + "_" + key2 + "_" + key3);
	}
	
	public T get(int key1, int key2, int key3) {
		return super.get(key1 + "_" + key2 + "_" + key3);
	}

	public static final int key1(String key) {
		return Integer.parseInt(key.split("_")[0]);
	}

	public static final int key2(String key) {
		return Integer.parseInt(key.split("_")[1]);
	}

	public static final int key3(String key) {
		return Integer.parseInt(key.split("_")[2]);
	}
	
}
