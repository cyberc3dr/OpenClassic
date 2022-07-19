package org.spacehq.openclassic.client.gamemode;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.block.StepSound;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.client.util.BlockUtils;

import com.mojang.minecraft.entity.mob.Creeper;
import com.mojang.minecraft.entity.mob.Mob;
import com.mojang.minecraft.entity.mob.Pig;
import com.mojang.minecraft.entity.mob.Sheep;
import com.mojang.minecraft.entity.mob.Skeleton;
import com.mojang.minecraft.entity.mob.Spider;
import com.mojang.minecraft.entity.mob.Zombie;

public class SurvivalGameMode extends GameMode {

	@SuppressWarnings("unchecked")
	private static final Class<? extends Mob> ANIMALS[] = new Class[] { Pig.class, Sheep.class };
	@SuppressWarnings("unchecked")
	private static final Class<? extends Mob> ALL[] = new Class[] { Zombie.class, Skeleton.class, Creeper.class, Spider.class, Pig.class, Sheep.class };
	
	private int hitX;
	private int hitY;
	private int hitZ;
	private int hits;
	private int blockHardness;
	private int hitDelay;
	private int soundCounter;

	@Override
	public void preparePlayer(Player player) {
		player.getInventoryContents()[8] = VanillaBlock.TNT.getId();
		player.getInventoryAmounts()[8] = 10;
	}

	@Override
	public void breakBlock(int x, int y, int z) {
		BlockType block = OpenClassic.getClient().getLevel().getBlockTypeAt(x, y, z);
		BlockUtils.dropItems(block, (ClientLevel) OpenClassic.getClient().getLevel(), x, y, z);
		super.breakBlock(x, y, z);
	}

	@Override
	public boolean canPlace(int block) {
		return ((ClientPlayer) OpenClassic.getClient().getPlayer()).getHandle().inventory.removeSelected(block);
	}

	@Override
	public void hitBlock(int x, int y, int z) {
		BlockType block = OpenClassic.getClient().getLevel().getBlockTypeAt(x, y, z);
		if(block != null && BlockUtils.getHardness(block) == 0) {
			this.breakBlock(x, y, z);
		}
	}

	@Override
	public void resetHits() {
		this.hits = 0;
		this.soundCounter = 0;
		this.hitDelay = 0;
	}

	@Override
	public void hitBlock(int x, int y, int z, BlockFace face) {
		if(this.hitDelay > 0) {
			this.hitDelay--;
		} else if(x == this.hitX && y == this.hitY && z == this.hitZ) {
			BlockType type = OpenClassic.getClient().getLevel().getBlockTypeAt(x, y, z);
			if(type != null) {
				this.blockHardness = BlockUtils.getHardness(type);
				BlockUtils.spawnBlockParticles((ClientLevel) OpenClassic.getClient().getLevel(), x, y, z, face);
				this.hits++;
				if(this.soundCounter % 4 == 0) {
					StepSound sound = type.getStepSound();
					OpenClassic.getClient().getAudioManager().playSound(sound.getSound(), x, y, z, (sound.getVolume() + 1.0F) / 8F, sound.getPitch() * 0.5F);
				}

				this.soundCounter++;
				if(this.hits == this.blockHardness + 1) {
					this.breakBlock(x, y, z);
					this.hits = 0;
					this.hitDelay = 5;
				}
			}
		} else {
			this.hits = 0;
			this.soundCounter = 0;
			this.hitX = x;
			this.hitY = y;
			this.hitZ = z;
		}
	}
	
	@Override
	public void applyBlockCracks(float time) {
		if(OpenClassic.getClient().getLevel() != null) {
			if(this.hits <= 0) {
				((ClientLevel) OpenClassic.getClient().getLevel()).getRenderer().setCracks(0);
			} else {
				((ClientLevel) OpenClassic.getClient().getLevel()).getRenderer().setCracks((this.hits + time - 1) / this.blockHardness);
			}
		}
	}

	@Override
	public float getReachDistance() {
		return 4;
	}

