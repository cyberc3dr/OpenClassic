package org.spacehq.openclassic.game.network.msg;

import java.util.Arrays;

/**
 * Sent when a client needs level data.
 */
public class LevelDataMessage extends Message {

	private short length;
	private byte[] data;
	private byte percent;
	
	public LevelDataMessage(short length, byte[] data, byte percent) {
		this.length = length;
		this.data = data;
		this.percent = percent;
	}
	
	/**
	 * Gets the length of the data.
	 * @return The data length.
	 */
	public short getLength() {
		return this.length;
	}

	/**
	 * Gets the level data in this packet.
	 * @return The packet's level data.
	 */
	public byte[] getData() {
		return this.data;
	}
	
	/**
	 * Get the percentage of completion of the level data exchange.
	 * @return The percentage.
	 */
	public byte getPercent() {
		return this.percent;
	}
	
	@Override
	public String toString() {
		return "LevelDataMessage{length=" + length + ",data=" + Arrays.toString(data) + ",percent=" + percent + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 3;
	}
	
}
