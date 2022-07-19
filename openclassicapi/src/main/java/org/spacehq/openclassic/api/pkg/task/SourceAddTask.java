package org.spacehq.openclassic.api.pkg.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.io.IOUtils;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.command.Sender;
import org.spacehq.openclassic.api.config.Configuration;

/**
 * A task that adds a source.
 */
public class SourceAddTask implements Runnable {

	private String id;
	private String url;
	private Sender executor;
	
	public SourceAddTask(String id, String url, Sender executor) {
		this.id = id;
		this.url = url;
		this.executor = executor;
	}
	
	@Override
	public void run() {	
		Configuration sources = OpenClassic.getGame().getPackageManager().getSourcesList();
		
		if(sources.getString(this.id) != null && !sources.getString(this.id).equals("")) {
			if(this.executor != null) this.executor.sendMessage(Color.RED + "A source with the specified ID already exists.");
			return;
		}
		
		sources.setValue(this.id, this.url);
		sources.save();
		
		if(this.executor != null) this.executor.sendMessage(Color.AQUA + "Downloading package list...");
		File file = new File(OpenClassic.getGame().getDirectory(), "source-cache/" + id + "-packages.yml");
		
		if(file.exists()) {
			file.delete();
		}
		
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to create source cache file!");
			OpenClassic.getLogger().severe("Failed to create file \"" + file.getName() + "\" when attempting to add a source!");
			e.printStackTrace();
			return;
		}
		
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;
		
		try {
		    URL u = new URL(this.url + "/packages.yml");
		    rbc = Channels.newChannel(u.openStream());
		    fos = new FileOutputStream(file);
		    
		    int length = 0;
		    
		    try {
		        HttpURLConnection content = (HttpURLConnection) u.openConnection();
		        length = content.getContentLength();
		    } catch (Exception e) {
				if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to download source package list.");
				OpenClassic.getLogger().severe("Failed to determine file length!");
				e.printStackTrace();
				return;
		    }
		    
		    fos.getChannel().transferFrom(rbc, 0, length);
		} catch(Exception e) {
			if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to download source package list.");
			OpenClassic.getLogger().severe("Failed to download file from \"" + this.url + "/packages.yml\"!");
			e.printStackTrace();
			return;
		} finally {
			IOUtils.closeQuietly(rbc);
			IOUtils.closeQuietly(fos);
		}
		
		if(this.executor != null) this.executor.sendMessage(Color.GREEN + "The source \"" + this.id + "\" has been added successfully!");
	}

}
