package com.mojang.minecraft.entity.mob;

import java.util.List;

import org.lwjgl.opengl.GL11;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.api.util.Constants;
import org.spacehq.openclassic.client.level.ClientLevel;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.mob.ai.AI;
import com.mojang.minecraft.entity.mob.ai.BasicAI;
import com.mojang.minecraft.entity.model.ModelManager;

public class Mob extends Entity {

	public static final int ATTACK_DURATION = 5;
	public static final int TOTAL_AIR_SUPPLY = 300;
	public static final ModelManager modelCache = new ModelManager();
	public int invulnerableDuration = 20;
	public float rot;
	public float timeOffs;
	public float speed;
	public float rotA = (float) (Math.random() + 1) * 0.01F;
	protected float bodyYaw = 0;
	protected float oBodyYaw = 0;
	protected float oRun;
	protected float run;
	protected float animStep;
	protected float animStepO;
	protected int tickCount = 0;
	public boolean hasHair = true;
	public boolean allowAlpha = true;
	public float rotOffs = 0;
	public String modelName = null;
	protected float bobStrength = 1;
	protected int deathScore = 0;
	public float renderOffset = 0;
	public int health = 20;
	public int lastHealth;
	public int invulnerableTime = 0;
	public int airSupply = 300;
	public int hurtTime;
	public int hurtDuration;
	public float hurtDir = 0;
	public int deathTime = 0;
	public int attackTime = 0;
	public float oTilt;
	public float tilt;
	public boolean dead = false;
	public AI ai;
	private int drownTime = 20;
	private int burnTime = 10;
	private Texture texture;

	public Mob(ClientLevel level, Texture texture) {
		super(level);
		this.timeOffs = (float) Math.random() * 12398;
		this.rot = (float) (Math.random() * MathHelper.DTWO_PI);
		this.speed = 1;
		this.ai = new BasicAI();
		this.footSize = 0.5F;
		this.texture = texture;
	}

	@Override
	public boolean isPickable() {
		return !this.removed;
	}

	@Override
	public boolean isPushable() {
		return !this.removed;
	}

	@Override
	public void tick() {
		super.tick();
		this.oTilt = this.tilt;
		if(this.attackTime > 0) {
			this.attackTime--;
		}

		if(this.hurtTime > 0) {
			this.hurtTime--;
		}

		if(this.invulnerableTime > 0) {
			this.invulnerableTime--;
		}

		if(this.health <= 0) {
			this.deathTime++;
			if(this.deathTime > 20) {
				if(this.ai != null) {
					this.ai.beforeRemove();
				}

				this.remove();
			}
		}

		if(this.isUnderWater()) {
			if(this.airSupply > 0) {
				this.airSupply--;
			} else {
				this.drownTime++;
				if(this.drownTime > 20) {
					this.hurt(null, 2);
					this.drownTime = 0;
				}
			}
		} else {
			this.drownTime = 20;
			this.airSupply = Constants.MAX_AIR;
		}

		BlockType liquid = this.getLiquid();
		if(liquid != null) {
			this.fallDistance = 0;
			if(liquid.getId() == VanillaBlock.LAVA.getId() || liquid.getId() == VanillaBlock.STATIONARY_LAVA.getId()) {
				this.burnTime++;
				if(this.burnTime > 10) {
					this.hurt(null, 7);
					this.burnTime = 0;
				}
			} else {
				this.burnTime = 10;
			}
		} else {
			this.burnTime = 10;
		}

		this.animStepO = this.animStep;
		this.oBodyYaw = this.bodyYaw;
		this.tickCount++;
		this.aiStep();
		float xDistance = this.pos.getX() - this.pos.getPreviousX();
		float zDistance = this.pos.getZ() - this.pos.getPreviousZ();
		float xzDistance = (float) Math.sqrt(xDistance * xDistance + zDistance * zDistance);
		float yaw = this.bodyYaw;
		float animStep = 0;
		this.oRun = this.run;
		float friction = 0;
		if(xzDistance > 0.05F) {
			friction = 1;
			animStep = xzDistance * 3;
		}

		if(!this.onGround) {
			friction = 0;
		}

		this.run += (friction - this.run) * 0.3F;

		float change = yaw - this.bodyYaw;
		while(change < -180) {
			change += 360;
		}

		while(change >= 180) {
			change -= 360;
		}

		this.bodyYaw += change * 0.1F;

		change = this.pos.getYaw() - this.bodyYaw;
		while(change < -180) {
			change += 360;
		}

		while(change >= 180) {
			change -= 360;
		}

		boolean negative = change < -90 || change >= 90;
		if(change < -75) {
			change = -75;
		}

		if(change >= 75) {
			change = 75;
		}

		this.bodyYaw = this.pos.getYaw() - change;
		this.bodyYaw += change * 0.1F;
		if(negative) {
			animStep = -animStep;
		}

		while(this.bodyYaw - this.oBodyYaw < -180) {
			this.oBodyYaw -= 360;
		}

		while(this.bodyYaw - this.oBodyYaw >= 180) {
			this.oBodyYaw += 360;
		}

		this.animStep += animStep;
	}

