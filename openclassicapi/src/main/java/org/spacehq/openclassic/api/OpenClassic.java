package org.spacehq.openclassic.api;

import java.util.logging.Logger;

/**
 * The central class of OpenClassic.
 */
public class OpenClassic {

	private static final Logger logger = Logger.getLogger("OpenClassic");
	private static Game game;
	
	/**
	 * Gets the current game instance.
	 * @return The game instance.
	 */
	public static Game getGame() {
		return game;
	}
	
	/**
	 * Gets the current server instance.
	 * @return The server instance (null if the server instance doesn't exist).
	 */
	public static Server getServer() {
		return game instanceof Server ? (Server) game : null;
	}
	
	/**
	 * Gets the current client instance.
	 * @return The client instance (null if the client instance doesn't exist).
	 */
	public static Client getClient() {
		return game instanceof Client ? (Client) game : null;
	}
	
	/**
	 * Sets the current game instance.
	 * @param game The game instance.
	 */
	public static void setGame(Game game) {
		if(OpenClassic.game != null && game != null) return;
		OpenClassic.game = game;
	}
	
	/**
	 * Returns true if the game is running.
	 * @return True if the game is running.
	 */
	public static boolean isRunning() {
		return game.isRunning();
	}
	
	/**
	 * Gets OpenClassic's logger.
	 * @return The logger.
	 */
	public static Logger getLogger() {
		return logger;
	}
	
	private OpenClassic() {
	}
	
}
