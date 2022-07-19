package org.spacehq.openclassic.game.network.msg;

/**
 * Sent when a player is spawned.
 */
public class PlayerSpawnMessage extends Message {
	
	private byte playerId;
	private String name;
	private float x;
	private float y;
	private float z;
	private float yaw;
	private float pitch;
	
	public PlayerSpawnMessage(byte playerId, String name, float x, float y, float z, float yaw, float pitch) {
		this.playerId = playerId;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	/**
	 * Gets the player ID of the spawned player.
	 * @return The player's ID.
	 */
	public byte getPlayerId() {
		return this.playerId;
	}
	
	/**
	 * Gets the name of the spawned player.
	 * @return The player's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the X of the spawned player.
	 * @return The player's X.
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Gets the Y of the spawned player.
	 * @return The player's Y.
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Gets the Z of the spawned player.
	 * @return The player's Z.
	 */
	public float getZ() {
		return this.z;
	}
	
	/**
	 * Gets the yaw of the spawned player.
	 * @return The player's yaw.
	 */
	public float getYaw() {
		return this.yaw;
	}
	
	/**
	 * Gets the pitch of the spawned player.
	 * @return The player's pitch.
	 */
	public float getPitch() {
		return this.pitch;
	}
	
	@Override
	public String toString() {
		return "PlayerSpawnMessage{playerid=" + playerId + ",name=" + name + ",x=" + x + ",y=" + y + ",z=" + z + ",yaw=" + yaw + ",pitch=" + pitch + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 7;
	}
	
}
