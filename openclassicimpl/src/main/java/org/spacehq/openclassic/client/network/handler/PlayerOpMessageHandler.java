package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.PlayerOpMessage;
import org.spacehq.openclassic.game.util.InternalConstants;

public class PlayerOpMessageHandler extends MessageHandler<PlayerOpMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerOpMessage message) {
		if(message.getOp() == InternalConstants.OP) {
			player.setCanBreakUnbreakables(true);
		} else if(message.getOp() == InternalConstants.NOT_OP) {
			player.setCanBreakUnbreakables(false);
		}
	}

}
