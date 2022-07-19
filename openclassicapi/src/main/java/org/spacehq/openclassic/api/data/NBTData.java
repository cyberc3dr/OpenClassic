package org.spacehq.openclassic.api.data;

import com.github.steveice10.opennbt.NBTIO;
import com.github.steveice10.opennbt.tag.builtin.*;
import com.github.steveice10.opennbt.tag.builtin.custom.*;
import org.spacehq.openclassic.api.OpenClassic;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Represents an NBT data file.
 */
public class NBTData {

	private CompoundTag data;
	
	public NBTData(String name) {
		this.data = new CompoundTag(name);
	}
	
	/**
	 * Loads NBT data from the given file.
	 * @param file File to load from.
	 */
	public void load(String file) {
		File f = new File(file);
		if(!f.exists()) {
			if(!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			
			try {
				f.createNewFile();
			} catch (IOException e) {
				OpenClassic.getLogger().severe("Failed to create new file for NBTData " + this.data.getName() + "!");
				e.printStackTrace();
				return;
			}
			
			return;
		}
		
		try {
			CompoundTag tag = NBTIO.readFile(f);
			this.data.clear();
			
			for(String name : tag.keySet()) {
				this.data.put(tag.get(name));
			}
		} catch (EOFException e) {
			this.data.clear();
			return;
		} catch (IOException e) {
			OpenClassic.getLogger().severe("Failed to open stream for NBTData " + this.data.getName() + "!");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Saves the NBT data to the given file.
	 * @param file File to save to.
	 */
	public void save(String file) {
		File f = new File(file);
		if(!f.exists()) {
			if(!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			
			try {
				f.createNewFile();
			} catch (IOException e) {
				OpenClassic.getLogger().severe("Failed to create new file for NBTData " + this.data.getName() + "!");
				e.printStackTrace();
				return;
			}
		}
		
		try {
			NBTIO.writeFile(this.data, f);
		} catch (IOException e) {
			OpenClassic.getLogger().severe("Failed to open stream for NBTData " + this.data.getName() + "!");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param b Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, byte b) {
		return this.data.put(new ByteTag(name, b));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param b Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, byte b[]) {
		return this.data.put(new ByteArrayTag(name, b));
	}
	
	/**
	 * Puts a CompoundTag.
	 * @param compound Tag to put.
	 * @return The resulting Tag.
	 */
	public Tag put(CompoundTag compound) {
		return this.data.put(compound);
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param d Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, double d) {
		return this.data.put(new DoubleTag(name, d));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param d Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, double d[]) {
		return this.data.put(new DoubleArrayTag(name, d));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param f Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, float f) {
		return this.data.put(new FloatTag(name, f));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param f Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, float f[]) {
		return this.data.put(new FloatArrayTag(name, f));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param i Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, int i) {
		return this.data.put(new IntTag(name, i));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param i Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, int i[]) {
		return this.data.put(new IntArrayTag(name, i));
	}
	
	/**
	 * Puts a ListTag with the given value, type, and name.
	 * @param name Name to put.
	 * @param l List to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, List<Tag> l) {
		return this.data.put(new ListTag(name, l));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param l Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, long l) {
		return this.data.put(new LongTag(name, l));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param l Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, long l[]) {
		return this.data.put(new LongArrayTag(name, l));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param s Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, short s) {
		return this.data.put(new ShortTag(name, s));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param s Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, short s[]) {
		return this.data.put(new ShortArrayTag(name, s));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param s Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, String s) {
		return this.data.put(new StringTag(name, s));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param s Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, String s[]) {
		return this.data.put(new StringArrayTag(name, s));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param o Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, Serializable o) {
		return this.data.put(new SerializableTag(name, o));
	}
	
	/**
	 * Puts a tag with the given value and the given name.
	 * @param name Name to put.
	 * @param o Value to put.
	 * @return The resulting Tag.
	 */
	public Tag put(String name, Serializable o[]) {
		return this.data.put(new SerializableArrayTag(name, o));
	}
	
	/**
	 * Gets the Tag with the given name.
	 * @param name Name of the tag.
	 * @return The resulting Tag.
	 */
	public Tag get(String name) {
		return this.data.get(name);
	}
	
	/**
	 * Removes the Tag with the given name from the data.
	 * @param name Name of the tag.
	 * @return The removed tag.
	 */
	public Tag remove(String name) {
		return this.data.remove(name);
	}
	
	/**
	 * Gets the keys of the data.
	 * @return The data's keys.
	 */
	public Set<String> keySet() {
		return this.data.keySet();
	}
	
	/**
	 * Gets the Tags of the data.
	 * @return The data's Tags.
	 */
	public Collection<Tag> values() {
		return this.data.values();
	}
	
	/**
	 * Gets the size of the data.
	 * @return The data's size.
	 */
	public int size() {
		return this.data.size();
	}
	
	/**
	 * Clears the data.
	 */
	public void clear() {
		this.data.clear();
	}
	
}
