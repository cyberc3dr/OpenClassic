package com.mojang.minecraft.entity.player;

import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.Blocks;

public class Inventory {

	public static final int POP_TIME_DURATION = 5;

	public int[] slots = new int[9];
	public int[] count = new int[9];
	public boolean[] pop = new boolean[9];
	public int selected = 0;

	public Inventory() {
		for(int slot = 0; slot < 9; slot++) {
			this.slots[slot] = -1;
			this.count[slot] = 0;
		}
	}

	public int getSelected() {
		return this.slots[this.selected];
	}

	private int getFirst(int block) {
		for(int slot = 0; slot < this.slots.length; slot++) {
			if(this.slots[slot] == block) {
				return slot;
			}
		}

		return -1;
	}

	private int getFirstNotFull(int block) {
		for(int slot = 0; slot < this.slots.length; slot++) {
			if(this.slots[slot] == block && this.count[slot] < 99) {
				return slot;
			}
		}

		return -1;
	}

	public void selectBlock(int block, boolean creative) {
		int slot = this.getFirst(block);
		if(slot >= 0) {
			this.selected = slot;
		} else {
			if(creative && Blocks.fromId(block) != null && Blocks.fromId(block).isSelectable()) {
				this.replaceSlot(Blocks.fromId(block));
			}
		}
	}

	public void scrollSelection(int direction) {
		if(direction > 0) {
			direction = 1;
		}

		if(direction < 0) {
			direction = -1;
		}

		this.selected -= direction;
		while(this.selected < 0) {
			this.selected += this.slots.length;
		}

		while(this.selected >= this.slots.length) {
			this.selected -= this.slots.length;
		}
	}

	public void replaceSlot(BlockType block) {
		if(block != null) {
			int slot = this.getFirst(block.getId());
			if(slot >= 0) {
				this.slots[slot] = this.slots[this.selected];
			}

			this.slots[this.selected] = block.getId();
		}
	}

	public boolean addResource(int block) {
		int slot = this.getFirstNotFull(block);
		if(slot < 0) {
			slot = this.getFirst(-1);
		}

		if(slot >= 0) {
			this.slots[slot] = block;
			this.count[slot]++;
			this.pop[slot] = true;
			return true;
		}

		return false;
	}
	
	public boolean addResource(int block, int count) {
		boolean result = false;
		for(int cnt = 0; cnt < count; cnt++) {
			if(this.addResource(block)) {
				result = true;
			}
		}

		return result;
	}

	public boolean removeSelected(int block) {
		int slot = this.selected;
		if(block != this.slots[slot]) {
			return false;
		} else {
			this.count[slot]--;
			if(this.count[slot] <= 0) {
				this.slots[slot] = -1;
				this.count[slot] = 0;
			}

			return true;
		}
	}
}
