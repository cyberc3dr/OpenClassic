package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.PlayerRotationMessage;

import com.mojang.minecraft.entity.player.net.NetworkPlayer;

public class PlayerRotationMessageHandler extends MessageHandler<PlayerRotationMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerRotationMessage message) {
		if(message.getPlayerId() >= 0) {
			NetworkPlayer moving = (NetworkPlayer) ((ClientLevel) OpenClassic.getClient().getLevel()).getPlayer(message.getPlayerId());
			if(moving != null) {
				moving.queue(message.getYaw(), message.getPitch());
			}
		}
	}

}
