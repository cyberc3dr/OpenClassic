package org.spacehq.openclassic.client.network.handler.custom;

import java.net.MalformedURLException;
import java.net.URL;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.audio.AudioRegisterMessage;

public class AudioRegisterMessageHandler extends MessageHandler<AudioRegisterMessage> {

	@Override
	public void handle(ClassicSession session, Player player, AudioRegisterMessage message) {
		try {
			if(message.isMusic()) {
				OpenClassic.getGame().getAudioManager().registerMusic(message.getIdentifier(), new URL(message.getUrl()), message.isIncluded());
			} else {
				OpenClassic.getGame().getAudioManager().registerSound(message.getIdentifier(), new URL(message.getUrl()), message.isIncluded());
			}
		} catch(MalformedURLException e) {
			OpenClassic.getLogger().warning("Audio URL \"" + message.getUrl() + "\" is invalid!");
		}
	}

}
