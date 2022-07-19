package org.spacehq.openclassic.client.network.handler.custom;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.LevelPropertyMessage;

public class LevelPropertyMessageHandler extends MessageHandler<LevelPropertyMessage> {

	@Override
	public void handle(ClassicSession session, Player player, LevelPropertyMessage message) {
		if(message.getType().equals("sky")) {
			OpenClassic.getClient().getLevel().setSkyColor(message.getValue());
		} else if(message.getType().equals("fog")) {
			OpenClassic.getClient().getLevel().setFogColor(message.getValue());
		} else if(message.getType().equals("cloud")) {
			OpenClassic.getClient().getLevel().setCloudColor(message.getValue());
		} else if(message.getType().equals("raining")) {
			OpenClassic.getClient().getLevel().setRaining(message.getValue() == 1 ? true : false);
		}
	}

}
