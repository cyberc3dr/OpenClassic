package org.spacehq.openclassic.server.network;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.game.network.HandlerLookup;
import org.spacehq.openclassic.game.network.msg.IdentificationMessage;
import org.spacehq.openclassic.game.network.msg.PlayerChatMessage;
import org.spacehq.openclassic.game.network.msg.PlayerSetBlockMessage;
import org.spacehq.openclassic.game.network.msg.PlayerTeleportMessage;
import org.spacehq.openclassic.game.network.msg.custom.CustomMessage;
import org.spacehq.openclassic.game.network.msg.custom.GameInfoMessage;
import org.spacehq.openclassic.game.network.msg.custom.KeyChangeMessage;
import org.spacehq.openclassic.game.network.msg.custom.PluginMessage;
import org.spacehq.openclassic.server.network.handler.IdentificationMessageHandler;
import org.spacehq.openclassic.server.network.handler.PlayerChatMessageHandler;
import org.spacehq.openclassic.server.network.handler.PlayerSetBlockMessageHandler;
import org.spacehq.openclassic.server.network.handler.PlayerTeleportMessageHandler;
import org.spacehq.openclassic.server.network.handler.custom.CustomMessageHandler;
import org.spacehq.openclassic.server.network.handler.custom.GameInfoMessageHandler;
import org.spacehq.openclassic.server.network.handler.custom.KeyChangeMessageHandler;
import org.spacehq.openclassic.server.network.handler.custom.PluginMessageHandler;

public class ServerHandlerLookup extends HandlerLookup {

	public ServerHandlerLookup() {
		try {
			this.bind(IdentificationMessage.class, IdentificationMessageHandler.class);
			this.bind(PlayerSetBlockMessage.class, PlayerSetBlockMessageHandler.class);
			this.bind(PlayerTeleportMessage.class, PlayerTeleportMessageHandler.class);
			this.bind(PlayerChatMessage.class, PlayerChatMessageHandler.class);

			// Custom
			this.bind(GameInfoMessage.class, GameInfoMessageHandler.class);
			this.bind(KeyChangeMessage.class, KeyChangeMessageHandler.class);
			this.bind(PluginMessage.class, PluginMessageHandler.class);
			this.bind(CustomMessage.class, CustomMessageHandler.class);
		} catch(Exception e) {
			OpenClassic.getLogger().severe("Failed to register network messages!");
			e.printStackTrace();
		}
	}

}
