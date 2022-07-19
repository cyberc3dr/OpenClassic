package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerDisconnectMessage;
import org.spacehq.openclassic.server.util.ChannelBufferUtils;

public class PlayerDisconnectCodec extends MessageCodec<PlayerDisconnectMessage> {

	public PlayerDisconnectCodec() {
		super(PlayerDisconnectMessage.class, (byte) 0x0e);
	}

	@Override
	public ChannelBuffer encode(PlayerDisconnectMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		ChannelBufferUtils.writeString(buffer, message.getMessage());

		return buffer;
	}

	@Override
	public PlayerDisconnectMessage decode(ChannelBuffer buffer) throws IOException {
		String message = ChannelBufferUtils.readString(buffer);

		return new PlayerDisconnectMessage(message);
	}

}
