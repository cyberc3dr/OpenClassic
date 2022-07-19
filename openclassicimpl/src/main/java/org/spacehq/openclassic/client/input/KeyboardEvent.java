package org.spacehq.openclassic.client.input;

public class KeyboardEvent {

	private int key;
	private char c;
	private boolean state;
	private boolean repeat;
	
	public KeyboardEvent(int key, char c, boolean state, boolean repeat) {
		this.key = key;
		this.c = c;
		this.state = state;
		this.repeat = repeat;
	}
	
	public int getKey() {
		return this.key;
	}
	
	public char getCharacter() {
		return this.c;
	}
	
	public boolean getState() {
		return this.state;
	}

	public boolean isRepeat() {
		return this.repeat;
	}
	
}
