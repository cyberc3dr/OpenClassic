package org.spacehq.openclassic.client.network.handler.custom;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.audio.AudioPlayMessage;

public class AudioPlayMessageHandler extends MessageHandler<AudioPlayMessage> {

	@Override
	public void handle(ClassicSession session, Player player, AudioPlayMessage message) {
		if(message.isMusic()) {
			OpenClassic.getGame().getAudioManager().playMusic(message.getIdentifier(), message.isLooping());
		} else {
			OpenClassic.getGame().getAudioManager().playSound(message.getIdentifier(), message.getX(), message.getY(), message.getZ(), message.getVolume(), message.getPitch());
		}
	}

}
