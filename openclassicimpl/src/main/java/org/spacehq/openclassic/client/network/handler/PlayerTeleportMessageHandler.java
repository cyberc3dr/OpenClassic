package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.PlayerTeleportMessage;

import com.mojang.minecraft.entity.player.net.NetworkPlayer;

public class PlayerTeleportMessageHandler extends MessageHandler<PlayerTeleportMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerTeleportMessage message) {
		if(message.getPlayerId() < 0) {
			OpenClassic.getClient().getPlayer().moveTo(message.getX(), message.getY(), message.getZ(), message.getYaw(), message.getPitch());
		} else {
			NetworkPlayer moving = (NetworkPlayer) ((ClientLevel) OpenClassic.getClient().getLevel()).getPlayer(message.getPlayerId());
			if(moving != null) {
				moving.teleport((short) (message.getX() * 32), (short) ((message.getY() * 32) - 22), (short) (message.getZ() * 32), message.getYaw(), message.getPitch());
			}
		}
	}

}
