package org.spacehq.openclassic.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.model.Texture;

public class GameTexture implements TextureBase {

	protected URL url;
	protected BufferedImage image;
	protected int x = 0;
	protected int y = 0;
	
	protected int frameWidth = -1;
	protected int frameHeight = -1;
	protected int frameSpeed = -1;
	
	public GameTexture(URL url) {
		if(url != null) {
			this.url = url;
			this.loadImage(url);
		}
	}
	
	public GameTexture(URL url, int frameWidth, int frameHeight, int frameSpeed) {
		this.url = url;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameSpeed = frameSpeed;
		this.loadImage(url);
	}
	
	@Override
	public String getPath() {
		return this.url.getPath();
	}
	
	@Override
	public URL getURL() {
		return this.url;
	}
	
	@Override
	public int[] getRGBA() {
		BufferedImage image = this.image;
		int rgba[] = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgba, 0, image.getWidth());
		return rgba;
	}
	
	@Override
	public int getRGB(int x, int y) {
		return this.image.getRGB(x, y);
	}
	
	@Override
	public int getX() {
		return 0;
	}
	
	@Override
	public int getY() {
		return 0;
	}
	
	@Override
	public int getWidth() {
		return this.image.getWidth();
	}
	
	@Override
	public int getHeight() {
		return this.image.getHeight();
	}
	
	@Override
	public int getFullWidth() {
		return this.getWidth();
	}

	@Override
	public int getFullHeight() {
		return this.getHeight();
	}
	
	@Override
	public Texture getSubTexture(int id, int width, int height) {
		int w = (this.getWidth() / width);
		int h = (this.getHeight() / height);
		return this.getSubTexture((id % w) * w, (id / h) * h, width, height);
	}
	
	@Override
	public Texture getSubTexture(int x, int y, int width, int height) {
		return new SubTexture(x, y, width, height, this);
	}
	
	@Override
	public int getFrameWidth() {
		return this.frameWidth;
	}

	@Override
	public int getFrameHeight() {
		return this.frameHeight;
	}

	@Override
	public int getFrameSpeed() {
		return this.frameSpeed;
	}
	
	@Override
	public void bind() {
	}

	@Override
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void loadImage(URL url) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(url);
		} catch(IOException e) {
			if(e.getCause() == null || !e.getCause().getMessage().contains("Server returned HTTP response code: 403 for URL: http://s3.amazonaws.com/MinecraftSkins/")) {
				OpenClassic.getLogger().severe("Failed to read texture \"" + url.toString() + "\"");
				e.printStackTrace();
			}
			
			return;
		}
		
		this.image = image;
	}
	
}
