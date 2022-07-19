package org.spacehq.openclassic.game.network;

import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.jboss.netty.channel.Channel;

import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.msg.*;

public abstract class ClassicSession {

	private static final List<Class<? extends Message>> VANILLA = new ArrayList<Class<? extends Message>>();
	
	static {
		VANILLA.add(IdentificationMessage.class);
		VANILLA.add(PingMessage.class);
		VANILLA.add(LevelInitializeMessage.class);
		VANILLA.add(LevelDataMessage.class);
		VANILLA.add(LevelFinalizeMessage.class);
		VANILLA.add(PlayerSetBlockMessage.class);
		VANILLA.add(BlockChangeMessage.class);
		VANILLA.add(PlayerSpawnMessage.class);
		VANILLA.add(PlayerTeleportMessage.class);
		VANILLA.add(PlayerPositionRotationMessage.class);
		VANILLA.add(PlayerPositionMessage.class);
		VANILLA.add(PlayerRotationMessage.class);
		VANILLA.add(PlayerDespawnMessage.class);
		VANILLA.add(PlayerChatMessage.class);
		VANILLA.add(PlayerDisconnectMessage.class);
		VANILLA.add(PlayerOpMessage.class);
	}
	
	protected Channel channel;
	private final Queue<Message> messageQueue = new ArrayDeque<Message>();
	private State state = State.IDENTIFYING;
	private Player player;
	private HandlerLookup lookup;

	public ClassicSession(HandlerLookup lookup) {
		this.lookup = lookup;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		if(this.player != null) {
			throw new IllegalStateException();
		}

		this.player = player;
	}

	@SuppressWarnings("unchecked")
	public boolean tick() {
		Message message;
		while((message = this.messageQueue.poll()) != null) {
			MessageHandler<Message> handler = (MessageHandler<Message>) this.lookup.find(message.getClass());
			if(handler != null) {
				handler.handle(this, this.player, message);
			}
		}

		return true;
	}

	public void send(Message message) {
		if(!this.isConnected()) {
			return;
		}

		if(!VANILLA.contains(message.getClass()) && !this.sendCustomMessages()) {
			return;
		}

		this.channel.write(message);
	}

	public abstract boolean sendCustomMessages();

	public boolean isConnected() {
		return this.channel != null && this.channel.isOpen();
	}

	public void disconnect(String reason) {
		this.channel.close();
	}

	public SocketAddress getAddress() {
		return this.channel.getRemoteAddress();
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [address=" + this.channel.getRemoteAddress() + "]";
	}

	public <T extends Message> void messageReceived(T message) {
		this.messageQueue.add(message);
	}

	public void dispose() {
		if(this.player != null) {
			this.player = null;
		}
	}
	
	public enum State {
		IDENTIFYING,
		PREPARING,
		GAME;
	}

}
