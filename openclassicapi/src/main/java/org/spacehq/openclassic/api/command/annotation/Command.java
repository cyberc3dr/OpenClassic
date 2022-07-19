package org.spacehq.openclassic.api.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.spacehq.openclassic.api.command.Sender;

/**
 * This annotation represents a command method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * Gets the aliases of the command.
	 * @return The command's aliases.
	 */
	public String[] aliases();
	
	/**
	 * Gets the command's description.
	 * @return The command's description.
	 */
	public String desc();

	/**
	 * Gets the command's usage.
	 * @return The command's usage.
	 */
	public String usage() default "";

	/**
	 * Gets the command's permission node.
	 * @return The permission node.
	 */
	public String permission();

	/**
	 * Gets the command's minimum arguments.
	 * @return The minimum arguments.
	 */
	public int min() default 0;

	/**
	 * Gets the command's maximum arguments.
	 * @return The maximum arguments.
	 */
	public int max() default 64;
	
	/**
	 * Gets the senders allowed to use this command.
	 * @return The senders allowed to use this command.
	 */
	public Class<? extends Sender>[] senders() default {};
	
}
