package org.spacehq.openclassic.client.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.client.render.ClientTexture;
import org.spacehq.openclassic.client.render.RenderHelper;

public class Minimap extends GuiComponent {

	private BufferedImage texture;
	private ClientTexture tex;

	public Minimap(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
		this.texture = new BufferedImage(width - 2, height - 2, BufferedImage.TYPE_INT_ARGB);
		this.tex = new ClientTexture(this.texture);
	}

	@Override
	public void render(float delta, int mouseX, int mouseY) {
		if(OpenClassic.getClient().getLevel() == null) {
			return;
		}
		
		this.updateTexture();
		RenderHelper.getHelper().drawBox(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), Color.black.getRGB());
		RenderHelper.getHelper().drawTexture(this.tex, this.getX() + 1, this.getY() + 1, 1);
	}

	private void updateTexture() {
		if(OpenClassic.getClient().getLevel() == null) return;
		int imgX = 0;
		int imgY = 0;
		int scale = 2;
		int xrad = this.texture.getWidth() / scale / 2;
		int zrad = this.texture.getHeight() / scale / 2;
		for(int x = OpenClassic.getClient().getPlayer().getPosition().getBlockX() - xrad; x <= OpenClassic.getClient().getPlayer().getPosition().getBlockX() + xrad; x++) {
			for(int z = OpenClassic.getClient().getPlayer().getPosition().getBlockZ() - zrad; z <= OpenClassic.getClient().getPlayer().getPosition().getBlockZ() + zrad; z++) {
				int y = OpenClassic.getClient().getLevel().getHighestBlockY(x, z);
				BlockType type = OpenClassic.getClient().getLevel().getBlockTypeAt(x, y, z);
				while(!type.isOpaque() && !type.getPreventsRendering()) {
					int old = y;
					y = OpenClassic.getClient().getLevel().getHighestBlockY(x, z, y - 1);
					if(y == old || y < 0) {
						type = null;
						break;
					}

					type = OpenClassic.getClient().getLevel().getBlockTypeAt(x, y, z);
				}

				int rgb = type == null ? 0 : this.getRGB(type);
				for(int w = 0; w < scale; w++) {
					for(int h = 0; h < scale; h++) {
						if(imgX + w < this.texture.getWidth() && imgY + h < this.texture.getHeight()) {
							if(type != null) {
								this.texture.setRGB(imgX + w, imgY + h, rgb);
								this.tex.resetTextureId();
							} else {
								this.texture.setRGB(imgX + w, imgY + h, 0);
								this.tex.resetTextureId();
							}
						}
					}
				}

				imgY += scale;
			}

			imgY = 0;
			imgX += scale;
		}

		for(int w = 0; w < scale; w++) {
			for(int h = 0; h < scale; h++) {
				this.texture.setRGB((this.texture.getWidth() / 2) + w, (this.texture.getHeight() / 2) + h, Color.orange.getRGB());
				this.tex.resetTextureId();
			}
		}
	}

	// TODO: Possible to base more on block texture?
	private int getRGB(BlockType type) {
		if(type == null || type.getModel() == null || type.getModel().getQuads().size() == 0) {
			return 0;
		}
		
		int quad = 1;
		if(type.getModel().getQuads().size() < 2) {
			quad = 0;
		}
		
		Texture tex = type.getModel().getQuad(quad).getTexture();
		if(tex == null) {
			return 0;
		}
		
		return tex.getRGB(0, 0);
	}

}
