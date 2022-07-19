package org.spacehq.openclassic.api.gui.base;

import org.spacehq.openclassic.api.gui.GuiComponent;

/**
 * A box that fades between two colors.
 */
public class FadingBox extends GuiComponent {

	private int color;
	private int fadeTo;
	
	public FadingBox(String name, int x, int y, int width, int height, int color, int fadeTo) {
		super(name, x, y, width, height);
		this.color = color;
		this.fadeTo = fadeTo;
	}
	
	/**
	 * Gets the color of this fading box.
	 * @return The fading box's color.
	 */
	public int getColor() {
		return this.color;
	}
	
	/**
	 * Gets the color that this fading box fades to.
	 * @return The fading box's color to fade to.
	 */
	public int getFadeColor() {
		return this.fadeTo;
	}
	
	@Override
	public void render(float delta, int mouseX, int mouseY) {
		ComponentHelper.getHelper().renderFadingBox(this);
	}

}
