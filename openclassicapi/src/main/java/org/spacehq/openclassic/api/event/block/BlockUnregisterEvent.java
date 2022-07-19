package org.spacehq.openclassic.api.event.block;

import com.zachsthings.onevent.Event;
import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.block.BlockType;

/**
 * Called when a block is unregistered.
 */
public class BlockUnregisterEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private BlockType type;
	
	public BlockUnregisterEvent(BlockType type) {
		this.type = type;
	}
	
	/**
	 * Gets the unregistered BlockType.
	 * @return The unregistered BlockType.
	 */
	public BlockType getBlock() {
		return this.type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
