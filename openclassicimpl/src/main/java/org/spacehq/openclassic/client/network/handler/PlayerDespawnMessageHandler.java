package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.PlayerDespawnMessage;

import com.mojang.minecraft.entity.player.net.NetworkPlayer;

public class PlayerDespawnMessageHandler extends MessageHandler<PlayerDespawnMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerDespawnMessage message) {
		NetworkPlayer despawning = (NetworkPlayer) ((ClientLevel) OpenClassic.getClient().getLevel()).getPlayer(message.getPlayerId());
		if(message.getPlayerId() >= 0 && despawning != null) {
			despawning.clear();
			((ClientLevel) OpenClassic.getClient().getLevel()).removeEntity(despawning);
		}
	}

}
