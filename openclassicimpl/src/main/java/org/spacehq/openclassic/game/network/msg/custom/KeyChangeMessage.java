package org.spacehq.openclassic.game.network.msg.custom;

import org.spacehq.openclassic.game.network.msg.Message;

/**
 * Sent/Recieved when a key's state is changed.
 */
public class KeyChangeMessage extends Message {

	private int key;
	private boolean pressed;
	
	public KeyChangeMessage(int key, boolean pressed) {
		this.key = key;
		this.pressed = pressed;
	}
	
	/**
	 * Gets the changed key.
	 * @return The changed key.
	 */
	public int getKey() {
		return this.key;
	}
	
	/**
	 * Returns true if the key has been pressed.
	 * @return True if the key has been pressed.
	 */
	public boolean isPressed() {
		return this.pressed;
	}
	
	@Override
	public String toString() {
		return "KeyChangeMessage{key=" + key + ",pressed=" + pressed + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 20;
	}
	
}
