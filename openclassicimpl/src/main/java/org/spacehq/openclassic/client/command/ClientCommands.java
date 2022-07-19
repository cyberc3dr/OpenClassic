package org.spacehq.openclassic.client.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.command.CommandExecutor;
import org.spacehq.openclassic.api.command.Sender;
import org.spacehq.openclassic.api.command.annotation.Command;
import org.spacehq.openclassic.api.player.Player;

// TODO: Translate descriptions
public class ClientCommands extends CommandExecutor {

	@Command(aliases = { "help" }, desc = "Shows a list of commands and what they do.", permission = "openclassic.commands.help")
	public void help(Sender sender, String command, String args[]) {
		int page = 1;
		if(args.length > 0) {
			try {
				page = Integer.parseInt(args[0]);
			} catch(NumberFormatException e) {
				sender.sendMessage("help.invalid-page");
				return;
			}
		}

		if(page < 1) {
			sender.sendMessage("help.invalid-page");
			return;
		}

		List<Object> available = new ArrayList<Object>();
		for(CommandExecutor exec : OpenClassic.getGame().getCommandExecutors()) {
			for(Method method : exec.getCommands()) {
				Command cmd = method.getAnnotation(Command.class);
	
				boolean match = false;
				if(cmd.senders().length > 0) {
					for(Class<? extends Sender> allowed : cmd.senders()) {
						if(allowed.isAssignableFrom(sender.getClass())) {
							match = true;
						}
					}
				} else {
					match = true;
				}
	
				if(sender.hasPermission(cmd.permission()) && match) {
					available.add(cmd);
				}
			}
		}
		
		for(org.spacehq.openclassic.api.command.Command cmd : OpenClassic.getGame().getCommands()) {
			boolean match = false;
			if(cmd.getSenders().length > 0) {
				for(Class<? extends Sender> allowed : cmd.getSenders()) {
					if(allowed.isAssignableFrom(sender.getClass())) {
						match = true;
					}
				}
			} else {
				match = true;
			}

			if(sender.hasPermission(cmd.getPermission()) && match) {
				available.add(cmd);
			}
		}

		int pages = (int) Math.ceil((double) available.size() / 17);
		if(page > pages) {
			sender.sendMessage("help.page-not-found");
			return;
		}

		sender.sendMessage("help.pages", page, pages);

		for(int index = (page - 1) * 17; index < ((page - 1) * 17) + 17; index++) {
			if(index >= available.size()) break;
			String aliases[] = null;
			String desc = null;
			String usage = null;
			if(available.get(index) instanceof Command) {
				Command cmd = (Command) available.get(index);
				aliases = cmd.aliases();
				desc = cmd.desc();
				usage = cmd.usage();
			} else {
				org.spacehq.openclassic.api.command.Command cmd = (org.spacehq.openclassic.api.command.Command) available.get(index);
				aliases = cmd.getAliases();
				desc = cmd.getDescription();
				usage = cmd.getUsage();
			}

			String aliasString = aliases[0];
			if(aliases.length > 1) {
				aliasString = Arrays.toString(aliases);
			}

			sender.sendMessage(Color.AQUA + sender.getCommandPrefix() + aliasString + " - " + desc + Color.AQUA + " - " + sender.getCommandPrefix() + aliasString + " " + usage);
		}
	}

	@Command(aliases = { "pkg" }, desc = "Manages installed packages on the server.", permission = "openclassic.commands.pkg", min = 1, max = 3, usage = "<option> [args]")
	public void pkg(Sender sender, String command, String args[]) {
		if(args[0].equalsIgnoreCase("install")) {
			if(args.length < 2) {
				sender.sendMessage(Color.RED + "command.usage", "/pkg install <name>");
				return;
			}

			OpenClassic.getGame().getPackageManager().install(args[1], sender);
		} else if(args[0].equalsIgnoreCase("remove")) {
			if(args.length < 2) {
				sender.sendMessage(Color.RED + "command.usage", "/pkg remove <name>");
				return;
			}

			OpenClassic.getGame().getPackageManager().remove(args[1], sender);
		} else if(args[0].equalsIgnoreCase("update")) {
			if(args.length < 2) {
				sender.sendMessage(Color.RED + "command.usage", "/pkg update <name>");
				return;
			}

			OpenClassic.getGame().getPackageManager().update(args[1], sender);
		} else if(args[0].equalsIgnoreCase("add-source")) {
			if(args.length < 3) {
				sender.sendMessage(Color.RED + "command.usage", "/pkg add-source <id> <url>");
				return;
			}

			OpenClassic.getGame().getPackageManager().addSource(args[1], args[2], sender);
		} else if(args[0].equalsIgnoreCase("remove-source")) {
			if(args.length < 2) {
				sender.sendMessage(Color.RED + "command.usage", "/pkg remove-source <id>");
				return;
			}

			OpenClassic.getGame().getPackageManager().removeSource(args[1], sender);
		} else if(args[0].equalsIgnoreCase("update-sources")) {
			OpenClassic.getGame().getPackageManager().updateSources(sender);
		} else {
			sender.sendMessage("pkg.invalid-operation", "install, remove, update, add-source, remove-source, update-sources");
		}
	}

