package org.spacehq.openclassic.api.gui.base;

import org.spacehq.openclassic.api.gui.GuiComponent;

/**
 * A translucent background, as seen in the Ingame Menu Screen.
 */
public class TranslucentBackground extends GuiComponent {

	private boolean autoScale = false;
	
	public TranslucentBackground(String name) {
		super(name, 0, 0, 0, 0);
		this.autoScale = true;
	}
	
	public TranslucentBackground(String name, int x, int y, int width, int height) {
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
		ComponentHelper.getHelper().renderTranslucentBackground(this);
	}

}
