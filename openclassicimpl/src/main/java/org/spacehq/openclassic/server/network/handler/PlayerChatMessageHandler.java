package org.spacehq.openclassic.server.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.event.player.PlayerChatEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.ClassicSession.State;
import org.spacehq.openclassic.game.network.msg.PlayerChatMessage;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.server.ClassicServer;
import org.spacehq.openclassic.server.player.ServerPlayer;

import com.zachsthings.onevent.EventManager;

public class PlayerChatMessageHandler extends MessageHandler<PlayerChatMessage> {

	@SuppressWarnings("unused")
	private static final String illegalChars = "[^0-9a-zA-Z!\"#$%&'()*+,-./:;<=>?@[\\]^_{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«]";

	@Override
	public void handle(ClassicSession session, Player player, PlayerChatMessage message) {
		if(session.getState() != State.GAME) return;

		String chat = message.getMessage().trim();

		for(char ch : chat.toCharArray()) {
			if(ch < 32 || ch >= 127) {
				session.disconnect("Illegal character in chat!");
				return;
			}
		}

		if(chat.startsWith("/")) {
			((ClassicServer) OpenClassic.getGame()).processCommand(player, chat.substring(1, chat.length()));
		} else {
			PlayerChatEvent event = EventManager.callEvent(new PlayerChatEvent(player, chat));
			if(event.isCancelled()) return;

			((ClassicServer) OpenClassic.getServer()).sendToAll(new PlayerChatMessage(((ServerPlayer) player).getPlayerId(), String.format(event.getFormat(), player.getDisplayName(), event.getMessage())));
			OpenClassic.getLogger().info(String.format(event.getFormat(), player.getDisplayName(), event.getMessage()));
		}
	}

}