	@Override
	public boolean useItem(Player player, int type) {
		BlockType block = Blocks.fromId(type);
		if(block == VanillaBlock.RED_MUSHROOM && ((ClientPlayer) OpenClassic.getClient().getPlayer()).getHandle().inventory.removeSelected(type)) {
			player.damage(3);
			return true;
		} else if(block == VanillaBlock.BROWN_MUSHROOM && ((ClientPlayer) OpenClassic.getClient().getPlayer()).getHandle().inventory.removeSelected(type)) {
			player.heal(5);
			return true;
		}

		return false;
	}

	@Override
	public void apply(Player player) {
		for(int slot = 0; slot < 9; slot++) {
			player.getInventoryContents()[slot] = -1;
			player.getInventoryAmounts()[slot] = 0;
		}

		player.getInventoryContents()[8] = VanillaBlock.TNT.getId();
		player.getInventoryAmounts()[8] = 10;
	}

	@Override
	public void spawnMobs() {
		ClientLevel level = (ClientLevel) OpenClassic.getClient().getLevel();
		int area = level.getWidth() * level.getHeight() * level.getDepth() / 64 / 64 / 64;
		if(level.getRandom().nextInt(100) < area && level.getEntityCount(Mob.class) < area * 20) {
			this.spawn(OpenClassic.getClient().getPlayer().getPosition(), area, false);
		}
	}

	@Override
	public void prepareLevel(ClientLevel level) {
		this.spawn(level.getSpawn(), level.getWidth() * level.getHeight() * level.getDepth() / 800, true);
	}
	
	private int spawn(Position pos, int max, boolean showProgress) {
		if(showProgress) {
			OpenClassic.getClient().getProgressBar().setText("Spawning...");
		}
		
		ClientLevel level = (ClientLevel) pos.getLevel();
		int count = 0;
		for(int mob = 0; mob < max; mob++) {
			if(showProgress) {
				OpenClassic.getClient().getProgressBar().setProgress(mob * 100 / (max - 1));
				OpenClassic.getClient().getProgressBar().render();
			}

			Class<? extends Mob> spawnable[] = ANIMALS;
			if(OpenClassic.getClient().getSettings().getIntSetting("options.survival").getValue() > 1) {
				spawnable = ALL;
			}

			int type = level.getRandom().nextInt(spawnable.length);
			int rx = level.getRandom().nextInt(level.getWidth());
			int ry = (int) (Math.min(level.getRandom().nextFloat(), level.getRandom().nextFloat()) * level.getHeight());
			int rz = level.getRandom().nextInt(level.getDepth());
			BlockType block = level.getBlockTypeAt(rx, ry, rz);
			if(!BlockUtils.preventsRendering(level, rx, ry, rz) && !(block != null && block.isLiquid()) && (!level.isLit(rx, ry, rz) || level.getRandom().nextInt(5) == 0)) {
				for(int pass = 0; pass < 3; pass++) {
					int bx = rx;
					int by = ry;
					int bz = rz;
					for(int run = 0; run < 3; run++) {
						bx += level.getRandom().nextInt(6) - level.getRandom().nextInt(6);
						by += level.getRandom().nextInt(1) - level.getRandom().nextInt(1);
						bz += level.getRandom().nextInt(6) - level.getRandom().nextInt(6);
						if(bx >= 0 && bz >= 1 && by >= 0 && by < level.getHeight() - 2 && bx < level.getWidth() && bz < level.getDepth() && BlockUtils.preventsRendering(level, bx, by - 1, bz) && !BlockUtils.preventsRendering(level, bx, by, bz) && !BlockUtils.preventsRendering(level, bx, by + 1, bz)) {
							float sx = bx + 0.5f;
							float sy = by + 1;
							float sz = bz + 0.5f;
							float dx = sx - pos.getX();
							float dy = sy - pos.getY();
							float dz = sz - pos.getZ();
							if(dx * dx + dy * dy + dz * dz < 256) {
								continue;
							}

							Mob spawning = null;
							try {
								spawning = spawnable[type].getConstructor(ClientLevel.class, float.class, float.class, float.class).newInstance(level, sx, sy, sz);
							} catch(Exception e) {
								OpenClassic.getLogger().severe("Failed to spawn mob \"" + spawnable[type].getSimpleName() + "\"");
								e.printStackTrace();
							}

							if(spawning != null && level.isFree(spawning.bb)) {
								count++;
								level.addEntity(spawning);
							}
						}
					}
				}
			}
		}

		return count;
	}
	
}
