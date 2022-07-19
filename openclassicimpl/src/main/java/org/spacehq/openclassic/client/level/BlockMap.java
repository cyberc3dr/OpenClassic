package org.spacehq.openclassic.client.level;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.client.render.Frustum;
import org.spacehq.openclassic.client.render.RenderHelper;

import com.mojang.minecraft.entity.Entity;

public class BlockMap {

	private int width;
	private int height;
	private int depth;
	public List<Entity>[] entityGrid;
	public List<Entity> all = new ArrayList<Entity>();

	@SuppressWarnings("unchecked")
	public BlockMap(int width, int height, int depth) {
		this.width = width / 16;
		this.height = height / 16;
		this.depth = depth / 16;
		if(this.width == 0) {
			this.width = 1;
		}

		if(this.height == 0) {
			this.height = 1;
		}

		if(this.depth == 0) {
			this.depth = 1;
		}

		this.entityGrid = new ArrayList[this.width * this.height * this.depth];
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				for(int z = 0; z < this.depth; z++) {
					this.entityGrid[(z * this.height + y) * this.width + x] = new ArrayList<Entity>();
				}
			}
		}
	}

	public void insert(Entity entity) {
		int sx = this.getSlotX(entity.pos.getBlockX());
		int sy = this.getSlotY(entity.pos.getBlockY());
		int sz = this.getSlotZ(entity.pos.getBlockZ());
		this.all.add(entity);
		this.entityGrid[(sz * this.height + sy) * this.width + sx].add(entity);
		entity.xOld = entity.pos.getX();
		entity.yOld = entity.pos.getY();
		entity.zOld = entity.pos.getZ();
		entity.blockMap = this;
	}

	public void remove(Entity entity) {
		int sx = this.getSlotX((int) entity.xOld);
		int sy = this.getSlotY((int) entity.yOld);
		int sz = this.getSlotZ((int) entity.zOld);
		this.entityGrid[(sz * this.height + sy) * this.width + sx].remove(entity);
		this.all.remove(entity);
	}

	public void moved(Entity entity) {
		int sx = this.getSlotX((int) entity.xOld);
		int sy = this.getSlotY((int) entity.yOld);
		int sz = this.getSlotZ((int) entity.zOld);
		int sx2 = this.getSlotX(entity.pos.getBlockX());
		int sy2 = this.getSlotY(entity.pos.getBlockY());
		int sz2 = this.getSlotZ(entity.pos.getBlockZ());
		if(sx != sx2 || sy != sy2 || sz != sz2) {
			this.remove(entity);
			this.insert(entity);
			entity.xOld = entity.pos.getX();
			entity.yOld = entity.pos.getY();
			entity.zOld = entity.pos.getZ();
		}
	}

	public List<Entity> getEntities(Entity exclude, float x, float y, float z, float x2, float y2, float z2) {
		return this.getEntities(exclude, x, y, z, x2, y2, z2, new ArrayList<Entity>());
	}

	public List<Entity> getEntities(Entity exclude, float x, float y, float z, float x2, float y2, float z2, List<Entity> result) {
		int sx = this.getSlotX((int) x);
		int sy = this.getSlotY((int) y);
		int sz = this.getSlotZ((int) z);
		int sx2 = this.getSlotX((int) x2);
		int sy2 = this.getSlotY((int) y2);
		int sz2 = this.getSlotZ((int) z2);
		for(int ex = sx - 1; ex <= sx2 + 1; ex++) {
			for(int ey = sy - 1; ey <= sy2 + 1; ey++) {
				for(int ez = sz - 1; ez <= sz2 + 1; ez++) {
					if(ex >= 0 && ey >= 0 && ez >= 0 && ex < this.width && ey < this.height && ez < this.depth) {
						List<Entity> entities = this.entityGrid[(ez * this.height + ey) * this.width + ex];
						for(Entity entity : entities) {
							if(entity != exclude && entity.intersects(x, y, z, x2, y2, z2)) {
								result.add(entity);
							}
						}
					}
				}
			}
		}

		return result;
	}

	public void removeSurvivalEntities() {
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				for(int z = 0; z < this.depth; z++) {
					List<Entity> entities = this.entityGrid[(z * this.height + y) * this.width + x];
					for(int index = 0; index < entities.size(); index++) {
						Entity entity = entities.get(index);
						if(!entity.isCreativeModeAllowed()) {
							entities.remove(entity);
							index--;
						}
					}
				}
			}
		}

		for(int index = 0; index < this.all.size(); index++) {
			Entity entity = this.all.get(index);
			if(!entity.isCreativeModeAllowed()) {
				this.all.remove(entity);
				index--;
			}
		}
	}

	public void clear() {
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				for(int z = 0; z < this.depth; z++) {
					this.entityGrid[(z * this.height + y) * this.width + x].clear();
				}
			}
		}
	}

	public List<Entity> getEntities(Entity exclude, BoundingBox bb) {
		return this.getEntities(exclude, bb.getX1(), bb.getY1(), bb.getZ1(), bb.getX2(), bb.getY2(), bb.getZ2(), new ArrayList<Entity>());
	}

	public List<Entity> getEntities(Entity exclude, BoundingBox bb, List<Entity> to) {
		return this.getEntities(exclude, bb.getX1(), bb.getY1(), bb.getZ1(), bb.getX2(), bb.getY2(), bb.getZ2(), to);
	}

	public void tickAll() {
		for(int index = 0; index < this.all.size(); index++) {
			Entity entity = this.all.get(index);
			entity.tick();
			if(entity.removed) {
				this.remove(entity);
				index--;
			} else {
				int omx = (int) (entity.xOld / 16.0F);
				int omy = (int) (entity.yOld / 16.0F);
				int omz = (int) (entity.zOld / 16.0F);
				int mx = (int) (entity.pos.getX() / 16.0F);
				int my = (int) (entity.pos.getY() / 16.0F);
				int mz = (int) (entity.pos.getZ() / 16.0F);
				if(omx != mx || omy != my || omz != mz) {
					this.moved(entity);
				}
			}
		}
	}

	public void render(float dt) {
		RenderHelper.getHelper().setLighting(true);
		for(int x = 0; x < this.width; x++) {
			float x1 = (x << 4) - 2;
			float x2 = (x + 1 << 4) + 2;
			for(int y = 0; y < this.height; y++) {
				float y1 = (y << 4) - 2;
				float y2 = (y + 1 << 4) + 2;
				for(int z = 0; z < this.depth; z++) {
					List<Entity> entities = this.entityGrid[(z * this.height + y) * this.width + x];
					if(entities.size() != 0) {
						float z1 = (z << 4) - 2;
						float z2 = (z + 1 << 4) + 2;
						if(Frustum.isBoxInFrustum(x1, y1, z1, x2, y2, z2)) {
							int plane = 0;
							boolean empty = true;
							while(true) {
								if(plane >= 6) {
									empty = true;
									break;
								}

								if(Frustum.frustum[plane][0] * x1 + Frustum.frustum[plane][1] * y1 + Frustum.frustum[plane][2] * y1 + Frustum.frustum[plane][3] <= 0.0F) {
									empty = false;
									break;
								}

								if(Frustum.frustum[plane][0] * x2 + Frustum.frustum[plane][1] * y1 + Frustum.frustum[plane][2] * y1 + Frustum.frustum[plane][3] <= 0.0F) {
									empty = false;
									break;
								}

								if(Frustum.frustum[plane][0] * x1 + Frustum.frustum[plane][1] * y2 + Frustum.frustum[plane][2] * y1 + Frustum.frustum[plane][3] <= 0.0F) {
									empty = false;
									break;
								}

								if(Frustum.frustum[plane][0] * x2 + Frustum.frustum[plane][1] * y2 + Frustum.frustum[plane][2] * y1 + Frustum.frustum[plane][3] <= 0.0F) {
									empty = false;
									break;
								}

								if(Frustum.frustum[plane][0] * x1 + Frustum.frustum[plane][1] * y1 + Frustum.frustum[plane][2] * z2 + Frustum.frustum[plane][3] <= 0.0F) {
									empty = false;
									break;
								}

								if(Frustum.frustum[plane][0] * x2 + Frustum.frustum[plane][1] * y1 + Frustum.frustum[plane][2] * z2 + Frustum.frustum[plane][3] <= 0.0F) {
									empty = false;
									break;
								}

								if(Frustum.frustum[plane][0] * x1 + Frustum.frustum[plane][1] * y2 + Frustum.frustum[plane][2] * z2 + Frustum.frustum[plane][3] <= 0.0F) {
									empty = false;
									break;
								}

								if(Frustum.frustum[plane][0] * x2 + Frustum.frustum[plane][1] * y2 + Frustum.frustum[plane][2] * z2 + Frustum.frustum[plane][3] <= 0.0F) {
									empty = false;
									break;
								}

								plane++;
							}

							Position pos = OpenClassic.getClient().getPlayer().getPosition();
							for(int index = 0; index < entities.size(); index++) {
								Entity entity = entities.get(index);
								float sqDistance = entity.pos.distanceSquared(pos);
								float size = entity.bb != null ? entity.bb.getSize() * 64 : 64;
								if(sqDistance < size * size) {
									if(!empty) {
										BoundingBox bb = entity.bb;
										if(bb != null && !Frustum.isBoxInFrustum(bb.getX1(), bb.getY1(), bb.getZ1(), bb.getX2(), bb.getY2(), bb.getZ2())) {
											continue;
										}
									}

									entity.render(dt);
								}
							}
						}
					}
				}
			}
		}
		
		RenderHelper.getHelper().setLighting(false);
	}
	
	private int getSlotX(int entityX) {
		int ret = entityX / 16;
		if(ret < 0) {
			ret = 0;
		}

		if(ret >= this.width) {
			ret = this.width - 1;
		}
		
		return ret;
	}
	
	private int getSlotY(int entityY) {
		int ret = entityY / 16;
		if(ret < 0) {
			ret = 0;
		}

		if(ret >= this.height) {
			ret = this.height - 1;
		}
		
		return ret;
	}
	
	private int getSlotZ(int entityZ) {
		int ret = entityZ / 16;
		if(ret < 0) {
			ret = 0;
		}

		if(ret >= this.depth) {
			ret = this.depth - 1;
		}
		
		return ret;
	}
	
}
