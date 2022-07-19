package com.mojang.minecraft.entity.model;

import org.spacehq.openclassic.api.math.MathHelper;

public class SpiderModel extends Model {

	private ModelPart head = new ModelPart(32, 4);
	private ModelPart neck;
	private ModelPart body;
	private ModelPart leg1;
	private ModelPart leg2;
	private ModelPart leg3;
	private ModelPart leg4;
	private ModelPart leg5;
	private ModelPart leg6;
	private ModelPart leg7;
	private ModelPart leg8;

	public SpiderModel() {
		this.head.setBounds(-4, -4, -8, 8, 8, 8, 0);
		this.head.setPosition(0, 0, -3);
		this.neck = new ModelPart(0, 0);
		this.neck.setBounds(-3, -3, -3, 6, 6, 6, 0);
		this.body = new ModelPart(0, 12);
		this.body.setBounds(-5, -4, -6, 10, 8, 12, 0);
		this.body.setPosition(0, 0, 9);
		this.leg1 = new ModelPart(18, 0);
		this.leg1.setBounds(-15, -1, -1, 16, 2, 2, 0);
		this.leg1.setPosition(-4, 0, 2);
		this.leg2 = new ModelPart(18, 0);
		this.leg2.setBounds(-1, -1, -1, 16, 2, 2, 0);
		this.leg2.setPosition(4, 0, 2);
		this.leg3 = new ModelPart(18, 0);
		this.leg3.setBounds(-15, -1, -1, 16, 2, 2, 0);
		this.leg3.setPosition(-4, 0, 1);
		this.leg4 = new ModelPart(18, 0);
		this.leg4.setBounds(-1, -1, -1, 16, 2, 2, 0);
		this.leg4.setPosition(4, 0, 1);
		this.leg5 = new ModelPart(18, 0);
		this.leg5.setBounds(-15, -1, -1, 16, 2, 2, 0);
		this.leg5.setPosition(-4, 0, 0);
		this.leg6 = new ModelPart(18, 0);
		this.leg6.setBounds(-1, -1, -1, 16, 2, 2, 0);
		this.leg6.setPosition(4, 0, 0);
		this.leg7 = new ModelPart(18, 0);
		this.leg7.setBounds(-15, -1, -1, 16, 2, 2, 0);
		this.leg7.setPosition(-4, 0, -1);
		this.leg8 = new ModelPart(18, 0);
		this.leg8.setBounds(-1, -1, -1, 16, 2, 2, 0);
		this.leg8.setPosition(4, 0, -1);
	}

	@Override
	public void render(float animStep, float runProgress, float dt, float yaw, float pitch, float scale) {
		this.head.yaw = yaw / 57.295776F;
		this.head.pitch = pitch / 57.295776F;
		this.leg1.roll = -0.7853982F;
		this.leg2.roll = 0.7853982F;
		this.leg3.roll = -0.7853982F * 0.74F;
		this.leg4.roll = 0.7853982F * 0.74F;
		this.leg5.roll = -0.7853982F * 0.74F;
		this.leg6.roll = 0.7853982F * 0.74F;
		this.leg7.roll = -0.7853982F;
		this.leg8.roll = 0.7853982F;
		this.leg1.yaw = 0.3926991F * 2;
		this.leg2.yaw = -0.3926991F * 2;
		this.leg3.yaw = 0.3926991F;
		this.leg4.yaw = -0.3926991F;
		this.leg5.yaw = -0.3926991F;
		this.leg6.yaw = 0.3926991F;
		this.leg7.yaw = -0.3926991F * 2;
		this.leg8.yaw = 0.3926991F * 2;
		float frontYaw = -(MathHelper.cos(animStep * 0.6F * 2) * 0.4F) * runProgress;
		float middleYaw1 = -(MathHelper.cos(animStep * 0.6F * 2 + MathHelper.PI) * 0.4F) * runProgress;
		float middleYaw2 = -(MathHelper.cos(animStep * 0.6F * 2 + MathHelper.HALF_PI) * 0.4F) * runProgress;
		float backYaw = -(MathHelper.cos(animStep * 0.6F * 2 + MathHelper.ONE_AND_HALF_PI) * 0.4F) * runProgress;
		float frontRoll = Math.abs(MathHelper.sin(animStep * 0.6F) * 0.4F) * runProgress;
		float middleRoll1 = Math.abs(MathHelper.sin(animStep * 0.6F + MathHelper.PI) * 0.4F) * runProgress;
		float middleRoll2 = Math.abs(MathHelper.sin(animStep * 0.6F + MathHelper.HALF_PI) * 0.4F) * runProgress;
		float backRoll = Math.abs(MathHelper.sin(animStep * 0.6F + MathHelper.ONE_AND_HALF_PI) * 0.4F) * runProgress;
		this.leg1.yaw += frontYaw;
		this.leg2.yaw -= frontYaw;
		this.leg3.yaw += middleYaw1;
		this.leg4.yaw -= middleYaw1;
		this.leg5.yaw += middleYaw2;
		this.leg6.yaw -= middleYaw2;
		this.leg7.yaw += backYaw;
		this.leg8.yaw -= backYaw;
		this.leg1.roll += frontRoll;
		this.leg2.roll -= frontRoll;
		this.leg3.roll += middleRoll1;
		this.leg4.roll -= middleRoll1;
		this.leg5.roll += middleRoll2;
		this.leg6.roll -= middleRoll2;
		this.leg7.roll += backRoll;
		this.leg8.roll -= backRoll;
		this.head.render(scale);
		this.neck.render(scale);
		this.body.render(scale);
		this.leg1.render(scale);
		this.leg2.render(scale);
		this.leg3.render(scale);
		this.leg4.render(scale);
		this.leg5.render(scale);
		this.leg6.render(scale);
		this.leg7.render(scale);
		this.leg8.render(scale);
	}
	
}
