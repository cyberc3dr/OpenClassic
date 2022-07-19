package org.spacehq.openclassic.game.network.codec.custom;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.custom.KeyChangeMessage;

public class KeyChangeCodec extends MessageCodec<KeyChangeMessage> {

	public KeyChangeCodec() {
		super(KeyChangeMessage.class, (byte) 0x14);
	}

	@Override
	public ChannelBuffer encode(KeyChangeMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(5);
		buffer.writeInt(message.getKey());
		buffer.writeByte(message.isPressed() ? 1 : 0);
		return buffer;
	}

	@Override
	public KeyChangeMessage decode(ChannelBuffer buffer) throws IOException {
		int key = buffer.readInt();
		boolean pressed = buffer.readByte() == 1;
		return new KeyChangeMessage(key, pressed);
	}

}
