package com.mojang.minecraft.entity.model;

import org.spacehq.openclassic.api.math.MathHelper;

public class HumanoidModel extends Model {

	public ModelPart head;
	public ModelPart hair;
	public ModelPart body;
	public ModelPart rightArm;
	public ModelPart leftArm;
	public ModelPart rightLeg;
	public ModelPart leftLeg;

	public HumanoidModel() {
		this(0);
	}

	public HumanoidModel(float offset) {
		this.head = new ModelPart(0, 0);
		this.head.setBounds(-4, -8, -4, 8, 8, 8, offset);
		this.hair = new ModelPart(32, 0);
		this.hair.setBounds(-4, -8, -4, 8, 8, 8, offset + 0.5F);
		this.body = new ModelPart(16, 16);
		this.body.setBounds(-4, 0, -2, 8, 12, 4, offset);
		this.rightArm = new ModelPart(40, 16);
		this.rightArm.setBounds(-3, -2, -2, 4, 12, 4, offset);
		this.rightArm.setPosition(-5, 2, 0);
		this.leftArm = new ModelPart(40, 16);
		this.leftArm.mirror = true;
		this.leftArm.setBounds(-1, -2, -2, 4, 12, 4, offset);
		this.leftArm.setPosition(5, 2, 0);
		this.rightLeg = new ModelPart(0, 16);
		this.rightLeg.setBounds(-2, 0, -2, 4, 12, 4, offset);
		this.rightLeg.setPosition(-2, 12, 0);
		this.leftLeg = new ModelPart(0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.setBounds(-2, 0, -2, 4, 12, 4, offset);
		this.leftLeg.setPosition(2, 12, 0);
	}

	@Override
	public void render(float animStep, float runProgress, float dt, float yaw, float pitch, float scale) {
		this.setRotationAngles(animStep, runProgress, dt, yaw, pitch, scale);
		this.head.render(scale);
		this.body.render(scale);
		this.rightArm.render(scale);
		this.leftArm.render(scale);
		this.rightLeg.render(scale);
		this.leftLeg.render(scale);
	}

	public void setRotationAngles(float animStep, float runProgress, float dt, float yaw, float pitch, float scale) {
		this.head.yaw = yaw / 57.295776F;
		this.head.pitch = pitch / 57.295776F;
		this.rightArm.pitch = MathHelper.cos(animStep * 0.6F + MathHelper.PI) * 2 * runProgress;
		this.rightArm.roll = (MathHelper.cos(animStep * 0.2F) + 1) * runProgress;
		this.leftArm.pitch = MathHelper.cos(animStep * 0.6F) * 2 * runProgress;
		this.leftArm.roll = (MathHelper.cos(animStep * 0.3F) - 1) * runProgress;
		this.rightLeg.pitch = MathHelper.cos(animStep * 0.6F) * 1.4F * runProgress;
		this.leftLeg.pitch = MathHelper.cos(animStep * 0.6F + MathHelper.PI) * 1.4F * runProgress;
		this.rightArm.roll += MathHelper.cos(dt * 0.09F) * 0.05F + 0.05F;
		this.leftArm.roll -= MathHelper.cos(dt * 0.09F) * 0.05F + 0.05F;
		this.rightArm.pitch += MathHelper.sin(dt * 0.07F) * 0.05F;
		this.leftArm.pitch -= MathHelper.sin(dt * 0.07F) * 0.05F;
	}
}
