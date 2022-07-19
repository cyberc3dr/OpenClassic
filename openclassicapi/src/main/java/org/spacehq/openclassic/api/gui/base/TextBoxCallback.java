package org.spacehq.openclassic.api.gui.base;

/**
 * A callback for reacting to text box typing.
 */
public interface TextBoxCallback {

	/**
	 * Called when a text box is typed in.
	 * @param box Text box being typed in.
	 * @param oldText The text before the box was typed in.
	 */
	public void onType(TextBox box, String oldText);
	
}
