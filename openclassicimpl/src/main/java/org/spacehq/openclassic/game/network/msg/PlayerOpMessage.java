package org.spacehq.openclassic.game.network.msg;

/**
 * Sent when a player is made an OP.
 */
public class PlayerOpMessage extends Message {
	
	private byte op;
	
	public PlayerOpMessage(byte op) {
		this.op = op;
	}
	
	/**
	 * Gets whether the player is an OP. (Compare to Constants.OP and Constants.NOT_OP)
	 * @return Whether the player is an OP.
	 */
	public byte getOp() {
		return this.op;
	}
	
	@Override
	public String toString() {
		return "PlayerOpMessage{op=" + op + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 15;
	}
	
}
