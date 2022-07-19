package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.PlayerPositionMessage;

import com.mojang.minecraft.entity.player.net.NetworkPlayer;

public class PlayerPositionMessageHandler extends MessageHandler<PlayerPositionMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerPositionMessage message) {
		if(message.getPlayerId() >= 0) {
			NetworkPlayer moving = (NetworkPlayer) ((ClientLevel) OpenClassic.getClient().getLevel()).getPlayer(message.getPlayerId());
			if(moving != null) {
				moving.queue((byte) (message.getXChange() * 32), (byte) (message.getYChange() * 32), (byte) (message.getZChange() * 32));
			}
		}
	}

}
