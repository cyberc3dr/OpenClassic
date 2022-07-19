package org.spacehq.openclassic.api.config.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.config.Configuration;
import org.spacehq.openclassic.api.config.Node;

@SuppressWarnings("unchecked")
public class YamlConfig implements Configuration {

	private File file;
	private Map<String, Object> root;
	private Yaml yaml;
	
	public YamlConfig() {
		this("");
	}
	
	public YamlConfig(String path) {
		this(path != null && !path.equals("") ? new File(path) : null);
	}
	
	public YamlConfig(File file) {
		this.file = file;
		this.root = new HashMap<String, Object>();

		DumperOptions options = new DumperOptions();
		options.setIndent(4);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		this.yaml = new Yaml(new SafeConstructor(), new Representer(), options);
	}
	
	@Override
	public void load() {
		if(this.file == null) {
			return;
		}
		
		FileInputStream in = null;
		try {
			if(!this.file.exists()) {
				if(this.file.getParentFile() != null && !this.file.getParentFile().exists()) {
					this.file.getParentFile().mkdirs();
				}

				this.file.createNewFile();
			}

			in = new FileInputStream(this.file);
			this.load(in);
		} catch (IOException e) {
			OpenClassic.getLogger().severe("Failed to load from file " + this.file.getPath() + "!");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	@Override
	public void load(InputStream in) {
		this.root = (Map<String, Object>) this.yaml.load(in);
		if(this.root == null) this.root = new HashMap<String, Object>();
	}

	@Override
	public void save() {
		if(this.file == null) {
			return;
		}
		
		FileOutputStream out = null;
		try {
			if(!this.file.exists()) {
				if(!this.file.getParentFile().exists()) {
					this.file.getParentFile().mkdirs();
				}
				
				this.file.createNewFile();
			}

			out = new FileOutputStream(this.file);
			this.save(out);
		} catch (IOException e) {
			OpenClassic.getLogger().severe("Failed to save config file to " + this.file.getPath() + "!");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	@Override
	public void save(OutputStream out) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(out, "UTF-8");
			this.yaml.dump(this.root, writer);
		} catch (IOException e) {
			OpenClassic.getLogger().severe("Failed to save config file to stream!");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	@Override
	public Node getNode(String path) {
		return new Node(path, this);
	}

	@Override
	public List<String> getAbsoluteKeys(boolean deep) {
		return this.getAbsoluteKeys("", deep);
	}

	@Override
	public List<String> getAbsoluteKeys(String path, boolean deep) {
		List<String> keys = this.getRelativeKeys(path, deep);
		if(!path.equals("")) {
			List<String> ret = new ArrayList<String>();
			for(String key : keys) {
				ret.add(path + "." + key);
			}
			
			keys = ret;
		}
		
		return keys;
	}

	@Override
	public List<String> getRelativeKeys(String path, boolean deep) {
		Object o = this.getValue(path);
		List<String> ret = new ArrayList<String>();
		if(o instanceof Map) {
			Map<String, Object> m = (Map<String, Object>) o;
			for(String key : m.keySet()) {
				ret.add(key);
			}
			
			if(deep) {
				for(String key : m.keySet()) {
					Object ob = m.get(key);
					if(ob instanceof Map) {
						this.addKeys(key, ob, ret);
					}
				}
			}
		}
		
		return ret;
	}
	
	private void addKeys(String path, Object o, List<String> list) {
		Map<String, Object> m = (Map<String, Object>) o;
		for(String key : m.keySet()) {
			list.add(path + "." + key);
			Object ob = m.get(key);
			if(ob instanceof Map) {
				this.addKeys(path + "." + key, ob, list);
			}
		}
	}

	@Override
	public List<Node> getNodes(boolean deep) {
		return this.getNodes("", deep);
	}

	@Override
	public List<Node> getNodes(String path, boolean deep) {
		List<Node> ret = new ArrayList<Node>();
		for(String key : this.getAbsoluteKeys(path, deep)) {
			ret.add(this.getNode(key));
		}
		
		return ret;
	}
	
	@Override
	public boolean contains(String path) {
		return this.getValue(path) != null;
	}

	@Override
	public Object getValue(String path) {
		return this.getValue(path, null);
	}

	@Override
	public Object getValue(String path, Object def) {
		if(path.equals("")) {
			return new HashMap<String, Object>(this.root);
		}
		
		if(!path.contains(".")) {
			Object value = this.root.get(path);
			return value;
		}

		String[] parts = path.split("\\.");
		Map<String, Object> node = this.root;
		boolean change = false;
		for(int index = 0; index < parts.length; index++) {
			Object obj = node.get(parts[index]);
			if(obj == null) {
				if(def != null) {
					if(index == parts.length - 1) {
						obj = def;
					} else {
						obj = new HashMap<String, Object>();
					}
					
					node.put(parts[index], obj);
					change = true;
				} else {
					return null;
				}
			}

			if(index == parts.length - 1) {
				if(change) {
					this.save();
				}
				
				return obj;
			}

			try {
				node = (Map<String, Object>) obj;
			} catch (ClassCastException e) {
				if(change) {
					this.save();
				}
				
				return null;
			}
		}

		if(change) {
			this.save();
		}
		
		return null;
	}

	@Override
	public String getString(String path) {
		return this.getString(path, "");
	}

	@Override
	public String getString(String path, String def) {
		Object value = this.getValue(path, def);
		if(value instanceof String) {
			return (String) value;
		} else if(def != null) {
			this.setValue(path, def);
			this.save();
		}

		return def;
	}

	@Override
	public boolean getBoolean(String path) {
		return this.getBoolean(path, false);
	}

	@Override
	public boolean getBoolean(String path, boolean def) {
		Boolean val = CastUtil.castBoolean(this.getValue(path, def));
		if(val != null) {
			return val;
		} else {
			this.setValue(path, def);
			this.save();
		}

		return def;
	}

	@Override
	public byte getByte(String path) {
		return this.getByte(path, (byte) 0);
	}

	@Override
	public byte getByte(String path, byte def) {
		Byte val = CastUtil.castByte(this.getValue(path, def));
		if(val != null) {
			return val;
		} else {
			this.setValue(path, def);
			this.save();
		}

		return def;
	}

	@Override
	public short getShort(String path) {
		return this.getShort(path, (short) 0);
	}

	@Override
	public short getShort(String path, short def) {
		Short val = CastUtil.castShort(this.getValue(path, def));
		if(val != null) {
			return val;
		} else {
			this.setValue(path, def);
			this.save();
		}

		return def;
	}

	@Override
	public int getInteger(String path) {
		return this.getInteger(path, 0);
	}

	@Override
	public int getInteger(String path, int def) {
		Integer val = CastUtil.castInt(this.getValue(path, def));
		if(val != null) {
			return val;
		} else {
			this.setValue(path, def);
			this.save();
		}

		return def;
	}

	@Override
	public long getLong(String path) {
		return this.getLong(path, 0);
	}

	@Override
	public long getLong(String path, long def) {
		Long val = CastUtil.castLong(this.getValue(path, def));
		if(val != null) {
			return val;
		} else {
			this.setValue(path, def);
			this.save();
		}

		return def;
	}

	@Override
	public float getFloat(String path) {
		return this.getFloat(path, 0);
	}

	@Override
	public float getFloat(String path, float def) {
		Float val = CastUtil.castFloat(this.getValue(path, def));
		if(val != null) {
			return val;
		} else {
			this.setValue(path, def);
			this.save();
		}

		return def;
	}

	@Override
	public double getDouble(String path) {
		return this.getDouble(path, 0);
	}

	@Override
	public double getDouble(String path, double def) {
		Double val = CastUtil.castDouble(this.getValue(path, def));
		if(val != null) {
			return val;
		} else {
			this.setValue(path, def);
			this.save();
		}

		return def;
	}

	@Override
	public <T> List<T> getList(String path, Class<T> type) {
		return this.getList(path, type, new ArrayList<T>());
	}

	@Override
	public <T> List<T> getList(String path, Class<T> type, List<T> def) {
		Object value = this.getValue(path);
		if(value != null && value instanceof List) {
			try {
				return new ArrayList<T>((List<T>) value);
			} catch(ClassCastException e) {
				this.setValue(path, def);
				this.save();
			}
		} else {
			this.setValue(path, def);
			this.save();
		}

		return new ArrayList<T>(def);
	}
	
	@Override
	public void applyDefault(String path, Object value) {
		if(!this.contains(path)) {
			this.setValue(path, value);
		}
	}

	@Override
	public void setValue(String path, Object value) {
		if(!path.contains(".")) {
			if(value == null) {
				this.root.remove(path);
			} else {
				this.root.put(path, value);
			}
			
			return;
		}

		String[] parts = path.split("\\.");
		Map<String, Object> node = this.root;

		for(int index = 0; index < parts.length; index++) {
			Object obj = node.get(parts[index]);

			if(index == parts.length - 1) {
				if(value == null) {
					node.remove(parts[index]);
				} else {
					node.put(parts[index], value);
				}
				
				return;
			}

			if(obj == null || !(obj instanceof Map)) {
				if(value != null) {
					obj = new HashMap<String, Object>();
					node.put(parts[index], obj);
				} else {
					return;
				}
			}

			node = (Map<String, Object>) obj;
		}
	}

	@Override
	public void remove(String path) {
		this.setValue(path, null);
	}

}
