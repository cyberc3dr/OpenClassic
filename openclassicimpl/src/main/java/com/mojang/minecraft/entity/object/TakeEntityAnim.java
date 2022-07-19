package com.mojang.minecraft.entity.object;

import org.spacehq.openclassic.client.level.ClientLevel;

import com.mojang.minecraft.entity.Entity;

public class TakeEntityAnim extends Entity {

	private int time = 0;
	private Entity taking;
	private Entity player;
	private float xorg;
	private float yorg;
	private float zorg;

	public TakeEntityAnim(ClientLevel level, Entity taking, Entity player) {
		super(level);
		this.taking = taking;
		this.player = player;
		this.setSize(1, 1);
		this.xorg = taking.pos.getX();
		this.yorg = taking.pos.getY();
		this.zorg = taking.pos.getZ();
	}

	@Override
	public void tick() {
		super.tick();
		this.time++;
		if(this.time >= 3) {
			this.remove();
		}

		float distance = (this.time / 3) * (this.time / 3);
		this.pos.set(this.xorg + (this.player.pos.getX() - this.xorg) * distance, this.yorg + (this.player.pos.getY() - 1 - this.yorg) * distance, this.zorg + (this.player.pos.getZ() - this.zorg) * distance);
		this.taking.pos.set(this.pos);
		this.setPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
	}

	@Override
	public void render(float dt) {
		this.taking.render(dt);
	}
	
}
