package org.spacehq.openclassic.api.event.plugin;

import com.zachsthings.onevent.Event;

import org.spacehq.openclassic.api.plugin.Plugin;

/**
 * Represents an event involving a plugin.
 */
public abstract class PluginEvent extends Event {

	private Plugin plugin;
	
	public PluginEvent(Plugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Gets the plugin involved in this event.
	 * @return The plugin in the event.
	 */
	public Plugin getPlugin() {
		return this.plugin;
	}
	
}
