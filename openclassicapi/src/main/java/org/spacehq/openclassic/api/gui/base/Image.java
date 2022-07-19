package org.spacehq.openclassic.api.gui.base;

import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.gui.GuiComponent;

/**
 * A widget that displays a texture.
 */
public class Image extends GuiComponent {

	private Texture tex;
	
	public Image(String name, int x, int y, Texture tex) {
		this(name, x, y, tex.getWidth(), tex.getHeight(), tex);
	}
	
	public Image(String name, int x, int y, int width, int height, Texture tex) {
		super(name, x, y, width, height);
		this.tex = tex;
	}
	
	/**
	 * Gets the texture being displayed.
	 * @return The texture being displayed.
	 */
	public Texture getTexture() {
		return this.tex;
	}
	
	/**
	 * Sets the texture being displayed.
	 * @param tex The new texture being displayed.
	 */
	public void setTexture(Texture tex, boolean resizeToImage) {
		this.tex = tex;
		if(resizeToImage) {
			this.setSize(tex.getWidth(), tex.getHeight());
		}
	}
	
	@Override
	public void render(float delta, int mouseX, int mouseY) {
		ComponentHelper.getHelper().renderImage(this);
	}

}
