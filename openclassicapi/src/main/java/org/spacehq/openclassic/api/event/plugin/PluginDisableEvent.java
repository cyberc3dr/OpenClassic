package org.spacehq.openclassic.api.event.plugin;

import com.zachsthings.onevent.HandlerList;

import org.spacehq.openclassic.api.plugin.Plugin;

/**
 * Called when a plugin is disabled.
 */
public class PluginDisableEvent extends PluginEvent {

	private static final HandlerList handlers = new HandlerList();
	
	public PluginDisableEvent(Plugin plugin) {
		super(plugin);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
