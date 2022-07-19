package org.spacehq.openclassic.server.network.handler.custom;

import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.plugin.RemotePluginInfo;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.PluginMessage;
import org.spacehq.openclassic.server.player.ServerPlayer;

public class PluginMessageHandler extends MessageHandler<PluginMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PluginMessage message) {
		((ServerPlayer) player).getClientInfo().addPlugin(new RemotePluginInfo(message.getName(), message.getVersion()));
	}

}
