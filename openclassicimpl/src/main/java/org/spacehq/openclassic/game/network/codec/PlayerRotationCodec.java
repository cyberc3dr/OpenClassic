package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerRotationMessage;

public class PlayerRotationCodec extends MessageCodec<PlayerRotationMessage> {

	public PlayerRotationCodec() {
		super(PlayerRotationMessage.class, (byte) 0x0b);
	}

	@Override
	public ChannelBuffer encode(PlayerRotationMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(3);

		buffer.writeByte(message.getPlayerId());
		buffer.writeByte((byte) ((int) (message.getYaw() * 256 / 360) & 255));
		buffer.writeByte((byte) ((int) (message.getPitch() * 256 / 360) & 255));

		return buffer;
	}

	@Override
	public PlayerRotationMessage decode(ChannelBuffer buffer) throws IOException {
		byte playerId = buffer.readByte();
		float yaw = (buffer.readByte() * 360) / 256f;
		float pitch = (buffer.readByte() * 360) / 256f;

		return new PlayerRotationMessage(playerId, yaw, pitch);
	}

}
