package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.PlayerSpawnMessage;

import com.mojang.minecraft.entity.player.net.NetworkPlayer;

public class PlayerSpawnMessageHandler extends MessageHandler<PlayerSpawnMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerSpawnMessage message) {
		if(message.getPlayerId() >= 0) {
			ClientPlayer ocplayer = new ClientPlayer();
			ocplayer.setName(Color.stripColor(message.getName()));
			NetworkPlayer p = new NetworkPlayer((ClientLevel) OpenClassic.getClient().getLevel(), message.getPlayerId(), message.getName(), ocplayer, message.getX(), message.getY() - 0.6875f, message.getZ(), message.getYaw(), message.getPitch());
			ocplayer.setHandle(p);
			((ClientLevel) OpenClassic.getClient().getLevel()).addEntity(p);
		} else {
			OpenClassic.getClient().getLevel().setSpawn(new Position(OpenClassic.getClient().getLevel(), message.getX(), message.getY(), message.getZ(), message.getYaw(), message.getPitch()));
			OpenClassic.getClient().getPlayer().moveTo(message.getX(), message.getY(), message.getZ(), message.getYaw(), message.getPitch());
		}
	}

}
