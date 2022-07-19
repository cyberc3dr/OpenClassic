package org.spacehq.openclassic.api.permissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.config.Configuration;
import org.spacehq.openclassic.api.config.yaml.YamlConfig;

/**
 * Manages the server's permissions.
 */
public class PermissionManager {
	
	private Configuration perms;
	private List<Group> groups = new ArrayList<Group>();
	
	/**
	 * Loads the server's permissions and groups.
	 */
	public void load() {
		if(this.perms == null) {
			this.perms = new YamlConfig(new File(OpenClassic.getGame().getDirectory(), "permissions.yml"));
		}
		
		this.perms.load();
		this.loadGroups();
	}
	
	/**
	 * Saves the server's permissions and groups.
	 */
	public void save() {
		for(Group group : this.groups) {
			this.perms.setValue(group.getName() + ".inherits", group.getInheritedGroup());
			this.perms.setValue(group.getName() + ".default", group.isDefault());
			this.perms.setValue(group.getName() + ".permissions", group.getPermissions());
			this.perms.setValue(group.getName() + ".players", group.getPlayers());
		}
		
		List<String> keys = this.perms.getAbsoluteKeys(false);
		if(keys.size() > this.groups.size()) {
			for(String name : keys) {
				boolean match = false;
				for(Group group : this.groups) {
					if(group.getName().equalsIgnoreCase(name)) match = true;
				}
				
				if(!match) {
					this.perms.remove(name);
				}
			}
		}
		
		this.perms.save();
	}
	
	/**
	 * Reloads the server's permissions and groups.
	 */
	public void reload() {
		this.save();
		this.perms.load();
		this.loadGroups();
	}
	
	/**
	 * Loads the server's groups.
	 */
	public void loadGroups() {
		for(String key : this.perms.getAbsoluteKeys(false)) {
			String name = key;
			
			try {
				String inherits = this.perms.getString(key + ".inherits");
				boolean def = this.perms.getBoolean(key + ".default");
				List<String> permissions = this.perms.getList(key + ".permissions", String.class);
				List<String> players = this.perms.getList(key + ".players", String.class);
				
				this.groups.add(new Group(name, inherits, def, permissions, players));
			} catch(Exception e) {
				OpenClassic.getLogger().severe("Exception while loading a permissions entry! It's probably invalid!");
				e.printStackTrace();
			}
		}

		if(groups.size() <= 0) {
			OpenClassic.getLogger().info("No groups found! Creating default group...");
			
			Group group = new Group("default", "", true, new ArrayList<String>(), new ArrayList<String>());
			this.addGroup(group);
			this.save();
		}
	}
	
	/**
	 * Adds a group to the group list.
	 * @param group Group to add.
	 */
	public void addGroup(Group group) {
		this.groups.add(group);
	}
	
	/**
	 * Removes a group from the group list.
	 * @param group Group to remove.
	 */
	public void removeGroup(Group group) {
		this.groups.remove(group);
	}
	
	/**
	 * Gets the group with the given name.
	 * @param name Name to look for.
	 * @return Group with the name.
	 */
	public Group getGroup(String name) {
		for(Group group : this.groups) {
			if(group.getName().equalsIgnoreCase(name)) return group;
		}
		
		return null;
	}
	
	/**
	 * Gets the default group.
	 * @return The default group.
	 */
	public Group getDefaultGroup() {
		for(Group group : this.groups) {
			if(group.isDefault()) return group;
		}
		
		OpenClassic.getLogger().severe("No default group found! Expect errors!");
		return null;
	}
	
	/**
	 * Gets the group of the given player.
	 * @param player Player to get the group of.
	 * @return The player's group.
	 */
	public Group getPlayerGroup(String player) {
		for(Group group : this.groups) {
			if(group.getPlayers().contains(player.toLowerCase())) return group;
		}
		
		Group group = this.getDefaultGroup();
		group.addPlayer(player);
		return group;
	}
	
	/**
	 * Sets the player's group.
	 * @param player Player to set the group of.
	 * @param group Group to set the player to.
	 */
	public void setPlayerGroup(String player, Group group) {
		Group old = this.getPlayerGroup(player);
		
		if(old != null) old.removePlayer(player);
		group.addPlayer(player);
		
		if(OpenClassic.getServer().getPlayer(player) != null) {
			if(old != null) {
				if(!old.hasPermission("openclassic.commands.solid") && group.hasPermission("openclassic.commands.solid")) {
					OpenClassic.getServer().getPlayer(player).setCanBreakUnbreakables(true);
				} else if(old.hasPermission("openclassic.commands.solid") && !group.hasPermission("openclassic.commands.solid")) {
					OpenClassic.getServer().getPlayer(player).setCanBreakUnbreakables(false);
				}
			} else if(group.hasPermission("openclassic.commands.solid")) {
				OpenClassic.getServer().getPlayer(player).setCanBreakUnbreakables(true);
			}
		}
	}
	
	/**
	 * Checks if the player has the given permission.
	 * @param player Player to check.
	 * @param permission Permission to check for.
	 * @return True if the player has the given permission.
	 */
	public boolean hasPermission(String player, String permission) {
		if(this.getPlayerGroup(player) != null) {
			return this.getPlayerGroup(player).hasPermission(permission);
		}
		
		return false;
	}
	
	/**
	 * Checks if the group has the given permission.
	 * @param group Group to check.
	 * @param permission Permission to check for.
	 * @return True if the group has the given permission.
	 */
	public boolean hasPermission(Group group, String permission) {
		if(group != null) return group.hasPermission(permission);
		
		return false;
	}
	
}
