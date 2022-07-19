package org.spacehq.openclassic.api.settings;

import org.spacehq.openclassic.api.OpenClassic;

/**
 * A boolean setting.
 */
public class BooleanSetting extends Setting {

	public BooleanSetting(String name) {
		super(name);
	}
	
	/**
	 * Gets this setting's value.
	 * @return The setting's value.
	 */
	public boolean getValue() {
		return OpenClassic.getGame().getConfig().getBoolean(this.getName(), false);
	}
	
	/**
	 * Sets this setting's value.
	 * @param The setting's value.
	 */
	public void setValue(boolean value) {
		OpenClassic.getGame().getConfig().setValue(this.getName(), value);
		OpenClassic.getGame().getConfig().save();
	}
	
	/**
	 * Applies a default value to this setting if it is not set.
	 * @param The setting's default value.
	 */
	public void setDefault(boolean value) {
		if(!OpenClassic.getGame().getConfig().contains(this.getName())) {
			OpenClassic.getGame().getConfig().setValue(this.getName(), value);
			OpenClassic.getGame().getConfig().save();
		}
	}

	@Override
	public String getStringValue() {
		return this.getValue() ? OpenClassic.getGame().getTranslator().translate("options.on") : OpenClassic.getGame().getTranslator().translate("options.off");
	}

	@Override
	public void toggle() {
		this.setValue(!this.getValue());
	}

}
