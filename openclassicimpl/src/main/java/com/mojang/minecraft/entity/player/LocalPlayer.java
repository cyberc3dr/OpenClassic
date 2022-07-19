package com.mojang.minecraft.entity.player;

import java.util.List;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.event.player.PlayerMoveEvent;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.game.util.InternalConstants;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.model.HumanoidModel;
import com.mojang.minecraft.entity.object.Item;
import com.zachsthings.onevent.EventManager;

public class LocalPlayer extends Player {

	public InputHandler input = new InputHandler();
	public float oBob;
	public float bob;
	public boolean speedHack = false;

	public LocalPlayer(ClientLevel level, ClientPlayer openclassic) {
		super(level, 0, 0, 0, openclassic);
		if(level != null) {
			level.removeEntity(this);
			level.addEntity(this);
		}

		this.heightOffset = 1.62F;
		this.health = 20;
		this.modelName = "humanoid";
		this.rotOffs = 180.0F;
		this.ai = new LocalPlayerAI(this);
	}

	@Override
	public void resetPos() {
		this.resetPos(null);
	}

	@Override
	public void resetPos(Position pos) {
		this.deathTime = 0;
		this.heightOffset = 1.62F;
		this.setSize(0.6F, 1.8F);
		super.resetPos(pos);
		this.deathTime = 0;
	}

	@Override
	public void aiStep() {
		this.oBob = this.bob;
		this.input.updateMovement();
		super.aiStep();
		float bob = (float) Math.sqrt(this.xd * this.xd + this.zd * this.zd);
		float tilt = (float) Math.atan((-this.yd * 0.2F)) * 15.0F;
		if(bob > 0.1F) {
			bob = 0.1F;
		}

		if(!this.onGround || this.health <= 0) {
			bob = 0.0F;
		}

		if(this.onGround || this.health <= 0) {
			tilt = 0.0F;
		}

		this.bob += (bob - this.bob) * 0.4F;
		this.tilt += (tilt - this.tilt) * 0.8F;

		List<Entity> entities = this.getClientLevel().findEntities(this, this.bb.grow(1, 0, 1));
		if(this.health > 0 && entities != null) {
			for(Entity entity : entities) {
				entity.playerTouch(this);
			}
		}
	}

	@Override
	public void render(float dt) {
	}
	
	public HumanoidModel getModel() {
		return (HumanoidModel) modelCache.getModel(this.modelName);
	}

	@Override
	public void die(Entity cause) {
		this.setSize(0.2F, 0.2F);
		this.setPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
		this.yd = 0.1F;
		if(cause != null) {
			this.xd = -MathHelper.cos((this.hurtDir + this.pos.getYaw()) * MathHelper.DEG_TO_RAD) * 0.1F;
			this.zd = -MathHelper.sin((this.hurtDir + this.pos.getYaw()) * MathHelper.DEG_TO_RAD) * 0.1F;
		} else {
			this.xd = this.zd = 0.0F;
		}

		for(int slot = 0; slot < this.inventory.slots.length; slot++) {
			if(this.inventory.slots[slot] != -1) {
				this.getClientLevel().addEntity(new Item(this.getClientLevel(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.inventory.slots[slot], this.inventory.count[slot]));
			}
		}

		this.heightOffset = 0.1F;
	}

	@Override
	public void remove() {
	}

	@Override
	public boolean isShootable() {
		return true;
	}

	@Override
	public void hurt(Entity entity, int damage) {
		if(OpenClassic.getClient().isInSurvival()) {
			OpenClassic.getGame().getAudioManager().playSound("random.hurt", this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1, (this.getClientLevel().getRandom().nextFloat() - this.getClientLevel().getRandom().nextFloat()) * 0.2F + 1.0F);
			super.hurt(entity, damage);
		}
	}

	@Override
	public boolean isCreativeModeAllowed() {
		return true;
	}

	@Override
	public void moveHeading(float forward, float strafe, float speed) {
		if(((ClassicClient) OpenClassic.getClient()).getHacks() && OpenClassic.getClient().getHackSettings().getBooleanSetting("hacks.speed").getValue() && this.speedHack) {
			super.moveHeading(forward, strafe, 2.5F);
		} else {
			super.moveHeading(forward, strafe, speed);
		}
	}
	
	@Override
	public void turn(float yaw, float pitch) {
		this.pos.setYaw((float) (this.pos.getYaw() + yaw * InternalConstants.SENSITIVITY_VALUE[OpenClassic.getClient().getSettings().getIntSetting("options.sensitivity").getValue()]));
		this.pos.setPitch((float) (this.pos.getPitch() - pitch * InternalConstants.SENSITIVITY_VALUE[OpenClassic.getClient().getSettings().getIntSetting("options.sensitivity").getValue()]));
	}

	@Override
	public void moveTo(float x, float y, float z, float yaw, float pitch) {
		Position to = new Position(this.pos.getLevel(), x, y, z, (byte) yaw, (byte) pitch);
		PlayerMoveEvent event = EventManager.callEvent(new PlayerMoveEvent(this.openclassic, this.pos, to));
		if(event.isCancelled()) {
			return;
		}

		super.moveTo(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ(), event.getTo().getYaw(), event.getTo().getPitch());
	}

	public void dropHeldItem() {
		int id = this.inventory.slots[this.inventory.selected];
		int amount = this.inventory.count[this.inventory.selected];
		if(id > 0 && amount > 0) {
			if(amount > 1) {
				this.inventory.count[this.inventory.selected]--;
			} else {
				this.inventory.slots[this.inventory.selected] = -1;
				this.inventory.count[this.inventory.selected] = 0;
			}
			
			Item item = new Item(this.getClientLevel(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), id, 1);
			item.delay = 40;
			item.xd = MathHelper.sin((this.pos.getYaw() / 180) * (float) Math.PI) * MathHelper.cos((this.pos.getPitch() / 180) * (float) Math.PI) * 0.3f;
			item.zd = -MathHelper.cos((this.pos.getYaw() / 180) * (float) Math.PI) * MathHelper.cos((this.pos.getPitch() / 180) * (float) Math.PI) * 0.3f;
			item.yd = -MathHelper.sin((this.pos.getPitch() / 180) * (float) Math.PI) * 0.3f + 0.1f;
			float mod = this.getClientLevel().getRandom().nextFloat() * (float) Math.PI * 2;
			float off = 0.02f * this.getClientLevel().getRandom().nextFloat();
			item.xd += Math.cos(mod) * off;
			item.yd += (this.getClientLevel().getRandom().nextFloat() - this.getClientLevel().getRandom().nextFloat()) * 0.1f;
			item.zd += Math.sin(mod) * off;
			this.getClientLevel().addEntity(item);
		}
	}

	@Override
	public byte getPlayerId() {
		return -1;
	}

}
