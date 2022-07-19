package org.spacehq.openclassic.api.pkg.task;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.command.Sender;
import org.spacehq.openclassic.api.config.Configuration;
import org.spacehq.openclassic.api.config.yaml.YamlConfig;

/**
 * A task that updates a package.
 */
public class PackageUpdateTask implements Runnable {

	private String name;
	private Sender executor;
	
	public PackageUpdateTask(String name, Sender executor) {
		this.name = name;
		this.executor = executor;
	}
	
	@Override
	public void run() {
		Configuration pkgs = OpenClassic.getGame().getPackageManager().getInstalled();
		
		if(pkgs.getNode(this.name) == null) {
			if(this.executor != null) this.executor.sendMessage(Color.RED + "This package isn't installed!");
			return;
		}
		
		String curr = pkgs.getString(this.name + ".version");
		
		if(this.executor != null) this.executor.sendMessage(Color.AQUA + "Locating package...");
		
		File cache = new File(OpenClassic.getGame().getDirectory(), "source-cache");
		if(!cache.exists()) cache.mkdirs();
		
		Configuration source = null;
		String url = "";
		String version = "";
		String depends = "";
		String desc = "";
		String enable = "";
		
		for(File file : cache.listFiles()) {
			source = new YamlConfig(file);
			source.load();
			
			if(source.getNode(this.name) == null) continue;
			
			url = source.getString(this.name + ".url");
			version = source.getString(this.name + ".latest");
			depends = source.getString(this.name + ".depends");
			desc = source.getString(this.name + ".desc");
			enable = source.getString(this.name + ".enable");
			break;
		}
		
		if(version == null || version.equals("") || url == null || url.equals("")) {
			if(this.executor != null) executor.sendMessage(Color.RED + "The package " + this.name + " was not found in any of your sources.");
			return;
		}
		
		if(!curr.equals(version)) {
			if(depends != null && !depends.equals("")) {
				for(String depend : depends.split(",")) {
					if(pkgs.getNode(depend) == null) {
						if(this.executor != null) executor.sendMessage(Color.RED + "An update has been found, however the dependency " + this.name + " is not installed. Please install it before updating this package.");
						return;
					}
				}
			}
			
			if(this.executor != null) executor.sendMessage(Color.AQUA + "An update has been found, installing...");
			OpenClassic.getGame().getPackageManager().remove(this.name);
			
			if(this.executor != null) this.executor.sendMessage(Color.AQUA + "Downloading package...");
			
			File file = new File(OpenClassic.getGame().getDirectory(), "temp/" + this.name + "-" + version + ".zip");
			
			if(file.exists()) {
				file.delete();
			}
			
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to create package file!");
				OpenClassic.getLogger().severe("Failed to create file \"" + file.getName() + "\" when attempting to download a file!");
				e.printStackTrace();
				return;
			}
			
			ReadableByteChannel rbc = null;
			FileOutputStream fos = null;
			
			try {
			    URL u = new URL(url + "/" + version + ".zip");
			    rbc = Channels.newChannel(u.openStream());
			    fos = new FileOutputStream(file);
			    
			    int length = 0;
			    
			    try {
			        HttpURLConnection content = (HttpURLConnection) u.openConnection();
			        length = content.getContentLength();
			    } catch (Exception e) {
					if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to download replacement package.");
					OpenClassic.getLogger().severe("Failed to determine file length!");
					e.printStackTrace();
					return;
			    }
			    
			    fos.getChannel().transferFrom(rbc, 0, length);
			} catch(Exception e) {
				if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to download replacement package.");
				OpenClassic.getLogger().severe("Failed to download file from \"" + url + "/" + version + ".zip\"!");
				e.printStackTrace();
				return;
			} finally {
				IOUtils.closeQuietly(rbc);
				IOUtils.closeQuietly(fos);
			}
			
			if(this.executor != null) this.executor.sendMessage(Color.AQUA + "Updating package...");
			
			List<String> files = new ArrayList<String>();
			List<String> dirs = new ArrayList<String>();
			
			ZipFile zfile = null;
			InputStream is = null;
			DataOutputStream out = null;
			
			try {
				zfile = new ZipFile(file);
				Enumeration<? extends ZipEntry> entries = zfile.entries();
				
				while(entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					if(entry.isDirectory()) {
						(new File(OpenClassic.getGame().getDirectory(), entry.getName())).mkdir();
						dirs.add("plugins/" + entry.getName());
					} else {
						is = zfile.getInputStream(entry);
						out = new DataOutputStream(new FileOutputStream(new File(OpenClassic.getGame().getDirectory(), "plugins/" + entry.getName())));
						
						byte[] buffer = new byte[1024];
						int len;
						
						while((len = is.read(buffer)) >= 0) {
							out.write(buffer, 0, len);
						}
									
						files.add("plugins/" + entry.getName());
					}
				}
			} catch (IOException e) {
				if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to download package.");
				OpenClassic.getLogger().severe("Failed to unzip file \"" + file.getName() + "\"!");
				e.printStackTrace();
				return;
			} finally {
				IOUtils.closeQuietly(zfile);
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(out);
			}
			
			pkgs.setValue(this.name + ".url", url);
			pkgs.setValue(this.name + ".version", version);
			pkgs.setValue(this.name + ".desc", desc);
			pkgs.setValue(this.name + ".files", files);
			pkgs.setValue(this.name + ".dirs", dirs);
			pkgs.setValue(this.name + ".plugins", enable);
			pkgs.save();
			
			if(enable != null && !enable.equals("")) {
				for(String plugin : enable.split(",")) {
					OpenClassic.getGame().getPluginManager().loadPlugin(new File(OpenClassic.getGame().getDirectory(), "plugins/" + plugin + ".jar"));
				}
			}
			
			if(this.executor != null) this.executor.sendMessage(Color.GREEN + "The package \"" + this.name + "\" has been updated.");
		} else {
			if(this.executor != null) this.executor.sendMessage(Color.RED + "No updates are available for this package.");
		}
	}
	
}
