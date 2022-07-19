package com.mojang.minecraft.entity.model;

import org.spacehq.openclassic.api.math.MathHelper;

public class AnimalModel extends Model {

	public ModelPart head = new ModelPart(0, 0);
	public ModelPart body;
	public ModelPart leg1;
	public ModelPart leg2;
	public ModelPart leg3;
	public ModelPart leg4;

	public AnimalModel(int baseY) {
		this.head.setBounds(-4, -4, -8, 8, 8, 8, 0);
		this.head.setPosition(0, (18 - baseY), -6);
		this.body = new ModelPart(28, 8);
		this.body.setBounds(-5, -10, -7, 10, 16, 8, 0);
		this.body.setPosition(0, (17 - baseY), 2);
		this.leg1 = new ModelPart(0, 16);
		this.leg1.setBounds(-2, 0, -2, 4, baseY, 4, 0);
		this.leg1.setPosition(-3, (24 - baseY), 7);
		this.leg2 = new ModelPart(0, 16);
		this.leg2.setBounds(-2, 0, -2, 4, baseY, 4, 0);
		this.leg2.setPosition(3, (24 - baseY), 7);
		this.leg3 = new ModelPart(0, 16);
		this.leg3.setBounds(-2, 0, -2, 4, baseY, 4, 0);
		this.leg3.setPosition(-3, (24 - baseY), -5);
		this.leg4 = new ModelPart(0, 16);
		this.leg4.setBounds(-2, 0, -2, 4, baseY, 4, 0);
		this.leg4.setPosition(3, (24 - baseY), -5);
	}

	@Override
	public void render(float animStep, float runProgress, float dt, float yaw, float pitch, float scale) {
		this.head.yaw = yaw / 57.295776F;
		this.head.pitch = pitch / 57.295776F;
		this.body.pitch = MathHelper.HALF_PI;
		this.leg1.pitch = MathHelper.cos(animStep * 0.6F) * 1.4F * runProgress;
		this.leg2.pitch = MathHelper.cos(animStep * 0.6F + MathHelper.PI) * 1.4F * runProgress;
		this.leg3.pitch = MathHelper.cos(animStep * 0.6F + MathHelper.PI) * 1.4F * runProgress;
		this.leg4.pitch = MathHelper.cos(animStep * 0.6F) * 1.4F * runProgress;
		this.head.render(scale);
		this.body.render(scale);
		this.leg1.render(scale);
		this.leg2.render(scale);
		this.leg3.render(scale);
		this.leg4.render(scale);
	}
}
