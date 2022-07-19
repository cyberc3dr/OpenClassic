package org.spacehq.openclassic.api.player;

import java.net.SocketAddress;
import java.util.List;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.command.Sender;
import org.spacehq.openclassic.api.data.NBTData;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.permissions.Group;
import org.spacehq.openclassic.api.plugin.RemotePluginInfo;

/**
 * Represents a player.
 */
public interface Player extends Sender {
	
	/**
	 * Gets the player's current position.
	 * @return The player's position.
	 */
	public Position getPosition();
	
	/**
	 * Sets the player's display name.
	 * @param name Display name to set.
	 */
	public void setDisplayName(String name);
	
	/**
	 * Gets the player's block placement mode.
	 * @return The current block placement mode
	 */
	public BlockType getPlaceMode();
	
	/**
	 * Sets the player's block placement mode.
	 * @param type Block to set it to.
	 */
	public void setPlaceMode(BlockType type);
	
	/**
	 * Moves the player to the given position.
	 * @param pos Position to move to.
	 */
	public void moveTo(Position pos);
	
	/**
	 * Moves the player to the given coordinates.
	 * @param x X to move to.
	 * @param y Y to move to.
	 * @param z Z to move to.
	 */
	public void moveTo(float x, float y, float z);
	
	/**
	 * Moves the player to the given coordinates and rotation.
	 * @param x X to move to.
	 * @param y Y to move to.
	 * @param z Z to move to.
	 * @param yaw Yaw to move to.
	 * @param pitch Pitch to move to.
	 */
	public void moveTo(float x, float y, float z, float yaw, float pitch);
	
	/**
	 * Moves the player to the given level and coordinates.
	 * @param level Level to move to.
	 * @param x X to move to.
	 * @param y Y to move to.
	 * @param z Z to move to.
	 */
	public void moveTo(Level level, float x, float y, float z);
	
	/**
	 * Moves the player to the given level, coordinates, and rotation.
	 * @param level Level to move to.
	 * @param x X to move to.
	 * @param y Y to move to.
	 * @param z Z to move to.
	 * @param yaw Yaw to move to.
	 * @param pitch Pitch to move to.
	 */
	public void moveTo(Level level, float x, float y, float z, float yaw, float pitch);
	
	/**
	 * Gets the player's group.
	 * @return The player's group.
	 */
	public Group getGroup();

	/**
	 * Sets the player's group.
	 * @param group The group to set the player to.
	 */
	public void setGroup(Group group);
	
	/**
	 * Gets the player's IP.
	 * @return The players IP.
	 */
	public String getIp();
	
	/**
	 * Gets the player's SocketAddress.
	 * @return The player's SocketAddress.
	 */
	public SocketAddress getAddress();
	
	/**
	 * Kicks the player.
	 * @param reason The reason the player is being kicked.
	 */
	public void disconnect(String reason);
	
	/**
	 * Returns true if the player is using the OpenClassic client.
	 * @return True if the player has the custom client.
	 */
	public boolean hasCustomClient();
	
	/**
	 * Gets the version of the player's OpenClassic client. ("unknown" if not using the custom client)
	 * @return The player's client version.
	 */
	public String getClientVersion();
	
	/**
	 * Gets the player's NBTData.
	 * @return The player's NBTData.
	 */
	public NBTData getData();
	
	/**
	 * Gets a list of plugins from the client/server the player is connected to/from.
	 * @return A list of the player's plugins.
	 */
	public List<RemotePluginInfo> getPlugins();

	/**
	 * Sends a message or command from the player.
	 * @param message Message to send.
	 */
	public void chat(String message);
	
	/**
	 * Hides the given player from this player.
	 * @param player Player to hide.
	 */
	public void hidePlayer(Player player);
	
	/**
	 * Shows the given player to this player.
	 * @param player Player to show.
	 */
	public void showPlayer(Player player);
	
	/**
	 * Returns true if this player can see the given player.
	 * @param player Player to check for.
	 * @return If this player can see the other player.
	 */
	public boolean canSee(Player player);
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns the player's remaining invulnerable time.
	 * @return The player's invulnerable time.
	 */
	public int getInvulnerableTime();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns whether the player is underwater.
	 * @return Whether the player is underwater.
	 */
	public boolean isUnderwater();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns the player's health.
	 * @return The player's health.
	 */
	public int getHealth();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Sets the player's health.
	 * @param health The player's new health.
	 */
	public void setHealth(int health);
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Heals the player.
	 * @param health The amount of health to heal.
	 */
	public void heal(int health);
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Damages the player.
	 * @param damage The amount of damage to deal.
	 */
	public void damage(int damage);
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns whether the player is dead.
	 * @return Whether the player is dead.
	 */
	public boolean isDead();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns the player's previous health.
	 * @return The player's previous health.
	 */
	public int getPreviousHealth();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns the player's remaining air.
	 * @return The player's remaining air.
	 */
	public int getAir();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Sets the player's air.
	 * @param air The player's new air value.
	 */
	public void setAir(int air);
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns the player's score.
	 * @return The player's score.
	 */
	public int getScore();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Sets the player's score.
	 * @param score The player's new score.
	 */
	public void setScore(int score);
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns the player's remaining arrows.
	 * @return The player's remaining arrows.
	 */
	public int getArrows();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Sets the player's arrows.
	 * @param arrows The player's new arrow count.
	 */
	public void setArrows(int arrows);
	
	/**
	 * !! CLIENT ONLY !!
	 * Returns the player's selected hotbar slot.
	 * @return The player's selected hotbar slot.
	 */
	public int getSelectedSlot();
	
	/**
	 * !! CLIENT ONLY !!
	 * Returns the player's hotbar contents.
	 * @return The player's hotbar contents.
	 */
	public int[] getInventoryContents();
	
	/**
	 * !! CLIENT SURVIVAL ONLY !!
	 * Returns the player's hotbar amounts.
	 * @return The player's hotbar amounts.
	 */
	public int[] getInventoryAmounts();
	
	/**
	 * !! CLIENT ONLY !!
	 * Replaces the selected slot with the given block, swapping blocks with another
	 * slot if the block is already in another slot.
	 * @param block Block to replace with.
	 */
	public void replaceSelected(BlockType block);
	
	/**
	 * Respawns the player at the spawn point.
	 */
	public void respawn();

	/**
	 * Gets whether the player can break unbreakable blocks.
	 * @return Whether the player can break unbreakable blocks.
	 */
	public boolean canBreakUnbreakables();
	
	/**
	 * Sets whether the player can break unbreakable blocks.
	 * @param canBreak Whether the player can break unbreakable blocks.
	 */
	public void setCanBreakUnbreakables(boolean canBreak);
	
	/**
	 * Sends a custom packet message to the player.
	 * @param id Id of the message.
	 * @param data Data contained in the message.
	 */
	public void sendCustomMessage(String id, byte data[]);
	
}
