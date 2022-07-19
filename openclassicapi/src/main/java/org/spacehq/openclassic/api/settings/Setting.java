package org.spacehq.openclassic.api.settings;

/**
 * A setting.
 */
public abstract class Setting {

	private String name;
	
	public Setting(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name of this setting.
	 * @return The setting's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets whether this setting is visible.
	 * @return Whether this setting is visible.
	 */
	public boolean isVisible() {
		return true;
	}
	
	/**
	 * Gets the string value of this setting.
	 * @return The setting's string value.
	 */
	public abstract String getStringValue();
	
	/**
	 * Toggles this setting.
	 */
	public abstract void toggle();
	
}
