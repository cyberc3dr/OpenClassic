package org.spacehq.openclassic.api.plugin;

/**
 * Info about a plugin on a connected client/server
 */
public class RemotePluginInfo {

	private String name;
	private String version;
	
	public RemotePluginInfo(String name, String version) {
		this.name = name;
		this.version = version;
	}
	
	/**
	 * Gets the name of the plugin.
	 * @return The plugin's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the version of the plugin.
	 * @return The plugin's version.
	 */
	public String getVersion() {
		return this.version;
	}
	
}
