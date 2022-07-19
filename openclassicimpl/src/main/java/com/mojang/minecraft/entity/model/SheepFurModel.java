package com.mojang.minecraft.entity.model;

public class SheepFurModel extends AnimalModel {

	public SheepFurModel() {
		super(12);
		this.head = new ModelPart(0, 0);
		this.head.setBounds(-3, -4, -4, 6, 6, 6, 0.6F);
		this.head.setPosition(0, 6, -8);
		this.body = new ModelPart(28, 8);
		this.body.setBounds(-4, -10, -7, 8, 16, 6, 1.75F);
		this.body.setPosition(0, 5, 2);
		this.leg1 = new ModelPart(0, 16);
		this.leg1.setBounds(-2, 0, -2, 4, 6, 4, 0.5F);
		this.leg1.setPosition(-3, 12, 7);
		this.leg2 = new ModelPart(0, 16);
		this.leg2.setBounds(-2, 0, -2, 4, 6, 4, 0.5F);
		this.leg2.setPosition(3, 12, 7);
		this.leg3 = new ModelPart(0, 16);
		this.leg3.setBounds(-2, 0, -2, 4, 6, 4, 0.5F);
		this.leg3.setPosition(-3, 12, -5);
		this.leg4 = new ModelPart(0, 16);
		this.leg4.setBounds(-2, 0, -2, 4, 6, 4, 0.5F);
		this.leg4.setPosition(3, 12, -5);
	}
	
}
