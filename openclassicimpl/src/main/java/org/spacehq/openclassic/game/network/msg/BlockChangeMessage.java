package org.spacehq.openclassic.game.network.msg;

/**
 * Sent when a block is changed.
 */
public class BlockChangeMessage extends Message {
	
	private short x;
	private short y;
	private short z;
	private byte type;
	
	public BlockChangeMessage(short x, short y, short z, byte type) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}
	
	/**
	 * Gets the X coordinate of the change as a short.
	 * @return The X coordinate.
	 */
	public short getX() {
		return this.x;
	}
	
	/**
	 * Gets the Y coordinate of the change as a short.
	 * @return The Y coordinate.
	 */
	public short getY() {
		return this.y;
	}
	
	/**
	 * Gets the Z coordinate of the change as a short.
	 * @return The Z coordinate.
	 */
	public short getZ() {
		return this.z;
	}
	
	/**
	 * Gets the block type this change represents.
	 * @return The block type.
	 */
	public byte getBlock() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return "BlockChangeMessage{x=" + x + ",y=" + y + ",z=" + z + ",block=" + type + "}";
	}

	@Override
	public byte getOpcode() {
		return 6;
	}
	
}
