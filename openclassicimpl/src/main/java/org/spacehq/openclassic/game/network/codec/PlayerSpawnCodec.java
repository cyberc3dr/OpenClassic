package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerSpawnMessage;
import org.spacehq.openclassic.server.util.ChannelBufferUtils;

public class PlayerSpawnCodec extends MessageCodec<PlayerSpawnMessage> {

	public PlayerSpawnCodec() {
		super(PlayerSpawnMessage.class, (byte) 0x07);
	}

	@Override
	public ChannelBuffer encode(PlayerSpawnMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		buffer.writeByte(message.getPlayerId());
		ChannelBufferUtils.writeString(buffer, message.getName());
		buffer.writeShort((short) (message.getX() * 32));
		buffer.writeShort((short) (message.getY() * 32));
		buffer.writeShort((short) (message.getZ() * 32));
		buffer.writeByte((byte) ((int) (message.getYaw() * 256 / 360) & 255));
		buffer.writeByte((byte) ((int) (message.getPitch() * 256 / 360) & 255));

		return buffer;
	}

	@Override
	public PlayerSpawnMessage decode(ChannelBuffer buffer) throws IOException {
		byte playerId = buffer.readByte();
		String name = ChannelBufferUtils.readString(buffer);
		float x = buffer.readShort() / 32;
		float y = buffer.readShort() / 32;
		float z = buffer.readShort() / 32;
		float yaw = (buffer.readByte() * 360) / 256f;
		float pitch = (buffer.readByte() * 360) / 256f;

		return new PlayerSpawnMessage(playerId, name, x, y, z, yaw, pitch);
	}

}
