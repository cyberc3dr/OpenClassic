package org.spacehq.openclassic.client.render;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.TextureFactory;

public class ClientTextureFactory extends TextureFactory {

	private List<ClientTexture> textures = new ArrayList<ClientTexture>();
	
	@Override
	public Texture newTexture(URL url) {
		for(ClientTexture tex : this.textures) {
			if(tex.getURL().equals(url)) {
				return tex;
			}
		}
		
		ClientTexture texture = new ClientTexture(url);
		texture.resetTextureId();
		this.textures.add(texture);
		return texture;
	}
	
	@Override
	public Texture newTexture(URL url, int frameWidth, int frameHeight, int frameSpeed) {
		for(ClientTexture tex : this.textures) {
			if(tex.getURL().equals(url)) {
				return tex;
			}
		}
		
		ClientTexture texture = new ClientTexture(url, frameWidth, frameHeight, frameSpeed);
		texture.resetTextureId();
		this.textures.add(texture);
		return texture;
	}
	
	public void updateTextures() {
		for(ClientTexture texture : new ArrayList<ClientTexture>(this.textures)) {
			if(texture.isDisposed()) {
				this.textures.remove(texture);
			}
			
			texture.update();
		}
	}
	
	public void renderUpdateTextures() {
		for(ClientTexture texture : new ArrayList<ClientTexture>(this.textures)) {
			if(texture.isDisposed()) {
				this.textures.remove(texture);
			}
			
			texture.renderUpdate();
		}
	}
	
	public void resetTextures() {
		for(ClientTexture texture : this.textures) {
			texture.resetTextureId();
		}
	}
	
	public void reloadTextures() {
		for(ClientTexture texture : this.textures) {
			texture.reload();
		}
	}

}
