package org.spacehq.openclassic.game.network.msg;

/**
 * Sent before level data to prepare the client for data.
 */
public class LevelInitializeMessage extends Message {

	@Override
	public String toString() {
		return "LevelInitializeMessage{}";
	}
	
	@Override
	public byte getOpcode() {
		return 2;
	}
	
}
