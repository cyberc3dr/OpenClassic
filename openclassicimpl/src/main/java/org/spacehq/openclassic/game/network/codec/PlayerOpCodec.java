package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerOpMessage;

public class PlayerOpCodec extends MessageCodec<PlayerOpMessage> {

	public PlayerOpCodec() {
		super(PlayerOpMessage.class, (byte) 0x0f);
	}

	@Override
	public ChannelBuffer encode(PlayerOpMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);

		buffer.writeByte(message.getOp());

		return buffer;
	}

	@Override
	public PlayerOpMessage decode(ChannelBuffer buffer) throws IOException {
		byte op = buffer.readByte();

		return new PlayerOpMessage(op);
	}

}
