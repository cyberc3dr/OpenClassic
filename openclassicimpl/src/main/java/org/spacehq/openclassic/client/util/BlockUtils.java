package org.spacehq.openclassic.client.util;

import java.util.Random;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.block.model.Model;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.level.particle.TerrainParticle;

import com.mojang.minecraft.entity.object.Item;

public class BlockUtils {

	private static Random rand = new Random();

	public static boolean canExplode(BlockType type) {
		return type != VanillaBlock.AIR && type != VanillaBlock.STONE && type != VanillaBlock.COBBLESTONE && type != VanillaBlock.BEDROCK && type != VanillaBlock.COAL_ORE && type != VanillaBlock.IRON_ORE && type != VanillaBlock.GOLD_ORE && type != VanillaBlock.GOLD_BLOCK && type != VanillaBlock.IRON_BLOCK && type != VanillaBlock.SLAB && type != VanillaBlock.DOUBLE_SLAB && type != VanillaBlock.BRICK_BLOCK && type != VanillaBlock.MOSSY_COBBLESTONE && type != VanillaBlock.OBSIDIAN;
	}

	public static int getHardness(BlockType type) {
		if(type == VanillaBlock.SAPLING || type == VanillaBlock.DANDELION || type == VanillaBlock.ROSE || type == VanillaBlock.BROWN_MUSHROOM || type == VanillaBlock.RED_MUSHROOM || type == VanillaBlock.TNT) {
			return 0;
		} else if(type == VanillaBlock.RED_CLOTH || type == VanillaBlock.ORANGE_CLOTH || type == VanillaBlock.YELLOW_CLOTH || type == VanillaBlock.LIME_CLOTH || type == VanillaBlock.GREEN_CLOTH || type == VanillaBlock.AQUA_GREEN_CLOTH || type == VanillaBlock.CYAN_CLOTH || type == VanillaBlock.BLUE_CLOTH || type == VanillaBlock.PURPLE_CLOTH || type == VanillaBlock.INDIGO_CLOTH || type == VanillaBlock.VIOLET_CLOTH || type == VanillaBlock.MAGENTA_CLOTH || type == VanillaBlock.PINK_CLOTH || type == VanillaBlock.BLACK_CLOTH || type == VanillaBlock.GRAY_CLOTH || type == VanillaBlock.WHITE_CLOTH) {
			return 16;
		} else if(type == VanillaBlock.GRASS || type == VanillaBlock.DIRT || type == VanillaBlock.GRAVEL || type == VanillaBlock.SPONGE || type == VanillaBlock.SAND) {
			return 12;
		} else if(type == VanillaBlock.STONE || type == VanillaBlock.MOSSY_COBBLESTONE || type == VanillaBlock.SLAB || type == VanillaBlock.DOUBLE_SLAB) {
			return 20;
		} else if(type == VanillaBlock.COBBLESTONE || type == VanillaBlock.WOOD || type == VanillaBlock.BOOKSHELF) {
			return 30;
		} else if(type == VanillaBlock.BEDROCK) {
			return 19980;
		} else if(type == VanillaBlock.WATER || type == VanillaBlock.STATIONARY_WATER || type == VanillaBlock.LAVA || type == VanillaBlock.STATIONARY_LAVA) {
			return 2000;
		} else if(type == VanillaBlock.GOLD_ORE || type == VanillaBlock.IRON_ORE || type == VanillaBlock.COAL_ORE || type == VanillaBlock.GOLD_BLOCK) {
			return 60;
		} else if(type == VanillaBlock.LOG) {
			return 50;
		} else if(type == VanillaBlock.LEAVES) {
			return 4;
		} else if(type == VanillaBlock.GLASS) {
			return 6;
		} else if(type == VanillaBlock.IRON_BLOCK) {
			return 100;
		} else if(type == VanillaBlock.OBSIDIAN) {
			return 200;
		}

		return 0;
	}

	public static int getDrop(BlockType type) {
		if(type == VanillaBlock.STONE) {
			return VanillaBlock.COBBLESTONE.getId();
		} else if(type == VanillaBlock.GRASS) {
			return VanillaBlock.DIRT.getId();
		} else if(type == VanillaBlock.GOLD_ORE) {
			return VanillaBlock.GOLD_BLOCK.getId();
		} else if(type == VanillaBlock.IRON_ORE) {
			return VanillaBlock.IRON_BLOCK.getId();
		} else if(type == VanillaBlock.COAL_ORE || type == VanillaBlock.DOUBLE_SLAB) {
			return VanillaBlock.SLAB.getId();
		} else if(type == VanillaBlock.LOG) {
			return VanillaBlock.WOOD.getId();
		} else if(type == VanillaBlock.LEAVES) {
			return VanillaBlock.SAPLING.getId();
		}

		return type.getId();
	}

