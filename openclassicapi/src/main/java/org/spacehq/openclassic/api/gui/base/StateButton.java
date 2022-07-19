package org.spacehq.openclassic.api.gui.base;

/**
 * Represents a button with a state. The text in the button displays as so: "Text: State"
 */
public class StateButton extends Button {

	private String state;
	
	public StateButton(String name, int x, int y, String text) {
		super(name, x, y, 400, 40, text);
	}
	
	public StateButton(String name, int x, int y, int width, int height, String text) {
		super(name, x, y, width, height, text);
	}
	
	/**
	 * Gets the state of the button.
	 * @return The button's state.
	 */
	public String getState() {
		return this.state;
	}
	
	/**
	 * Sets the state of this button.
	 * @param state The new state of this button.
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public void render(float delta, int mouseX, int mouseY) {
		String text = this.getText();
		this.setText(text + ": " + this.getState());
		super.render(delta, mouseX, mouseY);
		this.setText(text);
	}
	
}
