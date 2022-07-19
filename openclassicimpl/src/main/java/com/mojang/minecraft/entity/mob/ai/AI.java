package com.mojang.minecraft.entity.mob.ai;

import org.spacehq.openclassic.client.level.ClientLevel;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.mob.Mob;

public abstract class AI {

	public int defaultLookAngle = 0;

	public abstract void tick(ClientLevel level, Mob mob);

	public abstract void beforeRemove();

	public abstract void hurt(Entity cause, int damage);
	
}
