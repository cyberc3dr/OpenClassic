package org.spacehq.openclassic.server.network.handler.custom;

import org.spacehq.openclassic.api.event.player.PlayerKeyChangeEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.KeyChangeMessage;

import com.zachsthings.onevent.EventManager;

public class KeyChangeMessageHandler extends MessageHandler<KeyChangeMessage> {

	@Override
	public void handle(ClassicSession session, Player player, KeyChangeMessage message) {
		EventManager.callEvent(new PlayerKeyChangeEvent(player, message.getKey(), message.isPressed()));

		// TODO: Add handling if player has gui open
	}

}
