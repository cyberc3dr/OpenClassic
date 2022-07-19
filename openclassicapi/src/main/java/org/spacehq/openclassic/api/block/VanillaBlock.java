package org.spacehq.openclassic.api.block;

import java.lang.reflect.Field;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.model.CuboidModel;
import org.spacehq.openclassic.api.block.model.Model;
import org.spacehq.openclassic.api.block.model.PlantModel;
import org.spacehq.openclassic.api.block.model.LiquidModel;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.TextureFactory;
import org.spacehq.openclassic.api.block.physics.FallingBlockPhysics;
import org.spacehq.openclassic.api.block.physics.FlowerPhysics;
import org.spacehq.openclassic.api.block.physics.HalfBlockPhysics;
import org.spacehq.openclassic.api.block.physics.LiquidPhysics;
import org.spacehq.openclassic.api.block.physics.MushroomPhysics;
import org.spacehq.openclassic.api.block.physics.TreeGrowthPhysics;
import org.spacehq.openclassic.api.block.physics.SpongePhysics;
import org.spacehq.openclassic.api.block.physics.SpreadPhysics;

/**
 * Represents a vanilla block type.
 */
public class VanillaBlock {

	private static final Texture WATER_TEX = TextureFactory.getFactory().newTexture(VanillaBlock.class.getResource("/textures/level/water_anim.png"), 16, 16, 2);
	private static final Texture LAVA_TEX = TextureFactory.getFactory().newTexture(VanillaBlock.class.getResource("/textures/level/lava_anim.png"), 16, 16, 2);
	
