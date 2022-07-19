package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.network.ClientSession;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.ClassicSession.State;
import org.spacehq.openclassic.game.network.msg.LevelFinalizeMessage;
import org.spacehq.openclassic.game.network.MessageHandler;

public class LevelFinalizeMessageHandler extends MessageHandler<LevelFinalizeMessage> {

	@Override
	public void handle(ClassicSession session, Player player, LevelFinalizeMessage message) {
		byte data[] = ((ClientSession) session).finishLevel();
		ClientLevel level = new ClientLevel();
		level.setName("ServerLevel");
		level.setAuthor(OpenClassic.getClient().getServerIP());
		level.setCreationTime(System.currentTimeMillis());
		level.setData(message.getWidth(), message.getHeight(), message.getDepth(), data);
		((ClassicClient) OpenClassic.getClient()).setLevel(level);
		session.setState(State.GAME);
		OpenClassic.getClient().getProgressBar().setSubtitleScaled(true);
		OpenClassic.getClient().getProgressBar().setVisible(false);
	}

}
