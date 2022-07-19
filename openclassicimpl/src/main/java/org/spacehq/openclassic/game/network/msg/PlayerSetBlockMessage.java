package org.spacehq.openclassic.game.network.msg;

/**
 * Recieved when a player breaks/places a block.
 */
public class PlayerSetBlockMessage extends Message {
	
	private short x;
	private short y;
	private short z;
	private boolean place;
	private byte type;
	
	public PlayerSetBlockMessage(short x, short y, short z, boolean place, byte type) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.place = place;
		this.type = type;
	}
	
	/**
	 * Gets the X of the block as a short.
	 * @return The X of the block.
	 */
	public short getX() {
		return this.x;
	}
	
	/**
	 * Gets the Y of the block as a short.
	 * @return The Y of the block.
	 */
	public short getY() {
		return this.y;
	}
	
	/**
	 * Gets the Z of the block as a short.
	 * @return The Z of the block.
	 */
	public short getZ() {
		return this.z;
	}
	
	/**
	 * Returns true if the player is placing a block.
	 * @return True if the player is placing a block.
	 */
	public boolean isPlacing() {
		return place;
	}
	
	/**
	 * Gets the new block type in the player's hand.
	 * @return The block type.
	 */
	public byte getBlock() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return "PlayerSetBlockMessage{x=" + x + ",y=" + y + ",z=" + z + ",place=" + place + ",block=" + type + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 5;
	}
	
}