	public static int getDropCount(BlockType type) {
		if(type == VanillaBlock.AIR || type == VanillaBlock.WATER || type == VanillaBlock.STATIONARY_WATER || type == VanillaBlock.LAVA || type == VanillaBlock.STATIONARY_LAVA || type == VanillaBlock.TNT || type == VanillaBlock.BOOKSHELF) {
			return 0;
		} else if(type == VanillaBlock.DOUBLE_SLAB) {
			return 2;
		} else if(type == VanillaBlock.GOLD_ORE || type == VanillaBlock.COAL_ORE || type == VanillaBlock.IRON_ORE) {
			return rand.nextInt(3) + 1;
		} else if(type == VanillaBlock.LOG) {
			return rand.nextInt(3) + 3;
		} else if(type == VanillaBlock.LEAVES) {
			return rand.nextInt(10) == 0 ? 1 : 0;
		}

		return 1;
	}

	public static void dropItems(BlockType block, ClientLevel level, int x, int y, int z) {
		dropItems(block, level, x, y, z, 1);
	}

	public static void dropItems(BlockType block, ClientLevel level, int x, int y, int z, float chance) {
		if(OpenClassic.getClient().isInSurvival()) {
			int dropCount = getDropCount(block);
			for(int count = 0; count < dropCount; count++) {
				if(rand.nextFloat() <= chance) {
					float xOffset = rand.nextFloat() * 0.7f + 0.15f;
					float yOffset = rand.nextFloat() * 0.7f + 0.15f;
					float zOffset = rand.nextFloat() * 0.7f + 0.15f;
					level.addEntity(new Item(level, x + xOffset, y + yOffset, z + zOffset, getDrop(block)));
				}
			}
		}
	}
	
	public static boolean preventsRendering(ClientLevel level, float x, float y, float z, float distance) {
		return preventsRendering(level, x - distance, y - distance, z - distance) || preventsRendering(level, x - distance, y - distance, z + distance) || preventsRendering(level, x - distance, y + distance, z - distance) || preventsRendering(level, x - distance, y + distance, z + distance) || preventsRendering(level, x + distance, y - distance, z - distance) || preventsRendering(level, x + distance, y - distance, z + distance) || preventsRendering(level, x + distance, y + distance, z - distance) || preventsRendering(level, x + distance, y + distance, z + distance);
	}

	public static boolean preventsRendering(ClientLevel level, float x, float y, float z) {
		BlockType type = level.getBlockTypeAt((int) x, (int) y, (int) z);
		return type != null && type.getPreventsRendering();
	}
	
	public static void spawnDestructionParticles(BlockType block, ClientLevel level, int x, int y, int z) {
		for(int xMod = 0; xMod < 4; xMod++) {
			for(int yMod = 0; yMod < 4; yMod++) {
				for(int zMod = 0; zMod < 4; zMod++) {
					float particleX = x + (xMod + 0.5f) / 4;
					float particleY = y + (yMod + 0.5f) / 4;
					float particleZ = z + (zMod + 0.5f) / 4;
					level.getParticleManager().spawnParticle(new TerrainParticle(new Position(level, particleX, particleY, particleZ), particleX - x - 0.5F, particleY - y - 0.5F, particleZ - z - 0.5F, block));
				}
			}
		}
	}

	public static void spawnBlockParticles(ClientLevel level, int x, int y, int z, BlockFace face) {
		Model model = level.getBlockTypeAt(x, y, z).getModel();
		if(model.getSelectionBox(x, y, z) != null) {
			float particleX = x + rand.nextFloat() * (model.getSelectionBox(x, y, z).getX2() - model.getSelectionBox(x, y, z).getX1() - 0.1F * 2.0F) + 0.1F + model.getSelectionBox(x, y, z).getX1();
			float particleY = y + rand.nextFloat() * (model.getSelectionBox(x, y, z).getY2() - model.getSelectionBox(x, y, z).getY1() - 0.1F * 2.0F) + 0.1F + model.getSelectionBox(x, y, z).getY1();
			float particleZ = z + rand.nextFloat() * (model.getSelectionBox(x, y, z).getZ2() - model.getSelectionBox(x, y, z).getZ1() - 0.1F * 2.0F) + 0.1F + model.getSelectionBox(x, y, z).getZ1();
			if(face == BlockFace.DOWN) {
				particleY = y + model.getSelectionBox(x, y, z).getY1() - 0.1f;
			}
	
			if(face == BlockFace.UP) {
				particleY = y + model.getSelectionBox(x, y, z).getY2() + 0.1f;
			}
	
			if(face == BlockFace.WEST) {
				particleZ = z + model.getSelectionBox(x, y, z).getZ1() - 0.1f;
			}
	
			if(face == BlockFace.EAST) {
				particleZ = z + model.getSelectionBox(x, y, z).getZ2() + 0.1f;
			}
	
			if(face == BlockFace.SOUTH) {
				particleX = x + model.getSelectionBox(x, y, z).getX1() - 0.1f;
			}
	
			if(face == BlockFace.NORTH) {
				particleX = x + model.getSelectionBox(x, y, z).getX2() + 0.1f;
			}
	
			level.getParticleManager().spawnParticle((new TerrainParticle(new Position(level, particleX, particleY, particleZ), 0, 0, 0, level.getBlockTypeAt(x, y, z))).setPower(0.2f).scale(0.6f));
		}
	}

}
