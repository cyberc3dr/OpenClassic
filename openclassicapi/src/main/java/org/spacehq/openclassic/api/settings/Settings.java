package org.spacehq.openclassic.api.settings;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A class containing settings.
 */
public class Settings {

	private Map<String, Setting> settings = new LinkedHashMap<String, Setting>();
	
	/**
	 * Gets all registered settings.
	 * @return All registered settings.
	 */
	public Map<String, Setting> getSettings() {
		return new HashMap<String, Setting>(this.settings);
	}
	
	/**
	 * Gets the setting at the given map index.
	 * @return The setting at the given map index.
	 */
	public Setting getSetting(int index) {
		int i = 0;
		for(Setting s : this.settings.values()) {
			if(i == index) {
				return s;
			}
			
			i++;
		}
		
		return null;
	}
	
	/**
	 * Gets the setting with the given key.
	 * @param key Key of the setting.
	 * @return The setting to get.
	 */
	public Setting getSetting(String key) {
		return this.settings.get(key);
	}
	
	/**
	 * Gets the boolean setting with the given key.
	 * @param key Key of the setting.
	 * @return The setting to get, or null if it isn't a boolean setting.
	 */
	public BooleanSetting getBooleanSetting(String key) {
		Setting set = this.getSetting(key);
		if(!(set instanceof BooleanSetting)) {
			return null;
		}
		
		return (BooleanSetting) set;
	}
	
	/**
	 * Gets the integer setting with the given key.
	 * @param key Key of the setting.
	 * @return The setting to get, or null if it isn't an integer setting.
	 */
	public IntSetting getIntSetting(String key) {
		Setting set = this.getSetting(key);
		if(!(set instanceof IntSetting)) {
			return null;
		}
		
		return (IntSetting) set;
	}
	
	/**
	 * Registers the given setting.
	 * @param setting Setting to register.
	 */
	public void registerSetting(Setting setting) {
		this.settings.put(setting.getName(), setting);
	}
	
	/**
	 * Unregisters a setting.
	 * @param setting Setting to unregister.
	 */
	public void unregisterSetting(Setting setting) {
		this.settings.remove(setting.getName());
	}
	
}
