package org.spacehq.openclassic.api.event.player;

import com.zachsthings.onevent.Event;

import org.spacehq.openclassic.api.player.Player;

/**
 * Represents an event involving a player.
 */
public abstract class PlayerEvent extends Event {

	private Player player;
	
	public PlayerEvent(Player player) {
		this.player = player;
	}
	
	/**
	 * Gets the player involved in this event.
	 * @return The player involved.
	 */
	public Player getPlayer() {
		return this.player;
	}
	
}
