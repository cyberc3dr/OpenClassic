package org.spacehq.openclassic.api.event.player;

import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a player leaves the server.
 */
public class PlayerQuitEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();
	
    private String message;
    
    public PlayerQuitEvent(Player player, String message) {
    	super(player);
    	this.message = message;
    }
    
    /**
     * Gets the logout message.
     * @return The logout message.
     */
    public String getMessage() {
    	return this.message;
    }
    
    /**
     * Sets the logout message.
     * @param message The new logout message.
     */
    public void setMessage(String message) {
    	this.message = message;
    }

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
