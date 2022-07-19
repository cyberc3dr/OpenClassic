package org.spacehq.openclassic.game.network.codec.custom;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.custom.audio.MusicStopMessage;
import org.spacehq.openclassic.server.util.ChannelBufferUtils;

public class MusicStopCodec extends MessageCodec<MusicStopMessage> {

	public MusicStopCodec() {
		super(MusicStopMessage.class, (byte) 0x18);
	}

	@Override
	public ChannelBuffer encode(MusicStopMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		ChannelBufferUtils.writeExtendedString(buffer, message.getIdentifier());
		return buffer;
	}

	@Override
	public MusicStopMessage decode(ChannelBuffer buffer) throws IOException {
		String identifier = ChannelBufferUtils.readExtendedString(buffer);
		return new MusicStopMessage(identifier);
	}

}
