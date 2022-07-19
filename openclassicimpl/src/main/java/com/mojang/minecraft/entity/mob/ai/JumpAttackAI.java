package com.mojang.minecraft.entity.mob.ai;

public class JumpAttackAI extends BasicAttackAI {

	public JumpAttackAI() {
		this.runSpeed *= 0.8F;
	}

	@Override
	protected void jumpFromGround() {
		if(this.attackTarget == null) {
			super.jumpFromGround();
		} else {
			this.mob.xd = 0;
			this.mob.zd = 0;
			this.mob.moveHeading(0, 1, 0.6F);
			this.mob.yd = 0.5F;
		}
	}
	
}
