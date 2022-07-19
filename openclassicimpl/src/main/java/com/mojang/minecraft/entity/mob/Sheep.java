package com.mojang.minecraft.entity.mob;

import org.lwjgl.opengl.GL11;

import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.render.Textures;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.mob.ai.SheepAI;
import com.mojang.minecraft.entity.model.AnimalModel;
import com.mojang.minecraft.entity.object.Item;
import com.mojang.minecraft.entity.player.LocalPlayer;

public class Sheep extends QuadrupedMob {

	public boolean hasFur = true;
	public boolean grazing = false;
	public int grazingTime = 0;
	public float graze;
	public float grazeO;

	public Sheep(ClientLevel level, float x, float y, float z) {
		super(level, x, y, z, Textures.SHEEP);
		this.setSize(1.4F, 1.72F);
		this.setPos(x, y, z);
		this.heightOffset = 1.72F;
		this.modelName = "sheep";
		this.ai = new SheepAI(this);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.grazeO = this.graze;
		if(this.grazing) {
			this.graze += 0.2F;
		} else {
			this.graze -= 0.2F;
		}

		if(this.graze < 0) {
			this.graze = 0;
		}

		if(this.graze > 1) {
			this.graze = 1;
		}

	}

	@Override
	public void die(Entity cause) {
		if(cause != null) {
			cause.awardKillScore(this, 10);
		}

		if(this.hasFur) {
			int drops = (int) (Math.random() + Math.random() + 1);
			for(int count = 0; count < drops; count++) {
				this.getClientLevel().addEntity(new Item(this.getClientLevel(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), VanillaBlock.WHITE_CLOTH.getId()));
			}
		}

		super.die(cause);
	}

	@Override
	public void hurt(Entity cause, int damage) {
		if(this.hasFur && cause instanceof LocalPlayer) {
			this.hasFur = false;
			int wool = (int) (Math.random() * 3 + 1);

			for(int count = 0; count < wool; count++) {
				this.getClientLevel().addEntity(new Item(this.getClientLevel(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), VanillaBlock.WHITE_CLOTH.getId()));
			}

		} else {
			super.hurt(cause, damage);
		}
	}

	@Override
	public void renderModel(float animStep, float dt, float runProgress, float yaw, float pitch, float scale) {
		AnimalModel model = (AnimalModel) modelCache.getModel(this.modelName);
		float oHeadY = model.head.y;
		float oHeadZ = model.head.z;
		model.head.y += (this.grazeO + (this.graze - this.grazeO) * dt) * 8;
		model.head.z -= this.grazeO + (this.graze - this.grazeO) * dt;
		super.renderModel(animStep, dt, runProgress, yaw, pitch, scale);
		if(this.hasFur) {
			Textures.FUR.bind();
			GL11.glDisable(GL11.GL_CULL_FACE);
			AnimalModel fur = (AnimalModel) modelCache.getModel("sheep.fur");
			fur.head.yaw = model.head.yaw;
			fur.head.pitch = model.head.pitch;
			fur.head.y = model.head.y;
			fur.head.x = model.head.x;
			fur.body.yaw = model.body.yaw;
			fur.body.pitch = model.body.pitch;
			fur.leg1.pitch = model.leg1.pitch;
			fur.leg2.pitch = model.leg2.pitch;
			fur.leg3.pitch = model.leg3.pitch;
			fur.leg4.pitch = model.leg4.pitch;
			fur.head.render(scale);
			fur.body.render(scale);
			fur.leg1.render(scale);
			fur.leg2.render(scale);
			fur.leg3.render(scale);
			fur.leg4.render(scale);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}

		model.head.y = oHeadY;
		model.head.z = oHeadZ;
	}
	
}
