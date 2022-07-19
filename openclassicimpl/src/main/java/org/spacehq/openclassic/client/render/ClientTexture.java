package org.spacehq.openclassic.client.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.game.GameTexture;

public class ClientTexture extends GameTexture {
	
	private int textureId = -1;
	private boolean disposed = false;
	
	private int frameTimer = 0;
	private int currentFrame = 0;
	private BufferedImage frames[];
	private boolean dirty = false;
	
	public ClientTexture(URL url) {
		super(url);
	}
	
	public ClientTexture(URL url, int frameWidth, int frameHeight, int frameSpeed) {
		super(url, frameWidth, frameHeight, frameSpeed);
	}
	
	public ClientTexture(BufferedImage image) {
		super(null);
		this.image = image;
	}
	
	@Override
	public int[] getRGBA() {
		BufferedImage image = this.getCurrentImage();
		int rgba[] = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), rgba, 0, image.getWidth());
		return rgba;
	}
	
	@Override
	public int getRGB(int x, int y) {
		return this.getCurrentImage().getRGB(x, y);
	}
	
	@Override
	public int getWidth() {
		return this.getCurrentImage().getWidth();
	}
	
	@Override
	public int getHeight() {
		return this.getCurrentImage().getHeight();
	}
	
	@Override
	public void bind() {
		if(this.isDisposed()) {
			return;
		}
		
		if(this.textureId == -1) {
			this.textureId = this.generateTextureId();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.textureId);
	}
	
	private int generateTextureId() {
		int textureId = GL11.glGenTextures();
		this.bind(this.getCurrentImage(), textureId);
		return textureId;
	}
	
	private void bind(BufferedImage image, int textureId) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				int red = (pixel >> 16) & 0xFF;
				int blue = pixel & 0xFF;
				int green = (pixel >> 8) & 0xFF;
				int alpha = (pixel >> 24) & 0xFF;
				if(OpenClassic.getClient().getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
					green = (red * 30 + green * 70) / 100;
					blue = (red * 30 + blue * 70) / 100;
					red = (red * 30 + green * 59 + blue * 11) / 100;
				}

				buffer.put((byte) red);
				buffer.put((byte) green);
				buffer.put((byte) blue);
				buffer.put((byte) alpha);
			}
		}

		buffer.flip();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.smoothing").getValue() && RenderHelper.getHelper().getMipmapMode() != MipmapMode.NONE) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 2);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			switch(RenderHelper.getHelper().getMipmapMode()) {
				case GL30:
					GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
					break;
				case FRAMEBUFFER_EXT:
					EXTFramebufferObject.glGenerateMipmapEXT(GL11.GL_TEXTURE_2D);
					break;
			}
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
	}
	
	public void resetTextureId() {
		this.textureId = -1;
		this.dirty = true;
	}
	
	private BufferedImage getCurrentImage() {
		return this.frames != null ? this.frames[this.currentFrame] : this.image;
	}
	
	public void update() {
		if(this.frames != null) {
			this.frameTimer++;
			if(this.frameTimer >= this.frameSpeed) {
				this.frameTimer = 0;
				this.currentFrame++;
				if(this.currentFrame >= this.frames.length) {
					this.currentFrame = 0;
				}
				
				this.dirty = true;
			}
		}
	}
	
	public void renderUpdate() {
		if(this.dirty && this.getCurrentImage() != null) {
			if(this.textureId == -1) {
				this.textureId = this.generateTextureId();
			} else {
				this.bind(this.getCurrentImage(), this.textureId);
			}
			
			this.dirty = false;
		}
	}
	
	public boolean isDisposed() {
		return this.disposed;
	}
	
	public void dispose() {
		GL11.glDeleteTextures(this.textureId);
		this.disposed = true;
	}
	
	public void reload() {
		this.loadImage(this.url);
		this.resetTextureId();
	}
	
	@Override
	public void loadImage(URL url) {
		String pack = OpenClassic.getGame().getConfig().getString("options.resource-pack");
		if(!pack.equals("none")) {
			File f = new File(OpenClassic.getClient().getDirectory(), "resourcepacks/" + pack);
			ZipFile zip = null;
			try {
				zip = new ZipFile(f);
				String p = url.getPath();
				if(p.contains("!")) {
					p = p.substring(p.indexOf("!") + 1);
				}
				
				p = p.startsWith("/") ? p.substring(1, p.length()) : p;
				if(zip.getEntry(p) != null) {
					url = new URL("zip:" + f.toURI().toURL().toString() + "!/" + p);
				}
			} catch(IOException e) {
				OpenClassic.getLogger().severe("Failed to read resource pack.");
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(zip);
			}
		}

		super.loadImage(url);
		if(this.frameSpeed > -1) {
			this.frames = new BufferedImage[(this.image.getWidth() / frameWidth) * (this.image.getHeight() / frameHeight)];
			int count = 0;
			for(int x = 0; x < this.image.getWidth(); x += frameWidth) {
				for(int y = 0; y < this.image.getHeight(); y += frameHeight) {
					this.frames[count] = this.image.getSubimage(x, y, frameWidth, frameHeight);
					count++;
				}
			}
		}
	}
	
}
