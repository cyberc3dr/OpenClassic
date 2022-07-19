package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.network.ClientSession;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.LevelDataMessage;

public class LevelDataMessageHandler extends MessageHandler<LevelDataMessage> {

	@Override
	public void handle(ClassicSession session, Player player, LevelDataMessage message) {
		OpenClassic.getClient().getProgressBar().setProgress(message.getPercent());
		((ClientSession) session).appendLevelData(message.getData(), message.getLength());
	}

}
