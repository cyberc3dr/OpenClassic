package com.mojang.minecraft.entity.object;

import org.lwjgl.opengl.GL11;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.client.level.ClientLevel;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.player.LocalPlayer;

public class Item extends Entity {
	
	public float xd;
	public float yd;
	public float zd;
	private int resource;
	private int tickCount;
	private int age = 0;
	private int count = 0;
	public int delay = 10;

	public Item(ClientLevel level, float x, float y, float z, int block) {
		this(level, x, y, z, block, 1);
	}

	public Item(ClientLevel level, float x, float y, float z, int block, int count) {
		super(level);
		this.setSize(0.25F, 0.25F);
		this.heightOffset = this.bbHeight / 2;
		this.setPos(x, y, z);
		this.resource = block;
		this.count = count;
		this.pos.setYaw((float) (Math.random() * 360));
		this.xd = (float) (Math.random() * 0.2D - 0.1D);
		this.yd = 0.2F;
		this.zd = (float) (Math.random() * 0.2D - 0.1D);
		this.makeStepSound = false;
	}

	@Override
	public void tick() {
		super.tick();
		this.yd -= 0.04F;
		this.move(this.xd, this.yd, this.zd);
		this.xd *= 0.98F;
		this.yd *= 0.98F;
		this.zd *= 0.98F;
		if(this.onGround) {
			this.xd *= 0.7F;
			this.zd *= 0.7F;
			this.yd *= -0.5F;
		}

		this.tickCount++;
		this.age++;
		this.delay--;
		if(this.age >= 6000) {
			this.remove();
		}
		
		if(this.getLiquid() != null && this.getLiquid().getLiquidName().equals("lava")) {
			OpenClassic.getGame().getAudioManager().playSound("random.fizz", this.pos.getX(), this.pos.getY(), this.pos.getZ(), 0.4f, 2 + this.getClientLevel().getRandom().nextFloat() * 0.4f);
			this.remove();
		}
	}

	@Override
	public void render(float dt) {
		float rot = this.pos.getYaw() + (this.tickCount + dt) * 3;
		GL11.glPushMatrix();
		float rsin = MathHelper.sin(rot / 10);
		float bob = rsin * 0.1F + 0.1F;
		GL11.glTranslatef(this.pos.getInterpolatedX(dt), this.pos.getInterpolatedY(dt) + bob, this.pos.getInterpolatedZ(dt));
		GL11.glRotatef(rot, 0, 1, 0);
		GL11.glTranslatef(-0.1F, 0, -0.1F);
		Blocks.fromId(this.resource).getModel().renderScaled(0, 0, 0, 0.2F, 1);
		GL11.glPopMatrix();
	}

	@Override
	public void playerTouch(LocalPlayer player) {
		if(!this.removed && this.delay <= 0 && player.inventory.addResource(this.resource, this.count)) {
			OpenClassic.getGame().getAudioManager().playSound("random.pop", player.pos.getX(), player.pos.getY(), player.pos.getZ(), 0.2f, ((this.getClientLevel().getRandom().nextFloat() - this.getClientLevel().getRandom().nextFloat()) * 0.7f + 1) * 2);
			this.getClientLevel().addEntity(new TakeEntityAnim(this.getClientLevel(), this, player));
			this.remove();
		}
	}

}
