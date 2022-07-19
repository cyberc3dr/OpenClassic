package org.spacehq.openclassic.api.gui.base;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.input.InputHelper;
import org.spacehq.openclassic.api.input.Keyboard;

/**
 * Represents a text box.
 */
public class TextBox extends GuiComponent {

	private static final String ALLOWED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ,.:;-_\'*!\"#@%$/()=+?[]{}<>^";
	
	protected String text = "";
	protected int cursor = 0;
	protected boolean blink = true;
	protected int blinkDelay = 6;
	protected int max;
	protected boolean chatbox;
	private TextBoxCallback callback;
	
	public TextBox(String name, int x, int y) {
		this(name, x, y, 0);
	}
	
	public TextBox(String name, int x, int y, int max) {
		this(name, x, y, 400, 40, max);
	}
	
	public TextBox(String name, int x, int y, int width, int height) {
		this(name, x, y, width, height, false);
	}
	
	public TextBox(String name, int x, int y, int width, int height, int max) {
		this(name, x, y, width, height, max, false);
	}
	
	public TextBox(String name, int x, int y, boolean chatbox) {
		this(name, x, y, 400, 40, chatbox);
	}
	
	public TextBox(String name, int x, int y, int max, boolean chatbox) {
		this(name, x, y, 400, 40, max, chatbox);
	}
	
	public TextBox(String name, int x, int y, int width, int height, boolean chatbox) {
		this(name, x, y, width, height, 0, chatbox);
	}
	
	public TextBox(String name, int x, int y, int width, int height, int max, boolean chatbox) {
		super(name, x, y, width, height);
		this.chatbox = chatbox;
		this.max = max;
	}
	
	@Override
	public void onAttached(GuiComponent parent) {
		InputHelper.getHelper().enableRepeatEvents(true);
	}
	
	@Override
	public void onRemoved(GuiComponent parent) {
		InputHelper.getHelper().enableRepeatEvents(false);
	}
	
	/**
	 * Gets the text in this text box.
	 * @return The text box's text.
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Sets the text in this text box.
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
		if(this.text == null) this.text = "";
		
		this.cursor = text.length();
	}
	
	/**
	 * Gets whether this text box is rendered as a chat box.
	 * @return Whether this text box is a chat box.
	 */
	public boolean isChatBox() {
		return this.chatbox;
	}
	
	/**
	 * Gets the cursor position of this text box.
	 * @return The cursor position.
	 */
	public int getCursorPosition() {
		return this.cursor;
	}
	
	/**
	 * Gets the blinking state of this text box.
	 * @return The blinking state of this text box.
	 */
	public boolean getBlinkState() {
		return this.blink;
	}
	
	/**
	 * Gets the callback of this text box.
	 * @return This text box's callback.
	 */
	public TextBoxCallback getCallback() {
		return this.callback;
	}
	
	/**
	 * Sets the callback of this text box.
	 * @param callback Callback of this text box.
	 * @return This text box.
	 */
	public TextBox setCallback(TextBoxCallback callback) {
		this.callback = callback;
		return this;
	}
	
	@Override
	public void update(int mouseX, int mouseY) {
		if(!this.isFocused()) {
			if(this.blinkDelay != 10 && !this.blink) {
				this.blink = true;
			}
		} else {
			this.blinkDelay--;
			if(this.blinkDelay <= 0) {
				this.blink = !this.blink;
				this.blinkDelay = 6;
			}
		}
	}
	
	@Override
	public void onKeyPress(char c, int key) {
		if (key == Keyboard.KEY_BACK && this.text.length() > 0 && this.cursor > 0) {
			String old = this.text;
			this.text = this.text.substring(0, this.cursor - 1) + this.text.substring(this.cursor);
			this.cursor--;
			if(this.callback != null) {
				this.callback.onType(this, old);
			}
		}
		
		if(key == Keyboard.KEY_V && InputHelper.getHelper().isKeyDown(Keyboard.KEY_LCONTROL)) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable transfer = clipboard.getContents(null);
			if(transfer != null) {
				try {
					String old = this.text;
					for(char ch : ((String) transfer.getTransferData(DataFlavor.stringFlavor)).toCharArray()) {
						if (ALLOWED.indexOf(ch) >= 0) {
							this.text = this.text.substring(0, this.cursor) + ch + this.text.substring(this.cursor, this.text.length());
							this.cursor++;
						}
					}
					
					if(!old.equals(this.text)) {
						if(this.callback != null) {
							this.callback.onType(this, old);
						}
					}
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(key == Keyboard.KEY_LEFT && this.cursor > 0) {
			this.cursor--;
		}
		
		if(key == Keyboard.KEY_RIGHT && this.cursor < this.text.length()) {
			this.cursor++;
		}

		if (ALLOWED.indexOf(c) >= 0 && !((this.chatbox && this.text.length() >= 64) || (this.max > 0 && this.text.length() >= this.max))) {
			String old = this.text;
			this.text = this.text.substring(0, this.cursor) + c + this.text.substring(this.cursor, this.text.length());
			this.cursor++;
			if(this.callback != null) {
				this.callback.onType(this, old);
			}
		}
	}
	
	@Override
	public void render(float delta, int mouseX, int mouseY) {
		ComponentHelper.getHelper().renderTextBox(this);
	}

}
