package org.spacehq.openclassic.api.gui.base;

/**
 * Represents a text box.
 */
public class PasswordTextBox extends TextBox {
	
	public PasswordTextBox(String name, int x, int y) {
		this(name, x, y, 0);
	}
	
	public PasswordTextBox(String name, int x, int y, int max) {
		this(name, x, y, 400, 40, max);
	}
	
	public PasswordTextBox(String name, int x, int y, int width, int height) {
		this(name, x, y, width, height, false);
	}
	
	public PasswordTextBox(String name, int x, int y, int width, int height, int max) {
		this(name, x, y, width, height, max, false);
	}
	
	public PasswordTextBox(String name, int x, int y, boolean chatbox) {
		this(name, x, y, 400, 40, chatbox);
	}
	
	public PasswordTextBox(String name, int x, int y, int max, boolean chatbox) {
		this(name, x, y, 400, 40, max, chatbox);
	}
	
	public PasswordTextBox(String name, int x, int y, int width, int height, boolean chatbox) {
		this(name, x, y, width, height, 0, chatbox);
	}
	
	public PasswordTextBox(String name, int x, int y, int width, int height, int max, boolean chatbox) {
		super(name, x, y, width, height, max, chatbox);
	}
	
	@Override
	public void render(float delta, int mouseX, int mouseY) {
		String text = this.getText();
		this.setText(text.replaceAll("(?s).", "*"));
		super.render(delta, mouseX, mouseY);
		this.setText(text);
	}

}
