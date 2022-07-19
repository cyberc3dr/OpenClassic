package org.spacehq.openclassic.game.network.msg;

/**
 * Sent when a player changes their rotation.
 */
public class PlayerRotationMessage extends Message {
	
	private byte playerId;
	private float yaw;
	private float pitch;
	
	public PlayerRotationMessage(byte playerId, float yaw, float pitch) {
		this.playerId = playerId;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	/**
	 * Gets the ID of the affected player.
	 * @return The player's ID.
	 */
	public byte getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * Gets the new yaw of the player.
	 * @return The player's new yaw.
	 */
	public float getYaw() {
		return this.yaw;
	}
	
	/**
	 * Gets the new pitch of the player.
	 * @return The player's new pitch.
	 */
	public float getPitch() {
		return this.pitch;
	}
	
	@Override
	public String toString() {
		return "PlayerRotationMessage{playerid=" + playerId + ",yaw=" + yaw + ",pitch=" + pitch + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 11;
	}
	
}
