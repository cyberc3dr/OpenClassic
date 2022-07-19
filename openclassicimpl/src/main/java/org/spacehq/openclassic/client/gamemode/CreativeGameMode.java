package org.spacehq.openclassic.client.gamemode;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.gui.BlockSelectScreen;
import org.spacehq.openclassic.client.level.ClientLevel;

public class CreativeGameMode extends GameMode {

	public CreativeGameMode() {
		this.creative = true;
	}

	@Override
	public void openInventory() {
		OpenClassic.getClient().setActiveComponent(new BlockSelectScreen());
	}

	@Override
	public void apply(ClientLevel level) {
		level.removeSurvivalEntities();
	}

	@Override
	public void apply(Player player) {
		int slot = 0;
		for(BlockType block : Blocks.getBlocks()) {
			if(slot >= 9) {
				break;
			}

			if(block != null && block.isSelectable()) {
				player.getInventoryAmounts()[slot] = 1;
				player.getInventoryContents()[slot] = block.getId();
				slot++;
			}
		}
	}
	
}
