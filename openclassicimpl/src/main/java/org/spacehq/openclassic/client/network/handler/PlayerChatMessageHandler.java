package org.spacehq.openclassic.client.network.handler;

import com.mojang.minecraft.entity.player.net.NetworkPlayer;
import com.zachsthings.onevent.EventManager;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.event.player.PlayerChatEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.PlayerChatMessage;

public class PlayerChatMessageHandler extends MessageHandler<PlayerChatMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerChatMessage message) {
		if(message.getPlayerId() < 0) {
			player.sendMessage(Color.YELLOW + message.getMessage());
		} else {
			NetworkPlayer sender = (NetworkPlayer) ((ClientLevel) OpenClassic.getClient().getLevel()).getPlayer(message.getPlayerId());
			if(sender != null) {
				PlayerChatEvent event = EventManager.callEvent(new PlayerChatEvent(sender.openclassic, message.getMessage()));
				if(event.isCancelled()) {
					return;
				}
			}
			
			player.sendMessage(message.getMessage());
		}
	}

}
