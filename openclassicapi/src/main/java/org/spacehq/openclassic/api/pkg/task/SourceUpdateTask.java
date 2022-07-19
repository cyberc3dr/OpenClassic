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
import org.spacehq.openclassic.api.config.Node;

/**
 * A task that updates a source.
 */
public class SourceUpdateTask implements Runnable {

	private Sender executor;
	
	public SourceUpdateTask(Sender executor) {
		this.executor = executor;
	}
	
	@Override
	public void run() {	
		Configuration sources = OpenClassic.getGame().getPackageManager().getSourcesList();
		if(this.executor != null) this.executor.sendMessage(Color.AQUA + "Updating sources...");
		
		File file = null;
		String id = "";
		String url = "";
		
		for(Node node : sources.getNodes(false)) {
			id = node.getPath();
			url = node.getString();
			
			file = new File(OpenClassic.getGame().getDirectory(), "source-cache/" + id + "-packages.yml");
			
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
			    URL u = new URL(url + "/packages.yml");
			    rbc = Channels.newChannel(u.openStream());
			    fos = new FileOutputStream(file);
			    
			    int length = 0;
			    
			    try {
			        HttpURLConnection content = (HttpURLConnection) u.openConnection();
			        length = content.getContentLength();
			    } catch (Exception e) {
			    	if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to update source " + id + ".");
					OpenClassic.getLogger().severe("Failed to determine file length!");
					e.printStackTrace();
					return;
			    }
			    
			    fos.getChannel().transferFrom(rbc, 0, length);
			} catch(Exception e) {
				if(this.executor != null) this.executor.sendMessage(Color.RED + "Failed to update source " + id + ".");
				OpenClassic.getLogger().severe("Failed to download file from \"" + url + "/packages.yml\"!");
				e.printStackTrace();
				return;
			} finally {
				IOUtils.closeQuietly(rbc);
				IOUtils.closeQuietly(fos);
			}
		}
		
		if(this.executor != null) this.executor.sendMessage(Color.GREEN + "All sources have been updated.");
	}

}
