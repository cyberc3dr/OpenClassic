package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.event.player.PlayerKickEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.PlayerDisconnectMessage;

import com.zachsthings.onevent.EventManager;

public class PlayerDisconnectMessageHandler extends MessageHandler<PlayerDisconnectMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerDisconnectMessage message) {
		PlayerKickEvent event = EventManager.callEvent(new PlayerKickEvent(OpenClassic.getClient().getPlayer(), message.getMessage(), ""));
		session.disconnect(event.getReason());
	}

}
