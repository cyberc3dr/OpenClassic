package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.network.msg.BlockChangeMessage;

public class BlockChangeMessageHandler extends MessageHandler<BlockChangeMessage> {

	@Override
	public void handle(ClassicSession session, Player player, BlockChangeMessage message) {
		if(OpenClassic.getClient().getLevel() != null) {
			OpenClassic.getClient().getLevel().setBlockAt(message.getX(), message.getY(), message.getZ(), Blocks.fromId(message.getBlock()));
		}
	}

}
