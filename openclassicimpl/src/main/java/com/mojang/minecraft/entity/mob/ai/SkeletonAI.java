package com.mojang.minecraft.entity.mob.ai;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.player.ClientPlayer;

import com.mojang.minecraft.entity.mob.Mob;
import com.mojang.minecraft.entity.mob.Skeleton;
import com.mojang.minecraft.entity.object.Arrow;

public class SkeletonAI extends BasicAttackAI {

	private Skeleton parent;

	public SkeletonAI(Skeleton parent) {
		this.parent = parent;
	}

	@Override
	public void tick(ClientLevel level, Mob mob) {
		super.tick(level, mob);
		if(mob.health > 0 && this.random.nextInt(30) == 0 && this.attackTarget != null) {
			this.parent.shootArrow(level);
		}
	}

	@Override
	public void beforeRemove() {
		int arrows = (int) ((Math.random() + Math.random()) * 3 + 4);

		for(int count = 0; count < arrows; count++) {
			this.parent.getClientLevel().addEntity(new Arrow(this.parent.getClientLevel(), ((ClientPlayer) OpenClassic.getClient().getPlayer()).getHandle(), this.parent.pos.getX(), this.parent.pos.getY() - 0.2F, this.parent.pos.getZ(), (float) Math.random() * 360, -((float) Math.random()) * 60, 0.4F));
		}
	}
	
}
