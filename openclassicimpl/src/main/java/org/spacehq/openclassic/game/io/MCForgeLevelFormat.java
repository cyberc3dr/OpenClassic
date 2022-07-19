package org.spacehq.openclassic.game.io;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.game.level.ClassicLevel;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class MCForgeLevelFormat {

	public static Level load(ClassicLevel level, String file) throws IOException {
		File f = new File(file);
		FileInputStream in = new FileInputStream(f);
		DataInputStream data = new DataInputStream(in);

		long magic = data.readLong();
		if(magic != 7882256401675281664L) {
			OpenClassic.getLogger().severe(String.format(OpenClassic.getGame().getTranslator().translate("level.format-mismatch"), "MCForge 6"));
			OpenClassic.getLogger().severe(String.valueOf(magic));
			data.close();
			return null;
		}

		byte version = data.readByte();
		if(version != 1) {
			OpenClassic.getLogger().severe(OpenClassic.getGame().getTranslator().translate("level.unknown-version"));
			data.close();
			return null;
		}

		level.setName(file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf(".")));
		level.setAuthor("Unknown");
		level.setCreationTime(System.currentTimeMillis());
		String ldata = readString(data);
		int width = Integer.parseInt(ldata.split("\\@")[0]);
		int height = Integer.parseInt(ldata.split("\\@")[1]);
		int depth = Integer.parseInt(ldata.split("\\@")[2]);
		ldata = readString(data);
		int x = Integer.parseInt(ldata.split("\\!")[0]);
		int y = Integer.parseInt(ldata.split("\\!")[1]);
		int z = Integer.parseInt(ldata.split("\\!")[2]);
		level.setSpawn(new Position(level, x, y, z));
		ldata = readString(data);

		// ====METADATA==//
		int metadata = readInt(data);
		for(int i = 0; i < metadata; i++) {
			readString(data); // key
			readString(data); // value
		}
		// ====METADATA==//

		readInt(data); // block size
		int bytesize = readInt(data);
		byte[] compressed = new byte[bytesize];
		data.readFully(compressed);

		byte[] decompressed = decompress(compressed);
		byte[] blocks = new byte[width * depth * height];
		for(int i = 0; i < decompressed.length; i++) {
			blocks[i] = translateBlock(decompressed[i]);
		}

		level.setData(width, depth, height, blocks);
		data.close();

		try {
			f.delete();
		} catch(SecurityException e) {
			e.printStackTrace();
		}

		return level;
	}

	public static byte translateBlock(byte id) {
		if(id <= 49) {
			return id;
		}

		if(id == 111) {
			return VanillaBlock.LOG.getId();
		}

		return VanillaBlock.AIR.getId();
	}

	private static String readString(DataInputStream data) {
		try {
			int sizea = readEncodedInt(data);
			byte[] array = new byte[sizea];
			data.readFully(array);
			String ldata = new String(array, "US-ASCII");
			return ldata;
		} catch(Exception e) {
			return "";
		}
	}

	private static int readInt(DataInputStream data) {
		try {
			byte[] temp = new byte[4];
			data.readFully(temp);
			return temp[0] | (temp[1] << 8) | (temp[2] << 16) | (temp[3] << 24);
		} catch(IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private static int readEncodedInt(DataInputStream data) throws IOException {
		int num = 0;
		int num2 = 0;
		while(num2 != 35) {
			byte b = data.readByte();
			num |= (b & 127) << num2;
			num2 += 7;
			if((b & 128) == 0) return num;
		}

		throw new IOException("Format Exception");
	}

	private static byte[] decompress(byte[] gzip) {
		ByteArrayInputStream ba = null;
		GZIPInputStream gz = null;
		try {
			final int size = 4096;
			ba = new ByteArrayInputStream(gzip);
			gz = new GZIPInputStream(ba);
			byte[] data = new byte[size];
			gz.read(data);
			return data;
		} catch(IOException e) {
			e.printStackTrace();
			return new byte[0];
		} finally {
			if(gz != null) {
				try {
					gz.close();
				} catch(IOException e) {
				}
			}
		}
	}

	private MCForgeLevelFormat() {
	}

}
