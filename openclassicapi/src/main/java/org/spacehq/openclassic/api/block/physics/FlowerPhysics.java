package org.spacehq.openclassic.api.block.physics;

import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.VanillaBlock;

/**
 * Physics used for flowers.
 */
public class FlowerPhysics implements BlockPhysics {

	@Override
	public void update(Block block) {
		Block b = block.getRelative(BlockFace.DOWN);
		if (!block.getLevel().isLit(block.getPosition().getBlockX(), block.getPosition().getBlockY(), block.getPosition().getBlockZ()) || b.getType() != VanillaBlock.DIRT && b.getType() != VanillaBlock.GRASS) {
			block.setType(VanillaBlock.AIR);
		}
	}

	@Override
	public void onPlace(Block block) {
	}

	@Override
	public void onBreak(Block block) {
	}

	@Override
	public void onNeighborChange(Block block, Block neighbor) {
	}

}
