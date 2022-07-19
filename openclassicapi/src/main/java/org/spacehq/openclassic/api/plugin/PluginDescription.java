package org.spacehq.openclassic.api.plugin;

import org.spacehq.openclassic.api.plugin.PluginManager.LoadOrder;

/**
 * Represents the data stored in plugin.yml.
 */
public class PluginDescription {

	private String name;
	private String version;
	private String mainClass;
	private String depends[];
	private LoadOrder order;
	
	public PluginDescription(String name, String version, String mainClass, String depends, String order) {
		this.name = name;
		this.version = version;
		this.mainClass = mainClass;
		this.depends = (depends != null && !depends.equals("") && !depends.equals("[]") && !depends.equals("null") ? depends.split(", ") : new String[0]);
		this.order = order == null || order.equals("") || order.equals("null") ? LoadOrder.POSTWORLD : LoadOrder.valueOf(order.toUpperCase());
	}
	
	/**
	 * Returns a string containing the plugin's name and version.
	 * @return The plugin's full name.
	 */
	public String getFullName() {
		return this.getName() + " v" + this.getVersion();
	}
	
	/**
	 * Gets the plugin's name.
	 * @return The plugin's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the plugin's version.
	 * @return The plugin's version.
	 */
	public String getVersion() {
		return this.version;
	}
	
	/**
	 * Gets the plugin's main class.
	 * @return The plugin's main class.
	 */
	public String getMainClass() {
		return this.mainClass;
	}
	
	/**
	 * Gets the plugin's dependencies.
	 * @return The plugin's dependencies.
	 */
	public String[] getDependencies() {
		return this.depends;
	}
	
	/**
	 * Gets during what part of the server's startup the plugin loads.
	 * @return The plugin's load order.
	 */
	public LoadOrder getLoadOrder() {
		return this.order;
	}
	
}
