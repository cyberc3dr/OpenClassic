package org.spacehq.openclassic.client.level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.object.PrimedTnt;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.level.LevelInfo;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.particle.ParticleManager;
import org.spacehq.openclassic.client.render.level.LevelRenderer;
import org.spacehq.openclassic.client.util.BlockUtils;
import org.spacehq.openclassic.game.level.ClassicLevel;

public class ClientLevel extends ClassicLevel {

	private BlockMap blockMap;
	private ParticleManager particleManager = new ParticleManager();
	private LevelRenderer renderer = new LevelRenderer(this);

	public ClientLevel() {
		super();
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.night").getValue()) {
			this.setSkyColor(0);
			this.setFogColor(new Color(30, 30, 30, 70).getRGB());
			this.setCloudColor(new Color(30, 30, 30, 70).getRGB());
		}
	}
	
	public ClientLevel(LevelInfo info) {
		super(info);
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.night").getValue()) {
			this.setSkyColor(0);
			this.setFogColor(new Color(30, 30, 30, 70).getRGB());
			this.setCloudColor(new Color(30, 30, 30, 70).getRGB());
		}
	}
	
	@Override
	public void tick() {
		if(!OpenClassic.getClient().isInMultiplayer()) {
			super.tick();
		}
		
		this.renderer.ticks++;
		this.blockMap.tickAll();
		this.particleManager.tick();
	}

	@Override
	public void setData(int width, int height, int depth, byte blocks[]) {
		super.setData(width, height, depth, blocks);
		this.blockMap = new BlockMap(width, height, depth);
		this.refreshRenderer();
	}

	@Override
	protected void highestUpdated(int x, int z, int lower, int highest) {
		this.renderer.queueChunks(x - 1, lower - 1, z - 1, x + 1, highest + 1, z + 1);
	}

	@Override
	public List<Player> getPlayers() {
		List<Player> result = new ArrayList<Player>();
		for(com.mojang.minecraft.entity.player.Player p : this.findAll(com.mojang.minecraft.entity.player.Player.class)) {
			result.add(p.openclassic);
		}

		return result;
	}

	@Override
	public boolean setBlockIdAt(int x, int y, int z, byte type, boolean physics) {
		boolean ret = super.setBlockIdAt(x, y, z, type, physics && !OpenClassic.getClient().isInMultiplayer());
		this.renderer.queueChunks(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
		return ret;
	}

	@Override
	public float getBrightness(int x, int y, int z) {
		float brightness = super.getBrightness(x, y, z);
		BlockType block = this.getBlockTypeAt(x, y, z);
		if(block != null && block.getBrightness() <= 0) {
			brightness -= OpenClassic.getClient().getSettings().getBooleanSetting("options.night").getValue() ? 0.4f : 0;
		}

		return brightness;
	}

	// Client methods
	public ParticleManager getParticleManager() {
		return this.particleManager;
	}
	
	public LevelRenderer getRenderer() {
		return this.renderer;
	}
	
	public void refreshRenderer() {
		this.renderer.refresh();
	}
	
	public void render(float delta, int anaglyphPass) {
		this.renderer.render(delta, anaglyphPass);
	}
	
	public void renderEntities(float delta, int anaglyphPass) {
		this.blockMap.render(delta);
		this.particleManager.render(delta, OpenClassic.getClient().getPlayer());
	}
	
	public ArrayList<BoundingBox> getBoxes(BoundingBox bb) {
		ArrayList<BoundingBox> ret = new ArrayList<BoundingBox>();
		int x1 = (int) bb.getX1();
		int x2 = (int) bb.getX2() + 1;
		int y1 = (int) bb.getY1();
		int y2 = (int) bb.getY2() + 1;
		int z1 = (int) bb.getZ1();
		int z2 = (int) bb.getZ2() + 1;
		if(bb.getX1() < 0) {
			x1--;
		}

		if(bb.getY1() < 0) {
			y1--;
		}

		if(bb.getZ1() < 0) {
			z1--;
		}

		for(int x = x1; x < x2; x++) {
			for(int y = y1; y < y2; y++) {
				for(int z = z1; z < z2; z++) {
					if(x >= 0 && y >= 0 && z >= 0 && x < this.getWidth() && y < this.getHeight() && z < this.getDepth()) {
						BlockType type = this.getBlockTypeAt(x, y, z);
						if(type != null) {
							BoundingBox bb2 = type.getModel(this, x, y, z).getCollisionBox(x, y, z);
							if(type != null && bb2 != null && bb.intersectsInner(bb2)) {
								ret.add(bb2);
							}
						}
					} else if(x < 0 || y < 0 || z < 0 || x >= this.getWidth() || z >= this.getDepth()) {
						BoundingBox bb2 = VanillaBlock.BEDROCK.getModel(this, x, y, z).getCollisionBox(x, y, z);
						if(bb2 != null && bb.intersectsInner(bb2)) {
							ret.add(bb2);
						}
					}
				}
			}
		}

		return ret;
	}

	public BlockType getLiquid(BoundingBox bb) {
		for(BlockType block : this.getBlocksIn(bb)) {
			if(block.isLiquid()) {
				return block;
			}
		}

		return null;
	}

	public List<BlockType> getBlocksIn(BoundingBox bb) {
		int x1 = (int) bb.getX1();
		int x2 = (int) bb.getX2() + 1;
		int y1 = (int) bb.getY1();
		int y2 = (int) bb.getY2() + 1;
		int z1 = (int) bb.getZ1();
		int z2 = (int) bb.getZ2() + 1;
		if(x1 < 0) {
			x1 = 0;
		}

		if(y1 < 0) {
			y1 = 0;
		}

		if(z1 < 0) {
			z1 = 0;
		}

		if(x2 > this.getWidth()) {
			x2 = this.getWidth();
		}

		if(y2 > this.getHeight()) {
			y2 = this.getHeight();
		}

		if(z2 > this.getDepth()) {
			z2 = this.getDepth();
		}

		List<BlockType> ret = new ArrayList<BlockType>();
		for(int x = x1; x < x2; x++) {
			for(int y = y1; y < y2; y++) {
				for(int z = z1; z < z2; z++) {
					BlockType type = this.getBlockTypeAt(x, y, z);
					if(type != null) {
						ret.add(type);
					}
				}
			}
		}

		return ret;
	}

	public int getEntityCount(Class<? extends Entity> clazz) {
		int count = 0;
		for(int index = 0; index < this.blockMap.all.size(); index++) {
			Entity entity = this.blockMap.all.get(index);
			if(clazz.isAssignableFrom(entity.getClass())) {
				count++;
			}
		}

		return count;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> List<T> findAll(Class<T> clazz) {
		List<T> ret = new ArrayList<T>();
		for(Entity entity : this.blockMap.all) {
			if(clazz.isAssignableFrom(entity.getClass())) {
				ret.add((T) entity);
			}
		}

		return ret;
	}
	
	public List<Entity> getEntities(Entity exclude, BoundingBox bb) {
		return this.blockMap.getEntities(exclude, bb);
	}

	public boolean isFree(BoundingBox bb) {
		return this.blockMap.getEntities(null, bb).size() == 0;
	}

	public List<Entity> findEntities(Entity entity, BoundingBox bb) {
		return this.blockMap.getEntities(entity, bb);
	}

	public void addEntity(Entity entity) {
		this.blockMap.insert(entity);
		entity.setLevel(this);
	}

	public void removeEntity(Entity entity) {
		this.blockMap.remove(entity);
	}

	public void removeSurvivalEntities() {
		this.blockMap.removeSurvivalEntities();
	}

	public void explode(Entity entity, float x, float y, float z, float power) {
		if(OpenClassic.getClient().isInMultiplayer()) {
			return;
		}

		int minx = (int) (x - power - 1);
		int maxx = (int) (x + power + 1);
		int miny = (int) (y - power - 1);
		int maxy = (int) (y + power + 1);
		int minz = (int) (z - power - 1);
		int maxz = (int) (z + power + 1);
		OpenClassic.getGame().getAudioManager().playSound("random.explode", x, y, z, 1, 1);
		for(int bx = minx; bx < maxx; bx++) {
			for(int by = maxy - 1; by >= miny; by--) {
				for(int bz = minz; bz < maxz; bz++) {
					float dx = bx + 0.5f - x;
					float dy = by + 0.5f - y;
					float dz = bz + 0.5f - z;
					BlockType block = this.getBlockTypeAt(bx, by, bz);
					if(x >= 0 && y >= 0 && z >= 0 && x < this.getWidth() && y < this.getHeight() && z < this.getDepth() && dx * dx + dy * dy + dz * dz < power * power && block != null && BlockUtils.canExplode(block)) {
						this.setBlockAt(bx, by, bz, VanillaBlock.AIR);
						BlockUtils.dropItems(block, this, bx, by, bz, 0.3f);
						if(block == VanillaBlock.TNT && OpenClassic.getClient().isInSurvival()) {
							PrimedTnt tnt = new PrimedTnt(this, bx + 0.5f, by + 0.5f, bz + 0.5f);
							tnt.life = this.getRandom().nextInt(tnt.life / 4) + tnt.life / 8;
							this.addEntity(tnt);
						}
					}
				}
			}
		}

		List<Entity> entities = this.blockMap.getEntities(entity, minx, miny, minz, maxx, maxy, maxz);
		for(int index = 0; index < entities.size(); index++) {
			Entity e = entities.get(index);
			float pow = e.distanceTo(x, y, z) / power;
			if(pow <= 1) {
				e.hurt(entity, (int) ((1 - pow) * 15 + 1));
			}
		}
	}
	
	public com.mojang.minecraft.entity.player.Player getPlayer(byte id) {
		for(com.mojang.minecraft.entity.player.Player player : this.findAll(com.mojang.minecraft.entity.player.Player.class)) {
			if(player.getPlayerId() == id) {
				return player;
			}
		}
		
		return null;
	}

}
