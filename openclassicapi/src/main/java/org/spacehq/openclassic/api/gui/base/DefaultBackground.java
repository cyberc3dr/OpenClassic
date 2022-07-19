package org.spacehq.openclassic.api.gui.base;

import org.spacehq.openclassic.api.gui.GuiComponent;

/**
 * The default background of the client implementation.
 */
public class DefaultBackground extends GuiComponent {

	private boolean autoScale = false;
	
	public DefaultBackground(String name) {
		super(name, 0, 0, 0, 0);
		this.autoScale = true;
	}
	
	public DefaultBackground(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
	}
	
	@Override
	public void onAttached(GuiComponent parent) {
		if(this.autoScale) {
			this.setSize(parent.getWidth(), parent.getHeight());
		}
	}
	
	@Override
	public void render(float delta, int mouseX, int mouseY) {
		ComponentHelper.getHelper().renderDefaultBackground(this);
	}

}
