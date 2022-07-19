package org.spacehq.openclassic.api.command;

import org.spacehq.openclassic.api.OpenClassic;

/**
 * Represents the console when it sends a command.
 */
public class Console implements Sender {

	@Override
	public void sendMessage(String message) {
		OpenClassic.getLogger().info(OpenClassic.getGame().getTranslator().translate(message));
	}
	
	@Override
	public void sendMessage(String message, Object... args) {
		OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate(message), args));
	}

	@Override
	public String getName() {
		return "Server";
	}
	
	@Override
	public String getDisplayName() {
		return "Server";
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return true;
	}
	
	@Override
	public String getCommandPrefix() {
		return "";
	}

	@Override
	public String getLanguage() {
		return OpenClassic.getGame().getLanguage();
	}
	
}
