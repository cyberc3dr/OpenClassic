package org.spacehq.openclassic.server.network.handler.custom;

import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.GameInfoMessage;
import org.spacehq.openclassic.server.player.ServerPlayer;

public class GameInfoMessageHandler extends MessageHandler<GameInfoMessage> {

	@Override
	public void handle(ClassicSession session, final Player player, GameInfoMessage message) {
		((ServerPlayer) player).getClientInfo().setVersion(message.getVersion());
		((ServerPlayer) player).getClientInfo().setLanguage(message.getLanguage());
	}

}
