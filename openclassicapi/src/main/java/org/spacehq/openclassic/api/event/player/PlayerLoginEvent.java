package org.spacehq.openclassic.api.event.player;

import java.net.SocketAddress;

import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a player logs in.
 */
public class PlayerLoginEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();
	
    private final SocketAddress address;
    private Result result = Result.ALLOWED;
    private String message = "";
    
    public PlayerLoginEvent(Player player, SocketAddress address) {
        super(player);
        this.address = address;
    }

    public PlayerLoginEvent(Player player, SocketAddress address, Result result, String message) {
        this(player, address);
        this.result = result;
        this.message = message;
    }
    
    /**
     * Gets the player's address.
     * @return The player's address.
     */
    public SocketAddress getAddress() {
    	return this.address;
    }
    
    /**
     * Gets the login result.
     * @return The login result..
     */
    public Result getResult() {
    	return this.result;
    }
    
    /**
     * Sets the login result.
     * @param The new login result.
     */
    public void setResult(Result result) {
    	this.result = result;
    }
    
    /**
     * Gets the kick message.
     * @return The kick message.
     */
    public String getKickMessage() {
    	return this.message;
    }
    
    /**
     * Sets the kick message.
     * @param The new kick message.
     */
    public void setKickMessage(String message) {
    	this.message = message;
    }
    
    /**
     * Allows the player to login.
     */
    public void allow() {
        this.result = Result.ALLOWED;
        this.message = "";
    }

    /**
     * Disallows the player from logging in.
     * @param Result of the login.
     * @param Message to kick the player with.
     */
    public void disallow(Result result, String message) {
        this.result = result;
        this.message = message;
    }
    
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
    
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
    /**
     * The result of a login attempt.
     */
    public enum Result {
        /**
         * The player is allowed to login.
         */
        ALLOWED,
        /**
         * The player isn't allowed to login since the server is full.
         */
        KICK_FULL,
        /**
         * The player isn't allowed to login since they are banned.
         */
        KICK_BANNED,
        /**
         * The player isn't allowed to login since they aren't on the whitelist.
         */
        KICK_WHITELIST,
        /**
         * The player isn't allowed to login due to other reasons.
         */
        KICK_OTHER
    }
	
}
