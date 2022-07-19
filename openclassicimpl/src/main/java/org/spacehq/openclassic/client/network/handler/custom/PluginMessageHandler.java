package org.spacehq.openclassic.client.network.handler.custom;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.plugin.RemotePluginInfo;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.PluginMessage;

public class PluginMessageHandler extends MessageHandler<PluginMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PluginMessage message) {
		((ClassicClient) OpenClassic.getClient()).addPluginInfo(new RemotePluginInfo(message.getName(), message.getVersion()));
	}

}
