package org.spacehq.openclassic.api;

import org.spacehq.openclassic.api.command.Command;
import org.spacehq.openclassic.api.command.CommandExecutor;
import org.spacehq.openclassic.api.command.Sender;
import org.spacehq.openclassic.api.config.Configuration;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.level.LevelInfo;
import org.spacehq.openclassic.api.level.generator.Generator;
import org.spacehq.openclassic.api.pkg.PackageManager;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.plugin.PluginManager;
import org.spacehq.openclassic.api.scheduler.Scheduler;
import org.spacehq.openclassic.api.sound.AudioManager;
import org.spacehq.openclassic.api.translate.Translator;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Represents a Minecraft game.
 */
public interface Game {

	/**
	 * Gets the game's package manager.
	 * @return The game's package manager.
	 */
	public PackageManager getPackageManager();
	
	/**
	 * Gets the game's scheduler.
	 * @return The game's scheduler.
	 */
	public Scheduler getScheduler();
	
	/**
	 * Gets the game's plugin manager.
	 * @return The game's plugin manager.
	 */
	public PluginManager getPluginManager();
	
	/**
	 * Registers a command.
	 * @param owner Owner the command belongs to.
	 * @param command Command to register.
	 */
	public void registerCommand(Object owner, Command command);
	
	/**
	 * Registers a command executor.
	 * @param owner Owner the executor belongs to.
	 * @param executor Executor to register.
	 */
	public void registerExecutor(Object owner, CommandExecutor executor);
	
	/**
	 * Unregisters a plugins commands.
	 * @param owner Owner the commands belongs to.
	 */
	public void unregisterCommands(Object owner);
	
	/**
	 * Unregisters a plugins executors.
	 * @param owner Owner the executors belongs to.
	 */
	public void unregisterExecutors(Object owner);
	
	/**
	 * Processes a sent command.
	 * @param sender Sender that is calling the command.
	 * @param command Command to send.
	 */
	public void processCommand(Sender sender, String command);
	
	/**
	 * Gets a list of all non-executor commands registered.
	 * @return All non-executor commands.
	 */
	public List<Command> getCommands();
	
	/**
	 * Gets a list of all executors registered.
	 * @return All executors.
	 */
	public List<CommandExecutor> getCommandExecutors();
	
	/**
	 * Shuts down the game.
	 */
	public void shutdown();
	
	/**
	 * Gets a list of players.
	 * @return A list of all players.
	 */
	public List<Player> getPlayers();
	
	/**
	 * Returns the player with the given name.
	 * @param name The name of the player.
	 * @return The player.
	 */
	public Player getPlayer(String name);
	
	/**
	 * Returns a list of players that have a name with the given string in it.
	 * @param name Name to look for.
	 * @return Players with the string in their name.
	 */
	public List<Player> matchPlayer(String name);
	
	/**
	 * Creates a level with the given info.
	 * @param info Info to use.
	 * @param generator Generator to generate with.
	 * @return The created level.
	 */
	public Level createLevel(LevelInfo info, Generator generator);
	
	/**
	 * Returns true if the game is running.
	 * @return True if the game is running.
	 */
	public boolean isRunning();
	
	/**
	 * Gets the game's configuration.
	 * @return The game's configuration.
	 */
	public Configuration getConfig();
	
	/**
	 * Registers a generator to the given name.
	 * @param name Name to register to.
	 * @param generator Generator to register.
	 */
	public void registerGenerator(String name, Generator generator);
	
	/**
	 * Gets the generator registered to the given name,
	 * @param name Name to look for.
	 * @return The generator.
	 */
	public Generator getGenerator(String name);
	
	/**
	 * Gets the generators registered.
	 * @return The registered generators.
	 */
	public Map<String, Generator> getGenerators();
	
	/**
	 * Returns true if a generator by this name exists.
	 * @param name Name to look for.
	 * @return True if the generator exists.
	 */
	public boolean isGenerator(String name);

	/**
	 * Gets the working directory of the game.
	 * @return The game's working directory.
	 */
	public File getDirectory();
	
	/**
	 * Reloads the game.
	 */
	public void reload();
	
	/**
	 * Gets the game's audio manager.
	 * @return The game's audio manager.
	 */
	public AudioManager getAudioManager();
	
	/**
	 * Gets the game's translator.
	 * @return The game's translator.
	 */
	public Translator getTranslator();
	
	/**
	 * Gets the game's language setting.
	 * @return The game's language setting.
	 */
	public String getLanguage();
	
}