	@Command(aliases = { "reload" }, desc = "Reloads OpenClassic.", permission = "openclassic.commands.reload")
	public void reload(Sender sender, String command, String args[]) {
		sender.sendMessage("reload.reloading");
		OpenClassic.getGame().reload();
		sender.sendMessage("reload.complete");
	}

	@Command(aliases = { "setspawn" }, desc = "Sets the spawn to your location", permission = "openclassic.commands.setspawn", senders = { Player.class })
	public void setspawn(Sender sender, String command, String args[]) {
		Player player = (Player) sender;
		player.getPosition().getLevel().setSpawn(player.getPosition());
		player.sendMessage("spawn.set", player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
	}

	@Command(aliases = { "solid", "bedrock" }, desc = "Toggles bedrock placement mode.", permission = "openclassic.commands.solid", senders = { Player.class })
	public void solid(Sender sender, String command, String args[]) {
		Player player = (Player) sender;
		if(player.getPlaceMode() != VanillaBlock.BEDROCK) {
			player.setPlaceMode(VanillaBlock.BEDROCK);
			player.setCanBreakUnbreakables(true);
			player.sendMessage("solid.enable");
		} else {
			player.setPlaceMode(null);
			player.setCanBreakUnbreakables(false);
			player.sendMessage("solid.disable");
		}
	}

	@Command(aliases = { "water" }, desc = "Toggles water placement mode.", permission = "openclassic.commands.water", senders = { Player.class })
	public void water(Sender sender, String command, String args[]) {
		Player player = (Player) sender;
		if(player.getPlaceMode() != VanillaBlock.WATER) {
			player.setPlaceMode(VanillaBlock.WATER);
			player.sendMessage("water.enable");
		} else {
			player.setPlaceMode(null);
			player.sendMessage("water.disable");
		}
	}

	@Command(aliases = { "stillwater" }, desc = "Toggles still water placement mode.", permission = "openclassic.commands.stillwater", senders = { Player.class })
	public void stillwater(Sender sender, String command, String args[]) {
		Player player = (Player) sender;
		if(player.getPlaceMode() != VanillaBlock.STATIONARY_WATER) {
			player.setPlaceMode(VanillaBlock.STATIONARY_WATER);
			player.sendMessage("stillwater.enable");
		} else {
			player.setPlaceMode(null);
			player.sendMessage("stillwater.disable");
		}
	}

	@Command(aliases = { "lava" }, desc = "Toggles lava placement mode.", permission = "openclassic.commands.lava", senders = { Player.class })
	public void lava(Sender sender, String command, String args[]) {
		Player player = (Player) sender;
		if(player.getPlaceMode() != VanillaBlock.LAVA) {
			player.setPlaceMode(VanillaBlock.LAVA);
			player.sendMessage("lava.enable");
		} else {
			player.setPlaceMode(null);
			player.sendMessage("lava.disable");
		}
	}

	@Command(aliases = { "stilllava" }, desc = "Toggles still lava placement mode.", permission = "openclassic.commands.stilllava", senders = { Player.class })
	public void stilllava(Sender sender, String command, String args[]) {
		Player player = (Player) sender;
		if(player.getPlaceMode() != VanillaBlock.STATIONARY_LAVA) {
			player.setPlaceMode(VanillaBlock.STATIONARY_LAVA);
			player.sendMessage("stilllava.enable");
		} else {
			player.setPlaceMode(null);
			player.sendMessage("stilllava.disable");
		}
	}

}
