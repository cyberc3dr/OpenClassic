package org.spacehq.openclassic.server.network.handler;

import org.spacehq.openclassic.api.block.Block;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.event.block.BlockBreakEvent;
import org.spacehq.openclassic.api.event.block.BlockPlaceEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.ClassicSession.State;
import org.spacehq.openclassic.game.network.msg.BlockChangeMessage;
import org.spacehq.openclassic.game.network.msg.PlayerSetBlockMessage;
import org.spacehq.openclassic.game.network.MessageHandler;

import com.zachsthings.onevent.EventManager;

public class PlayerSetBlockMessageHandler extends MessageHandler<PlayerSetBlockMessage> {

	@Override
	public void handle(ClassicSession session, Player player, PlayerSetBlockMessage message) {
		if(session.getState() != State.GAME) return;

		BlockType b = Blocks.fromId(message.getBlock());
		if(b == null || !b.isSelectable()) {
			player.sendMessage("server.denied-hack.block-type");
			return;
		}

		// TODO: Reach hack checks and check if player is in position

		BlockType old = player.getPosition().getLevel().getBlockTypeAt(message.getX(), message.getY(), message.getZ());
		if(!message.isPlacing() && old.isUnbreakable() && !player.hasPermission("openclassic.commands.solid")) {
			player.sendMessage("server.denied-hack.block-break");
			return;
		}

		Block block = player.getPosition().getLevel().getBlockAt(message.getX(), message.getY(), message.getZ());
		byte type = (message.isPlacing()) ? message.getBlock() : 0;
		if(message.isPlacing() && player.getPlaceMode() != null && type != 0) type = player.getPlaceMode().getId();

		if(!message.isPlacing()) {
			if(EventManager.callEvent(new BlockBreakEvent(block, player, Blocks.fromId(message.getBlock()))).isCancelled()) {
				session.send(new BlockChangeMessage((short) block.getPosition().getBlockX(), (short) block.getPosition().getBlockY(), (short) block.getPosition().getBlockZ(), block.getTypeId()));
				return;
			}
		}

		player.getPosition().getLevel().setBlockIdAt(message.getX(), message.getY(), message.getZ(), type);
		if(message.isPlacing()) {
			if(EventManager.callEvent(new BlockPlaceEvent(block, player, Blocks.fromId(message.getBlock()))).isCancelled()) {
				if(player.getPosition().getLevel().getBlockIdAt(message.getX(), message.getY(), message.getZ()) == type) {
					player.getPosition().getLevel().setBlockAt(message.getX(), message.getY(), message.getZ(), old);
				}

				return;
			}
		}

		if(block != null && block.getType() != null && block.getType().getPhysics() != null) {
			if(message.isPlacing()) {
				block.getType().getPhysics().onPlace(block);
			} else {
				block.getType().getPhysics().onBreak(block);
			}
		}
	}

}
