package com.mojang.minecraft.entity.player;

import com.mojang.minecraft.entity.mob.ai.BasicAI;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.client.ClassicClient;

public class LocalPlayerAI extends BasicAI {

	private LocalPlayer parent;

	public LocalPlayerAI(LocalPlayer parent) {
		this.parent = parent;
	}

	@Override
	public void update() {
		this.jumping = this.parent.input.jumping;
		this.flyDown = this.parent.input.flyDown;
		this.parent.speedHack = this.parent.input.speed;
		this.xxa = this.parent.input.xxa;
		this.yya = this.parent.input.yya;
		if(((ClassicClient) OpenClassic.getClient()).getHacks() && this.parent.input.toggleFly && OpenClassic.getClient().getHackSettings().getBooleanSetting("hacks.flying").getValue()) {
			this.flying = !this.flying;
			if(this.flying) {
				this.mob.yd = 0;
			}
		}

		if(!((ClassicClient) OpenClassic.getClient()).getHacks() || !OpenClassic.getClient().getHackSettings().getBooleanSetting("hacks.flying").getValue()) {
			this.flying = false;
		}

		this.parent.input.toggleFly = false;
	}
	
}
