package org.spacehq.openclassic.client.network.handler.custom;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.audio.MusicStopMessage;

public class MusicStopMessageHandler extends MessageHandler<MusicStopMessage> {

	@Override
	public void handle(ClassicSession session, Player player, MusicStopMessage message) {
		if(message.getIdentifier().equals("all_music")) {
			OpenClassic.getGame().getAudioManager().stopMusic();
		} else {
			OpenClassic.getGame().getAudioManager().stop(message.getIdentifier());
		}
	}

}
