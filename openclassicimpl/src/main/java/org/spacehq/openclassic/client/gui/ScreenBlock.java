package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.block.BlockType;

public class ScreenBlock {

	private BlockType block;
	private int x;
	private int y;
	
	public ScreenBlock(BlockType block, int x, int y) {
		this.block = block;
		this.x = x;
		this.y = y;
	}
	
	public BlockType getBlock() {
		return this.block;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
}
