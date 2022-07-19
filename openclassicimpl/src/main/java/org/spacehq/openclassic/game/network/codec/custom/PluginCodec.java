package org.spacehq.openclassic.game.network.codec.custom;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.custom.PluginMessage;
import org.spacehq.openclassic.server.util.ChannelBufferUtils;

public class PluginCodec extends MessageCodec<PluginMessage> {

	public PluginCodec() {
		super(PluginMessage.class, (byte) 0x19);
	}

	@Override
	public ChannelBuffer encode(PluginMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		ChannelBufferUtils.writeExtendedString(buffer, message.getName());
		ChannelBufferUtils.writeExtendedString(buffer, message.getVersion());

		return buffer;
	}

	@Override
	public PluginMessage decode(ChannelBuffer buffer) throws IOException {
		String name = ChannelBufferUtils.readExtendedString(buffer);
		String version = ChannelBufferUtils.readExtendedString(buffer);
		return new PluginMessage(name, version);
	}

}
