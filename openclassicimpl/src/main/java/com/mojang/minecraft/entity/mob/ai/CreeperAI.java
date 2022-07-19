package com.mojang.minecraft.entity.mob.ai;

import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.client.level.particle.TerrainParticle;

import com.mojang.minecraft.entity.Entity;

public class CreeperAI extends BasicAttackAI {

	@Override
	public boolean attack(Entity entity) {
		if(super.attack(entity)) {
			this.mob.hurt(entity, 6);
			return true;
		}

		return false;
	}

	@Override
	public void beforeRemove() {
		this.mob.getClientLevel().explode(this.mob, this.mob.pos.getX(), this.mob.pos.getY(), this.mob.pos.getZ(), 4);
		for(int count = 0; count < 500; count++) {
			float particleX = (float) this.random.nextGaussian();
			float particleY = (float) this.random.nextGaussian();
			float particleZ = (float) this.random.nextGaussian();
			float len = (float) Math.sqrt(particleX * particleX + particleY * particleY + particleZ * particleZ);
			float xd = particleX / len / len;
			float yd = particleY / len / len;
			float zd = particleZ / len / len;
			this.mob.getClientLevel().getParticleManager().spawnParticle(new TerrainParticle(this.mob.pos.clone().add(particleX, particleY, particleZ), xd, yd, zd, VanillaBlock.LEAVES));
		}
	}
	
}
