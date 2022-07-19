package org.spacehq.openclassic.game.io;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.*;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.game.level.ClassicLevel;

import java.io.File;
import java.io.IOException;

public class IndevLevelFormat {

	@SuppressWarnings("unchecked")
	public static Level read(ClassicLevel level, String file) throws IOException {
		File f = new File(OpenClassic.getGame().getDirectory(), file);
		CompoundTag data = NBTIO.readFile(f);
		CompoundTag map = (CompoundTag) data.get("Map");
		ListTag spawn = (ListTag) map.get("Spawn");
		CompoundTag about = (CompoundTag) data.get("About");

		short width = ((ShortTag) map.get("Width")).getValue();
		short height = ((ShortTag) map.get("Height")).getValue();
		short depth = ((ShortTag) map.get("Length")).getValue();
		byte[] blocks = ((ByteArrayTag) map.get("Blocks")).getValue();

		for(int index = 0; index < blocks.length; index++) {
			blocks[index] = convert(blocks[index]);
		}

		double x = ((ShortTag) spawn.get(0)).getValue() / 32d;
		double y = ((ShortTag) spawn.get(1)).getValue() / 32d;
		double z = ((ShortTag) spawn.get(2)).getValue() / 32d;

		String name = ((StringTag) about.get("Name")).getValue();
		String author = ((StringTag) about.get("Author")).getValue();
		long created = ((LongTag) about.get("CreatedOn")).getValue();

		level.setName(name);
		level.setAuthor(author);
		level.setCreationTime(created);
		level.setData(width, height, depth, blocks);

		level.setSpawn(new Position(level, (float) x, (float) y, (float) z));
		try {
			f.delete();
		} catch(SecurityException e) {
			e.printStackTrace();
		}

		return level;
	}

	public static byte convert(byte input) {
		byte out = input;

		if(input == 50 || input == 51) {
			out = 1;
		}

		if(input == 52) {
			out = 8;
		}

		return out;
	}

}
