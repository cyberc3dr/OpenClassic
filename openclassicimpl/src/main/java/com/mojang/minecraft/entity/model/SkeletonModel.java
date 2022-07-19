package com.mojang.minecraft.entity.model;

public class SkeletonModel extends ZombieModel {

	public SkeletonModel() {
		this.rightArm = new ModelPart(40, 16);
		this.rightArm.setBounds(-1, -2, -1, 2, 12, 2, 0);
		this.rightArm.setPosition(-5, 2, 0);
		this.leftArm = new ModelPart(40, 16);
		this.leftArm.mirror = true;
		this.leftArm.setBounds(-1, -2, -1, 2, 12, 2, 0);
		this.leftArm.setPosition(5, 2, 0);
		this.rightLeg = new ModelPart(0, 16);
		this.rightLeg.setBounds(-1, 0, -1, 2, 12, 2, 0);
		this.rightLeg.setPosition(-2, 12, 0);
		this.leftLeg = new ModelPart(0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.setBounds(-1, 0, -1, 2, 12, 2, 0);
		this.leftLeg.setPosition(2, 12, 0);
	}
	
}
