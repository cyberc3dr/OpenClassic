package org.spacehq.openclassic.server.network.handler;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.event.player.PlayerMoveEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.ClassicSession.State;
import org.spacehq.openclassic.game.network.msg.PlayerTeleportMessage;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.server.level.ServerLevel;
import org.spacehq.openclassic.server.player.ServerPlayer;

import com.zachsthings.onevent.EventManager;

public class PlayerTeleportMessageHandler extends MessageHandler<PlayerTeleportMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerTeleportMessage message) {
		if(session.getState() != State.GAME) return;

		Position to = new Position(player.getPosition().getLevel(), message.getX(), message.getY(), message.getZ(), message.getYaw(), message.getPitch());
		/*
		 * TODO: detect teleports properly if(!player.teleported && (to.getX() -
		 * player.getPosition().getX() >= 3 || to.getX() -
		 * player.getPosition().getX() <= -3 || to.getZ() -
		 * player.getPosition().getZ() >= 3 || to.getZ() -
		 * player.getPosition().getZ() <= -3)) { PlayerRespawnEvent event =
		 * EventManager.callEvent(new PlayerRespawnEvent(player, to));
		 * if(event.isCancelled()) { session.send(new
		 * PlayerTeleportMessage((byte) -1, player.getPosition().getX(),
		 * player.getPosition().getY(), player.getPosition().getZ(),
		 * player.getPosition().getYaw(), player.getPosition().getPitch()));
		 * return; } to = event.getPosition(); }
		 */

		PlayerMoveEvent event = EventManager.callEvent(new PlayerMoveEvent(player, player.getPosition(), to));
		Position old = to;
		to = event.getTo();

		if(event.isCancelled()) {
			session.send(new PlayerTeleportMessage((byte) -1, event.getFrom().getX(), event.getFrom().getY(), event.getFrom().getZ(), event.getFrom().getYaw(), event.getFrom().getPitch()));
			return;
		}

		if(!to.equals(old)) {
			session.send(new PlayerTeleportMessage((byte) -1, to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch()));
		}

		player.getPosition().setX(to.getX());
		player.getPosition().setY(to.getY());
		player.getPosition().setZ(to.getZ());
		player.getPosition().setYaw(to.getYaw());
		player.getPosition().setPitch(to.getPitch());

		((ServerLevel) player.getPosition().getLevel()).sendToAllExcept(player, new PlayerTeleportMessage(((ServerPlayer) player).getPlayerId(), to.getX(), to.getY() + 0.59375f, to.getZ(), (byte) to.getYaw(), (byte) to.getPitch()));
	}

}
