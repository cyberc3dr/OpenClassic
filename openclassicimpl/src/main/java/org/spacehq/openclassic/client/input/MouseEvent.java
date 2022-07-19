package org.spacehq.openclassic.client.input;

public class MouseEvent {

	private int x;
	private int y;
	private int dwheel;
	private int button;
	private boolean state;
	
	public MouseEvent(int x, int y, int dwheel, int button, boolean state) {
		this.x = x;
		this.y = y;
		this.dwheel = dwheel;
		this.button = button;
		this.state = state;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getDWheel() {
		return this.dwheel;
	}
	
	public int getButton() {
		return this.button;
	}
	
	public boolean getState() {
		return this.state;
	}
	
}
