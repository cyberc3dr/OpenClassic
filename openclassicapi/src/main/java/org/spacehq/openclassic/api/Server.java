package org.spacehq.openclassic.api;

import org.spacehq.openclassic.api.command.Console;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.permissions.PermissionManager;

import java.util.List;

/**
 * Represents the OpenClassic Server.
 */
public interface Server extends Game {
	
	/**
	 * Broadcasts a message via chat, translating it as a key
	 * if a translation is available.
	 * @param message The message to broadcast.
	 */
	public void broadcastMessage(String message);
	
	/**
	 * Broadcasts a message via chat, translating it as a key
	 * if a translation is available and formatting it using
	 * String.format.
	 * @param message The message to broadcast.
	 */
	public void broadcastMessage(String message, Object... args);
	
	/**
	 * Gets the server's MOTD.
	 * @return The server's MOTD.
	 */
	public String getMotd();
	
	/**
	 * Sets the server's MOTD.
	 * @param motd The server's new MOTD.
	 */
	public void setMotd(String motd);
	
	/**
	 * Gets the server's name.
	 * @return The server's name.
	 */
	public String getServerName();
	
	/**
	 * Sets the server's name.
	 * @param name The server's new name.
	 */
	public void setServerName(String name);
	
	/**
	 * Gets the maximum amount of players allowed to be on at once.
	 * @return The maximum amount of players.
	 */
	public int getMaxPlayers();
	
	/**
	 * Sets the maximum amount of players allowed to be on at once.
	 * @param max The new maximum.
	 */
	public void setMaxPlayers(int max);
	
	/**
	 * Gets the server's network port.
	 * @return The server's network port.
	 */
	public int getPort();
	
	/**
	 * Sets the server's network port.
	 * @param port The server's new network port.
	 */
	public void setPort(int port);
	
	/**
	 * Gets whether the server will display on the minecraft.net server list.
	 * @return Whether the server will display.
	 */
	public boolean isPublic();
	
	/**
	 * Sets whether the server will display on the minecraft.net server list.
	 * @return serverPublic Whether the server will display.
	 */
	public void setPublic(boolean serverPublic);
	
	/**
	 * Returns true if the server is set to verify names on minecraft.net
	 * @return Whether the server verifies names.
	 */
	public boolean isOnlineMode();
	
	/**
	 * Sets whether the server verifies names on minecraft.net
	 * @param online Whether the server verifies names.
	 */
	public void setOnlineMode(boolean online);
	
	/**
	 * Gets the server's minecraft.net URL.
	 * @return The server's minecraft.net URL.
	 */
	public String getURL();
	
	/**
	 * Returns true if the server uses a whitelist.
	 * @return True if the server uses a whitelist.
	 */
	public boolean doesUseWhitelist();
	
	/**
	 * Sets whether the server uses a whitelist.
	 * @param whitelist Whether the server uses a whitelist.
	 */
	public void setUseWhitelist(boolean whitelist);
	
	/**
	 * Returns true if the given player is whitelisted.
	 * @param player Player to check.
	 * @return player Whether the player is whitelisted.
	 */
	public boolean isWhitelisted(String player);
	
	/**
	 * Returns true if the given player is banned.
	 * @param player Player to check.
	 * @return player Whether the player is banned.
	 */
	public boolean isBanned(String player);
	
	/**
	 * Returns true if the given IP address is banned.
	 * @param address IP address to check.
	 * @return player Whether the IP address is banned.
	 */
	public boolean isIpBanned(String address);
	
	/**
	 * Bans the given player.
	 * @param player Player to ban.
	 */
	public void banPlayer(String player);
	
	/**
	 * Bans the given player using the given reason.
	 * @param player Player to ban.
	 * @param reason Reason for the ban.
	 */
	public void banPlayer(String player, String reason);
	
	/**
	 * Unbans the given player.
	 * @param player Player to unban.
	 */
	public void unbanPlayer(String player);
	
	/**
	 * Bans the given IP address.
	 * @param address Address to ban.
	 */
	public void banIp(String address);
	
	/**
	 * Bans the given IP address with the given reason.
	 * @param address Address to ban.
	 * @param reason Reason for the ban.
	 */
	public void banIp(String address, String reason);
	
	/**
	 * Unbans the given IP address.
	 * @param address Address to unban.
	 */
	public void unbanIp(String address);
	
	/**
	 * Allows the given player to logging in to this server if whitelisting is enabled.
	 * @param player Player to allow.
	 */
	public void whitelist(String player);
	
	/**
	 * Denies the given player from logging in to this server if whitelisting is enabled.
	 * @param player Player to deny.
	 */
	public void unwhitelist(String player);
	
	/**
	 * Gets the reason for the given player's ban.
	 * @param player Player to get the ban reason for.
	 * @return The player's ban reason.
	 */
	public String getBanReason(String player);
	
	/**
	 * Gets the reason for the given IP address's IP ban.
	 * @param address IP address to get the ban reason for.
	 * @return The IP's ban reason.
	 */
	public String getIpBanReason(String address);

	/**
	 * Gets all the players banned on this server.
	 * @return All the banned players.
	 */
	public List<String> getBannedPlayers();
	
	/**
	 * Gets all the IPs banned on this server.
	 * @return All the banned IPs.
	 */
	public List<String> getBannedIps();
	
	/**
	 * Gets all the players whitelisted on this server.
	 * @return All the whitelisted players.
	 */
	public List<String> getWhitelistedPlayers();
	
	/**
	 * Gets the server's permission manager.
	 * @return The server's permission manager.
	 */
	public PermissionManager getPermissionManager();
	
	/**
	 * Gets the default level.
	 * @return The default level.
	 */
	public Level getDefaultLevel();
	
	/**
	 * Gets the level with the given name.
	 * @param name Name of the level.
	 * @return The level.
	 */
	public Level getLevel(String name);
	
	/**
	 * Gets all the levels loaded onto the server.
	 * @return All the levels loaded.
	 */
	public List<Level> getLevels();
	
	/**
	 * Loads the level with the given name.
	 * @param name Name of the level.
	 * @return The loaded level.
	 */
	public Level loadLevel(String name);
	
	/**
	 * Loads the level with the given name.
	 * @param name Name of the level.
	 * @param create Whether to create the level if it isn't found.
	 * @return The loaded level.
	 */
	public Level loadLevel(String name, boolean create);
	
	/**
	 * Unloads the level with the given name.
	 * @param name Level to unload.
	 */
	public void unloadLevel(String name);
	
	/**
	 * Unloads the level with the given name.
	 * @param name Level to unload.
	 * @param announce Whether to announce the level being unloaded.
	 */
	public void unloadLevel(String name, boolean announce);
	
	/**
	 * Saves all the loaded levels.
	 */
	public void saveLevels();
	
	/**
	 * Saves the level with the given name.
	 * @param name Level to save.
	 */
	public void saveLevel(String name);
	
	/**
	 * Saves the given level.
	 * @param level Level to save.
	 */
	public void saveLevel(Level level);

	/**
	 * Gets the sender used to send console commands.
	 * @return The console command sender.
	 */
	public Console getConsoleSender();
	
}
