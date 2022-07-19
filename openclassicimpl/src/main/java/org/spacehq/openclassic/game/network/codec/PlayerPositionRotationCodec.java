package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerPositionRotationMessage;

public class PlayerPositionRotationCodec extends MessageCodec<PlayerPositionRotationMessage> {

	public PlayerPositionRotationCodec() {
		super(PlayerPositionRotationMessage.class, (byte) 0x09);
	}

	@Override
	public ChannelBuffer encode(PlayerPositionRotationMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(9);

		buffer.writeByte(message.getPlayerId());
		buffer.writeByte((byte) (message.getXChange() * 32));
		buffer.writeByte((byte) (message.getYChange() * 32));
		buffer.writeByte((byte) (message.getZChange() * 32));
		buffer.writeByte((byte) ((int) (message.getYaw() * 256 / 360) & 255));
		buffer.writeByte((byte) ((int) (message.getPitch() * 256 / 360) & 255));

		return buffer;
	}

	@Override
	public PlayerPositionRotationMessage decode(ChannelBuffer buffer) throws IOException {
		byte playerId = buffer.readByte();
		float xChange = buffer.readByte() / 32;
		float yChange = buffer.readByte() / 32;
		float zChange = buffer.readByte() / 32;
		float yaw = (buffer.readByte() * 360) / 256f;
		float pitch = (buffer.readByte() * 360) / 256f;

		return new PlayerPositionRotationMessage(playerId, xChange, yChange, zChange, yaw, pitch);
	}

}
