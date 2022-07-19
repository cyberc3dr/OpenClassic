package com.mojang.minecraft.entity.object;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.player.LocalPlayer;
import org.lwjgl.opengl.GL11;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.render.Renderer;
import org.spacehq.openclassic.client.render.Textures;

import java.util.List;

public class Arrow extends Entity {

	private float xd;
	private float yd;
	private float zd;
	private boolean hasHit = false;
	private int stickTime = 0;
	private Entity owner;
	private int time = 0;
	private int type = 0;
	private float gravity = 0.0F;
	private int damage;

	public Arrow(ClientLevel level, Entity owner, float x, float y, float z, float yaw, float pitch, float power) {
		super(level);
		this.owner = owner;
		this.setSize(0.3F, 0.5F);
		this.heightOffset = this.bbHeight / 2;
		this.damage = 3;
		if(!(owner instanceof LocalPlayer)) {
			this.type = 1;
		} else {
			this.damage = 7;
		}

		this.heightOffset = 0.25F;
		float ycos = MathHelper.cos(-yaw * MathHelper.DEG_TO_RAD - MathHelper.PI);
		float ysin = MathHelper.sin(-yaw * MathHelper.DEG_TO_RAD - MathHelper.PI);
		float pcos = MathHelper.cos(-pitch * MathHelper.DEG_TO_RAD);
		float psin = MathHelper.sin(-pitch * MathHelper.DEG_TO_RAD);
		this.slide = false;
		this.gravity = 1 / power;
		//this.xo -= ycos * 0.2F;
		//this.zo += ysin * 0.2F;
		x -= ycos * 0.2F;
		z += ysin * 0.2F;
		this.xd = ysin * pcos * power;
		this.yd = psin * power;
		this.zd = ycos * pcos * power;
		this.setPos(x, y, z);
		float len = (float) Math.sqrt(this.xd * this.xd + this.zd * this.zd);
		this.pos.setYaw((float) (Math.atan2(this.xd, this.zd) * MathHelper.DRAD_TO_DEG));
		this.pos.setYaw(this.pos.getYaw());
		this.pos.setPitch((float) (Math.atan2(this.yd, len) * MathHelper.DRAD_TO_DEG));
		this.pos.setPitch(this.pos.getPitch());
		this.makeStepSound = false;
	}

