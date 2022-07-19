package org.spacehq.openclassic.game.network.msg.custom.audio;

import org.spacehq.openclassic.game.network.msg.Message;

/**
 * Sent by the server when an audio file is registered.
 */
public class AudioRegisterMessage extends Message {

	private String identifier;
	private String url;
	private boolean included;
	private boolean music;
	
	public AudioRegisterMessage(String identifier, String url, boolean included, boolean music) {
		this.identifier = identifier;
		this.url = url;
		this.included = included;
		this.music = music;
	}
	
	/**
	 * Gets the identifier of the audio file.
	 * @return The audio file's identifier.
	 */
	public String getIdentifier() {
		return this.identifier;
	}
	
	/**
	 * Gets the URL of the audio file.
	 * @return The audio file's URL.
	 */
	public String getUrl() {
		return this.url;
	}
	
	/**
	 * Returns true if the audio file is included with the client.
	 * @return True if the audio file is included.
	 */
	public boolean isIncluded() {
		return this.included;
	}
	
	/**
	 * Returns true if music is being registered.
	 * @return True if music is being registered.
	 */
	public boolean isMusic() {
		return this.music;
	}
	
	@Override
	public String toString() {
		return "AudioRegisterMessage{identifier=" + identifier + ",url=" + url + ",included=" + included + ",music=" + music + "}";
	}

	@Override
	public byte getOpcode() {
		return 22;
	}

}
