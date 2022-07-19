package com.mojang.minecraft.entity.mob.ai;

import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.math.MathHelper;

import com.mojang.minecraft.entity.mob.Sheep;

public class SheepAI extends BasicAI {

	private Sheep parent;

	public SheepAI(Sheep parent) {
		this.parent = parent;
	}

	@Override
	public void update() {
		float xDiff = -0.7F * MathHelper.sin(this.parent.pos.getYaw() * MathHelper.DEG_TO_RAD);
		float zDiff = 0.7F * MathHelper.cos(this.parent.pos.getYaw() * MathHelper.DEG_TO_RAD);
		int x = (int) (this.mob.pos.getX() + xDiff);
		int y = (int) (this.mob.pos.getY() - 2);
		int z = (int) (this.mob.pos.getZ() + zDiff);
		if(this.parent.grazing) {
			if(this.mob.getClientLevel().getBlockTypeAt(x, y, z) != VanillaBlock.GRASS) {
				this.parent.grazing = false;
			} else {
				if(this.parent.grazingTime++ == 60) {
					this.mob.getClientLevel().setBlockAt(x, y, z, VanillaBlock.DIRT);
					if(this.random.nextInt(5) == 0) {
						this.parent.hasFur = true;
					}
				}

				this.xxa = 0;
				this.yya = 0;
				this.mob.pos.setPitch(40 + this.parent.grazingTime / 2 % 2 * 10);
			}
		} else {
			if(this.mob.getClientLevel().getBlockTypeAt(x, y, z) == VanillaBlock.GRASS) {
				this.parent.grazing = true;
				this.parent.grazingTime = 0;
			}

			super.update();
		}
	}
	
}
