package org.spacehq.openclassic.api.event.game;

import com.zachsthings.onevent.Cancellable;
import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.command.Sender;

/**
 * Called when a command is executed.
 */
public class PreCommandEvent extends GameEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	private Sender sender;
	private String command;
	
	public PreCommandEvent(Sender sender, String command) {
		this.sender = sender;
		this.command = command;
	}
	
	/**
	 * Gets the sender sending the command.
	 * @return The command sender.
	 */
	public Sender getSender() {
		return this.sender;
	}
	
	/**
	 * Gets the command being sent.
	 * @return The sent command.
	 */
	public String getCommand() {
		return this.command;
	}
	
	/**
	 * Sets the command being sent.
	 * @param command The new command to send.
	 */
	public void setCommand(String command) {
		this.command = command;
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
