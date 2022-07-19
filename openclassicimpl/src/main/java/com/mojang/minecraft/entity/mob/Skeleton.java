package com.mojang.minecraft.entity.mob;

import org.spacehq.openclassic.client.level.ClientLevel;

import com.mojang.minecraft.entity.mob.ai.BasicAttackAI;
import com.mojang.minecraft.entity.mob.ai.SkeletonAI;
import com.mojang.minecraft.entity.object.Arrow;

public class Skeleton extends Zombie {

	public Skeleton(ClientLevel level, float x, float y, float z) {
		super(level, x, y, z, "/textures/entity/mob/skeleton.png");
		this.modelName = "skeleton";
		this.deathScore = 120;
		BasicAttackAI ai = new SkeletonAI(this);
		ai.runSpeed = 0.3F;
		ai.damage = 8;
		this.ai = ai;
	}

	public void shootArrow(ClientLevel level) {
		level.addEntity(new Arrow(level, this, this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getYaw() + 180 + (float) (Math.random() * 45 - 22.5), this.pos.getPitch() - (float) (Math.random() * 45 - 10), 1));
	}

}