	public void aiStep() {
		if(this.ai != null) {
			this.ai.tick(this.getClientLevel(), this);
		}
	}

	protected void bindTexture() {
		this.texture.bind();
	}

	@Override
	public void render(float dt) {
		if(this.modelName != null) {
			float attackTime = this.attackTime - dt;
			if(attackTime < 0) {
				attackTime = 0;
			}

			while(this.oBodyYaw - this.bodyYaw < -180) {
				this.oBodyYaw += 360;
			}

			while(this.oBodyYaw - this.bodyYaw >= 180) {
				this.oBodyYaw -= 360;
			}

			float bodyYaw = this.oBodyYaw + (this.bodyYaw - this.oBodyYaw) * dt;
			float runProgress = this.oRun + (this.run - this.oRun) * dt;
			float yaw = this.pos.getInterpolatedYaw(dt) - bodyYaw;
			float pitch = this.pos.getInterpolatedPitch(dt);
			float animStep = this.animStepO + (this.animStep - this.animStepO) * dt;
			float brightness = this.getBrightness(dt);
			float bob = -Math.abs(MathHelper.cos(animStep * 0.6662F)) * 5 * runProgress * this.bobStrength - 23;
			GL11.glPushMatrix();
			GL11.glColor3f(brightness, brightness, brightness);
			GL11.glTranslatef(this.pos.getInterpolatedX(dt), this.pos.getInterpolatedY(dt) - 1.62F + this.renderOffset, this.pos.getInterpolatedZ(dt));
			float hurtRot = this.hurtTime - dt;
			if(hurtRot > 0 || this.health <= 0) {
				if(hurtRot < 0) {
					hurtRot = 0;
				} else {
					hurtRot /= this.hurtDuration;
					hurtRot = MathHelper.sin(hurtRot * hurtRot * hurtRot * hurtRot * MathHelper.PI) * 14.0F;
				}

				float deathRot = 0;
				if(this.health <= 0) {
					deathRot = (this.deathTime + dt) / 20F;
					hurtRot += deathRot * deathRot * 800;
					if(hurtRot > 90) {
						hurtRot = 90;
					}
				}

				GL11.glRotatef(180 - bodyYaw + this.rotOffs, 0, 1, 0);
				GL11.glScalef(1, 1, 1);
				GL11.glRotatef(-this.hurtDir, 0, 1, 0);
				GL11.glRotatef(-hurtRot, 0, 0, 1);
				GL11.glRotatef(this.hurtDir, 0, 1, 0);
				GL11.glRotatef(-(180 - bodyYaw + this.rotOffs), 0, 1, 0);
			}

			GL11.glTranslatef(0, -bob * 0.0625F, 0);
			GL11.glScalef(1, -1, 1);
			GL11.glRotatef(180 - bodyYaw + this.rotOffs, 0, 1, 0);
			if(!this.allowAlpha) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
			} else {
				GL11.glDisable(GL11.GL_CULL_FACE);
			}

			GL11.glScalef(-1, 1, 1);
			modelCache.getModel(this.modelName).attackTime = attackTime / 5;
			this.bindTexture();
			this.renderModel(animStep, dt, runProgress, yaw, pitch, 0.0625F);
			if(this.invulnerableTime > this.invulnerableDuration - 10) {
				GL11.glColor4f(1, 1, 1, 0.75F);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
				this.bindTexture();
				this.renderModel(animStep, dt, runProgress, yaw, pitch, 0.0625F);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			if(!this.allowAlpha) {
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			} else {
				GL11.glEnable(GL11.GL_CULL_FACE);
			}

			GL11.glColor4f(1, 1, 1, 1);
			GL11.glPopMatrix();
		}
	}

