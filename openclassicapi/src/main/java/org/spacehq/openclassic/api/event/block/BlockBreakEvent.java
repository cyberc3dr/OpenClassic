package org.spacehq.openclassic.api.event.block;

import com.zachsthings.onevent.Cancellable;
import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.player.Player;

/**
 * Called when a block is broken.
 */
public class BlockBreakEvent extends BlockEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private BlockType held;
	private Player player;
	private boolean cancelled = false;
	
	public BlockBreakEvent(Block block, Player player, BlockType held) {
		super(block);
		this.player = player;
		this.held = held;
	}
	
	/**
	 * Gets the player breaking the block.
	 * @return The player breaking the block.
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Gets the block held by the player breaking the block.
	 * @return The block held by the player.
	 */
	public BlockType getHeldBlock() {
		return this.held;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
