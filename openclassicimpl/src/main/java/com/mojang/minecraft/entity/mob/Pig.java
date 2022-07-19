package com.mojang.minecraft.entity.mob;

import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.render.Textures;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.object.Item;

public class Pig extends QuadrupedMob {

	public Pig(ClientLevel level, float x, float y, float z) {
		super(level, x, y, z, Textures.PIG);
		this.heightOffset = 1.72F;
		this.modelName = "pig";
	}

	@Override
	public void die(Entity cause) {
		if(cause != null) {
			cause.awardKillScore(this, 10);
		}

		int drops = (int) (Math.random() + Math.random() + 1);
		for(int count = 0; count < drops; count++) {
			this.getClientLevel().addEntity(new Item(this.getClientLevel(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), VanillaBlock.BROWN_MUSHROOM.getId()));
		}

		super.die(cause);
	}
	
}
