package com.mojang.minecraft.entity.model;

import org.spacehq.openclassic.api.math.MathHelper;

public class ZombieModel extends HumanoidModel {

	@Override
	public void setRotationAngles(float animStep, float runProgress, float dt, float yaw, float pitch, float scale) {
		super.setRotationAngles(animStep, runProgress, dt, yaw, pitch, scale);
		float armYaw = MathHelper.sin(this.attackTime * MathHelper.PI);
		float armPitch = MathHelper.sin((1 - (1 - this.attackTime) * (1 - this.attackTime)) * MathHelper.PI);
		this.rightArm.roll = 0;
		this.leftArm.roll = 0;
		this.rightArm.yaw = -(0.1F - armYaw * 0.6F);
		this.leftArm.yaw = 0.1F - armYaw * 0.6F;
		this.rightArm.pitch = -MathHelper.HALF_PI;
		this.leftArm.pitch = -MathHelper.HALF_PI;
		this.rightArm.pitch -= armYaw * 1.2F - armPitch * 0.4F;
		this.leftArm.pitch -= armYaw * 1.2F - armPitch * 0.4F;
		this.rightArm.roll += MathHelper.cos(dt * 0.09F) * 0.05F + 0.05F;
		this.leftArm.roll -= MathHelper.cos(dt * 0.09F) * 0.05F + 0.05F;
		this.rightArm.pitch += MathHelper.sin(dt * 0.067F) * 0.05F;
		this.leftArm.pitch -= MathHelper.sin(dt * 0.067F) * 0.05F;
	}
	
}
