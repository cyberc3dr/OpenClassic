package org.spacehq.openclassic.api.block;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.level.Level;

/**
 * Represents a block.
 */
public class Block {

	private Position pos;
	
	public Block(Position pos) {
		this.pos = pos;
	}
	
	/**
	 * Gets the block's ID.
	 * @return The block's ID.
	 */
	public byte getTypeId() {
		return this.pos.getLevel().getBlockIdAt(pos);
	}
	
	/**
	 * Sets the block's ID.
	 * @param id Block ID to set.
	 */
	public boolean setTypeId(byte id) {
		return this.pos.getLevel().setBlockIdAt(pos, id);
	}
	
	/**
	 * Sets the block's ID.
	 * @param id Block ID to set.
	 * @param physics Whether or not to apply physics.
	 */
	public boolean setTypeId(byte id, boolean physics) {
		return this.pos.getLevel().setBlockIdAt(pos, id, physics);
	}
	
	/**
	 * Gets the block's type.
	 * @return The block's type.
	 */
	public BlockType getType() {
		return Blocks.fromId(this.getTypeId());
	}
	
	/**
	 * Sets the block's type.
	 * @param type VanillaBlock to set.
	 */
	public boolean setType(BlockType type) {
		return this.setTypeId(type.getId());
	}
	
	/**
	 * Sets the block's type.
	 * @param type VanillaBlock to set.
	 * @param physics Whether or not to apply physics.
	 */
	public boolean setType(BlockType type, boolean physics) {
		return this.setTypeId(type.getId(), physics);
	}
	
	/**
	 * Gets the block's position.
	 * @return The block's position.
	 */
	public Position getPosition() {
		return this.pos;
	}
	
	/**
	 * Gets the block's level.
	 * @return The block's level.
	 */
	public Level getLevel() {
		return this.pos.getLevel();
	}
	
	/**
	 * Gets the block relative to the given face.
	 * @param face Face to get the block relative to.
	 * @return The block relative.
	 */
	public Block getRelative(BlockFace face) {
		return this.getRelative(face.getModX(), face.getModY(), face.getModZ());
	}
	
	/**
	 * Gets the block relative to the given coordinates.
	 * @param x X to get the block relative to.
	 * @param y Y to get the block relative to.
	 * @param z Z to get the block relative to.
	 * @return The block relative.
	 */
	public Block getRelative(int x, int y, int z) {
		Position pos = this.pos.clone().add(x, y, z);
		if(pos.getBlockX() >= this.pos.getLevel().getWidth() || pos.getBlockX() < 0) return null;
		if(pos.getBlockY() >= this.pos.getLevel().getHeight() || pos.getBlockY() < 0) return null;
		if(pos.getBlockZ() >= this.pos.getLevel().getDepth() || pos.getBlockZ() < 0) return null;
		
		return new Block(pos);
	}
	
}
