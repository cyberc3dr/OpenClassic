package org.spacehq.openclassic.game.network.msg.custom;

import org.spacehq.openclassic.game.network.msg.Message;

/**
 * Changes the colors of certain aspects of a level on the client.
 */
public class LevelPropertyMessage extends Message {

	private String type;
	private int value;
	
	public LevelPropertyMessage(String type, int value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Gets the type of change this message contains.
	 * @return The type of change.
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * Gets the value being changed to.
	 * @return The new value.
	 */
	public int getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "LevelColorMessage{type=" + type + ",value=" + value + "}";
	}

	@Override
	public byte getOpcode() {
		return 21;
	}
	
}
