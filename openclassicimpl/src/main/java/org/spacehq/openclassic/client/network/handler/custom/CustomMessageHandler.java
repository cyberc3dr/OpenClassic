package org.spacehq.openclassic.client.network.handler.custom;

import org.spacehq.openclassic.api.event.player.CustomMessageEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.CustomMessage;

import com.zachsthings.onevent.EventManager;

public class CustomMessageHandler extends MessageHandler<CustomMessage> {

	@Override
	public void handle(ClassicSession session, Player player, CustomMessage message) {
		EventManager.callEvent(new CustomMessageEvent(player, message.getId(), message.getData()));
	}

}
