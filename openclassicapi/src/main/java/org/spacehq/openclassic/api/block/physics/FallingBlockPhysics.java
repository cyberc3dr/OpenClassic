package org.spacehq.openclassic.api.block.physics;

import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.VanillaBlock;

/**
 * Physics used in falling blocks.
 */
public class FallingBlockPhysics implements BlockPhysics {

	private BlockType block;
	
	public FallingBlockPhysics(BlockType block) {
		this.block = block;
	}
	
	@Override
	public void update(Block block) {
		this.fall(block);
	}

	@Override
	public void onPlace(Block block) {
		this.fall(block);
	}

	@Override
	public void onBreak(Block block) {
	}

	@Override
	public void onNeighborChange(Block block, Block neighbor) {
		this.fall(block);
	}
	
	private void fall(Block block) {
		Block relative = block.getRelative(BlockFace.DOWN);
		if(relative != null && relative.getType().canPlaceIn()) {
			block.setType(VanillaBlock.AIR);
			relative.setType(this.block);
			block.getLevel().delayTick(relative.getPosition());
		}
	}

}
