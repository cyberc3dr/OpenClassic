package org.spacehq.openclassic.api.sound;

import java.net.URL;

import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.player.Player;

public interface AudioManager {

	/**
	 * Registers a file to a sound identifier.
	 * @param sound Identifier to register to.
	 * @param file File to register.
	 * @param included Whether the sound is included with the client.
	 */
	public void registerSound(String sound, URL file, boolean included);
	
	/**
	 * Registers a file to a music identifier.
	 * @param music Identifier to register to.
	 * @param file File to register.
	 * @param included Whether the music is included with the client.
	 */
	public void registerMusic(String music, URL file, boolean included);
	
	/**
	 * Plays the given sound.
	 * @param sound Sound to play.
	 * @param volume Volume to play at.
	 * @param pitch Pitch to play at. (0 for default)
	 * @return True if the sound was found in the registry.
	 */
	public boolean playSound(String sound, float volume, float pitch);
	
	/**
	 * Plays the given sound.
	 * @param player Player to play for.
	 * @param sound Sound to play.
	 * @param volume Volume to play at.
	 * @param pitch Pitch to play at. (0 for default)
	 * @return True if the sound was found in the registry.
	 */
	public boolean playSound(Player player, String sound, float volume, float pitch);
	
	/**
	 * Plays the given sound.
	 * @param level Level to play the sound in.
	 * @param sound Sound to play.
	 * @param volume Volume to play at.
	 * @param pitch Pitch to play at. (0 for default)
	 * @return True if the sound was found in the registry.
	 */
	public boolean playSound(Level level, String sound, float volume, float pitch);
	
	/**
	 * Plays the given sound.
	 * @param sound Sound to play.
	 * @param x X to play at.
	 * @param y Y to play at.
	 * @param z Z to play at.
	 * @param volume Volume to play at.
	 * @param pitch Pitch to play at. (0 for default)
	 * @return True if the sound was found in the registry.
	 */
	public boolean playSound(String sound, float x, float y, float z, float volume, float pitch);
	
	/**
	 * Plays the given sound.
	 * @param player Player to play for.
	 * @param sound Sound to play.
	 * @param x X to play at.
	 * @param y Y to play at.
	 * @param z Z to play at.
	 * @param volume Volume to play at.
	 * @param pitch Pitch to play at. (0 for default)
	 * @return True if the sound was found in the registry.
	 */
	public boolean playSound(Player player, String sound, float x, float y, float z, float volume, float pitch);
	
	/**
	 * Plays the given sound.
	 * @param level Level to play the sound in.
	 * @param sound Sound to play.
	 * @param x X to play at.
	 * @param y Y to play at.
	 * @param z Z to play at.
	 * @param volume Volume to play at.
	 * @param pitch Pitch to play at. (0 for default)
	 * @return True if the sound was found in the registry.
	 */
	public boolean playSound(Level level, String sound, float x, float y, float z, float volume, float pitch);
	
	/**
	 * Plays the given music.
	 * @param music Music to play.
	 * @return True if the music was found in the registry.
	 */
	public boolean playMusic(String music);
	
	/**
	 * Plays the given music.
	 * @param player Player to play for.
	 * @param music Music to play.
	 * @return True if the music was found in the registry.
	 */
	public boolean playMusic(Player player, String music);
	
	/**
	 * Plays the given music.
	 * @param level Level to play the music in.
	 * @param music Music to play.
	 * @return True if the music was found in the registry.
	 */
	public boolean playMusic(Level level, String music);
	
	/**
	 * Plays the given music.
	 * @param music Music to play.
	 * @param loop Whether to loop the music.
	 * @return True if the music was found in the registry.
	 */
	public boolean playMusic(String music, boolean loop);
	
	/**
	 * Plays the given music.
	 * @param player Player to play for.
	 * @param music Music to play.
	 * @param loop Whether to loop the music.
	 * @return True if the music was found in the registry.
	 */
	public boolean playMusic(Player player, String music, boolean loop);
	
	/**
	 * Plays the given music.
	 * @param level Level to play the music in.
	 * @param music Music to play.
	 * @param loop Whether to loop the music.
	 * @return True if the music was found in the registry.
	 */
	public boolean playMusic(Level level, String music, boolean loop);
	
	/**
	 * Checks if any music is playing.
	 * @return True if music is playing.
	 */
	public boolean isPlayingMusic();
	
	/**
	 * Stops any playing music.
	 */
	public void stopMusic();
	
	/**
	 * Stops any playing music.
	 * @param Player to stop music for.
	 */
	public void stopMusic(Player player);
	
	/**
	 * Checks if the given music is playing.
	 * @param music Music to check for.
	 * @return True if the music is playing.
	 */
	public boolean isPlaying(String music);
	
	/**
	 * Stops the given music.
	 * @param music Music to stop.
	 */
	public void stop(String music);
	
	/**
	 * Stops the given music.
	 * @param music Music to stop.
	 * @param Player to stop music for.
	 */
	public void stop(Player player, String music);
	
}
