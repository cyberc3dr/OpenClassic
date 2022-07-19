package org.spacehq.openclassic.game.network.msg.custom.audio;

import org.spacehq.openclassic.game.network.msg.Message;

/**
 * Sent from the server when audio is played.
 */
public class AudioPlayMessage extends Message {

	private String identifier;
	private float x;
	private float y;
	private float z;
	private float volume;
	private float pitch;
	private boolean music;
	private boolean loop;
	
	public AudioPlayMessage(String identifier, float x, float y, float z, float volume, float pitch, boolean music, boolean loop) {
		this.identifier = identifier;
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.pitch = pitch;
		this.music = music;
		this.loop = loop;
	}
	
	/**
	 * Gets the identifier of the audio.
	 * @return The audio's identifier.
	 */
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * Gets the X the audio is being played at. (sound only)
	 * @return The X of the audio.
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Gets the Y the audio is being played at. (sound only)
	 * @return The Y of the audio.
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Gets the Z the audio is being played at. (sound only)
	 * @return The Z of the audio.
	 */
	public float getZ() {
		return this.z;
	}
	
	/**
	 * Gets the volume the audio is being played at.
	 * @return The volume of the audio.
	 */
	public float getVolume() {
		return this.volume;
	}
	
	/**
	 * Gets the pitch the audio is being played at.
	 * @return The pitch of the audio.
	 */
	public float getPitch() {
		return this.pitch;
	}
	
	/**
	 * Returns true if the audio is music.
	 * @return True if the audio is music.
	 */
	public boolean isMusic() {
		return this.music;
	}
	
	/**
	 * Returns true if the audio is looping (music only)
	 * @return True if the audio is looping.
	 */
	public boolean isLooping() {
		return this.loop;
	}

	@Override
	public String toString() {
		return "AudioPlayMessage{identifier=" + identifier + ",x=" + x + ",y=" + y + ",z=" + z + ",volume=" + volume + ",pitch=" + pitch + ",music=" + music + ",loop=" + loop + "}";
	}

	@Override
	public byte getOpcode() {
		return 23;
	}
	
}
