package org.spacehq.openclassic.api.event.game;

import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.command.Sender;

/**
 * Called when a command is not found.
 */
public class CommandNotFoundEvent extends GameEvent {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean showMessage = true;
	private Sender sender;
	private String command;
	
	public CommandNotFoundEvent(Sender sender, String command) {
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
	
	/**
	 * Returns true if the unknown command message will show.
	 * @return True if the message will show.
	 */
	public boolean showMessage() {
		return this.showMessage;
	}
	
	/**
	 * Sets whether the unknown command message will show.
	 * @param show Whether the message will show.
	 */
	public void setShowMessage(boolean show) {
		this.showMessage = show;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
