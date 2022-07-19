package org.spacehq.openclassic.game.network.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spacehq.openclassic.game.network.MessageCodec;
import org.spacehq.openclassic.game.network.msg.PlayerTeleportMessage;

public class PlayerTeleportCodec extends MessageCodec<PlayerTeleportMessage> {

	public PlayerTeleportCodec() {
		super(PlayerTeleportMessage.class, (byte) 0x08);
	}

	@Override
	public ChannelBuffer encode(PlayerTeleportMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(9);

		buffer.writeByte(message.getPlayerId());
		buffer.writeShort((short) (message.getX() * 32));
		buffer.writeShort((short) (message.getY() * 32));
		buffer.writeShort((short) (message.getZ() * 32));
		buffer.writeByte((byte) ((int) (message.getYaw() * 256 / 360) & 255));
		buffer.writeByte((byte) ((int) (message.getPitch() * 256 / 360) & 255));

		return buffer;
	}

	@Override
	public PlayerTeleportMessage decode(ChannelBuffer buffer) throws IOException {
		byte playerId = buffer.readByte();
		float x = buffer.readShort() / 32;
		float y = buffer.readShort() / 32;
		float z = buffer.readShort() / 32;
		float yaw = (buffer.readByte() * 360) / 256f;
		float pitch = (buffer.readByte() * 360) / 256f;

		return new PlayerTeleportMessage(playerId, x, y, z, yaw, pitch);
	}

}
