package org.spacehq.openclassic.api.gui.base;

/**
 * A callback for reacting to button clicks.
 */
public interface ButtonCallback {

	/**
	 * Called when a button with this listener is clicked.
	 * @param button Button that was clicked.
	 */
	public void onButtonClick(Button button);
	
}
