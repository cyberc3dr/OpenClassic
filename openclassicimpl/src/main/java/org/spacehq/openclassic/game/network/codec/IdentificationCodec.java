package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.IdentificationMessage;
import org.spacehq.openclassic.server.util.ChannelBufferUtils;

public class IdentificationCodec extends MessageCodec<IdentificationMessage> {

	public IdentificationCodec() {
		super(IdentificationMessage.class, (byte) 0x00);
	}

	@Override
	public ChannelBuffer encode(IdentificationMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		buffer.writeByte(message.getProtocolVersion());
		ChannelBufferUtils.writeString(buffer, message.getUsernameOrServerName());
		ChannelBufferUtils.writeString(buffer, message.getVerificationKeyOrMotd());
		buffer.writeByte(message.getOpOrCustomClient());

		return buffer;
	}

	@Override
	public IdentificationMessage decode(ChannelBuffer buffer) throws IOException {
		byte protocol = buffer.readByte();
		String username = ChannelBufferUtils.readString(buffer);
		String verificationKey = ChannelBufferUtils.readString(buffer);
		byte unused = buffer.readByte();

		return new IdentificationMessage(protocol, username, verificationKey, unused);
	}

}
