package org.spacehq.openclassic.api.event.player;

import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a custom message is recieved.
 */
public class CustomMessageEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();
	
	private String id;
	private byte data[];
	
	public CustomMessageEvent(Player player, String id, byte data[]) {
		super(player);
		this.id = id;
		this.data = data;
	}
	
	/**
	 * Gets the id of the message involved in this event.
	 * @return The id of the message involved in this event.
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Gets the data of the message involved in this event.
	 * @return The data of the message involved in this event.
	 */
	public byte[] getData() {
		return this.data;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
