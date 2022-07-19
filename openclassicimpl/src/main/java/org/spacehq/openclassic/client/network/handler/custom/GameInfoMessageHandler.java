package org.spacehq.openclassic.client.network.handler.custom;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.plugin.Plugin;
import org.spacehq.openclassic.api.util.Constants;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.custom.GameInfoMessage;
import org.spacehq.openclassic.game.network.msg.custom.PluginMessage;

public class GameInfoMessageHandler extends MessageHandler<GameInfoMessage> {

	@Override
	public void handle(ClassicSession session, final Player player, GameInfoMessage message) {
		OpenClassic.getLogger().info("Connected to OpenClassic v" + message.getVersion() + "!");
		((ClassicClient) OpenClassic.getClient()).setConnectedToOpenClassic(true);
		((ClassicClient) OpenClassic.getClient()).setServerVersion(message.getVersion());
		session.send(new GameInfoMessage(Constants.VERSION, OpenClassic.getGame().getLanguage()));
		for(Plugin plugin : OpenClassic.getClient().getPluginManager().getPlugins()) {
			session.send(new PluginMessage(plugin.getDescription().getName(), plugin.getDescription().getVersion()));
		}
	}

}