	public static final BlockType AIR = new BlockType(0, StepSound.NONE, new Model()).setPreventsRendering(false).setOpaque(false).setSelectable(false).setPlaceIn(true);
	public static final BlockType STONE = new BlockType(1, StepSound.STONE, 1);
	public static final BlockType GRASS = new BlockType(2, StepSound.GRASS, new int[] { 2, 0, 3, 3, 3, 3 }).setSelectable(false);
	public static final BlockType DIRT = new BlockType(3, StepSound.GRAVEL, 2);
	public static final BlockType COBBLESTONE = new BlockType(4, StepSound.STONE, 16);
	public static final BlockType WOOD = new BlockType(5, StepSound.WOOD, 4);
	public static final BlockType SAPLING = new BlockType(6, StepSound.GRASS, new PlantModel(BlockType.TERRAIN_TEXTURE, 15, 16, 16)).setPreventsRendering(false).setOpaque(false);
	public static final BlockType BEDROCK = new BlockType(7, StepSound.STONE, 17).setSelectable(false).setUnbreakable(false);
	public static final BlockType WATER = new BlockType(8, StepSound.NONE, new LiquidModel(WATER_TEX, 0, false, 16, 16)).addOutwardModel(new LiquidModel(WATER_TEX, 0, true, 16, 16), BlockFace.UP).setTickDelay(5).setLiquid(true).setPreventsRendering(false).setSelectable(false).setPlaceIn(true).setFogDensity(0.1f).setFogColor(5, 5, 51).setSpeedModifier(0.8f).setLiquidName("water");
	public static final BlockType STATIONARY_WATER = new BlockType(9, StepSound.NONE, new LiquidModel(WATER_TEX, 0, false, 16, 16)).addOutwardModel(new LiquidModel(WATER_TEX, 0, true, 16, 16), BlockFace.UP).setLiquid(true).setPreventsRendering(false).setSelectable(false).setPlaceIn(true).setFogDensity(0.1f).setFogColor(5, 5, 51).setSpeedModifier(0.8f).setLiquidName("water");
	public static final BlockType LAVA = new BlockType(10, StepSound.NONE, new LiquidModel(LAVA_TEX, 0, false, 16, 16)).addOutwardModel(new LiquidModel(LAVA_TEX, 0, true, 16, 16), BlockFace.UP).setTickDelay(20).setLiquid(true).setPreventsRendering(false).setSelectable(false).setPlaceIn(true).setBrightness(1).setFogDensity(2).setFogColor(153, 26, 0).setSpeedModifier(0.5f).setLiquidName("lava");
	public static final BlockType STATIONARY_LAVA = new BlockType(11, StepSound.NONE, new LiquidModel(LAVA_TEX, 0, false, 16, 16)).addOutwardModel(new LiquidModel(LAVA_TEX, 0, true, 16, 16), BlockFace.UP).setLiquid(true).setPreventsRendering(false).setSelectable(false).setPlaceIn(true).setBrightness(1).setFogDensity(2).setFogColor(153, 26, 0).setSpeedModifier(0.5f).setLiquidName("lava");
	public static final BlockType SAND = new BlockType(12, StepSound.SAND, 18).setTickDelay(1);
	public static final BlockType GRAVEL = new BlockType(13, StepSound.GRAVEL, 19).setTickDelay(1);
	public static final BlockType GOLD_ORE = new BlockType(14, StepSound.STONE, 32);
	public static final BlockType IRON_ORE = new BlockType(15, StepSound.STONE, 33);
	public static final BlockType COAL_ORE = new BlockType(16, StepSound.STONE, 34);
	public static final BlockType LOG = new BlockType(17, StepSound.WOOD, new int[] { 21, 21, 20, 20, 20, 20 });
	public static final BlockType LEAVES = new BlockType(18, StepSound.GRASS, 22).setOpaque(false).setPreventsRendering(false);
	public static final BlockType SPONGE = new BlockType(19, StepSound.GRASS, 48);
	public static final BlockType GLASS = new BlockType(20, StepSound.METAL, 49).setOpaque(false).setPreventsRendering(false).setPreventsOwnRendering(true);
	public static final BlockType RED_CLOTH = new BlockType(21, StepSound.CLOTH, 64);
	public static final BlockType ORANGE_CLOTH = new BlockType(22, StepSound.CLOTH, 65);
	public static final BlockType YELLOW_CLOTH = new BlockType(23, StepSound.CLOTH, 66);
	public static final BlockType LIME_CLOTH = new BlockType(24, StepSound.CLOTH, 67);
	public static final BlockType GREEN_CLOTH = new BlockType(25, StepSound.CLOTH, 68);
	public static final BlockType AQUA_GREEN_CLOTH = new BlockType(26, StepSound.CLOTH, 69);
	public static final BlockType CYAN_CLOTH = new BlockType(27, StepSound.CLOTH, 70);
	public static final BlockType BLUE_CLOTH = new BlockType(28, StepSound.CLOTH, 71);
	public static final BlockType PURPLE_CLOTH = new BlockType(29, StepSound.CLOTH, 72);
	public static final BlockType INDIGO_CLOTH = new BlockType(30, StepSound.CLOTH, 73);
	public static final BlockType VIOLET_CLOTH = new BlockType(31, StepSound.CLOTH, 74);
	public static final BlockType MAGENTA_CLOTH = new BlockType(32, StepSound.CLOTH, 75);
	public static final BlockType PINK_CLOTH = new BlockType(33, StepSound.CLOTH, 76);
	public static final BlockType BLACK_CLOTH = new BlockType(34, StepSound.CLOTH, 77);
	public static final BlockType GRAY_CLOTH = new BlockType(35, StepSound.CLOTH, 78);
	public static final BlockType WHITE_CLOTH = new BlockType(36, StepSound.CLOTH, 79);
	public static final BlockType DANDELION = new BlockType(37, StepSound.GRASS, new PlantModel(BlockType.TERRAIN_TEXTURE, 13, 16, 16)).setOpaque(false).setPreventsRendering(false);
	public static final BlockType ROSE = new BlockType(38, StepSound.GRASS, new PlantModel(BlockType.TERRAIN_TEXTURE, 12, 16, 16)).setOpaque(false).setPreventsRendering(false);
	public static final BlockType BROWN_MUSHROOM = new BlockType(39, StepSound.GRASS, new PlantModel(BlockType.TERRAIN_TEXTURE, 29, 16, 16)).setOpaque(false).setPreventsRendering(false);
	public static final BlockType RED_MUSHROOM = new BlockType(40, StepSound.GRASS, new PlantModel(BlockType.TERRAIN_TEXTURE, 28, 16, 16)).setOpaque(false).setPreventsRendering(false);
	public static final BlockType GOLD_BLOCK = new BlockType(41, StepSound.METAL, new int[] { 56, 24, 40, 40, 40, 40 });
	public static final BlockType IRON_BLOCK = new BlockType(42, StepSound.METAL, new int[] { 55, 23, 39, 39, 39, 39 });
	public static final BlockType DOUBLE_SLAB = new BlockType(43, StepSound.STONE, new int[] { 6, 6, 5, 5, 5, 5 }).setSelectable(false);
	public static final BlockType SLAB = new BlockType(44, StepSound.STONE, new CuboidModel(BlockType.TERRAIN_TEXTURE, new int[] { 6, 6, 5, 5, 5, 5 }, 0, 0, 0, 1, 0.5F, 1, 16, 16)).setPreventsRendering(false);
	public static final BlockType BRICK_BLOCK = new BlockType(45, StepSound.STONE, 7);
	public static final BlockType TNT = new BlockType(46, StepSound.GRASS, new int[] { 10, 9, 8, 8, 8, 8 });
	public static final BlockType BOOKSHELF = new BlockType(47, StepSound.WOOD, new int[] { 4, 4, 35, 35, 35, 35 });
	public static final BlockType MOSSY_COBBLESTONE = new BlockType(48, StepSound.STONE, 36);
	public static final BlockType OBSIDIAN = new BlockType(49, StepSound.STONE, 37);
	
