package com.mojang.minecraft.entity.mob;

import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.render.Textures;

import com.mojang.minecraft.entity.mob.ai.BasicAttackAI;

public class Zombie extends HumanoidMob {

	public Zombie(ClientLevel level, float x, float y, float z) {
		this(level, x, y, z, "/textures/entity/mob/zombie.png");
	}
	
	public Zombie(ClientLevel level, float x, float y, float z, String texture) {
		super(level, x, y, z, Textures.ZOMBIE);
		this.modelName = "zombie";
		this.heightOffset = 1.62F;
		this.deathScore = 80;
		this.ai = new BasicAttackAI();
		this.ai.defaultLookAngle = 30;
		((BasicAttackAI) this.ai).runSpeed = 1;
	}
	
}
