package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerDespawnMessage;

public class PlayerDespawnCodec extends MessageCodec<PlayerDespawnMessage> {

	public PlayerDespawnCodec() {
		super(PlayerDespawnMessage.class, (byte) 0x0c);
	}

	@Override
	public ChannelBuffer encode(PlayerDespawnMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);

		buffer.writeByte(message.getPlayerId());

		return buffer;
	}

	@Override
	public PlayerDespawnMessage decode(ChannelBuffer buffer) throws IOException {
		byte playerId = buffer.readByte();

		return new PlayerDespawnMessage(playerId);
	}

}
