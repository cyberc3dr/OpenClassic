package org.spacehq.openclassic.game.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.unsynchronized.arrayobj;
import org.unsynchronized.classdesc;
import org.unsynchronized.content;
import org.unsynchronized.field;
import org.unsynchronized.instance;
import org.unsynchronized.jdeserialize;
import org.unsynchronized.stringobj;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.game.level.ClassicLevel;

public class MinecraftLevelFormat {

	public static Level read(ClassicLevel level, String file) throws IOException {
		File fi = new File(OpenClassic.getGame().getDirectory(), file);
		DataInputStream in = new DataInputStream(new GZIPInputStream(new FileInputStream(new File(OpenClassic.getGame().getDirectory(), file))));
		int magic = in.readInt();
		byte version = in.readByte();
		if(magic != 0x271bb788) {
			OpenClassic.getLogger().severe(OpenClassic.getGame().getTranslator().translate("level.unsupported-format"));
		}

		if(version != 2) {
			OpenClassic.getLogger().severe(OpenClassic.getGame().getTranslator().translate("level.unsupported-version"));
		}

		int width = 0;
		int height = 0;
		int depth = 0;
		Byte dat[] = new Byte[0];
		Position spawn = new Position(level, 0, 0, 0, 0, 0);

		jdeserialize deserial = new jdeserialize(OpenClassic.getGame().getDirectory() + File.pathSeparator + file);
		deserial.run(in, false);
		for(content cont : deserial.getContent()) {
			Map<classdesc, Map<field, Object>> data = ((instance) cont).fielddata;
			for(classdesc desc : data.keySet()) {
				for(field f : data.get(desc).keySet()) {
					if(f.name.equals("blocks")) {
						dat = ((arrayobj) data.get(desc).get(f)).data.toArray(new Byte[((arrayobj) data.get(desc).get(f)).data.size()]);
					} else if(f.name.equals("width")) {
						width = (Integer) data.get(desc).get(f);
					} else if(f.name.equals("height")) {
						depth = (Integer) data.get(desc).get(f);
					} else if(f.name.equals("depth")) {
						height = (Integer) data.get(desc).get(f);
					} else if(f.name.equals("xSpawn")) {
						spawn.setX((Integer) data.get(desc).get(f) + 0.5f);
					} else if(f.name.equals("ySpawn")) {
						spawn.setY((Integer) data.get(desc).get(f));
					} else if(f.name.equals("zSpawn")) {
						spawn.setZ((Integer) data.get(desc).get(f) + 0.5f);
					} else if(f.name.equals("rotSpawn")) {
						spawn.setYaw((Float) data.get(desc).get(f));
					} else if(f.name.equals("name")) {
						level.setName(((stringobj) data.get(desc).get(f)).value);
					} else if(f.name.equals("createTime")) {
						level.setCreationTime((Long) data.get(desc).get(f));
					} else if(f.name.equals("creator") && data.get(desc).get(f) != null) {
						level.setAuthor(((stringobj) data.get(desc).get(f)).value);
					} else if(f.name.equals("skyColor")) {
						level.setSkyColor((Integer) data.get(desc).get(f));
					} else if(f.name.equals("fogColor")) {
						level.setFogColor((Integer) data.get(desc).get(f));
					} else if(f.name.equals("cloudColor")) {
						level.setCloudColor((Integer) data.get(desc).get(f));
					}
				}
			}
		}

		byte blocks[] = new byte[dat.length];
		for(int index = 0; index < dat.length; index++) {
			blocks[index] = dat[index];
		}

		level.setSpawn(spawn);
		level.setData(width, height, depth, blocks);

		try {
			fi.delete();
		} catch(SecurityException e) {
			e.printStackTrace();
		}

		return level;
	}

	private MinecraftLevelFormat() {
	}

}
