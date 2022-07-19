package org.spacehq.openclassic.game.network.msg;

/**
 * Sent after level data to finalize the level.
 */
public class LevelFinalizeMessage extends Message {

	private short width;
	private short height;
	private short depth;
	
	public LevelFinalizeMessage(short width, short height, short depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	/**
	 * Gets the width of the level. (X axis)
	 * @return The level's width.
	 */
	public short getWidth() {
		return this.width;
	}
	
	/**
	 * Gets the height of the level. (Y axis)
	 * @return The level's height.
	 */
	public short getHeight() {
		return this.height;
	}
	
	/**
	 * Gets the depth of the level. (Z axis)
	 * @return The level's depth.
	 */
	public short getDepth() {
		return this.depth;
	}
	
	@Override
	public String toString() {
		return "LevelFinalizeMessage{width=" + width + ",height=" + height + ",depth=" + depth + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 4;
	}
	
}
