package org.spacehq.openclassic.api.event.player;

import com.zachsthings.onevent.Cancellable;
import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a player chats.
 */
public class PlayerChatEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
    private String message;
    private String format = "%1$s" + Color.WHITE + ": %2$s";
    
    public PlayerChatEvent(Player player, String message) {
    	super(player);
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
    
    /**
     * Gets the format of the chat output.
     * @return The chat format.
     */
    public String getFormat() {
    	return this.format;
    }
    
    /**
     * Sets the format of the chat output.
     * @param format The new chat format.
     */
    public void setFormat(String format) {
    	this.format = format;
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
