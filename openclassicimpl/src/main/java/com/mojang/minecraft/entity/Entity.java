package com.mojang.minecraft.entity;

import com.mojang.minecraft.entity.player.LocalPlayer;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.StepSound;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.client.level.BlockMap;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.game.util.InternalConstants;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {

	public Position pos;
	public float xd;
	public float yd;
	public float zd;
	public BoundingBox bb;
	public boolean onGround = false;
	public boolean horizontalCollision = false;
	public boolean collision = false;
	public boolean slide = true;
	public boolean removed = false;
	public float heightOffset = 0;
	public float bbWidth = 0.6F;
	public float bbHeight = 1.8F;
	public float walkDistO = 0;
	public float walkDist = 0;
	public boolean makeStepSound = true;
	public float fallDistance = 0;
	private int nextStep = 1;
	public BlockMap blockMap;
	public float xOld;
	public float yOld;
	public float zOld;
	public float ySlideOffset = 0;
	public float footSize = 0;
	public boolean noPhysics = false;
	public float pushthrough = 0;
	public boolean hovered = false;
	private boolean waterSplashed = false;

	public Entity(ClientLevel level) {
		this.pos = new Position(level, 0, 0, 0);
	}

	public void resetPos() {
		this.resetPos(null);
	}

	public void resetPos(Position pos) {
		if(pos != null) {
			pos = pos.clone();
			while(pos.getY() < this.pos.getLevel().getHeight() && this.getClientLevel().getBoxes(this.bb).size() != 0) {
				pos.setY(pos.getY() + 1);
			}

			this.setPos(pos.getX(), pos.getY(), pos.getZ());
			this.xd = 0;
			this.yd = 0;
			this.zd = 0;
			this.pos.setYaw(pos.getYaw());
			this.pos.setPitch(pos.getPitch());
		} else if(this.pos.getLevel() != null && this.pos.getLevel().getSpawn() != null) {
			float x = this.pos.getLevel().getSpawn().getX();
			float y = this.pos.getLevel().getSpawn().getY();
			float z = this.pos.getLevel().getSpawn().getZ();
			while(y < this.pos.getLevel().getHeight()) {
				this.setPos(x, y, z);
				if(this.getClientLevel().getBoxes(this.bb).size() == 0) {
					break;
				}

				y++;
			}

			this.xd = 0;
			this.yd = 0;
			this.zd = 0;
			this.pos.setYaw(this.pos.getLevel().getSpawn().getYaw());
			this.pos.setPitch(this.pos.getLevel().getSpawn().getPitch());
		}
	}

	public void remove() {
		this.removed = true;
	}

	public void setSize(float width, float height) {
		this.bbWidth = width;
		this.bbHeight = height;
	}

	protected void setRot(float yaw, float pitch) {
		this.pos.setYaw(yaw);
		this.pos.setPitch(pitch);
	}

	public void setPos(float x, float y, float z) {
		this.pos.set(x, y, z);
		float widthCenter = this.bbWidth / 2;
		float heightCenter = this.bbHeight / 2;
		this.bb = new BoundingBox(x - widthCenter, y - heightCenter, z - widthCenter, x + widthCenter, y + heightCenter, z + widthCenter);
	}

	public void turn(float yaw, float pitch) {
		this.pos.setYaw((float) (this.pos.getYaw() + yaw * InternalConstants.SENSITIVITY_VALUE[1]));
		this.pos.setPitch((float) (this.pos.getPitch() - pitch * InternalConstants.SENSITIVITY_VALUE[1]));
	}

	public void tick() {
		// Reset previous values by setting pos to itself.
		this.pos.set(this.pos);
		this.walkDistO = this.walkDist;
		BlockType block = this.getLiquid();
		if(block != null && block.getLiquidName() != null && block.getLiquidName().equals("water")) {
			if(!this.waterSplashed) {
				this.waterSplashed = true;
				float volume = (float) Math.sqrt(this.xd * this.xd * 0.2D + this.yd * this.yd + this.zd * this.zd * 0.2D) * 0.2f;
				if(volume > 1) {
					volume = 1;
				}

				OpenClassic.getGame().getAudioManager().playSound("random.splash", this.pos.getX(), this.pos.getY(), this.pos.getZ(), volume, 1 + (this.getClientLevel().getRandom().nextFloat() - this.getClientLevel().getRandom().nextFloat()) * 0.4f);
			}
		} else {
			this.waterSplashed = false;
		}
	}

	public boolean isFree(float x, float y, float z, float radius) {
		BoundingBox grown = this.bb.grow(radius, radius, radius).cloneMove(x, y, z);
		return this.getClientLevel().getBoxes(grown).size() <= 0 || this.getClientLevel().getLiquid(grown) == null;
	}

	public boolean isFree(float x, float y, float z) {
		BoundingBox moved = this.bb.cloneMove(x, y, z);
		return this.getClientLevel().getBoxes(moved).size() <= 0 || this.getClientLevel().getLiquid(moved) == null;
	}

	public void move(float x, float y, float z) {
		if(this.noPhysics) {
			this.bb.move(x, y, z);
			this.pos.set((this.bb.getX1() + this.bb.getX2()) / 2, this.bb.getY1() + this.heightOffset - this.ySlideOffset, (this.bb.getZ1() + this.bb.getZ2()) / 2);
		} else {
			float oldEntityX = this.pos.getX();
			float oldEntityZ = this.pos.getZ();
			float oldX = x;
			float oldY = y;
			float oldZ = z;
			BoundingBox copy = this.bb.clone();
			ArrayList<BoundingBox> cubes = this.getClientLevel().getBoxes(this.bb.expand(x, y, z));
			for(BoundingBox cube : cubes) {
				y = cube.clipYCollide(this.bb, y);
			}

			this.bb.move(0, y, 0);
			if(!this.slide && oldY != y) {
				x = 0;
				y = 0;
				z = 0;
			}

			boolean stepFurther = this.onGround || oldY != y && oldY < 0;
			for(BoundingBox cube : cubes) {
				x = cube.clipXCollide(this.bb, x);
			}

			this.bb.move(x, 0, 0);
			if(!this.slide && oldX != x) {
				z = 0.0F;
				y = 0.0F;
				x = 0.0F;
			}

			for(BoundingBox cube : cubes) {
				z = cube.clipZCollide(this.bb, z);
			}

			this.bb.move(0, 0, z);
			if(!this.slide && oldZ != z) {
				x = 0;
				y = 0;
				z = 0;
			}

			if(this.footSize > 0 && stepFurther && this.ySlideOffset < 0.05F && (oldX != x || oldZ != z)) {
				float newX = x;
				float newY = y;
				float newZ = z;
				x = oldX;
				y = this.footSize;
				z = oldZ;
				BoundingBox newCopy = this.bb.clone();
				this.bb = copy.clone();
				cubes = this.getClientLevel().getBoxes(this.bb.expand(oldX, y, oldZ));

				for(BoundingBox cube : cubes) {
					y = cube.clipYCollide(this.bb, y);
				}

				this.bb.move(0, y, 0);
				if(!this.slide && oldY != y) {
					z = 0;
					y = 0;
					x = 0;
				}

				for(BoundingBox cube : cubes) {
					x = cube.clipXCollide(this.bb, x);
				}

				this.bb.move(x, 0, 0);
				if(!this.slide && oldX != x) {
					z = 0;
					y = 0;
					x = 0;
				}

				for(BoundingBox cube : cubes) {
					z = cube.clipZCollide(this.bb, z);
				}

				this.bb.move(0, 0, z);
				if(!this.slide && oldZ != z) {
					z = 0;
					y = 0;
					x = 0;
				}

				if(newX * newX + newZ * newZ >= x * x + z * z) {
					x = newX;
					y = newY;
					z = newZ;
					this.bb = newCopy.clone();
				} else {
					this.ySlideOffset = (float) (this.ySlideOffset + 0.5D);
				}
			}

			this.horizontalCollision = oldX != x || oldZ != z;
			this.onGround = oldY != y && oldY < 0;
			this.collision = this.horizontalCollision || oldY != y;
			if(this.onGround) {
				if(this.fallDistance > 0) {
					this.causeFallDamage(this.fallDistance);
					this.fallDistance = 0;
				}
			} else if(y < 0.0F) {
				this.fallDistance -= y;
			}

			if(oldX != x) {
				this.xd = 0;
			}

			if(oldY != y) {
				this.yd = 0;
			}

			if(oldZ != z) {
				this.zd = 0;
			}

			this.pos.set((this.bb.getX1() + this.bb.getX2()) / 2, this.bb.getY1() + this.heightOffset - this.ySlideOffset, (this.bb.getZ1() + this.bb.getZ2()) / 2);
			float xDiff = this.pos.getX() - oldEntityX;
			float zDiff = this.pos.getZ() - oldEntityZ;
			if(this.onGround) {
				this.walkDist = (float) (this.walkDist + (float) Math.sqrt(xDiff * xDiff + zDiff * zDiff) * 0.6D);
				if(this.makeStepSound) {
					BlockType type = this.pos.getLevel().getBlockTypeAt((int) this.pos.getX(), (int) (this.pos.getY() - 0.2F - this.heightOffset), (int) this.pos.getZ());
					if(this.walkDist > this.nextStep && type != null) {
						this.nextStep++;
						if(type != null) {
							StepSound step = type.getStepSound();
							if(step != StepSound.NONE) {
								OpenClassic.getGame().getAudioManager().playSound(step.getSound(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), step.getVolume() * 0.75F, step.getPitch());
							}
						}
					}
				}
			}

			this.ySlideOffset *= 0.4F;
		}
	}

	protected void causeFallDamage(float distance) {
	}
	
	public BlockType getLiquid() {
		if(this.bb == null) {
			return null;
		}

		return this.getClientLevel().getLiquid(this.bb.grow(0, -0.4F, 0));
	}
	
	public List<BlockType> getBlockIn() {
		return this.getClientLevel().getBlocksIn(this.bb.grow(0, -0.4F, 0));
	}

	public boolean isUnderWater() {
		if(this.pos.getLevel() == null) {
			return false;
		}
		
		BlockType block = this.pos.getLevel().getBlockTypeAt((int) this.pos.getX(), (int) (this.pos.getY() + 0.12F), (int) this.pos.getZ());
		return block != null && (block.getLiquidName() != null && block.getLiquidName().equals("water"));
	}

	public void moveHeading(float forward, float strafe, float speed) {
		float len = (float) Math.sqrt(forward * forward + strafe * strafe);
		if(len >= 0.01F) {
			if(len < 1) {
				len = 1;
			}

			float mforward = forward * (speed / len);
			float mstrafe = strafe * (speed / len);
			float xangle = MathHelper.cos(this.pos.getYaw() * MathHelper.DEG_TO_RAD);
			float zangle = MathHelper.sin(this.pos.getYaw() * MathHelper.DEG_TO_RAD);

			this.xd += mforward * xangle - mstrafe * zangle;
			this.zd += mstrafe * xangle + mforward * zangle;
		}
	}

	public boolean isLit() {
		return this.pos.getLevel().isLit((int) this.pos.getX(), (int) this.pos.getY(), (int) this.pos.getZ());
	}

	public float getBrightness(float dt) {
		int y = (int) (this.pos.getY() + this.heightOffset / 2 - 0.5F);
		return this.pos.getLevel().getBrightness((int) this.pos.getX(), y, (int) this.pos.getZ());
	}

	public void render(float dt) {
	}

	public void setLevel(ClientLevel level) {
		this.pos.setLevel(level);
	}

	public void moveTo(float x, float y, float z, float yaw, float pitch) {
		this.pos.set(x, y, z);
		this.pos.setYaw(yaw);
		this.pos.setPitch(pitch);
		this.setPos(x, y, z);
	}

	public float distanceTo(Entity other) {
		float xDistance = this.pos.getX() - other.pos.getX();
		float yDistance = this.pos.getY() - other.pos.getY();
		float zDistance = this.pos.getZ() - other.pos.getZ();
		return (float) Math.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);
	}

	public float distanceTo(float x, float y, float z) {
		float xDistance = this.pos.getX() - x;
		float yDistance = this.pos.getY() - y;
		float zDistance = this.pos.getZ() - z;
		return (float) Math.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);
	}

	public float distanceToSqr(Entity other) {
		float xDistance = this.pos.getX() - other.pos.getX();
		float yDistance = this.pos.getY() - other.pos.getY();
		float zDistance = this.pos.getZ() - other.pos.getZ();
		return xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;
	}

	public void playerTouch(LocalPlayer player) {
	}

	public void push(Entity entity) {
		float xDiff = entity.pos.getX() - this.pos.getX();
		float zDiff = entity.pos.getZ() - this.pos.getZ();
		float sqXZDiff = xDiff * xDiff + zDiff * zDiff;
		if(sqXZDiff >= 0.01F) {
			float xzDiff = (float) Math.sqrt(sqXZDiff);
			xDiff /= xzDiff;
			zDiff /= xzDiff;
			xDiff /= xzDiff;
			zDiff /= xzDiff;
			xDiff *= 0.05F;
			zDiff *= 0.05F;
			xDiff *= 1 - this.pushthrough;
			zDiff *= 1 - this.pushthrough;
			this.push(-xDiff, 0, -zDiff);
			entity.push(xDiff, 0, zDiff);
		}
	}

	protected void push(float x, float y, float z) {
		this.xd += x;
		this.yd += y;
		this.zd += z;
	}

	public void hurt(Entity cause, int damage) {
	}

	public boolean intersects(float x1, float y1, float z1, float x2, float y2, float z2) {
		if(this.bb == null) {
			return false;
		}

		return this.bb.intersects(x1, y1, z1, x2, y2, z2);
	}

	public boolean isPickable() {
		return false;
	}

	public boolean isPushable() {
		return false;
	}

	public boolean isShootable() {
		return false;
	}

	public void awardKillScore(Entity entity, int amount) {
	}

	public boolean isCreativeModeAllowed() {
		return false;
	}

	public void renderHoverOver(float dt) {
	}
	
	public ClientLevel getClientLevel() {
		return (ClientLevel) this.pos.getLevel();
	}
	
}