	public void renderModel(float animStep, float dt, float runProgress, float yaw, float pitch, float scale) {
		modelCache.getModel(this.modelName).render(animStep, runProgress, this.tickCount + dt, yaw, pitch, scale);
	}

	public void heal(int amount) {
		if(this.health > 0) {
			this.health += amount;
			if(this.health > 20) {
				this.health = 20;
			}

			this.invulnerableTime = this.invulnerableDuration / 2;
		}
	}

	@Override
	public void hurt(Entity cause, int damage) {
		if(OpenClassic.getClient().isInSurvival()) {
			if(this.health > 0) {
				this.ai.hurt(cause, damage);
				if(this.invulnerableTime > this.invulnerableDuration / 2) {
					if(this.lastHealth - damage >= this.health) {
						return;
					}

					this.health = this.lastHealth - damage;
				} else {
					this.lastHealth = this.health;
					this.invulnerableTime = this.invulnerableDuration;
					this.health -= damage;
					this.hurtTime = this.hurtDuration = 10;
				}

				this.hurtDir = 0;
				if(cause != null) {
					float xDistance = cause.pos.getX() - this.pos.getX();
					float zDistance = cause.pos.getZ() - this.pos.getZ();
					this.hurtDir = (float) (Math.atan2(zDistance, xDistance) * 180 / Math.PI) - this.pos.getYaw();
					this.knockback(cause, damage, xDistance, zDistance);
				} else {
					this.hurtDir = ((int) (Math.random() * 2) * 180);
				}

				if(this.health <= 0) {
					this.die(cause);
				}
			}
		}
	}

	public void knockback(Entity entity, int damage, float xDistance, float zDistance) {
		float len = (float) Math.sqrt(xDistance * xDistance + zDistance * zDistance);
		this.xd /= 2;
		this.yd /= 2;
		this.zd /= 2;
		this.xd -= xDistance / len * 0.4F;
		this.yd += 0.4F;
		this.zd -= zDistance / len * 0.4F;
		if(this.yd > 0.4F) {
			this.yd = 0.4F;
		}
	}

	public void die(Entity cause) {
		if(OpenClassic.getClient().isInSurvival()) {
			if(this.deathScore > 0 && cause != null) {
				cause.awardKillScore(this, this.deathScore);
			}

			this.dead = true;
		}
	}

	@Override
	protected void causeFallDamage(float distance) {
		if(OpenClassic.getClient().isInSurvival()) {
			int damage = (int) Math.ceil((distance - 3));
			if(damage > 0) {
				this.hurt(null, damage);
			}
		}
	}

	public void travel(float x, float z) {
		boolean flying = this.ai instanceof BasicAI && ((BasicAI) this.ai).flying;
		List<BlockType> blocksIn = this.getBlockIn();
		BlockType liquid = this.getLiquid();
		if(liquid != null) {
			this.moveHeading(x, z, flying ? 0.125F : 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= liquid.getSpeedModifier();
			this.yd *= liquid.getSpeedModifier();
			this.zd *= liquid.getSpeedModifier();
			if(!flying) {
				this.yd = (float) (this.yd - 0.02D);
			}

			if(this.horizontalCollision && this.isFree(this.xd, this.yd + 0.6F, this.zd)) {
				this.yd = 0.3F;
			}
		} else {
			this.moveHeading(x, z, flying ? 0.125F : this.onGround ? 0.1F : 0.02F);
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.91F;
			this.yd *= 0.98F;
			this.zd *= 0.91F;
			for(BlockType block : blocksIn) {
				this.xd *= block.getSpeedModifier();
				this.yd *= block.getSpeedModifier();
				this.zd *= block.getSpeedModifier();
			}
			
			if(!flying) {
				this.yd = (float) (this.yd - 0.08D);
			}

			if(this.onGround) {
				float y = 0.6F;
				this.xd *= y;
				this.zd *= y;
			}
		}
	}

	@Override
	public boolean isShootable() {
		return true;
	}
	
}
