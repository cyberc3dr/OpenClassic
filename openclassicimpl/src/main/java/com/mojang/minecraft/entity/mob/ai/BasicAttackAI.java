package com.mojang.minecraft.entity.mob.ai;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.api.math.Vector;
import org.spacehq.openclassic.client.math.RayTracer;
import org.spacehq.openclassic.client.player.ClientPlayer;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.object.Arrow;

public class BasicAttackAI extends BasicAI {

	public int damage = 6;

	@Override
	public void update() {
		super.update();
		if(this.mob.health > 0) {
			this.doAttack();
		}
	}

	protected void doAttack() {
		if(this.attackTarget != null && this.attackTarget.removed) {
			this.attackTarget = null;
		}

		if(this.attackTarget == null) {
			float sqDistance = OpenClassic.getClient().getPlayer().getPosition().distanceSquared(mob.pos);
			if(sqDistance < 256) {
				this.attackTarget = ((ClientPlayer) OpenClassic.getClient().getPlayer()).getHandle();
			}
		}

		if(this.attackTarget != null) {
			float xDistance = OpenClassic.getClient().getPlayer().getPosition().getX() - mob.pos.getX();
			float yDistance = OpenClassic.getClient().getPlayer().getPosition().getY() - mob.pos.getY();
			float zDistance = OpenClassic.getClient().getPlayer().getPosition().getZ() - mob.pos.getZ();
			float sqDistance = xDistance * xDistance + yDistance * yDistance + zDistance * zDistance;

			if(sqDistance > 1024 && this.random.nextInt(100) == 0) {
				this.attackTarget = null;
			}

			if(this.attackTarget != null) {
				float distance = (float) Math.sqrt(sqDistance);
				this.mob.pos.setYaw((float) (Math.atan2(zDistance, xDistance) * MathHelper.DRAD_TO_DEG) - 90);
				this.mob.pos.setPitch(-((float) (Math.atan2(yDistance, (float) Math.sqrt(distance)) * MathHelper.DRAD_TO_DEG)));
				if((float) Math.sqrt(sqDistance) < 2 && this.attackDelay == 0) {
					this.attack(this.attackTarget);
				}
			}

		}
	}

	public boolean attack(Entity entity) {
		if(RayTracer.rayTrace(entity.getClientLevel(), new Vector(this.mob.pos.getZ(), this.mob.pos.getY(), this.mob.pos.getZ()), new Vector(entity.pos.getX(), entity.pos.getY(), entity.pos.getZ()), false) != null) {
			return false;
		} else {
			this.mob.attackTime = 5;
			this.attackDelay = this.random.nextInt(20) + 10;
			int damage = (int) ((this.random.nextFloat() + this.random.nextFloat()) / 2 * this.damage + 1);
			entity.hurt(this.mob, damage);
			this.noActionTime = 0;
			return true;
		}
	}

	@Override
	public void hurt(Entity cause, int damage) {
		super.hurt(cause, damage);
		if(cause instanceof Arrow) {
			cause = ((Arrow) cause).getOwner();
		}

		if(cause != null && !cause.getClass().equals(this.mob.getClass())) {
			this.attackTarget = cause;
		}
	}
	
}
