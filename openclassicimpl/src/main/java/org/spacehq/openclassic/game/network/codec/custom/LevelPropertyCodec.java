package org.spacehq.openclassic.game.network.codec.custom;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.custom.LevelPropertyMessage;
import org.spacehq.openclassic.server.util.ChannelBufferUtils;

public class LevelPropertyCodec extends MessageCodec<LevelPropertyMessage> {

	public LevelPropertyCodec() {
		super(LevelPropertyMessage.class, (byte) 0x15);
	}

	@Override
	public ChannelBuffer encode(LevelPropertyMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		ChannelBufferUtils.writeExtendedString(buffer, message.getType());
		buffer.writeInt(message.getValue());
		return buffer;
	}

	@Override
	public LevelPropertyMessage decode(ChannelBuffer buffer) throws IOException {
		String type = ChannelBufferUtils.readExtendedString(buffer);
		int value = buffer.readInt();
		return new LevelPropertyMessage(type, value);
	}

}
