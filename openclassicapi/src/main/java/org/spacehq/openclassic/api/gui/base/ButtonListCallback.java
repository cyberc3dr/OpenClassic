package org.spacehq.openclassic.api.gui.base;

/**
 * A callback for reacting to button list clicks.
 */
public interface ButtonListCallback {

	/**
	 * Called when a button belonging to a ButtonList widget with this listener is clicked.
	 * @param list ButtonList the button belongs to.
	 * @param button Button that was clicked.
	 */
	public void onButtonListClick(ButtonList list, Button button);
	
}
