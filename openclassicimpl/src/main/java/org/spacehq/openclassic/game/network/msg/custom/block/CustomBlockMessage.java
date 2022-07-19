package org.spacehq.openclassic.game.network.msg.custom.block;

import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.game.network.msg.Message;

/**
 * Contains a custom block.
 */
public class CustomBlockMessage extends Message {

	private BlockType block;
	
	public CustomBlockMessage(BlockType block) {
		this.block = block;
	}
	
	/**
	 * Gets the block contained in the message.
	 * @return The contained block.
	 */
	public BlockType getBlock() {
		return this.block;
	}
	
	@Override
	public String toString() {
		return "CustomBlockMessage{blockid=" + block.getId() + "}";
	}
	
	@Override
	public byte getOpcode() {
		return 17;
	}

}
