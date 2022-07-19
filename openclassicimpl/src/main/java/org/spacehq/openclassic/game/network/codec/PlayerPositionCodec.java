package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerPositionMessage;

public class PlayerPositionCodec extends MessageCodec<PlayerPositionMessage> {

	public PlayerPositionCodec() {
		super(PlayerPositionMessage.class, (byte) 0x0a);
	}

	@Override
	public ChannelBuffer encode(PlayerPositionMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(7);

		buffer.writeByte(message.getPlayerId());
		buffer.writeByte((byte) (message.getXChange() * 32));
		buffer.writeByte((byte) (message.getYChange() * 32));
		buffer.writeByte((byte) (message.getZChange() * 32));

		return buffer;
	}

	@Override
	public PlayerPositionMessage decode(ChannelBuffer buffer) throws IOException {
		byte playerId = buffer.readByte();
		float xChange = buffer.readByte() / 32f;
		float yChange = buffer.readByte() / 32f;
		float zChange = buffer.readByte() / 32f;

		return new PlayerPositionMessage(playerId, xChange, yChange, zChange);
	}

}
