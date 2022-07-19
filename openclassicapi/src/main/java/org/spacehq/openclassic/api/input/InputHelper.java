package org.spacehq.openclassic.api.input;

/**
 * A collection of input helper methods.
 */
public abstract class InputHelper {

	private static InputHelper helper;
	
	/**
	 * Gets the current InputHelper instance.
	 * @return The current instance.
	 */
	public static InputHelper getHelper() {
		return helper;
	}
	
	/**
	 * Sets the current InputHelper instance.
	 * @param helper The new instance.
	 */
	public static void setHelper(InputHelper helper) {
		if(InputHelper.helper != null || helper == null) return;
		InputHelper.helper = helper;
	}
	
	/**
	 * Returns true if the given key is down.
	 * @param key Key to check.
	 * @return True if the key is down.
	 */
	public abstract boolean isKeyDown(int key);

	/**
	 * Sets whether keyboard repeat events are enabled.
	 * @param enabled Whether keyboard repeat events are enabled.
	 */
	public abstract void enableRepeatEvents(boolean enabled);

	/**
	 * Gets a key name from a key id.
	 * @param key Key id to get the name of.
	 * @return The key's name.
	 */
	public abstract String getKeyName(int key);
	
}
