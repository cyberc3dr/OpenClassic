package org.spacehq.openclassic.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import org.spacehq.openclassic.api.Game;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.command.Command;
import org.spacehq.openclassic.api.command.CommandExecutor;
import org.spacehq.openclassic.api.command.Sender;
import org.spacehq.openclassic.api.config.Configuration;
import org.spacehq.openclassic.api.config.yaml.YamlConfig;
import org.spacehq.openclassic.api.event.game.CommandNotFoundEvent;
import org.spacehq.openclassic.api.event.game.PreCommandEvent;
import org.spacehq.openclassic.api.level.generator.Generator;
import org.spacehq.openclassic.api.pkg.PackageManager;
import org.spacehq.openclassic.api.plugin.Plugin;
import org.spacehq.openclassic.api.plugin.PluginManager;
import org.spacehq.openclassic.api.scheduler.Scheduler;
import org.spacehq.openclassic.api.translate.Language;
import org.spacehq.openclassic.api.translate.Translator;
import org.spacehq.openclassic.game.scheduler.ClassicScheduler;

import com.zachsthings.onevent.EventManager;

public abstract class ClassicGame implements Game {

	private final File directory;

	private final Configuration config;
	private final ClassicScheduler scheduler = new ClassicScheduler();

	private final PluginManager pluginManager = new PluginManager();
	private final PackageManager pkgManager;
	private final Translator translator = new Translator();

	private final Map<Object, List<Command>> commands = new HashMap<Object, List<Command>>();
	protected final Map<Object, List<CommandExecutor>> executors = new HashMap<Object, List<CommandExecutor>>();
	private final Map<String, Generator> generators = new HashMap<String, Generator>();

