package org.spacehq.openclassic.api;

import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.HUDComponent;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.settings.Settings;
import org.spacehq.openclassic.api.settings.bindings.Bindings;

/**
 * Represents the OpenClassic Client.
 */
public interface Client extends Game {

	/**
	 * Gets the client's settings.
	 * @return The client's settings.
	 */
	public Settings getSettings();
	
	/**
	 * Gets the client's hack settings.
	 * @return The client's hack settings.
	 */
	public Settings getHackSettings();
	
	/**
	 * Gets the client's key bindings.
	 * @return The client's key bindings.
	 */
	public Bindings getBindings();
	
	/**
	 * Gets the client's player.
	 * @return The client's player.
	 */
	public Player getPlayer();
	
	/**
	 * Gets the client's level.
	 * @return The client's level.
	 */
	public Level getLevel();
	
	/**
	 * Loads and enters the given level.
	 * @return name The level to load.
	 */
	public Level openLevel(String name);
	
	/**
	 * Saves the current level.
	 * @return Whether the level was saved.
	 */
	public boolean saveLevel();
	
	/**
	 * Saves the current level under another name.
	 * @param name Name to save under.
	 * @return Whether the level was saved.
	 */
	public boolean saveLevel(String name);
	
	/**
	 * Gets the current active main GUI component.
	 * @return The active component.
	 */
	public GuiComponent getActiveComponent();
	
	/**
	 * Sets the current active main GUI component.
	 * @param component Component to set as active.
	 */
	public void setActiveComponent(GuiComponent component);

	/**
	 * Returns true if the client is in a game session.
	 * @return True if the client is ingame.
	 */
	public boolean isInGame();

	/**
	 * Gets the HUD of the client.
	 * @return The client's HUD. (null if not in a game session)
	 */
	public HUDComponent getHUD();

	/**
	 * Exits the current level and returns to the main menu.
	 */
	public void exitGameSession();
	
	/**
	 * Gets whether the game is in a multiplayer session.
	 * @return Whether the game is in multiplayer.
	 */
	public boolean isInMultiplayer();
	
	/**
	 * Gets the multiplayer server IP that this client is connected to.
	 * @return The connected server's IP, or null if the client isn't connected to a server.
	 */
	public String getServerIP();
	
	/**
	 * Returns true if the client is connected to an OpenClassic server.
	 * @return True if the server is an OpenClassic server.
	 */
	public boolean isConnectedToOpenClassic();
	
	/**
	 * Gets the version of the OpenClassic server this client is connected to, if applicable.
	 * @return The server's version
	 */
	public String getServerVersion();
	
	/**
	 * Gets the client's progress bar display.
	 * @return The client's progress bar display.
	 */
	public ProgressBar getProgressBar();
	
	/**
	 * Returns true if the client is in survival mode.
	 * @return Whether the client is in survival mode.
	 */
	public boolean isInSurvival();

	/**
	 * Joins a server.
	 * @param url URL of the server.
	 */
	public void joinServer(String url);
	
}
