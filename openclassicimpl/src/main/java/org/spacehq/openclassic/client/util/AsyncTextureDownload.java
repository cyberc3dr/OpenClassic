package org.spacehq.openclassic.client.util;

import java.net.HttpURLConnection;
import java.net.URL;

import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.TextureFactory;
import org.spacehq.openclassic.client.render.ClientTexture;

public class AsyncTextureDownload implements Runnable {

	private String url;
	private Texture texture;
	
	public AsyncTextureDownload(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		HttpURLConnection conn = null;
		try {
			this.texture = TextureFactory.getFactory().newTexture(new URL(this.url));
		} catch(Exception e) {
			if(e.getCause() == null || !e.getCause().getMessage().contains("response code: 403")) {
				e.printStackTrace();
			}
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
	}

	public boolean hasTexture() {
		return this.texture != null && ((ClientTexture) this.texture).getImage() != null;
	}

	public Texture getTexture() {
		return this.texture;
	}
	
}