	public ClassicGame(File directory) {
		this.directory = directory;
		this.translator.register(new Language("English", "US", Main.class.getResourceAsStream("/lang/en_US.lang")));
		this.translator.setDefault("US");

		File file = new File(this.getDirectory(), "config.yml");
		if(!file.exists()) {
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			try {
				file.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		OpenClassic.setGame(this);
		this.pkgManager = new PackageManager();
		this.config = new YamlConfig(file);
		this.config.load();

		File langs = new File(this.directory, "lang");
		if(!langs.exists()) {
			try {
				langs.mkdirs();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		File[] languages = langs.listFiles();
		if(languages != null) {
			for(int ind = 0; ind < languages.length; ind++) {
				if(languages[ind].getName().endsWith(".id")) {
					try {
						BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(languages[ind])));
						String name = in.readLine();
						String code = in.readLine();
						in.close();
						String nme = languages[ind].getName();
						if(nme.endsWith(".id")) {
							nme = nme.substring(0, nme.length() - 3);
							ind++;
							String fname = languages[ind].getName();
							if(languages[ind].getName().endsWith(".lang")) {
								fname = fname.substring(0, fname.length() - 5);
								if(nme.equals(fname)) {
									this.translator.register(new Language(name, code, langs + File.separator + languages[ind].getName()));
									OpenClassic.getLogger().info("Language \"" + name + "\" has been successfully registered!");
								}
							}
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(this.getTranslator().get(this.config.getString("options.language")) == null) {
			this.config.setValue("options.language", "US");
		}
	}

	@Override
	public PackageManager getPackageManager() {
		return this.pkgManager;
	}

	@Override
	public Scheduler getScheduler() {
		return this.scheduler;
	}

	@Override
	public PluginManager getPluginManager() {
		return this.pluginManager;
	}

	@Override
	public void registerCommand(Object owner, Command command) {
		if(owner == null) {
			throw new IllegalArgumentException("Owner cannot be null.");
		}

		if(command == null) {
			throw new IllegalArgumentException("Command cannot be null.");
		}

		if(!this.commands.containsKey(owner)) {
			this.commands.put(owner, new ArrayList<Command>());
		}

		this.commands.get(owner).add(command);
	}

	@Override
	public void registerExecutor(Object owner, CommandExecutor executor) {
		if(owner == null) {
			throw new IllegalArgumentException("Owner cannot be null.");
		}

		if(executor == null) {
			throw new IllegalArgumentException("Executor cannot be null.");
		}
		if(!this.executors.containsKey(owner)) {
			this.executors.put(owner, new ArrayList<CommandExecutor>());
		}

		this.executors.get(owner).add(executor);
	}

	@Override
	public void unregisterCommands(Object owner) {
		if(owner == null) {
			throw new IllegalArgumentException("Owner cannot be null.");
		}

		if(!this.commands.containsKey(owner)) {
			return;
		}

		for(Command command : new ArrayList<Command>(this.commands.get(owner))) {
			this.commands.remove(command);
		}
	}

	@Override
	public void unregisterExecutors(Object owner) {
		if(owner == null) {
			throw new IllegalArgumentException("Owner cannot be null.");
		}

		if(!this.executors.containsKey(owner)) {
			return;
		}

		for(CommandExecutor command : new ArrayList<CommandExecutor>(this.executors.get(owner))) {
			this.executors.remove(command);
		}
	}

	@Override
	public void processCommand(Sender sender, String command) {
		if(command.length() == 0) return;
		PreCommandEvent event = EventManager.callEvent(new PreCommandEvent(sender, command));
		if(event.isCancelled()) {
			return;
		}

		String split[] = event.getCommand().split(" ");
		for(CommandExecutor executor : this.getCommandExecutors()) {
			if(executor.getCommand(split[0]) != null) {
				try {
					Method method = executor.getCommand(split[0]);
					org.spacehq.openclassic.api.command.annotation.Command annotation = method.getAnnotation(org.spacehq.openclassic.api.command.annotation.Command.class);

					if(annotation.senders().length > 0) {
						boolean match = false;

						for(Class<? extends Sender> allowed : annotation.senders()) {
							if(allowed.isAssignableFrom(sender.getClass())) {
								match = true;
							}
						}

						if(!match) {
							if(annotation.senders().length == 1) {
								sender.sendMessage("command.wrong-sender.single", annotation.senders()[0].getSimpleName().toLowerCase());
							} else {
								sender.sendMessage("command.wrong-sender.multi", Arrays.toString(annotation.senders()).toLowerCase());
							}

							return;
						}
					}

					if(!sender.hasPermission(annotation.permission())) {
						sender.sendMessage("command.no-perm");
						return;
					}

					if(split.length - 1 < annotation.min() || split.length - 1 > annotation.max()) {
						sender.sendMessage("command.usage", sender.getCommandPrefix() + split[0] + " " + annotation.usage());
						return;
					}

					method.invoke(executor, sender, split[0], Arrays.copyOfRange(split, 1, split.length));
				} catch(Exception e) {
					OpenClassic.getLogger().severe(String.format(this.translator.translate("command.fail-invoke"), split[0]));
					e.printStackTrace();
				}

				return;
			}
		}

		for(Command cmd : this.getCommands()) {
			if(Arrays.asList(cmd.getAliases()).contains(split[0])) {
				if(cmd.getSenders() != null && cmd.getSenders().length > 0) {
					boolean match = false;

					for(Class<? extends Sender> allowed : cmd.getSenders()) {
						if(sender.getClass() == allowed) {
							match = true;
						}
					}

					if(!match) {
						if(cmd.getSenders().length == 1) {
							sender.sendMessage(String.format(this.translator.translate("command.wrong-sender.single", sender.getLanguage()), cmd.getSenders()[0].getSimpleName().toLowerCase()));
						} else {
							sender.sendMessage(String.format(this.translator.translate("command.wrong-sender.multi", sender.getLanguage()), Arrays.toString(cmd.getSenders()).toLowerCase()));
						}
						return;
					}
				}

				if(!sender.hasPermission(cmd.getPermission())) {
					sender.sendMessage(this.translator.translate("command.no-perm", sender.getLanguage()));
					return;
				}

				if((split.length - 1) < cmd.getMinArgs() || (split.length - 1) > cmd.getMaxArgs()) {
					sender.sendMessage(this.translator.translate("command.usage", sender.getLanguage()) + ": " + sender.getCommandPrefix() + split[0] + " " + cmd.getUsage());
					return;
				}

				cmd.execute(sender, split[0], Arrays.copyOfRange(split, 1, split.length));
				return;
			}

			break;
		}

		CommandNotFoundEvent e = EventManager.callEvent(new CommandNotFoundEvent(sender, command));
		if(e.showMessage()) {
			sender.sendMessage(this.translator.translate("command.unknown", sender.getLanguage()));
		}
	}

	@Override
	public List<Command> getCommands() {
		List<Command> result = new ArrayList<Command>();
		for(List<Command> commands : this.commands.values()) {
			result.addAll(commands);
		}

		return result;
	}

	@Override
	public List<CommandExecutor> getCommandExecutors() {
		List<CommandExecutor> result = new ArrayList<CommandExecutor>();
		for(List<CommandExecutor> executors : this.executors.values()) {
			result.addAll(executors);
		}

		return result;
	}

	@Override
	public Configuration getConfig() {
		return this.config;
	}

	@Override
	public void registerGenerator(String name, Generator generator) {
		if(name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		}

		if(generator == null) {
			throw new IllegalArgumentException("Generator cannot be null.");
		}

		this.generators.put(name, generator);
	}

	@Override
	public Generator getGenerator(String name) {
		return this.generators.get(name);
	}

	@Override
	public Map<String, Generator> getGenerators() {
		return new HashMap<String, Generator>(this.generators);
	}

	@Override
	public boolean isGenerator(String name) {
		return this.getGenerator(name) != null;
	}

	@Override
	public File getDirectory() {
		return this.directory;
	}

	@Override
	public void reload() {
		this.config.save();
		this.config.load();

		for(Plugin plugin : this.pluginManager.getPlugins()) {
			plugin.reload();
		}
	}

	@Override
	public Translator getTranslator() {
		return this.translator;
	}

	@Override
	public String getLanguage() {
		return this.config.getString("options.language", "US");
	}

}
