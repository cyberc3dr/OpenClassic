package org.spacehq.openclassic.game.io;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.*;
import org.apache.commons.io.IOUtils;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.level.LevelInfo;
import org.spacehq.openclassic.api.level.generator.NormalGenerator;
import org.spacehq.openclassic.game.level.ClassicLevel;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class OpenClassicLevelFormat {

	public static Level load(ClassicLevel level, String name, boolean create) throws IOException {
		if(!(new File(OpenClassic.getGame().getDirectory(), "levels/" + name + ".map").exists())) {
			if(new File(OpenClassic.getGame().getDirectory(), "levels/" + name + ".mclevel").exists()) {
				OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("level.detected-format"), "Minecraft Indev"));
				IndevLevelFormat.read(level, "levels/" + name + ".mclevel");
				save(level);
				return level;
			}

			if(new File(OpenClassic.getGame().getDirectory(), "levels/" + name + ".mine").exists()) {
				OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("level.detected-format"), "Minecraft Classic"));
				MinecraftLevelFormat.read(level, "levels/" + name + ".mine");
				save(level);
				return level;
			}

			if(new File(OpenClassic.getGame().getDirectory(), "levels/" + name + ".dat").exists()) {
				OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("level.detected-format"), "Minecraft Classic"));
				MinecraftLevelFormat.read(level, "levels/" + name + ".dat");
				save(level);
				return level;
			}

			if(new File(OpenClassic.getGame().getDirectory(), "levels/" + name + ".lvl").exists()) {
				OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("level.detected-format"), "MCSharp"));
				MCSharpLevelFormat.load(level, "levels/" + name + ".lvl");
				save(level);
				return level;
			}

			if(new File(OpenClassic.getGame().getDirectory(), "levels/" + name + ".oclvl").exists()) {
				OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("level.detected-format"), "Old OpenClassic"));
				readOld(level, "levels/" + name + ".oclvl");
				save(level);
				return level;
			}
		}

		File levelFile = new File(OpenClassic.getGame().getDirectory(), "levels/" + name + ".map");
		if(!levelFile.exists()) {
			if(create) {
				OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("level.auto-create"), name));
				return OpenClassic.getGame().createLevel(new LevelInfo(name, new Position(null, 0, 65, 0), (short) 256, (short) 64, (short) 256), new NormalGenerator());
			} else {
				return null;
			}
		}

		CompoundTag root = NBTIO.readFile(levelFile);
		CompoundTag info = (CompoundTag) root.get("Info");
		if(info.get("Version") == null) {
			readOldNbt(level, root, info);
		} else {
			int version = ((IntTag) info.get("Version")).getValue();
			if(version == 1) {
				CompoundTag spawn = (CompoundTag) root.get("Spawn");
				CompoundTag map = (CompoundTag) root.get("Map");

				level.setName(((StringTag) info.get("Name")).getValue());
				level.setAuthor(((StringTag) info.get("Author")).getValue());
				level.setCreationTime(((LongTag) info.get("CreationTime")).getValue());

				float x = ((FloatTag) spawn.get("x")).getValue();
				float y = ((FloatTag) spawn.get("y")).getValue();
				float z = ((FloatTag) spawn.get("z")).getValue();
				float yaw = ((FloatTag) spawn.get("yaw")).getValue();
				float pitch = ((FloatTag) spawn.get("pitch")).getValue();
				level.setSpawn(new Position(level, x, y, z, yaw, pitch));

				short width = ((ShortTag) map.get("Width")).getValue();
				short height = ((ShortTag) map.get("Height")).getValue();
				short depth = ((ShortTag) map.get("Depth")).getValue();
				byte blocks[] = ((ByteArrayTag) map.get("Blocks")).getValue();
				level.setData(width, height, depth, blocks);
			} else {
				throw new IOException("Unknown OpenClassic map version: " + version);
			}
		}

		return level;
	}

	public static void readOldNbt(ClassicLevel level, CompoundTag root, CompoundTag info) {
		CompoundTag spawn = (CompoundTag) root.get("Spawn");
		CompoundTag map = (CompoundTag) root.get("Map");

		level.setName(((StringTag) info.get("Name")).getValue());
		level.setAuthor(((StringTag) info.get("Author")).getValue());
		level.setCreationTime(((LongTag) info.get("CreationTime")).getValue());

		double x = ((DoubleTag) spawn.get("x")).getValue();
		double y = ((DoubleTag) spawn.get("y")).getValue();
		double z = ((DoubleTag) spawn.get("z")).getValue();
		byte yaw = ((ByteTag) spawn.get("yaw")).getValue();
		byte pitch = ((ByteTag) spawn.get("pitch")).getValue();
		level.setSpawn(new Position(level, (float) x + 0.5f, (float) y, (float) z + 0.5f, yaw, pitch));

		short width = ((ShortTag) map.get("Width")).getValue();
		short height = ((ShortTag) map.get("Height")).getValue();
		short depth = ((ShortTag) map.get("Depth")).getValue();
		byte blocks[] = ((ByteArrayTag) map.get("Blocks")).getValue();
		level.setData(width, height, depth, blocks);
	}

	public static Level readOld(ClassicLevel level, String file) throws IOException {
		File f = new File(OpenClassic.getGame().getDirectory(), file);
		FileInputStream in = new FileInputStream(new File(OpenClassic.getGame().getDirectory(), file));
		GZIPInputStream gzipIn = new GZIPInputStream(in);
		DataInputStream data = new DataInputStream(gzipIn);

		level.setName(readString(data));
		level.setAuthor(readString(data));
		level.setCreationTime(data.readLong());

		double x = data.readDouble();
		double y = data.readDouble();
		double z = data.readDouble();
		byte yaw = data.readByte();
		byte pitch = data.readByte();
		level.setSpawn(new Position(level, (float) x, (float) y, (float) z, yaw, pitch));

		short width = data.readShort();
		short height = data.readShort();
		short depth = data.readShort();

		byte[] blocks = new byte[width * depth * height];
		data.read(blocks);

		level.setData(width, depth, height, blocks);

		data.close();
		try {
			f.delete();
		} catch(SecurityException e) {
			e.printStackTrace();
		}

		return level;
	}

	public static void save(Level level) throws IOException {
		File file = new File(OpenClassic.getGame().getDirectory(), "levels/" + level.getName() + ".map");
		CompoundTag root = new CompoundTag("Level");

		CompoundTag info = new CompoundTag("Info");
		info.put(new IntTag("Version", 1));
		info.put(new StringTag("Name", level.getName()));
		info.put(new StringTag("Author", level.getAuthor()));
		info.put(new LongTag("CreationTime", level.getCreationTime()));
		root.put(info);

		CompoundTag spawn = new CompoundTag("Spawn");
		spawn.put(new FloatTag("x", level.getSpawn().getX()));
		spawn.put(new FloatTag("y", level.getSpawn().getY()));
		spawn.put(new FloatTag("z", level.getSpawn().getZ()));
		spawn.put(new FloatTag("yaw", level.getSpawn().getYaw()));
		spawn.put(new FloatTag("pitch", level.getSpawn().getPitch()));
		root.put(spawn);

		CompoundTag map = new CompoundTag("Map");
		map.put(new ShortTag("Width", level.getWidth()));
		map.put(new ShortTag("Height", level.getHeight()));
		map.put(new ShortTag("Depth", level.getDepth()));
		map.put(new ByteArrayTag("Blocks", level.getBlocks()));
		root.put(map);

		NBTIO.writeFile(root, file);
	}

	private static String readString(DataInputStream in) throws IOException {
		StringBuilder builder = new StringBuilder();

		for(int length = in.readShort(); length > 0; length--) {
			builder.append(in.readChar());
		}

		return builder.toString();
	}

	private OpenClassicLevelFormat() {
	}

}
