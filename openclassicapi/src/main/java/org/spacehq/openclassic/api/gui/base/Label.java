package org.spacehq.openclassic.api.gui.base;

import org.spacehq.openclassic.api.gui.GuiComponent;

/**
 * A text label.
 */
public class Label extends GuiComponent {

	private String text;
	private boolean xCenter;
	private boolean scaled;
	private int ox;
	
	public Label(String name, int x, int y, String text) {
		this(name, x, y, text, false);
	}
	
	public Label(String name, int x, int y, String text, boolean xCenter) {
		this(name, x, y, text, xCenter, false);
	}

	public Label(String name, int x, int y, String text, boolean xCenter, boolean scaled) {
		super(name, x, y, ComponentHelper.getHelper().getStringWidth(text, scaled), 8);
		if(xCenter) {
			this.setPos(x - ComponentHelper.getHelper().getStringWidth(text, scaled) / 2, y);
		}
		
		this.ox = x;
		this.text = text;
		this.xCenter = xCenter;
		this.scaled = scaled;
	}
	
	/**
	 * Gets the text in this label.
	 * @return The text in this label.
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Gets whether the text should be centered on the x coordinate of the widget.
	 */
	public boolean useXCenter() {
		return this.xCenter;
	}
	
	/**
	 * Gets whether the text is scaled up 2x or not.
	 * @return Whether the text is scaled.
	 */
	public boolean isScaled() {
		return this.scaled;
	}
	
	/**
	 * Sets the text in this label.
	 * @param text The new text in this label.
	 */
	public void setText(String text) {
		this.text = text;
		if(this.xCenter) {
			this.setPos(this.ox - ComponentHelper.getHelper().getStringWidth(text, this.scaled) / 2, this.getY());
		}
	}
	
	@Override
	public void render(float delta, int mouseX, int mouseY) {
		ComponentHelper.getHelper().renderLabel(this);
	}

}
