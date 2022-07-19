package org.spacehq.openclassic.api.level;

import java.util.List;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.data.NBTData;
import org.spacehq.openclassic.api.player.Player;

/**
 * Represents a level.
 */
public interface Level {
	
	/**
	 * Gets all players in this world.
	 * @return All players in the world.
	 */
	public List<Player> getPlayers();
	
	/**
	 * Gets whether physics are enabled on this world.
	 * @return True if physics are enabled.
	 */
	public boolean getPhysicsEnabled();
	
	/**
	 * Sets whether physics are enabled on this world.
	 * @param enabled Whether physics are enabled.
	 */
	public void setPhysicsEnabled(boolean enabled);
	
	/**
	 * Gets this level's name.
	 * @return The level's name.
	 */
	public String getName();
	
	/**
	 * Gets this level's author.
	 * @return This level's author.
	 */
	public String getAuthor();
	
	/**
	 * Gets this level's creation time.
	 * @return This level's creation time.
	 */
	public long getCreationTime();
	
	/**
	 * Gets the spawn of this level.
	 * @return The level's spawn.
	 */
	public Position getSpawn();
	
	/**
	 * Sets the spawn of this level.
	 * @param pos New spawn.
	 */
	public void setSpawn(Position pos);

	/**
	 * Gets the width of the level. Width is the length along the X axis.
	 * @return The level's width.
	 */
	public short getWidth();
	
	/**
	 * Gets the height of the level. Height is the length along the Y axis.
	 * @return The level's height.
	 */
	public short getHeight();
	
	/**
	 * Gets the depth of this level. Depth is the length along the Z axis.
	 * @return The level's depth.
	 */
	public short getDepth();
	
	/**
	 * Gets the water level of the level.
	 * @return The level's water level.
	 */
	public short getWaterLevel();
	
	/**
	 * Gets the ground level of the level.
	 * @return The level's ground level.
	 */
	public short getGroundLevel();
	
	/**
	 * Gets an array of all the block IDs in the level.
	 * @return All the block IDs.
	 */
	public byte[] getBlocks();
	
	/**
	 * Gets the ID of the block at the given position.
	 * @param pos Position of the block.
	 * @return The block ID.
	 */
	public byte getBlockIdAt(Position pos);
	
	/**
	 * Gets the ID of the block at the given coordinates.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @return The block ID.
	 */
	public byte getBlockIdAt(int x, int y, int z);
	
	/**
	 * Gets the BlockType of the block at the given position.
	 * @param pos Position of the block.
	 * @return The BlockType.
	 */
	public BlockType getBlockTypeAt(Position pos);
	
	/**
	 * Gets the BlockType of the block at the given coordinates.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @return The BlockType.
	 */
	public BlockType getBlockTypeAt(int x, int y, int z);
	
	/**
	 * Gets the block at the given position.
	 * @param pos Position of the block.
	 * @return The block.
	 */
	public Block getBlockAt(Position pos);
	
	/**
	 * Gets the block at the given coordinates.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @return The block.
	 */
	public Block getBlockAt(int x, int y, int z);
	
	/**
	 * Gets the highest block Y at the given X and Z.
	 * @param x X to check.
	 * @param z Z to check.
	 * @return The Y of the highest non-air block.
	 */
	public int getHighestBlockY(int x, int z);
	
	/**
	 * Gets the highest block Y below the given Y at the given X and Z.
	 * @param x X to check.
	 * @param z Z to check.
	 * @param max Maximum Y of the block.
	 * @return The Y of the highest non-air block below the given Y.
	 */
	public int getHighestBlockY(int x, int z, int max);
	
	/**
	 * Returns true if there are no higher blocks at the given X and Z than the one at the given Y.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @return True if it is the highest.
	 */
	public boolean isHighest(int x, int y, int z);
	
	/**
	 * Returns true if the given block would be lit.
	 * @param pos Position of the block.
	 * @return Whether the block would be lit.
	 */
	public boolean isLit(Position pos);
	
	/**
	 * Returns true if the given block would be lit.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @return Whether the block would be lit.
	 */
	public boolean isLit(int x, int y, int z);
	
