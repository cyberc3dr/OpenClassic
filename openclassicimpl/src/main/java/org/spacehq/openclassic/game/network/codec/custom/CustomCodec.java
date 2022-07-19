package org.spacehq.openclassic.game.network.codec.custom;

import java.io.IOException;
import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.custom.CustomMessage;
import org.spacehq.openclassic.server.util.ChannelBufferUtils;

public class CustomCodec extends MessageCodec<CustomMessage> {

	public CustomCodec() {
		super(CustomMessage.class, (byte) 0x1a);
	}

	@Override
	public ChannelBuffer encode(CustomMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		ChannelBufferUtils.writeExtendedString(buffer, message.getId());
		byte data[] = message.getData();
		if(data.length > 1024) {
			data = Arrays.copyOfRange(data, 0, 64);
		}

		if(data.length < 1024) {
			byte[] newData = new byte[64];
			System.arraycopy(data, 0, newData, 0, data.length);

			data = newData;
		}

		buffer.writeBytes(data);
		buffer.writeInt(message.getData().length);

		return buffer;
	}

	@Override
	public CustomMessage decode(ChannelBuffer buffer) throws IOException {
		String id = ChannelBufferUtils.readExtendedString(buffer);
		byte[] data = new byte[1024];
		buffer.readBytes(data);
		return new CustomMessage(id, Arrays.copyOfRange(data, 0, buffer.readShort()));
	}

}
