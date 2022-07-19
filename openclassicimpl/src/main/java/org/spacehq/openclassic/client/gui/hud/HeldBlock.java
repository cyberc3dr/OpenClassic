package org.spacehq.openclassic.client.gui.hud;

import org.lwjgl.opengl.GL11;

import com.mojang.minecraft.entity.model.ModelPart;
import com.mojang.minecraft.entity.player.LocalPlayer;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.client.player.ClientPlayer;

public class HeldBlock {

	private BlockType block = null;
	private float heldPosition = 0;
	private float lastPosition = 0;
	private int heldOffset = 0;
	private boolean moving = false;
	
	public BlockType getBlock() {
		return this.block;
	}

	public void setHeldPosition(int pos) {
		this.heldPosition = pos;
	}
	
	public void move() {
		this.heldOffset = -1;
		this.moving = true;
	}
	
	public void render(float delta) {
		if(OpenClassic.getClient().getLevel() == null) {
			return;
		}
		
		GL11.glPushMatrix();
		if(this.moving) {
			float off = (this.heldOffset + delta) / 7F;
			float offsin = MathHelper.sin(off * MathHelper.PI);
			GL11.glTranslatef(-MathHelper.sin((float) Math.sqrt(off) * MathHelper.PI) * 0.4F, MathHelper.sin((float) Math.sqrt(off) * MathHelper.TWO_PI) * 0.2F, -offsin * 0.2F);
		}

		float heldPos = this.lastPosition + (this.heldPosition - this.lastPosition) * delta;
		GL11.glTranslatef(0.7F * 0.8F, -0.65F * 0.8F - (1 - heldPos) * 0.6F, -0.9F * 0.8F);
		GL11.glRotatef(45, 0, 1, 0);
		GL11.glEnable(GL11.GL_NORMALIZE);
		if(this.moving) {
			float off = (this.heldOffset + delta) / 7F;
			float offsin = MathHelper.sin((off) * off * MathHelper.PI);
			GL11.glRotatef(MathHelper.sin((float) Math.sqrt(off) * MathHelper.PI) * 80, 0, 1, 0);
			GL11.glRotatef(-offsin * 20, 1, 0, 0);
		}

		Position playerPos = OpenClassic.getClient().getPlayer().getPosition();
		float brightness = OpenClassic.getClient().getLevel().getBrightness(playerPos.getBlockX(), playerPos.getBlockY(), playerPos.getBlockZ());
		if(OpenClassic.getClient().getHUD().isVisible()) {
			if(this.block != null) {
				GL11.glScalef(0.4F, 0.4F, 0.4F);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				this.block.getModel().renderAll(0, 0, 0, brightness);
			} else {
				((ClientPlayer) OpenClassic.getClient().getPlayer()).getHandle().bindTexture();
				GL11.glScalef(1, -1, -1);
				GL11.glTranslatef(0, 0.2F, 0);
				GL11.glRotatef(-120, 0, 0, 1);
				GL11.glScalef(1, 1, 1);
				GL11.glColor4f(brightness, brightness, brightness, 1);
				ModelPart arm = ((LocalPlayer) ((ClientPlayer) OpenClassic.getClient().getPlayer()).getHandle()).getModel().leftArm;
				if(!arm.hasList) {
					arm.generateList(0.0625F);
				}

				GL11.glCallList(arm.list);
				GL11.glColor4f(1, 1, 1, 1);
			}
		}

		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glPopMatrix();
	}

	public void tick(BlockType held) {
		this.lastPosition = this.heldPosition;
		if(this.moving) {
			this.heldOffset++;
			if(this.heldOffset == 7) {
				this.heldOffset = 0;
				this.moving = false;
			}
		}
		
		float position = (held == this.block ? 1 : 0) - this.heldPosition;
		if(position < -0.4F) {
			position = -0.4F;
		}

		if(position > 0.4F) {
			position = 0.4F;
		}

		this.heldPosition += position;
		if(this.heldPosition < 0.1F) {
			this.block = held;
		}
	}

}
