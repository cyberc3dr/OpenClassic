package org.spacehq.openclassic.api.plugin;

import java.io.File;
import java.util.logging.Logger;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.config.Configuration;
import org.spacehq.openclassic.api.config.yaml.YamlConfig;
import org.spacehq.openclassic.api.data.NBTData;

/**
 * Represents a plugin.
 */
public abstract class Plugin {

	private Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private PluginDescription description;
	private Configuration config;
	private File dataFolder;
	private boolean enabled;
	private NBTData data;
	
	protected void init(PluginDescription description) {
		this.description = description;
		this.dataFolder = new File(OpenClassic.getGame().getDirectory(), "plugins/" + description.getName());
		this.config = new YamlConfig(new File(this.dataFolder, "config.yml"));
		this.data = new NBTData(description.getName());
	}
	
	/**
	 * Called when the plugin is loaded.
	 */
	public void onLoad() {
	}
	
	/**
	 * Called when the plugin is enabled.
	 */
	public void onEnable() {
	}
	
	/**
	 * Called when the plugin is disabled.
	 */
	public void onDisable() {
	}
	
	/**
	 * Called when the game is reloading. Plugins may reload themselves here.
	 */
	public void reload() {
	}
	
	/**
	 * Called when a tick occurs.
	 */
	public void tick() {
	}
	
	/**
	 * Gets the plugin's description.
	 * @return The plugin's description.
	 */
	public PluginDescription getDescription() {
		return this.description;
	}
	
	/**
	 * Gets the plugin's configuration.
	 * @return The plugin's configuration.
	 */
	public Configuration getConfig() {
		return this.config;
	}
	
	/**
	 * Gets the plugin's data folder.
	 * @return The plugin's data folder.
	 */
	public File getDataFolder() {
		return this.dataFolder;
	}
	
	/**
	 * Returns true if the plugin is enabled.
	 * @return If the plugin is enabled.
	 */
	public boolean isEnabled() {
		return this.enabled;
	}
	
	/**
	 * Sets whether the plugin is enabled.
	 * @param enable Whether the plugin is enabled.
	 */
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}
	
	/**
	 * Gets the logger belonging to this plugin.
	 * @return This plugin's logger.
	 */
	public Logger getLogger() {
		return this.logger;
	}
	
	/**
	 * Gets this plugin's NBTData.
	 * @return This plugin's NBTData.
	 */
	public NBTData getData() {
		return this.data;
	}
	
	@Override
	public String toString() {
		return this.getDescription().getFullName();
	}
	
}
