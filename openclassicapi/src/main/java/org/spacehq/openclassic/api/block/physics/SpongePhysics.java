package org.spacehq.openclassic.api.block.physics;

import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.VanillaBlock;

/**
 * Physics used in sponges to soak water.
 */
public class SpongePhysics implements BlockPhysics {

	@Override
	public void update(Block block) {
		this.soak(block);
	}

	@Override
	public void onPlace(Block block) {
		this.soak(block);
	}

	@Override
	public void onBreak(Block block) {
		for (int x = block.getPosition().getBlockX() - 2; x <= block.getPosition().getBlockX() + 2; x++) {
			for (int y = block.getPosition().getBlockY() - 2; y <= block.getPosition().getBlockY() + 2; y++) {
				for (int z = block.getPosition().getBlockZ() - 2; z <= block.getPosition().getBlockZ() + 2; z++) {
					Block b = block.getLevel().getBlockAt(x, y, z);
					if(b != null) {
						for(BlockFace face : BlockFace.values()) {
							Block relative = b.getRelative(face);
							if(relative != null && relative.getType().getPhysics() != null && relative.getType().getPhysics() instanceof LiquidPhysics) {
								relative.getType().getPhysics().onNeighborChange(relative, block);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onNeighborChange(Block block, Block neighbor) {
		this.soak(block);
	}
	
	private void soak(Block sponge) {
		for (int x = sponge.getPosition().getBlockX() - 2; x <= sponge.getPosition().getBlockX() + 2; x++) {
			for (int y = sponge.getPosition().getBlockY() - 2; y <= sponge.getPosition().getBlockY() + 2; y++) {
				for (int z = sponge.getPosition().getBlockZ() - 2; z <= sponge.getPosition().getBlockZ() + 2; z++) {
					Block block = sponge.getLevel().getBlockAt(x, y, z);
					if (block.getType().getPhysics() instanceof LiquidPhysics && ((LiquidPhysics) block.getType().getPhysics()).canSoak()) {
						block.setType(VanillaBlock.AIR, false);
					}
				}
			}
		}
	}
	
}
