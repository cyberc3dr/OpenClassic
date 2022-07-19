package org.spacehq.openclassic.game.network.msg;

/**
 * Sent to update a player's position and rotation.
 */
public class PlayerPositionRotationMessage extends Message {
	
	private byte playerId;
	private float xChange;
	private float yChange;
	private float zChange;
	private float yaw;
	private float pitch;
	
	public PlayerPositionRotationMessage(byte playerId, float xChange, float yChange, float zChange, float yaw, float pitch) {
		this.playerId = playerId;
		this.xChange = xChange;
		this.yChange = yChange;
		this.zChange = zChange;
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
	 * Gets the X change of the player.
	 * @return The X change.
	 */
	public float getXChange() {
		return this.xChange;
	}
	
	/**
	 * Gets the Y change of the player.
	 * @return The Y change.
	 */
	public float getYChange() {
		return this.yChange;
	}
	
	/**
	 * Gets the Z change of the player.
	 * @return The Z change.
	 */
	public float getZChange() {
		return this.zChange;
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
		return "PlayerPositionRotationMessage{playerid=" + playerId + ",xchange=" + xChange + ",ychange=" + yChange + ",zchange=" + zChange + ",yaw=" + yaw + ",pitch=" + pitch + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 9;
	}
	
}
