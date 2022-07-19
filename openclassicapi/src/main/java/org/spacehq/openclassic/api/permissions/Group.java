package org.spacehq.openclassic.api.permissions;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.OpenClassic;

/**
 * Represents a permission group.
 */
public class Group {

	private String name;
	private String inherits;
	private boolean def;
	private List<String> perms;
	private List<String> players = new ArrayList<String>();
	
	public Group(String name, String inherits, boolean def, List<String> perms, List<String> players) {
		this.name = name;
		this.inherits = inherits;
		this.def = def;
		this.perms = perms;
		this.players = players;
	}
	
	/**
	 * Gets the name of the group.
	 * @return The group's name.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the name of the group inherited by this group.
	 * @return The name of the group inherited by this group.
	 */
	public String getInheritedGroup() {
		return this.inherits;
	}
	
	/**
	 * Sets the group inherited by this group.
	 * @param group Group to inherit.
	 */
	public void setInheritedGroup(String group) {
		this.inherits = group;
	}
	
	/**
	 * Returns true if this group is the default group.
	 * @return True if this group is default.
	 */
	public boolean isDefault() {
		return this.def;
	}
	
	/**
	 * Sets whether this group is default.
	 * @param def Whether this group is default.
	 */
	public void setDefault(boolean def) {
		this.def = def;
	}
	
	/**
	 * Gets a list of permission nodes this group has.
	 * @return A list of nodes this group has.
	 */
	public List<String> getPermissions() {
		List<String> result = new ArrayList<String>(); // ArrayList<String>(this.perms) returns blank
		result.addAll(this.perms);
		return result;
	}
	
	/**
	 * Adds a permission to this group.
	 * @param permission Permission to add.
	 */
	public void addPermission(String permission) {
		this.perms.add(permission.toLowerCase());
	}
	
	/**
	 * Removes a permission from this group.
	 * @param permission Permission to remove.
	 */
	public void removePermission(String permission) {
		this.perms.remove(permission.toLowerCase());
	}
	
	/**
	 * Gets the players belonging to this group.
	 * @return A list of players belonging to this group.
	 */
	public List<String> getPlayers() {
		List<String> result = new ArrayList<String>(); // ArrayList<String>(this.players) returns blank
		result.addAll(this.players);
		return result;
	}
	
	/**
	 * Adds a player to this group.
	 * @param name Player to add.
	 */
	public void addPlayer(String name) {
		this.players.add(name.toLowerCase());
	}
	
	/**
	 * Removes a player from this group.
	 * @param name Player to remove.
	 */
	public void removePlayer(String name) {
		this.players.remove(name.toLowerCase());
	}
	
	/**
	 * Checks if this group has a permission.
	 * @param permission Permission to check for.
	 * @return True if this group has the given permission.
	 */
	public boolean hasPermission(String permission) {
		if(this.perms.contains(permission.toLowerCase())) {
			return true;
		}
		
		if(this.inherits != null && !this.inherits.equals("")) {
			return OpenClassic.getServer().getPermissionManager().getGroup(this.inherits).hasPermission(permission);
		}
		
		return false;
	}
	
	/**
	 * Returns true if the given group is or inherits this group.
	 * @param group Group to check for.
	 * @return Whether the group is or inherits this group.
	 */
	public boolean isSubGroup(Group group) {
		if(group.getName().equals(this.getName())) {
			return true;
		}
		
		Group g = OpenClassic.getServer().getPermissionManager().getGroup(this.getInheritedGroup());
		while(g != null) {
			if(group.getName().equals(g.getName())) {
				return true;
			}
			
			g = OpenClassic.getServer().getPermissionManager().getGroup(g.getInheritedGroup());
		}
		
		return false;
	}
	
}
