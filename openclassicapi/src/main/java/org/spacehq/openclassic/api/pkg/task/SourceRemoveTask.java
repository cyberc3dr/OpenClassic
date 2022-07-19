package org.spacehq.openclassic.api.pkg.task;

import java.io.File;
import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.command.Sender;
import org.spacehq.openclassic.api.config.Configuration;

/**
 * A task that removes a source.
 */
public class SourceRemoveTask implements Runnable {

	private String id;
	private Sender executor;
	
	public SourceRemoveTask(String id, Sender executor) {
		this.id = id;
		this.executor = executor;
	}
	
	@Override
	public void run() {
		Configuration sources = OpenClassic.getGame().getPackageManager().getSourcesList();
		
		if(sources.getNode(this.id) == null) {
			if(this.executor != null) this.executor.sendMessage(Color.RED + "The source is not in the server's source list.");
			return;
		}
		
		if(this.executor != null) this.executor.sendMessage(Color.AQUA + "Removing package cache...");
		
		(new File(OpenClassic.getGame().getDirectory(), "source-cache/" + id + "-packages.yml")).delete();
		
		sources.remove(this.id);
		sources.save();
		
		if(this.executor != null) this.executor.sendMessage(Color.GREEN + "The source \"" + this.id + "\" has been removed successfully!");
	}
	
}
