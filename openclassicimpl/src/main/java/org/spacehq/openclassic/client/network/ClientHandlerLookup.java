package org.spacehq.openclassic.client.network;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.client.network.handler.BlockChangeMessageHandler;
import org.spacehq.openclassic.client.network.handler.IdentificationMessageHandler;
import org.spacehq.openclassic.client.network.handler.LevelDataMessageHandler;
import org.spacehq.openclassic.client.network.handler.LevelFinalizeMessageHandler;
import org.spacehq.openclassic.client.network.handler.LevelInitializeMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerChatMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerDespawnMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerDisconnectMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerOpMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerPositionMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerPositionRotationMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerRotationMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerSpawnMessageHandler;
import org.spacehq.openclassic.client.network.handler.PlayerTeleportMessageHandler;
import org.spacehq.openclassic.client.network.handler.custom.AudioPlayMessageHandler;
import org.spacehq.openclassic.client.network.handler.custom.AudioRegisterMessageHandler;
import org.spacehq.openclassic.client.network.handler.custom.CustomBlockMessageHandler;
import org.spacehq.openclassic.client.network.handler.custom.CustomMessageHandler;
import org.spacehq.openclassic.client.network.handler.custom.GameInfoMessageHandler;
import org.spacehq.openclassic.client.network.handler.custom.LevelPropertyMessageHandler;
import org.spacehq.openclassic.client.network.handler.custom.MusicStopMessageHandler;
import org.spacehq.openclassic.client.network.handler.custom.PluginMessageHandler;
import org.spacehq.openclassic.game.network.HandlerLookup;
import org.spacehq.openclassic.game.network.msg.BlockChangeMessage;
import org.spacehq.openclassic.game.network.msg.IdentificationMessage;
import org.spacehq.openclassic.game.network.msg.LevelDataMessage;
import org.spacehq.openclassic.game.network.msg.LevelFinalizeMessage;
import org.spacehq.openclassic.game.network.msg.LevelInitializeMessage;
import org.spacehq.openclassic.game.network.msg.PlayerChatMessage;
import org.spacehq.openclassic.game.network.msg.PlayerDespawnMessage;
import org.spacehq.openclassic.game.network.msg.PlayerDisconnectMessage;
import org.spacehq.openclassic.game.network.msg.PlayerOpMessage;
import org.spacehq.openclassic.game.network.msg.PlayerPositionMessage;
import org.spacehq.openclassic.game.network.msg.PlayerPositionRotationMessage;
import org.spacehq.openclassic.game.network.msg.PlayerRotationMessage;
import org.spacehq.openclassic.game.network.msg.PlayerSpawnMessage;
import org.spacehq.openclassic.game.network.msg.PlayerTeleportMessage;
import org.spacehq.openclassic.game.network.msg.custom.CustomMessage;
import org.spacehq.openclassic.game.network.msg.custom.GameInfoMessage;
import org.spacehq.openclassic.game.network.msg.custom.LevelPropertyMessage;
import org.spacehq.openclassic.game.network.msg.custom.PluginMessage;
import org.spacehq.openclassic.game.network.msg.custom.audio.AudioPlayMessage;
import org.spacehq.openclassic.game.network.msg.custom.audio.AudioRegisterMessage;
import org.spacehq.openclassic.game.network.msg.custom.audio.MusicStopMessage;
import org.spacehq.openclassic.game.network.msg.custom.block.CustomBlockMessage;

public class ClientHandlerLookup extends HandlerLookup {

	public ClientHandlerLookup() {
		try {
			this.bind(IdentificationMessage.class, IdentificationMessageHandler.class);
			this.bind(LevelInitializeMessage.class, LevelInitializeMessageHandler.class);
			this.bind(LevelDataMessage.class, LevelDataMessageHandler.class);
			this.bind(LevelFinalizeMessage.class, LevelFinalizeMessageHandler.class);
			this.bind(BlockChangeMessage.class, BlockChangeMessageHandler.class);
			this.bind(PlayerSpawnMessage.class, PlayerSpawnMessageHandler.class);
			this.bind(PlayerTeleportMessage.class, PlayerTeleportMessageHandler.class);
			this.bind(PlayerPositionRotationMessage.class, PlayerPositionRotationMessageHandler.class);
			this.bind(PlayerPositionMessage.class, PlayerPositionMessageHandler.class);
			this.bind(PlayerRotationMessage.class, PlayerRotationMessageHandler.class);
			this.bind(PlayerDespawnMessage.class, PlayerDespawnMessageHandler.class);
			this.bind(PlayerChatMessage.class, PlayerChatMessageHandler.class);
			this.bind(PlayerDisconnectMessage.class, PlayerDisconnectMessageHandler.class);
			this.bind(PlayerOpMessage.class, PlayerOpMessageHandler.class);

			// Custom
			this.bind(GameInfoMessage.class, GameInfoMessageHandler.class);
			this.bind(CustomBlockMessage.class, CustomBlockMessageHandler.class);
			this.bind(LevelPropertyMessage.class, LevelPropertyMessageHandler.class);
			this.bind(AudioRegisterMessage.class, AudioRegisterMessageHandler.class);
			this.bind(AudioPlayMessage.class, AudioPlayMessageHandler.class);
			this.bind(MusicStopMessage.class, MusicStopMessageHandler.class);
			this.bind(PluginMessage.class, PluginMessageHandler.class);
			this.bind(CustomMessage.class, CustomMessageHandler.class);
		} catch(Exception e) {
			OpenClassic.getLogger().severe("Failed to register network messages!");
			e.printStackTrace();
		}
	}

}
