package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerSetBlockMessage;

public class PlayerSetBlockCodec extends MessageCodec<PlayerSetBlockMessage> {

	public PlayerSetBlockCodec() {
		super(PlayerSetBlockMessage.class, (byte) 0x05);
	}

	@Override
	public ChannelBuffer encode(PlayerSetBlockMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(8);

		buffer.writeShort(message.getX());
		buffer.writeShort(message.getY());
		buffer.writeShort(message.getZ());
		buffer.writeByte(message.isPlacing() ? 0x01 : 0x00);
		buffer.writeByte(message.getBlock());

		return buffer;
	}

	@Override
	public PlayerSetBlockMessage decode(ChannelBuffer buffer) throws IOException {
		short x = buffer.readShort();
		short y = buffer.readShort();
		short z = buffer.readShort();
		boolean placing = buffer.readByte() == 0x01;
		byte type = buffer.readByte();

		return new PlayerSetBlockMessage(x, y, z, placing, type);
	}

}
