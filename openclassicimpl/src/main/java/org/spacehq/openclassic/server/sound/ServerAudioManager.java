package org.spacehq.openclassic.server.sound;

import java.net.URL;

import org.apache.commons.lang3.Validate;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.sound.AudioManager;
import org.spacehq.openclassic.game.network.msg.custom.audio.AudioPlayMessage;
import org.spacehq.openclassic.game.network.msg.custom.audio.AudioRegisterMessage;
import org.spacehq.openclassic.game.network.msg.custom.audio.MusicStopMessage;
import org.spacehq.openclassic.server.player.ServerPlayer;

public class ServerAudioManager implements AudioManager {

	@Override
	public void registerSound(String sound, URL file, boolean included) {
		Validate.notNull(sound, "Sound cannot be null.");
		Validate.notNull(file, "URL cannot be null.");
		for(Player player : OpenClassic.getServer().getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioRegisterMessage(sound, file.getPath(), included, false));
		}
	}

	@Override
	public void registerMusic(String music, URL file, boolean included) {
		Validate.notNull(music, "Music cannot be null.");
		Validate.notNull(file, "URL cannot be null.");
		for(Player player : OpenClassic.getServer().getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioRegisterMessage(music, file.getPath(), included, true));
		}
	}

	@Override
	public boolean playSound(String sound, float volume, float pitch) {
		for(Player player : OpenClassic.getServer().getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioPlayMessage(sound, 0, 0, 0, volume, pitch, false, false));
		}

		return true;
	}

	@Override
	public boolean playSound(Player player, String sound, float volume, float pitch) {
		((ServerPlayer) player).getSession().send(new AudioPlayMessage(sound, 0, 0, 0, volume, pitch, false, false));
		return true;
	}
	
	@Override
	public boolean playSound(Level level, String sound, float volume, float pitch) {
		for(Player player : level.getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioPlayMessage(sound, 0, 0, 0, volume, pitch, false, false));
		}

		return true;
	}

	@Override
	public boolean playSound(String sound, float x, float y, float z, float volume, float pitch) {
		for(Player player : OpenClassic.getServer().getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioPlayMessage(sound, x, y, z, volume, pitch, false, false));
		}

		return true;
	}

	@Override
	public boolean playSound(Player player, String sound, float x, float y, float z, float volume, float pitch) {
		((ServerPlayer) player).getSession().send(new AudioPlayMessage(sound, x, y, z, volume, pitch, false, false));
		return true;
	}
	
	@Override
	public boolean playSound(Level level, String sound, float x, float y, float z, float volume, float pitch) {
		for(Player player : level.getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioPlayMessage(sound, x, y, z, volume, pitch, false, false));
		}

		return true;
	}

	@Override
	public boolean playMusic(String music) {
		for(Player player : OpenClassic.getServer().getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioPlayMessage(music, 0, 0, 0, 1, 1, true, false));
		}

		return true;
	}

	@Override
	public boolean playMusic(Player player, String music) {
		((ServerPlayer) player).getSession().send(new AudioPlayMessage(music, 0, 0, 0, 1, 1, true, false));
		return true;
	}
	
	@Override
	public boolean playMusic(Level level, String music) {
		for(Player player : level.getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioPlayMessage(music, 0, 0, 0, 1, 1, true, false));
		}

		return true;
	}

	@Override
	public boolean playMusic(String music, boolean loop) {
		for(Player player : OpenClassic.getServer().getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioPlayMessage(music, 0, 0, 0, 1, 1, true, true));
		}

		return true;
	}

	@Override
	public boolean playMusic(Player player, String music, boolean loop) {
		((ServerPlayer) player).getSession().send(new AudioPlayMessage(music, 0, 0, 0, 1, 1, true, true));
		return true;
	}
	
	@Override
	public boolean playMusic(Level level, String music, boolean loop) {
		for(Player player : level.getPlayers()) {
			((ServerPlayer) player).getSession().send(new AudioPlayMessage(music, 0, 0, 0, 1, 1, true, true));
		}

		return true;
	}

	@Override
	public boolean isPlayingMusic() {
		return true;
	}

	@Override
	public void stopMusic() {
		for(Player player : OpenClassic.getServer().getPlayers()) {
			((ServerPlayer) player).getSession().send(new MusicStopMessage("all_music"));
		}
	}

	@Override
	public void stopMusic(Player player) {
		((ServerPlayer) player).getSession().send(new MusicStopMessage("all_music"));
	}

	@Override
	public boolean isPlaying(String music) {
		return false;
	}

	@Override
	public void stop(String music) {
		for(Player player : OpenClassic.getServer().getPlayers()) {
			((ServerPlayer) player).getSession().send(new MusicStopMessage(music));
		}
	}

	@Override
	public void stop(Player player, String music) {
		((ServerPlayer) player).getSession().send(new MusicStopMessage(music));
	}

}
