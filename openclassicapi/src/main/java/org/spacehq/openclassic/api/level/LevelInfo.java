package org.spacehq.openclassic.api.level;

import org.spacehq.openclassic.api.Position;

/**
 * The basic info for creating a level.
 */
public class LevelInfo {

	private String name;
	private Position spawn;
	private short width;
	private short height;
	private short depth;
	
	public LevelInfo(String name, Position spawn, short width, short height, short depth) {
		this.name = name;
		this.spawn = spawn;
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	/**
	 * Gets the name of the level.
	 * @return The level's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the level's spawn.
	 * @return The level's spawn.
	 */
	public Position getSpawn() {
		return this.spawn;
	}
	
	/**
	 * Gets the width of the level.
	 * @return The level's width.
	 */
	public short getWidth() {
		return this.width;
	}
	
	/**
	 * Gets the height of the level.
	 * @return The level's height.
	 */
	public short getHeight() {
		return this.height;
	}
	
	/**
	 * Gets the depth of the level.
	 * @return The level's depth.
	 */
	public short getDepth() {
		return this.depth;
	}
	
}
