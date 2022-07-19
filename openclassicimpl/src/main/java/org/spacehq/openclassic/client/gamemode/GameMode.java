package org.spacehq.openclassic.client.gamemode;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.block.StepSound;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.event.block.BlockBreakEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.client.util.BlockUtils;
import org.spacehq.openclassic.game.network.msg.PlayerSetBlockMessage;

import com.zachsthings.onevent.EventManager;

public class GameMode {

	public boolean creative = false;

	public void apply(ClientLevel level) {
	}

	public void openInventory() {
	}

	public void hitBlock(int x, int y, int z) {
		this.breakBlock(x, y, z);
	}

	public boolean canPlace(int block) {
		return true;
	}

	public void breakBlock(int x, int y, int z) {
		Block block = OpenClassic.getClient().getLevel().getBlockAt(x, y, z);
		if(block == null) return;
		BlockType selected = Blocks.fromId(OpenClassic.getClient().getPlayer().getInventoryContents()[OpenClassic.getClient().getPlayer().getSelectedSlot()]);
		if(!OpenClassic.getClient().isInMultiplayer() && EventManager.callEvent(new BlockBreakEvent(block, OpenClassic.getClient().getPlayer(), selected)).isCancelled()) {
			return;
		}

		BlockType old = block.getType();
		boolean success = OpenClassic.getClient().getLevel().setBlockAt(x, y, z, VanillaBlock.AIR);
		if(old != null && success) {
			if(OpenClassic.getClient().isInMultiplayer()) {
				((ClientPlayer) OpenClassic.getClient().getPlayer()).getSession().send(new PlayerSetBlockMessage((short) x, (short) y, (short) z, false, selected.getId()));
			} else if(old.getPhysics() != null) {
				old.getPhysics().onBreak(block);
			}

			if(old.getStepSound() != StepSound.NONE) {
				if(old.getStepSound() == StepSound.SAND) {
					OpenClassic.getClient().getAudioManager().playSound(StepSound.GRAVEL.getSound(), x, y, z, (StepSound.GRAVEL.getVolume() + 1.0F) / 2.0F, StepSound.GRAVEL.getPitch() * 0.8F);
				} else {
					OpenClassic.getClient().getAudioManager().playSound(old.getStepSound().getSound(), x, y, z, (old.getStepSound().getVolume() + 1.0F) / 2.0F, old.getStepSound().getPitch() * 0.8F);
				}
			}

			BlockUtils.spawnDestructionParticles(old, (ClientLevel) OpenClassic.getClient().getLevel(), x, y, z);
		}
	}

	public void hitBlock(int x, int y, int z, BlockFace face) {
	}

	public void resetHits() {
	}

	public void applyBlockCracks(float time) {
	}

	public float getReachDistance() {
		return 5;
	}

	public boolean useItem(Player player, int type) {
		return false;
	}

	public void preparePlayer(Player player) {
	}

	public void spawnMobs() {
	}

	public void prepareLevel(ClientLevel level) {
	}

	public void apply(Player player) {
	}
	
}
