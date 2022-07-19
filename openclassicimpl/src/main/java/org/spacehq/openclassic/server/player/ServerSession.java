package org.spacehq.openclassic.server.player;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.event.player.PlayerKickEvent;
import org.spacehq.openclassic.api.event.player.PlayerQuitEvent;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.msg.BlockChangeMessage;
import org.spacehq.openclassic.game.network.msg.Message;
import org.spacehq.openclassic.game.network.msg.PingMessage;
import org.spacehq.openclassic.game.network.msg.PlayerDespawnMessage;
import org.spacehq.openclassic.game.network.msg.PlayerDisconnectMessage;
import org.spacehq.openclassic.game.network.msg.PlayerSpawnMessage;
import org.spacehq.openclassic.game.network.msg.PlayerTeleportMessage;
import org.spacehq.openclassic.server.ClassicServer;
import org.spacehq.openclassic.server.network.ServerHandlerLookup;

import com.zachsthings.onevent.EventManager;

public class ServerSession extends ClassicSession {

	private boolean pendingRemoval = false;
	public boolean disconnectMsgSent = false;
	private long lastPing = System.currentTimeMillis();

	public ServerSession(Channel channel) {
		super(new ServerHandlerLookup());
		this.channel = channel;
	}

	@Override
	public boolean tick() {
		if(this.pendingRemoval) {
			return false;
		}
		
		if(System.currentTimeMillis() - this.lastPing > 1000) {
			this.lastPing = System.currentTimeMillis();
			this.send(new PingMessage());
		}

		return super.tick();
	}

	@Override
	public void send(Message message) {
		if(message instanceof BlockChangeMessage && !this.getPlayer().hasCustomClient()) {
			BlockType block = Blocks.fromId(((BlockChangeMessage) message).getBlock());
			if(block.getId() > 49) {
				message = new BlockChangeMessage(((BlockChangeMessage) message).getX(), ((BlockChangeMessage) message).getY(), ((BlockChangeMessage) message).getZ(), VanillaBlock.STONE.getId());
			}
		}

		if(!this.canSendPlayerMessage(message, this.getPlayer())) return;
		super.send(message);
	}

	@Override
	public boolean sendCustomMessages() {
		return ((ServerPlayer) this.getPlayer()).hasCustomClient();
	}

	private boolean canSendPlayerMessage(Message message, Player player) {
		if(message instanceof PlayerDespawnMessage && ((PlayerDespawnMessage) message).getPlayerId() != -1 && !player.canSee(((ClassicServer) OpenClassic.getServer()).getPlayer(((PlayerDespawnMessage) message).getPlayerId()))) {
			return false;
		} else if(message instanceof PlayerTeleportMessage && ((PlayerTeleportMessage) message).getPlayerId() != -1 && !player.canSee(((ClassicServer) OpenClassic.getServer()).getPlayer(((PlayerTeleportMessage) message).getPlayerId()))) {
			return false;
		} else if(message instanceof PlayerSpawnMessage && ((PlayerSpawnMessage) message).getPlayerId() != -1 && !player.canSee(((ClassicServer) OpenClassic.getServer()).getPlayer(((PlayerSpawnMessage) message).getPlayerId()))) {
			return false;
		}

		return true;
	}

	@Override
	public void disconnect(String reason) {
		if(this.getPlayer() != null && this.getState() == State.GAME) {
			PlayerKickEvent event = EventManager.callEvent(new PlayerKickEvent(this.getPlayer(), reason, String.format(OpenClassic.getServer().getTranslator().translate("player.kicked"), this.getPlayer().getDisplayName(), reason)));
			if(event.isCancelled()) {
				return;
			}

			EventManager.callEvent(new PlayerQuitEvent(this.getPlayer(), event.getMessage()));
			OpenClassic.getServer().broadcastMessage(event.getMessage());
		} else {
			OpenClassic.getLogger().info(this.getAddress() + " disconnected by server: \"" + reason + "\"");
		}

		this.channel.write(new PlayerDisconnectMessage(reason)).addListener(ChannelFutureListener.CLOSE);
		this.disconnectMsgSent = true;
	}

	public void flagForRemoval() {
		this.pendingRemoval = true;
	}

	@Override
	public void dispose() {
		if(this.getPlayer() != null) {
			((ServerPlayer) this.getPlayer()).destroy();
		}

		super.dispose();
	}

}
