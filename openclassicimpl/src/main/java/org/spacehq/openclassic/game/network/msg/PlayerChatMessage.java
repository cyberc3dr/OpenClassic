package org.spacehq.openclassic.game.network.msg;

/**
 * Sent or recieved when a chat message is sent to/from a player.
 */
public class PlayerChatMessage extends Message {
	
	private byte playerId;
	private String message;
	
	public PlayerChatMessage(byte playerId, String message) {
		this.playerId = playerId;
		this.message = message;
	}
	
	/**
	 * Gets the ID of the chatting player. (The reciever's ID if it's not sent by another player.)
	 * @return The ID of the chatting player.
	 */
	public byte getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * Gets the message being sent.
	 * @return The message being sent.
	 */
	public String getMessage() {
		return this.message;
	}
	
	@Override
	public String toString() {
		return "PlayerChatMessage{playerid=" + playerId + ",message=" + message + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 13;
	}
	
}
