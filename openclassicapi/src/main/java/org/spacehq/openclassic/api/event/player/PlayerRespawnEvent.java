package org.spacehq.openclassic.api.event.player;

import com.zachsthings.onevent.Cancellable;
import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a player respawns with the Load Location key.
 * NOTICE: Only called on client currently due to detection issues.
 */
public class PlayerRespawnEvent extends PlayerEvent implements Cancellable {
    
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	private Position pos;
	
    public PlayerRespawnEvent(Player player, Position pos) {
    	super(player);
    	this.pos = pos;
    }
    
    /**
     * Gets the location the player is respawning at.
     * @return The respawn location.
     */
    public Position getPosition() {
    	return this.pos;
    }
    
    /**
     * Sets the location the player is respawning at.
     * @param pos The new respawn location.
     */
    public void setPosition(Position pos) {
    	this.pos = pos;
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
