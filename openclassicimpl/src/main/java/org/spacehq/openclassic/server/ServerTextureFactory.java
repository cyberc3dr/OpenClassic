package org.spacehq.openclassic.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.TextureFactory;
import org.spacehq.openclassic.game.GameTexture;

public class ServerTextureFactory extends TextureFactory {

	private List<GameTexture> textures = new ArrayList<GameTexture>();
	
	@Override
	public Texture newTexture(URL url) {
		GameTexture texture = new GameTexture(url);
		this.textures.add(texture);
		return texture;
	}
	
	@Override
	public Texture newTexture(URL url, int frameWidth, int frameHeight, int frameSpeed) {
		GameTexture texture = new GameTexture(url, frameWidth, frameHeight, frameSpeed);
		this.textures.add(texture);
		return texture;
	}

}
