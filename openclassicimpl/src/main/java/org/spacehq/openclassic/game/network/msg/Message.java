package org.spacehq.openclassic.game.network.msg;

/**
 * Represents a network message.
 */
public abstract class Message {

	@Override
	public abstract String toString();
	
	/**
	 * Gets the message's opcode.
	 * @return The message's opcode.
	 */
	public abstract byte getOpcode();
	
}
