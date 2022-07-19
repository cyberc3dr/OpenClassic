package com.mojang.minecraft.entity.player;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.client.render.Textures;
import org.spacehq.openclassic.client.util.AsyncTextureDownload;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.mob.HumanoidMob;

public abstract class Player extends HumanoidMob {

	public Texture skin;
	private AsyncTextureDownload download;
	
	public Inventory inventory = new Inventory();
	public int score = 0;
	public int arrows = 20;
	
	public ClientPlayer openclassic;

	public Player(ClientLevel level, float x, float y, float z, ClientPlayer openclassic) {
		super(level, x, y, z);
		this.armor = false;
		this.helmet = false;
		this.openclassic = openclassic;
		
		String name = this.getName();
		if(name.contains("@")) {
			name = name.substring(0, name.indexOf('@'));
		}

		String url = "http://s3.amazonaws.com/MinecraftSkins/" + Color.stripColor(name) + ".png";
		this.download = new AsyncTextureDownload(url);
		OpenClassic.getGame().getScheduler().scheduleAsyncTask(this, this.download);
	}
	
	public String getName() {
		return this.openclassic.getName() != null ? this.openclassic.getName() : "Player";
	}

	@Override
	public void bindTexture() {
		if(this.download != null && this.download.hasTexture()) {
			this.skin = this.download.getTexture();
			int[] imageData = this.skin.getRGBA();
			boolean hair = false;
			for(int index = 0; index < imageData.length; index++) {
				if(imageData[index] >>> 24 < 128) {
					hair = true;
					break;
				}
			}
			
			this.hasHair = hair;
			this.download = null;
		}

		if(this.skin == null) {
			Textures.DEFAULT_SKIN.bind();
		} else {
			this.skin.bind();
		}
	}
	
	public int getScore() {
		return this.score;
	}
	
	@Override
	public void awardKillScore(Entity killed, int score) {
		this.score += score;
	}
	
	public abstract byte getPlayerId();

}
