package org.spacehq.openclassic.api.config;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Configuration {

	/**
	 * Loads this configuration from a file, if one was provided.
	 */
	public void load();
	
	/**
	 * Loads this configuration from a stream.
	 */
	public void load(InputStream in);
	
	/**
	 * Saves this configuration to a file, if one was provided.
	 */
	public void save();
	
	/**
	 * Saves this configuration to a stream.
	 */
	public void save(OutputStream out);
	
	/**
	 * Gets a node at the given path.
	 * @param path Path of the node.
	 * @return The node at the given path.
	 */
	public Node getNode(String path);
	
	/**
	 * Gets the absolute paths of the keys in this configuration.
	 * @param deep Whether to recursively go deeper into each key or to only use the top level of keys.
	 * @return The absolute keys.
	 */
	public List<String> getAbsoluteKeys(boolean deep);
	
	/**
	 * Gets the absolute paths of the keys in this configuration in the given path.
	 * @param deep Whether to recursively go deeper into each key or to only use the top level of keys.
	 * @return The absolute keys.
	 */
	public List<String> getAbsoluteKeys(String path, boolean deep);
	
	/**
	 * Gets the paths of the keys in this configuration in and relative to the given path.
	 * @param deep Whether to recursively go deeper into each key or to only use the top level of keys.
	 * @return The relative keys.
	 */
	public List<String> getRelativeKeys(String path, boolean deep);
	
	/**
	 * Gets the nodes in this configuration.
	 * @param deep Whether to recursively go deeper into each node or to only use the top level of nodes.
	 * @return The configuration's nodes.
	 */
	public List<Node> getNodes(boolean deep);
	
	/**
	 * Gets the nodes in this configuration in the given path.
	 * @param deep Whether to recursively go deeper into each node or to only use the top level of nodes.
	 * @return The path's nodes.
	 */
	public List<Node> getNodes(String path, boolean deep);
	
	/**
	 * Gets whether this configuration contains the given path.
	 * @param path Path to look for.
	 * @return Whether this configuration contains the path.
	 */
	public boolean contains(String path);
	
	/**
	 * Gets the value at the given path.
	 * @param path Path of the value.
	 * @return The value at the path, or null if it doesn't exist.
	 */
	public Object getValue(String path);
	
	/**
	 * Gets the value at the given path, setting a default value
	 * if it doesn't exist.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The value at the path, or the value of def it it doesn't exist.
	 */
	public Object getValue(String path, Object def);
	
	/**
	 * Gets the string at the given path, setting an empty string
	 * as the value if it doesn't exist or isn't a string.
	 * @param path Path of the value.
	 * @return The string at the path.
	 */
	public String getString(String path);
	
	/**
	 * Gets the string at the given path, setting a default value
	 * if it doesn't exist or isn't a string.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The string at the path, or the value of def it it doesn't exist.
	 */
	public String getString(String path, String def);
	
	/**
	 * Gets the boolean at the given path, setting false
	 * as the value if it doesn't exist or isn't a boolean.
	 * @param path Path of the value.
	 * @return The boolean at the path.
	 */
	public boolean getBoolean(String path);
	
	/**
	 * Gets the boolean at the given path, setting a default value
	 * if it doesn't exist or isn't a boolean.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The boolean at the path, or the value of def it it doesn't exist.
	 */
	public boolean getBoolean(String path, boolean def);
	
	/**
	 * Gets the byte at the given path, setting 0
	 * as the value if it doesn't exist or isn't a byte.
	 * @param path Path of the value.
	 * @return The byte at the path.
	 */
	public byte getByte(String path);
	
	/**
	 * Gets the byte at the given path, setting a default value
	 * if it doesn't exist or isn't a byte.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The byte at the path, or the value of def it it doesn't exist.
	 */
	public byte getByte(String path, byte def);
	
	/**
	 * Gets the short at the given path, setting 0
	 * as the value if it doesn't exist or isn't a short.
	 * @param path Path of the value.
	 * @return The short at the path.
	 */
	public short getShort(String path);
	
	/**
	 * Gets the short at the given path, setting a default value
	 * if it doesn't exist or isn't a short.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The short at the path, or the value of def it it doesn't exist.
	 */
	public short getShort(String path, short def);
	
	/**
	 * Gets the integer at the given path, setting 0
	 * as the value if it doesn't exist or isn't an integer.
	 * @param path Path of the value.
	 * @return The integer at the path.
	 */
	public int getInteger(String path);
	
	/**
	 * Gets the integer at the given path, setting a default value
	 * if it doesn't exist or isn't an integer.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The integer at the path, or the value of def it it doesn't exist.
	 */
	public int getInteger(String path, int def);
	
	/**
	 * Gets the long at the given path, setting 0
	 * as the value if it doesn't exist or isn't a long.
	 * @param path Path of the value.
	 * @return The long at the path.
	 */
	public long getLong(String path);
	
	/**
	 * Gets the long at the given path, setting a default value
	 * if it doesn't exist or isn't a long.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The long at the path, or the value of def it it doesn't exist.
	 */
	public long getLong(String path, long def);
	
	/**
	 * Gets the float at the given path, setting 0
	 * as the value if it doesn't exist or isn't a float.
	 * @param path Path of the value.
	 * @return The float at the path.
	 */
	public float getFloat(String path);
	
	/**
	 * Gets the float at the given path, setting a default value
	 * if it doesn't exist or isn't a float.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The float at the path, or the value of def it it doesn't exist.
	 */
	public float getFloat(String path, float def);
	
	/**
	 * Gets the double at the given path, setting 0
	 * as the value if it doesn't exist or isn't a double.
	 * @param path Path of the value.
	 * @return The double at the path.
	 */
	public double getDouble(String path);
	
	/**
	 * Gets the double at the given path, setting a default value
	 * if it doesn't exist or isn't a double.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The double at the path, or the value of def it it doesn't exist.
	 */
	public double getDouble(String path, double def);
	
	/**
	 * Gets the list of the given type at the given path,
	 * setting an empty list as the value if it doesn't exist
	 * or isn't a list of the given type.
	 * @param path Path of the value.
	 * @return The list at the path.
	 */
	public <T> List<T> getList(String path, Class<T> type);
	
	/**
	 * Gets the list of the given type at the given path,
	 * setting a default value if it doesn't exist or isn't
	 * a list of the given type.
	 * @param path Path of the value.
	 * @param def Default value to use.
	 * @return The list at the path, or the value of def it it doesn't exist.
	 */
	public <T> List<T> getList(String path, Class<T> type, List<T> def);
	
	/**
	 * Applies a default value to a path if it is not assigned yet.
	 * @param path Path to apply to.
	 * @param value Value to apply.
	 */
	public void applyDefault(String path, Object value);
	
	/**
	 * Sets the value of the given path.
	 * @param path Path to set.
	 * @param value Value to set.
	 */
	public void setValue(String path, Object value);
	
	/**
	 * Removes the given path from this configuration.
	 * @param path Path to remove.
	 */
	public void remove(String path);
	
}
