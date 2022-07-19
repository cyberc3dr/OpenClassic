package org.spacehq.openclassic.api.gui.base;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.input.Mouse;

/**
 * Represents a button.
 */
public class Button extends GuiComponent {
	
	private String text;
	private boolean active = true;
	private ButtonCallback callback;

	public Button(String name, int x, int y, String text) {
		this(name, x, y, 400, 40, text);
	}
	
	public Button(String name, int x, int y, int width, int height, String text) {
		super(name, x, y, width, height);
		this.text = text;
	}
	
	/**
	 * Gets the button's text.
	 * @return The button's text.
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Sets the button's text.
	 * @param text Text to set.
	 */
	public void setText(String text) {
		this.text = text;
		if(this.text == null) this.text = "";
	}
	
	/**
	 * Returns true if this button is active.
	 * @return True if the button is active.
	 */
	public boolean isActive() {
		return this.active;
	}
	
	/**
	 * Sets whether the button is active.
	 * @param active Whether the button is active.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * Gets the callback of this button.
	 * @return This button's callback.
	 */
	public ButtonCallback getCallback() {
		return this.callback;
	}
	
	/**
	 * Sets the callback of this button.
	 * @param callback Callback of this button.
	 * @return This button.
	 */
	public Button setCallback(ButtonCallback callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public void onMouseClick(int x, int y, int button) {
		if(button != Mouse.LEFT_BUTTON || !this.isActive()) {
			return;
		}
		
		OpenClassic.getGame().getAudioManager().playSound("random.click", 1, 1);
		if(this.getCallback() != null) {
			this.getCallback().onButtonClick(this);
		}
	}
	
	@Override
	public void render(float delta, int mouseX, int mouseY) {
		ComponentHelper.getHelper().renderButton(this, mouseX, mouseY);
	}

}
