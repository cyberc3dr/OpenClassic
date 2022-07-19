package org.spacehq.openclassic.api.block;

/**
 * Represents the faces of a block.
 */
public enum BlockFace {

	/** The upper face. */
	UP(0, 1, 0),
	/** The lower face. */
	DOWN(0, -1, 0),
	/** The northern face. */
	NORTH(1, 0, 0),
	/** The southern face. */
	SOUTH(-1, 0, 0),
	/** The eastern face. */
	EAST(0, 0, 1),
	/** The western face. */
	WEST(0, 0, -1);
	
	private int modx;
	private int mody;
	private int modz;
	
	private BlockFace(int modx, int mody, int modz) {
		this.modx = modx;
		this.mody = mody;
		this.modz = modz;
	}
	
	/**
	 * Gets the X shift of this face from a block.
	 * @return The X shift.
	 */
	public int getModX() {
		return this.modx;
	}
	
	/**
	 * Gets the Y shift of this face from a block.
	 * @return The Y shift.
	 */
	public int getModY() {
		return this.mody;
	}
	
	/**
	 * Gets the Z shift of this face from a block.
	 * @return The Z shift.
	 */
	public int getModZ() {
		return this.modz;
	}
	
}