	static {
		VanillaBlock.SAND.setPhysics(new FallingBlockPhysics(VanillaBlock.SAND));
		VanillaBlock.GRAVEL.setPhysics(new FallingBlockPhysics(VanillaBlock.GRAVEL));
		VanillaBlock.ROSE.setPhysics(new FlowerPhysics());
		VanillaBlock.DANDELION.setPhysics(new FlowerPhysics());
		VanillaBlock.GRASS.setPhysics(new SpreadPhysics(VanillaBlock.GRASS, VanillaBlock.DIRT));
		VanillaBlock.WATER.setPhysics(new LiquidPhysics(VanillaBlock.WATER, true, true));
		VanillaBlock.LAVA.setPhysics(new LiquidPhysics(VanillaBlock.LAVA, false, false));
		VanillaBlock.RED_MUSHROOM.setPhysics(new MushroomPhysics());
		VanillaBlock.BROWN_MUSHROOM.setPhysics(new MushroomPhysics());
		VanillaBlock.SAPLING.setPhysics(new TreeGrowthPhysics(VanillaBlock.SAPLING));
		VanillaBlock.SPONGE.setPhysics(new SpongePhysics());
		VanillaBlock.SLAB.setPhysics(new HalfBlockPhysics(VanillaBlock.SLAB, VanillaBlock.DOUBLE_SLAB));
	}
	
	public static void registerAll() {
		try {
			for(Field f : VanillaBlock.class.getDeclaredFields()) {
				Object o = f.get(null);
				if(o instanceof BlockType) {
					Blocks.register((BlockType) o);
				}
			}
		} catch(Exception e) {
			OpenClassic.getLogger().severe("Failed to register vanilla blocks!");
			e.printStackTrace();
		}
	}
	
	public static void unregisterAll() {
		try {
			for(Field f : VanillaBlock.class.getDeclaredFields()) {
				Blocks.unregister(((BlockType) f.get(null)).getId()); 
			}
		} catch(Exception e) {
			OpenClassic.getLogger().severe("Failed to register vanilla blocks!");
			e.printStackTrace();
		}
	}
	
}
