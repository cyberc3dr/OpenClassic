package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.BlockChangeMessage;

public class BlockChangeCodec extends MessageCodec<BlockChangeMessage> {

	public BlockChangeCodec() {
		super(BlockChangeMessage.class, (byte) 0x06);
	}

	@Override
	public ChannelBuffer encode(BlockChangeMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(7);

		buffer.writeShort(message.getX());
		buffer.writeShort(message.getY());
		buffer.writeShort(message.getZ());
		buffer.writeByte(message.getBlock());

		return buffer;
	}

	@Override
	public BlockChangeMessage decode(ChannelBuffer buffer) throws IOException {
		short x = buffer.readShort();
		short y = buffer.readShort();
		short z = buffer.readShort();
		byte type = buffer.readByte();

		return new BlockChangeMessage(x, y, z, type);
	}

}
