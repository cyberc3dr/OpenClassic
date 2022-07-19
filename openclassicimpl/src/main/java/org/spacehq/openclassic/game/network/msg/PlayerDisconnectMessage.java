package org.spacehq.openclassic.game.network.msg;

/**
 * Sent to disconnect a player. (i.e. kicking)
 */
public class PlayerDisconnectMessage extends Message {
	
	private String message;
	
	public PlayerDisconnectMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Gets the disconnect message.
	 * @return The disconnect message.
	 */
	public String getMessage() {
		return this.message;
	}
	
	@Override
	public String toString() {
		return "PlayerDisconnectMessage{message=" + message + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 14;
	}
	
}
