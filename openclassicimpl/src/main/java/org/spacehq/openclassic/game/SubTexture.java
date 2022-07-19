package org.spacehq.openclassic.game;

import java.awt.image.BufferedImage;
import java.net.URL;

import org.spacehq.openclassic.api.block.model.Texture;

public class SubTexture implements TextureBase {

	private int x;
	private int y;
	private int width;
	private int height;
	private TextureBase parent;

	public SubTexture(int x, int y, int width, int height, TextureBase parent) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.parent = parent;
	}
	
	@Override
	public String getPath() {
		return this.parent.getPath();
	}

	@Override
	public URL getURL() {
		return this.parent.getURL();
	}
	
	@Override
	public int[] getRGBA() {
		int rgba[] = new int[this.width * this.height];
		this.parent.getImage().getRGB(this.parent.getX() + this.x, this.parent.getY() + this.y, this.width, this.height, rgba, 0, this.width);
		return rgba;
	}
	
	@Override
	public int getRGB(int x, int y) {
		return this.parent.getRGB(this.x + x, this.y + y);
	}

	@Override
	public int getX() {
		return this.x + this.parent.getX();
	}

	@Override
	public int getY() {
		return this.y + this.parent.getY();
	}
	
	@Override
	public int getWidth() {
		return this.width;
	}
	
	@Override
	public int getHeight() {
		return this.height;
	}
	
	@Override
	public int getFullWidth() {
		return this.parent.getFullWidth();
	}

	@Override
	public int getFullHeight() {
		return this.parent.getFullHeight();
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
		return this.parent.getFrameWidth();
	}

	@Override
	public int getFrameHeight() {
		return this.parent.getFrameHeight();
	}

	@Override
	public int getFrameSpeed() {
		return this.parent.getFrameSpeed();
	}

	@Override
	public void bind() {
		this.parent.bind();
	}

	@Override
	public BufferedImage getImage() {
		return this.parent.getImage();
	}

	public Texture getParent() {
		return this.parent;
	}
	
}
