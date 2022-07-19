package org.spacehq.openclassic.client.render;

import com.mojang.minecraft.entity.mob.Sheep;
import com.mojang.minecraft.entity.object.Arrow;

import sun.applet.Main;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.TextureFactory;

public class Textures {

	public static final Texture SMOKE_PARTICLE = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/level/smokeparticle.png"), 8, 8, 1);
	public static final Texture SMOKE_PARTICLE_1 = SMOKE_PARTICLE.getSubTexture(0, 8, 8);
	public static final Texture SMOKE_PARTICLE_2 = SMOKE_PARTICLE.getSubTexture(0, 8, 8);
	public static final Texture SMOKE_PARTICLE_3 = SMOKE_PARTICLE.getSubTexture(0, 8, 8);
	public static final Texture SMOKE_PARTICLE_4 = SMOKE_PARTICLE.getSubTexture(0, 8, 8);
	public static final Texture SMOKE_PARTICLE_5 = SMOKE_PARTICLE.getSubTexture(0, 8, 8);
	public static final Texture SMOKE_PARTICLE_6 = SMOKE_PARTICLE.getSubTexture(0, 8, 8);
	public static final Texture SMOKE_PARTICLE_7 = SMOKE_PARTICLE.getSubTexture(0, 8, 8);
	public static final Texture SMOKE_PARTICLE_8 = SMOKE_PARTICLE.getSubTexture(0, 8, 8);
	
	public static final Texture RAIN_PARTICLE = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/level/rainparticle.png"));
	public static final Texture RAIN = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/level/rain.png"));
	public static final Texture WATER = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/level/water.png"));
	public static final Texture CLOUDS = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/level/clouds.png"));
	public static final Texture ROCK = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/level/rock.png"));
	
	public static final Texture ARROW = TextureFactory.getFactory().newTexture(Arrow.class.getResource("/textures/entity/arrow.png"));
	public static final Texture ARMOR = TextureFactory.getFactory().newTexture(Arrow.class.getResource("/textures/entity/armor/plate.png"));
	public static final Texture DEFAULT_SKIN = TextureFactory.getFactory().newTexture(Arrow.class.getResource("/textures/entity/char.png"));
	public static final Texture CREEPER = TextureFactory.getFactory().newTexture(Sheep.class.getResource("/textures/entity/mob/creeper.png"));
	public static final Texture SHEEP = TextureFactory.getFactory().newTexture(Sheep.class.getResource("/textures/entity/mob/sheep.png"));
	public static final Texture FUR = TextureFactory.getFactory().newTexture(Sheep.class.getResource("/textures/entity/mob/sheep_fur.png"));
	public static final Texture ZOMBIE = TextureFactory.getFactory().newTexture(Sheep.class.getResource("/textures/entity/mob/zombie.png"));
	public static final Texture SPIDER = TextureFactory.getFactory().newTexture(Sheep.class.getResource("/textures/entity/mob/spider.png"));
	public static final Texture SKELETON = TextureFactory.getFactory().newTexture(Sheep.class.getResource("/textures/entity/mob/skeleton.png"));
	public static final Texture PIG = TextureFactory.getFactory().newTexture(Sheep.class.getResource("/textures/entity/mob/pig.png"));

	public static final Texture FONT = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/gui/font.png"));
	public static final Texture LOGO = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/gui/logo.png"));
	public static final Texture GUI = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/gui/gui.png"));
	public static final Texture ICONS = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/gui/icons.png"));
	public static final Texture DIRT = TextureFactory.getFactory().newTexture(Main.class.getResource("/textures/gui/dirt.png"));
	public static final Texture CROSSHAIR = ICONS.getSubTexture(0, 0, 32, 32);
    public static final Texture EMPTY_HEART = ICONS.getSubTexture(32, 0, 18, 18);
    public static final Texture EMPTY_HEART_FLASH =ICONS.getSubTexture(50, 0, 18, 18);
    public static final Texture FULL_HEART = ICONS.getSubTexture(104, 0, 18, 18);
    public static final Texture FULL_HEART_FLASH = ICONS.getSubTexture(140, 0, 18, 18);
    public static final Texture HALF_HEART = ICONS.getSubTexture(122, 0, 18, 18);
    public static final Texture HALF_HEART_FLASH = ICONS.getSubTexture(158, 0, 18, 18);
    public static final Texture BUBBLE = ICONS.getSubTexture(32, 36, 18, 18);
    public static final Texture POPPING_BUBBLE = ICONS.getSubTexture(50, 36, 18, 18);
	public static final Texture QUICK_BAR = GUI.getSubTexture(0, 0, 364, 44);
	public static final Texture SELECTION = GUI.getSubTexture(0, 44, 48, 44);
	public static final Texture BUTTON = GUI.getSubTexture(0, 132, 400, 40);
	public static final Texture BUTTON_HOVER = GUI.getSubTexture(0, 172, 400, 40);
	public static final Texture BUTTON_INACTIVE = GUI.getSubTexture(0, 92, 400, 40);
	
}
