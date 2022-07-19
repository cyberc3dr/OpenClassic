package org.spacehq.openclassic.server.player;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.plugin.RemotePluginInfo;

public class ClientInfo {

	private Player player;
	private boolean custom;
	private String version;
	private String language = "";
	private List<RemotePluginInfo> plugins = new ArrayList<RemotePluginInfo>();

	public ClientInfo(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return this.player;
	}

	public boolean isCustom() {
		return this.custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void addPlugin(RemotePluginInfo info) {
		this.plugins.add(info);
	}

	public List<RemotePluginInfo> getPlugins() {
		return this.plugins;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
