package org.spacehq.openclassic.api.gui;

import java.util.List;

/**
 * Represents the main game screen.
 */
public abstract class HUDComponent extends GuiComponent {

	public HUDComponent(String name) {
		super(name);
	}
	
	public HUDComponent(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
	}

	/**
	 * Gets the player hovered over on the player list display.
	 * @return The hovered over player.
	 */
	public abstract String getHoveredPlayer();
	
	/**
	 * Adds a chat message to the chat history.
	 * @param message Message to add.
	 */
	public abstract void addChat(String message);
	
	/**
	 * Gets all displayed chat history.
	 * @return All displayed chat history.
	 */
	public abstract List<String> getChat();

	/**
	 * Gets the chat message at the given index. (0 = bottom, 50 = max)
	 * @param index Index to look in
	 * @return Message at the given index.
	 */
	public abstract String getChatMessage(int index);
	
	/**
	 * Gets the most recently displayed chat message.
	 * @return The most recent chat message.
	 */
	public abstract String getLastChat();
	
}
