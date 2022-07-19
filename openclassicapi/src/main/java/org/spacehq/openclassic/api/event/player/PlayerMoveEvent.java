package org.spacehq.openclassic.api.event.player;

import com.zachsthings.onevent.Cancellable;
import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a player moves.
 */
public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	private Position from;
	private Position to;
    
    public PlayerMoveEvent(Player player, Position from, Position to) {
    	super(player);
    	this.from = from;
    	this.to = to;
    }
    
    /**
     * Gets the location this move is from.
     * @return The location the move is from.
     */
    public Position getFrom() {
    	return this.from;
    }
    
    /**
     * Sets the location this move is from.
     * @param from The location the move is from.
     */
    public void setFrom(Position from) {
    	this.from = from;
    }
    
    /**
     * Gets the location this move is to.
     * @return The location the move is to.
     */
    public Position getTo() {
    	return this.to;
    }
    
    /**
     * Sets the location this move is to.
     * @param to The location the move is to.
     */
    public void setTo(Position to) {
    	this.to = to;
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
