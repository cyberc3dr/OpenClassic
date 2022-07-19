package org.spacehq.openclassic.api.block.physics;

import org.spacehq.openclassic.api.block.Block;

/**
 * Represents a block's physics.
 */
public interface BlockPhysics {

	/**
	 * Called when a update occurs on the block.
	 * @param block Block to update.
	 */
	public void update(Block block);
	
	/**
	 * Called when the block is placed.
	 * @param block Block being placed.
	 */
	public void onPlace(Block block);
	
	/**
	 * Called when the block is broken.
	 * @param block Block being broken.
	 */
	public void onBreak(Block block);

	/**
	 * Called when a neighbor block is changed.
	 * @param block Block to update.
	 * @param neighbor Neighbor being changed.
	 */
	public void onNeighborChange(Block block, Block neighbor);
	
}
