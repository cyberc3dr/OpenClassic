package org.spacehq.openclassic.game.level;

public class DelayedTick {

	private int x;
	private int y;
	private int z;
	private int ticks;

	public DelayedTick(int x, int y, int z, int ticks) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.ticks = ticks;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public int getTicks() {
		return this.ticks;
	}
	
	public void decreaseTicks() {
		this.ticks--;
	}
	
}
