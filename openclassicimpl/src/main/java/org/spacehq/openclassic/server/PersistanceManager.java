package org.spacehq.openclassic.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.config.Configuration;
import org.spacehq.openclassic.api.config.yaml.YamlConfig;

public class PersistanceManager {

	private Configuration players = new YamlConfig(new File(OpenClassic.getGame().getDirectory(), "players.yml"));

	public void load() {
		this.players.load();

		if(this.players.getNode("bans") == null) {
			this.players.setValue("bans.exampleban", "reasonhere");
		}

		if(this.players.getNode("ipbans") == null) {
			this.players.setValue("ipbans.example-ip-ban", "reasonhere");
		}

		List<String> whitelist = this.players.getList("whitelist", String.class);
		if(whitelist == null || whitelist.size() == 0) {
			whitelist = new ArrayList<String>();
			whitelist.add("playergoeshere");
			this.players.setValue("whitelist", whitelist);
		}
	}

	public void save() {
		this.players.save();
	}

	public boolean isBanned(String player) {
		return this.players.getValue("bans." + player.toLowerCase()) != null;
	}

	public boolean isIpBanned(String address) {
		return this.players.getValue("ipbans." + address.replaceAll(".", "-")) != null;
	}

	public boolean isWhitelisted(String player) {
		List<String> whitelist = this.players.getList("whitelist", String.class);
		return whitelist != null && whitelist.contains(player.toLowerCase());
	}

	public void banPlayer(String player) {
		this.banPlayer(player, "You have been banned from this server.");
	}

	public void banPlayer(String player, String reason) {
		if(this.isBanned(player)) return;
		if(reason.equals("")) reason = "You have been banned from this server.";

		this.players.setValue("bans." + player.toLowerCase(), reason);
	}

	public void unbanPlayer(String player) {
		if(!this.isBanned(player)) return;

		this.players.remove("bans." + player.toLowerCase());
	}

	public void banIp(String address) {
		this.banIp(address, "You have been IP banned from this server.");
	}

	public void banIp(String address, String reason) {
		if(this.isIpBanned(address)) return;
		if(reason.equals("")) reason = "You have been IP banned from this server.";

		this.players.setValue("bans." + address.replaceAll(".", "-"), reason);
	}

	public void unbanIp(String address) {
		if(!this.isIpBanned(address)) return;

		this.players.remove("bans." + address.replaceAll(".", "-"));
	}

	public void whitelist(String player) {
		if(this.isWhitelisted(player)) return;

		List<String> whitelist = this.getWhitelistedPlayers();

		if(whitelist == null) whitelist = new ArrayList<String>();

		whitelist.add(player.toLowerCase());

		this.players.setValue("whitelist", whitelist);
	}

	public void unwhitelist(String player) {
		if(!this.isWhitelisted(player)) return;

		List<String> whitelist = this.getWhitelistedPlayers();

		if(whitelist == null) whitelist = new ArrayList<String>();

		whitelist.remove(player.toLowerCase());

		this.players.setValue("whitelist", whitelist);
	}

	public String getBanReason(String player) {
		if(!this.isBanned(player)) return "";

		return this.players.getString("bans." + player.toLowerCase());
	}

	public String getIpBanReason(String address) {
		if(!this.isIpBanned(address)) return "";

		return this.players.getString("ipbans." + address.replaceAll(".", "-"));
	}

	public List<String> getBannedPlayers() {
		List<String> bans = new ArrayList<String>();

		for(String ban : this.players.getRelativeKeys("bans", false)) {
			bans.add(ban);
		}

		return bans;
	}

	public List<String> getBannedIps() {
		List<String> bans = new ArrayList<String>();

		for(String ban : this.players.getRelativeKeys("ipbans", false)) {
			bans.add(ban.replaceAll("-", "."));
		}

		return bans;
	}

	public List<String> getWhitelistedPlayers() {
		return this.players.getList("whitelist", String.class);
	}

}