	/**
	 * Gets the brightness of a block, where 0 and below is pitch black and 1 and above is fully bright.
	 * @param pos Position of the block.
	 * @return The block's brightness.
	 */
	public float getBrightness(Position pos);
	
	/**
	 * Gets the brightness of a block, where 0 and below is pitch black and 1 and above is fully bright.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @return The block's brightness.
	 */
	public float getBrightness(int x, int y, int z);
	
	/**
	 * Sets the block ID at the given position to the given byte.
	 * @param pos Position of the block.
	 * @param type Type ID to set.
	 */
	public boolean setBlockIdAt(Position pos, byte type);
	
	/**
	 * Sets the block ID at the given position to the given byte.
	 * @param pos Position of the block.
	 * @param type Type ID to set.
	 * @param physics Whether to apply physics.
	 */
	public boolean setBlockIdAt(Position pos, byte type, boolean physics);
	
	/**
	 * Sets the block ID at the given coordinates to the given byte.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @param type Type ID to set.
	 */
	public boolean setBlockIdAt(int x, int y, int z, byte type);
	
	/**
	 * Sets the block ID at the given coordinates to the given byte.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @param type Type ID to set.
	 * @param physics Whether to apply physics.
	 */
	public boolean setBlockIdAt(int x, int y, int z, byte type, boolean physics);
	
	/**
	 * Sets the BlockType at the given position to the given BlockType.
	 * @param pos Position of the block.
	 * @param type BlockType to set.
	 */
	public boolean setBlockAt(Position pos, BlockType type);
	
	/**
	 * Sets the BlockType at the given position to the given BlockType.
	 * @param pos Position of the block.
	 * @param type BlockType to set.
	 * @param physics Whether to use physics.
	 */
	public boolean setBlockAt(Position pos, BlockType type, boolean physics);
	
	/**
	 * Sets the BlockType at the given coordinates to the given BlockType.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @param type BlockType to set.
	 */
	public boolean setBlockAt(int x, int y, int z, BlockType type);
	
	/**
	 * Sets the BlockType at the given coordinates to the given BlockType.
	 * @param x X of the block.
	 * @param y Y of the block.
	 * @param z Z of the block.
	 * @param type BlockType to set.
	 * @param physics Whether to use physics.
	 */
	public boolean setBlockAt(int x, int y, int z, BlockType type, boolean physics);

	/**
	 * Schedules a block to be ticked next time the server ticks.
	 * @param pos Position to schedule.
	 * @param block Type of the block to tick.
	 */
	public void delayTick(Position pos);

	/**
	 * Attempts to grow a tree at the given coordinates.
	 * @param x X of the tree.
	 * @param y Y of the tree.
	 * @param z Z of the tree.
	 * @return Whether the attempt was successful.
	 */
	public boolean growTree(int x, int y, int z);
	
	/**
	 * Gets this level's NBTData.
	 * @return This level's NBTData.
	 */
	public NBTData getData();
	
	/**
	 * Gets the sky color of the level.
	 * @return The level's sky color.
	 */
	public int getSkyColor();
	
	/**
	 * Sets the sky color of the level.
	 * @param color Sky color to set.
	 */
	public void setSkyColor(int color);
	
	/**
	 * Gets the fog color of the level.
	 * @return The level's fog color.
	 */
	public int getFogColor();
	
	/**
	 * Sets the fog color of the level.
	 * @param color Fog color to set.
	 */
	public void setFogColor(int color);
	
	/**
	 * Gets the cloud color of the level.
	 * @return The level's cloud color.
	 */
	public int getCloudColor();
	
	/**
	 * Sets the cloud color of the level.
	 * @param color Cloud color to set.
	 */
	public void setCloudColor(int color);
	
	/**
	 * Gets whether it is raining in this level.
	 * @return Whether it is raining.
	 */
	public boolean isRaining();
	
	/**
	 * Sets whether it is raining in this level.
	 * @param Whether it should be raining or not.
	 */
	public void setRaining(boolean raining);
	
}
