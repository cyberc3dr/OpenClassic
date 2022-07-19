package org.spacehq.openclassic.api.block.physics;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.level.Level;

/**
 * Physics used in liquids to make it flow.
 */
public class LiquidPhysics implements BlockPhysics {

	private BlockType block;
	private boolean soak;
	private boolean fluidMovement;
	
	public LiquidPhysics(BlockType block, boolean soak, boolean fluidMovement) {
		this.block = block;
		this.soak = soak;
		this.fluidMovement = fluidMovement;
	}
	
	@Override
	public void update(Block block) {
		boolean moving = false;
		int y = block.getPosition().getBlockY();
		
		while (true) {
			y--;
			if (block.getLevel().getBlockTypeAt(block.getPosition().getBlockX(), y, block.getPosition().getBlockZ()) != VanillaBlock.AIR || !this.canMove(block.getLevel(), block.getPosition().getBlockX(), y, block.getPosition().getBlockZ())) {
				break;
			}

			if (block.getLevel().setBlockAt(block.getPosition().getBlockX(), y, block.getPosition().getBlockZ(), this.block)) {
				moving = true;
			} else {
				break;
			}
			
			if(!this.fluidMovement) {
				break;
			}
		}

		y++;
		if (this.fluidMovement || !moving) {
			int x = block.getPosition().getBlockX();
			int yy = block.getPosition().getBlockY();
			int z = block.getPosition().getBlockZ();
			moving = moving | this.canFlow(block.getLevel(), x - 1, yy, z) | this.canFlow(block.getLevel(), x + 1, yy, z) | this.canFlow(block.getLevel(), x, yy, z - 1) | this.canFlow(block.getLevel(), x, yy, z + 1);
		}

		if (moving) {
			block.getLevel().delayTick(new Position(block.getLevel(), block.getPosition().getBlockX(), y, block.getPosition().getBlockZ()));
		}
	}
	
	private boolean canMove(Level level, int x, int y, int z) {
		if (this.soak) {
			for (int bx = x - 2; bx <= x + 2; bx++) {
				for (int by = y - 2; by <= y + 2; by++) {
					for (int bz = z - 2; bz <= z + 2; bz++) {
						BlockType block = level.getBlockTypeAt(bx, by, bz);
						if (block != null && block == VanillaBlock.SPONGE) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private boolean canFlow(Level level, int x, int y, int z) {
		if (level.getBlockTypeAt(x, y, z) == VanillaBlock.AIR) {
			if (!this.canMove(level, x, y, z)) {
				return false;
			}

			if (level.setBlockAt(x, y, z, this.block)) {
				level.delayTick(new Position(level, x, y, z));
			}
		}

		return false;
	}

	@Override
	public void onPlace(Block block) {
		block.getLevel().delayTick(block.getPosition());
	}

	@Override
	public void onBreak(Block block) {
	}

	@Override
	public void onNeighborChange(Block block, Block neighbor) {
		block.getLevel().delayTick(block.getPosition());
	}
	
	public boolean canSoak() {
		return this.soak;
	}

}
