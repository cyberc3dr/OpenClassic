package org.spacehq.openclassic.api.block;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.zachsthings.onevent.EventManager;

import org.spacehq.openclassic.api.event.block.BlockRegisterEvent;
import org.spacehq.openclassic.api.event.block.BlockUnregisterEvent;

/**
 * The block registry.
 */
public class Blocks {
	
	private static final BlockType registry[] = new BlockType[256];
	
	/**
	 * Gets the block with the given ID.
	 * @param id ID of the block.
	 * @return The block with the given ID.
	 */
	public static BlockType fromId(int id) {
		if(id < 0) {
			return null;
		}
		
		return registry[id];
	}
	
	/**
	 * Registers a block.
	 * @param block Block to register.
	 */
	public static void register(BlockType block) {
		Validate.notNull(block, "Block cannot be null.");
		EventManager.callEvent(new BlockRegisterEvent(block));
		registry[block.getId()] = block;
	}
	
	/**
	 * Unregisters a block.
	 * @param id ID of the Block to unregister.
	 */
	public static void unregister(int id) {
		if(id < 0) {
			return;
		}
		
		EventManager.callEvent(new BlockUnregisterEvent(fromId(id)));
		registry[id] = null;
	}
	
	/**
	 * Gets a list of all the blocks.
	 * @return A list of blocks.
	 */
	public static List<BlockType> getBlocks() {
		return Arrays.asList(registry);
	}
	
}
