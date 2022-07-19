package com.mojang.minecraft.entity.player.net;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.client.render.ClientTexture;
import org.spacehq.openclassic.client.render.RenderHelper;

import com.mojang.minecraft.entity.player.Player;

public class NetworkPlayer extends Player {

	private List<PositionUpdate> moveQueue = new LinkedList<PositionUpdate>();
	public int playerId;
	private int xp;
	private int yp;
	private int zp;
	public String displayName;

	public NetworkPlayer(ClientLevel level, int playerId, String displayName, ClientPlayer openclassic, float x, float y, float z, float yaw, float pitch) {
		super(level, x, y, z, openclassic);
		this.playerId = playerId;
		this.displayName = displayName;
		this.xp = (int) (x * 32);
		this.yp = (int) (y * 32);
		this.zp = (int) (z * 32);
		this.heightOffset = 0;
		this.pushthrough = 0.8F;
		this.pos.setPitch(pitch);
		this.pos.setYaw(yaw);
		this.renderOffset = 0.6875F;
		this.allowAlpha = false;
	}

	@Override
	public void aiStep() {
		int steps = 5;
		while(steps-- > 0 && this.moveQueue.size() > 10) {
			if(this.moveQueue.size() > 0) {
				this.setPos(this.moveQueue.remove(0));
			}
		}

		this.onGround = true;
	}
	
	public void setPos(PositionUpdate pos) {
		if(pos.position) {
			this.setPos(pos.x, pos.y, pos.z);
		} else {
			this.setPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
		}

		if(pos.rotation) {
			this.setRot(pos.yaw, pos.pitch);
		} else {
			this.setRot(this.pos.getYaw(), this.pos.getPitch());
		}
	}

	@Override
	public void renderHoverOver(float dt) {
		GL11.glPushMatrix();
		GL11.glTranslatef(this.pos.getInterpolatedX(dt), this.pos.getInterpolatedY(dt) + 0.8F + this.renderOffset, this.pos.getInterpolatedZ(dt));
		GL11.glRotatef(-OpenClassic.getClient().getPlayer().getPosition().getYaw(), 0, 1, 0);
		GL11.glScalef(0.05F, -0.05F, 0.05F);
		GL11.glTranslatef((-RenderHelper.getHelper().getStringWidth(this.displayName)) / 2F, 0, 0);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_COLOR_BUFFER_BIT);
		RenderHelper.getHelper().renderText(this.displayName, 0, 0, 16777215, false);
		GL11.glDepthFunc(GL11.GL_GREATER);
		GL11.glDepthMask(false);
		GL11.glColor4f(1, 1, 1, 0.8f);
		RenderHelper.getHelper().renderText(this.displayName, 0, 0, 16777215, false);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glTranslatef(1, 1, -0.05F);
		RenderHelper.getHelper().renderText(this.displayName, 0, 0, 5263440, false);
		GL11.glEnable(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPopMatrix();
	}
	
	public void teleport(short x, short y, short z, float yaw, float pitch) {
		float diffYaw = yaw - this.pos.getYaw();
		float diffPitch = pitch - this.pos.getPitch();
		while(diffYaw >= 180) {
			diffYaw -= 360;
		}

		while(diffYaw < -180) {
			diffYaw += 360;
		}

		while(diffPitch >= 180) {
			diffPitch -= 360;
		}

		while(diffPitch < -180) {
			diffPitch += 360;
		}

		float newYaw = this.pos.getYaw() + diffYaw * 0.5F;
		float newPitch = this.pos.getPitch() + diffPitch * 0.5F;
		this.moveQueue.add(new PositionUpdate((this.xp + x) / 64.0F, (this.yp + y) / 64.0F, (this.zp + z) / 64.0F, newYaw, newPitch));
		this.xp = x;
		this.yp = y;
		this.zp = z;
		this.moveQueue.add(new PositionUpdate(this.xp / 32.0F, this.yp / 32.0F, this.zp / 32.0F, yaw, pitch));
	}

	public void queue(byte xChange, byte yChange, byte zChange, float yaw, float pitch) {
		float diffYaw = yaw - this.pos.getYaw();
		float diffPitch = pitch - this.pos.getPitch();
		while(diffYaw >= 180) {
			diffYaw -= 360;
		}

		while(diffYaw < -180) {
			diffYaw += 360;
		}

		while(diffPitch >= 180) {
			diffPitch -= 360;
		}

		while(diffPitch < -180) {
			diffPitch += 360;
		}

		float newYaw = this.pos.getYaw() + diffYaw * 0.5F;
		float newPitch = this.pos.getPitch() + diffPitch * 0.5F;
		this.moveQueue.add(new PositionUpdate((this.xp + xChange / 2.0F) / 32.0F, (this.yp + yChange / 2.0F) / 32.0F, (this.zp + zChange / 2.0F) / 32.0F, newYaw, newPitch));
		this.xp += xChange;
		this.yp += yChange;
		this.zp += zChange;
		this.moveQueue.add(new PositionUpdate(this.xp / 32.0F, this.yp / 32.0F, this.zp / 32.0F, yaw, pitch));
	}

	public void queue(byte xChange, byte yChange, byte zChange) {
		this.moveQueue.add(new PositionUpdate((this.xp + xChange / 2.0F) / 32.0F, (this.yp + yChange / 2.0F) / 32.0F, (this.zp + zChange / 2.0F) / 32.0F));
		this.xp += xChange;
		this.yp += yChange;
		this.zp += zChange;
		this.moveQueue.add(new PositionUpdate(this.xp / 32.0F, this.yp / 32.0F, this.zp / 32.0F));
	}

	public void queue(float yaw, float pitch) {
		float diffYaw = yaw - this.pos.getYaw();
		float diffPitch = pitch - this.pos.getPitch();
		while(diffYaw >= 180) {
			diffYaw -= 360;
		}

		while(diffYaw < -180) {
			diffYaw += 360;
		}

		while(diffPitch >= 180) {
			diffPitch -= 360;
		}

		while(diffPitch < -180) {
			diffPitch += 360;
		}

		float newYaw = this.pos.getYaw() + diffYaw * 0.5F;
		float newPitch = this.pos.getPitch() + diffPitch * 0.5F;
		this.moveQueue.add(new PositionUpdate(newYaw, newPitch));
		this.moveQueue.add(new PositionUpdate(yaw, pitch));
	}

	public void clear() {
		if(this.skin != null) {
			((ClientTexture) this.skin).dispose();
		}
	}

	@Override
	public byte getPlayerId() {
		return (byte) this.playerId;
	}

}
