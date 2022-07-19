package org.spacehq.openclassic.api.command;

import org.apache.commons.lang3.Validate;

/**
 * Represents a command class.
 */
public abstract class Command {

	private String aliases[];
	private String permission;
	private int minArgs;
	private int maxArgs;
	private Class<? extends Sender> senders[];
	
	public Command(String aliases[], String permission) {
		this(aliases, permission, null);
	}
	
	public Command(String aliases[], String permission, Class<? extends Sender> senders[]) {
		this(aliases, permission, 0, senders);
	}
	
	public Command(String aliases[], String permission, int minArgs) {
		this(aliases, permission, minArgs, null);
	}
	
	public Command(String aliases[], String permission, int minArgs, Class<? extends Sender> senders[]) {
		this(aliases, permission, 0, 64, senders);
	}
	
	public Command(String aliases[], String permission, int minArgs, int maxArgs) {
		this(aliases, permission, minArgs, maxArgs, null);
	}
	
	public Command(String aliases[], String permission, int minArgs, int maxArgs, Class<? extends Sender> senders[]) {
		Validate.notNull(aliases, "Aliases cannot be null.");
		Validate.isTrue(aliases.length > 0, "Aliases cannot be empty.");
		this.aliases = aliases;
		this.permission = permission;
		this.minArgs = minArgs;
		this.maxArgs = maxArgs;
		this.senders = senders;
	}
	
	/**
	 * Gets the command's minimum arguments.
	 * @return The minimum arguments.
	 */
	public int getMinArgs() {
		return this.minArgs;
	}
	
	/**
	 * Gets the command's maximum arguments.
	 * @return The maximum arguments.
	 */
	public int getMaxArgs() {
		return this.maxArgs;
	}
	
	/**
	 * Gets the command's permission node.
	 * @return The permission node.
	 */
	public String getPermission() {
		return this.permission;
	}
	
	/**
	 * Gets the senders allowed to use this command.
	 * @return The senders allowed to use this command.
	 */
	public Class<? extends Sender>[] getSenders() {
		return this.senders;
	}
	
	/**
	 * Gets this command's aliases.
	 * @return This command's aliases.
	 */
	public String[] getAliases() {
		return this.aliases;
	}
	
	/**
	 * Gets the command's description.
	 * @return The command's description.
	 */
	public abstract String getDescription();
	
	/**
	 * Gets the command's usage.
	 * @return The command's usage.
	 */
	public abstract String getUsage();
	
	/**
	 * Executes the command.
	 * @param sender The sender using the command.
	 * @param command The alias being executed.
	 * @param args The arguments of the command.
	 */
	public abstract void execute(Sender sender, String command, String args[]);
	
}
