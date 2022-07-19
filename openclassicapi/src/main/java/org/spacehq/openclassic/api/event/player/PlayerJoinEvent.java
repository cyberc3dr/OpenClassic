package org.spacehq.openclassic.api.event.player;

import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a player joins the server.
 */
public class PlayerJoinEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();
	
    private String message;
    
    public PlayerJoinEvent(Player player, String message) {
    	super(player);
    	this.message = message;
    }
    
    /**
     * Gets the login message.
     * @return The login message.
     */
    public String getMessage() {
    	return this.message;
    }
    
    /**
     * Sets the login message.
     * @param message The new login message.
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