	@Override
	public void tick() {
		super.tick();
		this.time++;
		if(this.hasHit) {
			this.stickTime++;
			if(this.type == 0) {
				if(this.stickTime >= 300 && Math.random() < 0.009D) {
					this.remove();
					return;
				}
			} else if(this.type == 1 && this.stickTime >= 20) {
				this.remove();
			}

		} else {
			this.xd *= 0.998F;
			this.yd *= 0.998F;
			this.zd *= 0.998F;
			this.yd -= 0.02F * this.gravity;
			int len = (int) ((float) Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd) / 0.2F + 1);
			float movX = this.xd / len;
			float movY = this.yd / len;
			float movZ = this.zd / len;

			for(int mov = 0; mov < len && !this.collision; mov++) {
				BoundingBox collision = this.bb.expand(movX, movY, movZ);
				if(this.getClientLevel().getBoxes(collision).size() > 0) {
					this.collision = true;
				}

				List<Entity> entities = this.getClientLevel().getEntities(this, collision);
				for(int count = 0; count < entities.size(); count++) {
					Entity entity = entities.get(count);
					if(entity.isShootable() && (entity != this.owner || this.time > 5)) {
						OpenClassic.getGame().getAudioManager().playSound("random.drr", this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1, 1.2f / (this.getClientLevel().getRandom().nextFloat() * 0.2f + 0.9f));
						entity.hurt(this, this.damage);
						this.collision = true;
						this.remove();
						return;
					}
				}

				if(!this.collision) {
					this.bb.move(movX, movY, movZ);
					this.pos.add(movX, movY, movZ);
					this.blockMap.moved(this);
				}
			}

			if(this.collision) {
				OpenClassic.getGame().getAudioManager().playSound("random.drr", this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1, 1.2f / (this.getClientLevel().getRandom().nextFloat() * 0.2f + 0.9f));
				this.hasHit = true;
				this.xd = 0;
				this.yd = 0;
				this.zd = 0;
			}

			if(!this.hasHit) {
				float xzlen = (float) Math.sqrt(this.xd * this.xd + this.zd * this.zd);
				this.pos.setYaw((float) (Math.atan2(this.xd, this.zd) * 180 / Math.PI));
				this.pos.setPitch((float) (Math.atan2(this.yd, xzlen) * 180 / Math.PI));
			}

		}
	}

	@Override
	public void render(float dt) {
		Textures.ARROW.bind();
		float brightness = this.pos.getLevel().getBrightness(this.pos.getBlockX(), this.pos.getBlockY(), this.pos.getBlockZ());
		GL11.glPushMatrix();
		GL11.glTranslatef(this.pos.getInterpolatedX(dt), this.pos.getInterpolatedY(dt) - this.heightOffset / 2, this.pos.getInterpolatedZ(dt));
		GL11.glRotatef(this.pos.getInterpolatedYaw(dt) - 90, 0, 1, 0);
		GL11.glRotatef(this.pos.getInterpolatedPitch(dt), 0, 0, 1);
		GL11.glRotatef(45, 1, 0, 0);
		float endty2 = (0 + this.type * 10) / 32F;
		float endty1 = (5 + this.type * 10) / 32F;
		float ty2 = (5 + this.type * 10) / 32F;
		float ty1 = (10 + this.type * 10) / 32F;
		GL11.glScalef(0.05625F, 0.05625F, 0.05625F);
		Renderer.get().begin();
		Renderer.get().color(brightness, brightness, brightness);
		Renderer.get().normal(0.05625F, 0, 0);
		Renderer.get().vertexuv(-7, -2, -2, 0, ty2);
		Renderer.get().vertexuv(-7, -2, 2, 0.15625F, ty2);
		Renderer.get().vertexuv(-7, 2, 2, 0.15625F, ty1);
		Renderer.get().vertexuv(-7, 2, -2, 0, ty1);
		Renderer.get().end();
		Renderer.get().begin();
		Renderer.get().color(brightness, brightness, brightness);
		Renderer.get().normal(-0.05625F, 0, 0);
		Renderer.get().vertexuv(-7, 2, -2, 0, ty2);
		Renderer.get().vertexuv(-7, 2, 2, 0.15625F, ty2);
		Renderer.get().vertexuv(-7, -2, 2, 0.15625F, ty1);
		Renderer.get().vertexuv(-7, -2, -2, 0, ty1);
		Renderer.get().end();

		for(int end = 0; end < 4; end++) {
			GL11.glRotatef(90, 1, 0, 0);
			Renderer.get().begin();
			Renderer.get().color(brightness, brightness, brightness);
			Renderer.get().normal(0, -0.05625F, 0);
			Renderer.get().vertexuv(-8, -2, 0, 0, endty2);
			Renderer.get().vertexuv(8, -2, 0, 0.5F, endty2);
			Renderer.get().vertexuv(8, 2, 0, 0.5F, endty1);
			Renderer.get().vertexuv(-8, 2, 0, 0, endty1);
			Renderer.get().end();
		}

		GL11.glPopMatrix();
	}

	@Override
	public void awardKillScore(Entity killed, int score) {
		this.owner.awardKillScore(killed, score);
	}

	public Entity getOwner() {
		return this.owner;
	}

	@Override
	public void playerTouch(LocalPlayer player) {
		if(this.hasHit && this.owner == player && player.arrows < 99) {
			OpenClassic.getGame().getAudioManager().playSound("random.pop", player.pos.getX(), player.pos.getY(), player.pos.getZ(), 0.2f, ((this.getClientLevel().getRandom().nextFloat() - this.getClientLevel().getRandom().nextFloat()) * 0.7f + 1) * 2);
			this.getClientLevel().addEntity(new TakeEntityAnim(this.getClientLevel(), this, player));
			player.arrows++;
			this.remove();
		}
	}
	
}
