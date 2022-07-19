package org.spacehq.openclassic.client.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class ServerDataStore {

	private static final List<Server> servers = new ArrayList<Server>();
	private static final Map<String, String> favorites = new HashMap<String, String>();

	private static File favoriteStore;
	
	public static List<Server> getServers() {
		return new ArrayList<Server>(servers);
	}
	
	public static void addServer(Server server) {
		servers.add(server);
	}
	
	public static Map<String, String> getFavorites() {
		return new HashMap<String, String>(favorites);
	}
	
	public static void addFavorite(String name, String url) {
		favorites.put(name, url);
	}
	
	public static void removeFavorite(String name) {
		favorites.remove(name);
	}
	
	public static void loadFavorites(File dir) {
		favoriteStore = new File(dir, "favorites.txt");

		if(!favoriteStore.exists()) {
			try {
				favoriteStore.createNewFile();
				return;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(favoriteStore));
			String line = "";

			while((line = reader.readLine()) != null) {
				String favorite[] = line.split(", ");
				favorites.put(favorite[0], favorite[1]);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public static void saveFavorites() {
		if(favoriteStore == null) return;
		if(!favoriteStore.exists()) {
			try {
				favoriteStore.createNewFile();
				return;
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(favoriteStore));
			for(String favorite : favorites.keySet()) {
				writer.write(favorite + ", " + favorites.get(favorite));
				writer.newLine();
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	
}
