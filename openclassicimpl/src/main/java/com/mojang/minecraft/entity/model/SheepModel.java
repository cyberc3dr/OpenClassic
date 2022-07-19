package com.mojang.minecraft.entity.model;

public class SheepModel extends AnimalModel {

	public SheepModel() {
		super(12);
		this.head = new ModelPart(0, 0);
		this.head.setBounds(-3, -4, -6, 6, 6, 8, 0);
		this.head.setPosition(0, 6, -8);
		this.body = new ModelPart(28, 8);
		this.body.setBounds(-4, -10, -7, 8, 16, 6, 0);
		this.body.setPosition(0, 5, 2);
	}
	
}
