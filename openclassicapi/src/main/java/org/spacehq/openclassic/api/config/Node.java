package org.spacehq.openclassic.api.config;

import java.util.List;

public class Node {

	private String path;
	private Configuration parent;
	
	public Node(String path, Configuration parent) {
		this.path = path;
		this.parent = parent;
		if(path.endsWith(".")) {
			path = path.substring(0, path.length() - 1);
		}
	}
	
	public String getPath() {
		return this.path;
	}
	
	public Configuration getParent() {
		return this.parent;
	}
	
	public List<String> getKeys(boolean deep) {
		return this.parent.getAbsoluteKeys(this.path, deep);
	}
	
	public Node getNode(String subpath) {
		return this.parent.getNode(this.path + "." + subpath);
	}
	
	public List<Node> getNodes(boolean deep) {
		return this.parent.getNodes(this.path, deep);
	}
	
	public boolean exists() {
		return this.parent.contains(this.path);
	}
	
	public boolean contains(String subpath) {
		return this.parent.contains(this.path + "." + subpath);
	}
	
	public Object getValue() {
		return this.parent.getValue(this.path);
	}
	
	public Object getValue(Object def) {
		return this.parent.getValue(this.path, def);
	}
	
	public String getString() {
		return this.parent.getString(this.path);
	}
	
	public String getString(String def) {
		return this.parent.getString(this.path, def);
	}
	
	public boolean getBoolean() {
		return this.parent.getBoolean(this.path);
	}
	
	public boolean getBoolean(boolean def) {
		return this.parent.getBoolean(this.path, def);
	}
	
	public byte getByte() {
		return this.parent.getByte(this.path);
	}
	
	public byte getByte(byte def) {
		return this.parent.getByte(this.path, def);
	}
	
	public short getShort() {
		return this.parent.getShort(this.path);
	}
	
	public short getShort(short def) {
		return this.parent.getShort(this.path, def);
	}
	
	public int getInteger() {
		return this.parent.getInteger(this.path);
	}
	
	public int getInteger(int def) {
		return this.parent.getInteger(this.path, def);
	}
	
	public long getLong() {
		return this.parent.getLong(this.path);
	}
	
	public long getLong(long def) {
		return this.parent.getLong(this.path, def);
	}
	
	public float getFloat() {
		return this.parent.getFloat(this.path);
	}
	
	public float getFloat(float def) {
		return this.parent.getFloat(this.path, def);
	}
	
	public double getDouble() {
		return this.parent.getDouble(this.path);
	}
	
	public double getDouble(double def) {
		return this.parent.getDouble(this.path, def);
	}
	
	public <T> List<T> getList(Class<T> type) {
		return this.parent.getList(this.path, type);
	}
	
	public <T> List<T> getList(Class<T> type, List<T> def) {
		return this.parent.getList(this.path, type, def);
	}
	
	public void applyDefault(Object value) {
		this.parent.applyDefault(this.path, value);
	}
	
	public void setValue(Object value) {
		this.parent.setValue(this.path, value);
	}
	
	public void remove() {
		this.parent.remove(this.path);
	}
	
}
