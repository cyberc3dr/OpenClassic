package org.spacehq.openclassic.api.block.physics;

import java.util.Random;

import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockType;

/**
 * Physics used for grass.
 */
public class SpreadPhysics implements BlockPhysics {

	private static final Random rand = new Random();
	
	private BlockType block;
	private BlockType ungrown;
	
	public SpreadPhysics(BlockType block, BlockType ungrown) {
		this.block = block;
		this.ungrown = ungrown;
	}
	
	@Override
	public void update(Block block) {
		if (rand.nextInt(4) == 0) {
			if (!block.getLevel().isLit(block.getPosition().getBlockX(), block.getPosition().getBlockY(), block.getPosition().getBlockZ())) {
				block.setType(this.ungrown);
			} else {
				for (int count = 0; count < 4; ++count) {
					int x = block.getPosition().getBlockX() + rand.nextInt(3) - 1;
					int y = block.getPosition().getBlockY() + rand.nextInt(5) - 3;
					int z = block.getPosition().getBlockZ() + rand.nextInt(3) - 1;
					
					if (block.getLevel().getBlockTypeAt(x, y, z) == this.ungrown && block.getLevel().isLit(x, y, z)) {
						block.getLevel().setBlockAt(x, y, z, this.block);
					}
				}
			}
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
