package org.spacehq.openclassic.game.network.msg;

/**
 * Sent/Recieved when a player is moved.
 */
public class PlayerTeleportMessage extends Message {
	
	private byte playerId;
	private float x;
	private float y;
	private float z;
	private float yaw;
	private float pitch;
	
	public PlayerTeleportMessage(byte playerId, float x, float y, float z, float yaw, float pitch) {
		this.playerId = playerId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	/**
	 * Gets the ID of the moved player.
	 * @return The player's ID.
	 */
	public byte getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * Gets the player's new X.
	 * @return The new X.
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Gets the player's new Y.
	 * @return The new Y.
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Gets the player's new Z.
	 * @return The new Z.
	 */
	public float getZ() {
		return this.z;
	}
	
	/**
	 * Gets the player's new yaw.
	 * @return The new yaw.
	 */
	public float getYaw() {
		return this.yaw;
	}
	
	/**
	 * Gets the player's new pitch.
	 * @return The new pitch.
	 */
	public float getPitch() {
		return this.pitch;
	}
	
	@Override
	public String toString() {
		return "PlayerTeleportMessage{playerid=" + playerId + ",x=" + x + ",y=" + y + ",z=" + z + ",yaw=" + yaw + ",pitch=" + pitch + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 8;
	}
	
}
