package org.spacehq.openclassic.api.event.player;

import com.zachsthings.onevent.Cancellable;
import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a player is kicked.
 */
public class PlayerKickEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
    private String message;
    private String reason;
    
    public PlayerKickEvent(Player player, String reason, String message) {
    	super(player);
    	this.reason = reason;
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
    
    /**
     * Gets the kick reason.
     * @return The kick reason.
     */
    public String getReason() {
    	return this.reason;
    }
    
    /**
     * Sets the kick reason.
     * @param reason The new kick reason.
     */
    public void setReason(String reason) {
    	String old = this.reason;
    	this.reason = reason;
    	if(this.message.equals(this.getPlayer().getDisplayName() + Color.AQUA + " has been kicked. (" + old + Color.AQUA + ")")) {
    		this.message = this.getPlayer().getDisplayName() + Color.AQUA + " has been kicked. (" + reason + Color.AQUA + ")";
    	}
    }
    
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
