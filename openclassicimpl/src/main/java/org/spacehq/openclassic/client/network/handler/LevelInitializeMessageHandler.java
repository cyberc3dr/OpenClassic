package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.network.ClientSession;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.LevelInitializeMessage;

public class LevelInitializeMessageHandler extends MessageHandler<LevelInitializeMessage> {

	@Override
	public void handle(ClassicSession session, Player player, LevelInitializeMessage message) {
		if(!OpenClassic.getClient().isConnectedToOpenClassic()) {
			VanillaBlock.registerAll();
		}

		((ClassicClient) OpenClassic.getClient()).setLevel((ClientLevel) null);
		OpenClassic.getClient().setActiveComponent(null);
		((ClientSession) session).prepareForLevel();
	}

}
