package org.spacehq.openclassic.game.network.msg;

/**
 * Sent when a player's position is changed.
 */
public class PlayerPositionMessage extends Message {
	
	private byte playerId;
	private float xChange;
	private float yChange;
	private float zChange;
	
	public PlayerPositionMessage(byte playerId, float xChange, float yChange, float zChange) {
		this.playerId = playerId;
		this.xChange = xChange;
		this.yChange = yChange;
		this.zChange = zChange;
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
	
	@Override
	public String toString() {
		return "PlayerPositionMessage{playerid=" + playerId + ",xchange=" + xChange + ",ychange=" + yChange + ",zchange=" + zChange + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 10;
	}
	
}
