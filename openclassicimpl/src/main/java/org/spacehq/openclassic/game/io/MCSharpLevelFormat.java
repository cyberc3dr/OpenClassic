package org.spacehq.openclassic.game.io;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.VanillaBlock;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.game.level.ClassicLevel;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class MCSharpLevelFormat {

	public static Level load(ClassicLevel level, String file) throws IOException {
		File f = new File(OpenClassic.getGame().getDirectory(), file);
		FileInputStream in = new FileInputStream(f);
		GZIPInputStream decompressor = new GZIPInputStream(in);

		DataInputStream data = new DataInputStream(decompressor);

		int magic = convert(data.readShort());

		if(magic != 1874) {
			OpenClassic.getLogger().severe(String.format(OpenClassic.getGame().getTranslator().translate("level.format-mismatch"), "MCSharp v1"));
			OpenClassic.getLogger().severe(OpenClassic.getGame().getTranslator().translate("level.try-mcforge"));
			data.close();
			return MCForgeLevelFormat.load(level, file);
		}

		level.setName(file.substring(file.lastIndexOf("/") + 1, file.lastIndexOf(".")));
		level.setAuthor("Unknown");
		level.setCreationTime(System.currentTimeMillis());
		short width = convert(data.readShort());
		short height = convert(data.readShort());
		short depth = convert(data.readShort());

		short sx = data.readShort();
		short sy = data.readShort();
		short sz = data.readShort();
		byte yaw = (byte) data.readUnsignedByte();
		byte pitch = (byte) data.readUnsignedByte();
		level.setSpawn(new Position(level, sx, sy, sz, yaw, pitch));

		byte[] blocks = new byte[width * depth * height];

		for(int z = 0; z < depth; z++) {
			for(int y = 0; y < height; y++) {
				byte[] row = new byte[height];
				data.readFully(row);

				for(int x = 0; x < width; x++) {
					blocks[(y * height + z) * width + x] = translateBlock(row[x]);
				}
			}
		}

		level.setData(width, height, depth, blocks);

		data.close();
		try {
			f.delete();
		} catch(SecurityException e) {
			e.printStackTrace();
		}

		return level;
	}

	private static short convert(int convert) {
		return (short) (((convert >> 8) & 0xff) + ((convert << 8) & 0xff00));
	}

	public static byte translateBlock(byte id) {
		if(id <= 49) return id;

		if(id == 111) return VanillaBlock.LOG.getId();

		return VanillaBlock.AIR.getId();
	}

	private MCSharpLevelFormat() {
	}

}
