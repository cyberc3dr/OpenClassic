package com.mojang.minecraft.entity.mob;

import org.lwjgl.opengl.GL11;

import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.render.Textures;

import com.mojang.minecraft.entity.model.HumanoidModel;

public class HumanoidMob extends Mob {

	public boolean helmet = Math.random() < 0.2D;
	public boolean armor = Math.random() < 0.2D;

	public HumanoidMob(ClientLevel level, float x, float y, float z) {
		this(level, x, y, z, Textures.DEFAULT_SKIN);
	}
	
	public HumanoidMob(ClientLevel level, float x, float y, float z, Texture texture) {
		super(level, texture);
		this.modelName = "humanoid";
		this.setPos(x, y, z);
	}

	@Override
	public void renderModel(float animStep, float dt, float runProgress, float yaw, float pitch, float scale) {
		super.renderModel(animStep, dt, runProgress, yaw, pitch, scale);
		HumanoidModel model = (HumanoidModel) modelCache.getModel(this.modelName);
		GL11.glDisable(GL11.GL_CULL_FACE);
		if(this.hasHair) {
			model.hair.yaw = model.head.yaw;
			model.hair.pitch = model.head.pitch;
			model.hair.render(scale);
		}

		if(this.armor || this.helmet) {
			Textures.ARMOR.bind();
			HumanoidModel armored = (HumanoidModel) modelCache.getModel("humanoid.armor");
			armored.head.render = this.helmet;
			armored.body.render = this.armor;
			armored.rightArm.render = this.armor;
			armored.leftArm.render = this.armor;
			armored.rightLeg.render = false;
			armored.leftLeg.render = false;
			armored.head.yaw = model.head.yaw;
			armored.head.pitch = model.head.pitch;
			armored.rightArm.pitch = model.rightArm.pitch;
			armored.rightArm.roll = model.rightArm.roll;
			armored.leftArm.pitch = model.leftArm.pitch;
			armored.leftArm.roll = model.leftArm.roll;
			armored.rightLeg.pitch = model.rightLeg.pitch;
			armored.leftLeg.pitch = model.leftLeg.pitch;
			armored.head.render(scale);
			armored.body.render(scale);
			armored.rightArm.render(scale);
			armored.leftArm.render(scale);
			armored.rightLeg.render(scale);
			armored.leftLeg.render(scale);
		}
		
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
}
