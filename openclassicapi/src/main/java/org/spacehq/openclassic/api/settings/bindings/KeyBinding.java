package org.spacehq.openclassic.api.settings.bindings;

import org.spacehq.openclassic.api.OpenClassic;

/**
 * A client key binding.
 */
public class KeyBinding {

	private String name;
	
	public KeyBinding(String name, int defaultKey) {
		this.name = name;
		OpenClassic.getGame().getConfig().applyDefault(name, defaultKey);
	}
	
	/**
	 * Gets the name of the key binding.
	 * @return The binding's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the key that the binding is bound to.
	 * @return The bound key.
	 */
	public int getKey() {
		return OpenClassic.getGame().getConfig().getInteger(this.name);
	}
	
	/**
	 * Sets the key that the binding is bound to.
	 * @param The new bound key.
	 */
	public void setKey(int key) {
		OpenClassic.getGame().getConfig().setValue(this.name, key);
		OpenClassic.getGame().getConfig().save();
	}
	
}
