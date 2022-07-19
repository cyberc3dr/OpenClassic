package org.spacehq.openclassic.server.util;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.jboss.netty.buffer.ChannelBuffer;

public class ChannelBufferUtils {

	private static final Charset UTF_8 = Charset.forName("UTF-8");

	public static void writeString(ChannelBuffer buffer, String string) {
		byte data[] = string.getBytes(UTF_8);

		if(data.length > 64) {
			data = Arrays.copyOfRange(data, 0, 64);
		}

		if(data.length < 64) {
			byte[] newData = new byte[64];
			Arrays.fill(newData, (byte) 32);
			System.arraycopy(data, 0, newData, 0, data.length);

			data = newData;
		}

		for(int i = 0; i < 64; i++) {
			buffer.writeByte(data[i]);
		}
	}
	
	public static void writeExtendedString(ChannelBuffer buffer, String string) {
		byte data[] = string.getBytes(UTF_8);
		buffer.writeInt(data.length);
		for(int i = 0; i < data.length; i++) {
			buffer.writeByte(data[i]);
		}
	}

	public static String readString(ChannelBuffer buffer) {
		byte[] data = new byte[64];

		for(int i = 0; i < 64; i++) {
			data[i] = buffer.readByte();
		}

		return new String(data, UTF_8).trim();
	}
	
	public static String readExtendedString(ChannelBuffer buffer) {
		byte[] data = new byte[buffer.readInt()];
		for(int i = 0; i < data.length; i++) {
			data[i] = buffer.readByte();
		}

		return new String(data, UTF_8).trim();
	}

	private ChannelBufferUtils() {
	}

}
