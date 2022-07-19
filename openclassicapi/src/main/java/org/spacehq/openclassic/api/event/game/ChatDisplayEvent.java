package org.spacehq.openclassic.api.event.game;

import com.zachsthings.onevent.Cancellable;
import com.zachsthings.onevent.HandlerList;

/**
 * Called when chat is added to the HUD of a client.
 */
public class ChatDisplayEvent extends GameEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
    private String message;
    
    public ChatDisplayEvent(String message) {
    	this.message = message;
    }
    
    /**
     * Gets the message being sent.
     * @return The sent message.
     */
    public String getMessage() {
    	return this.message;
    }
    
    /**
     * Sets the message being sent.
     * @param message Message to send.
     */
    public void setMessage(String message) {
    	this.message = message;
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
