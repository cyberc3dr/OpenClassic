package org.spacehq.openclassic.api.block.model;

import java.net.URL;

/**
 * A factory for creating texture instances.
 */
public abstract class TextureFactory {

	private static TextureFactory factory;
	
	/**
	 * Gets the texture factory instance.
	 * @return The texture factory instance.
	 */
	public static TextureFactory getFactory() {
		return TextureFactory.factory;
	}
	
	/**
	 * Sets the texture factory instance if it is not already set.
	 * @param factory Texture factory to use.
	 */
	public static void setFactory(TextureFactory factory) {
		if(TextureFactory.factory != null) {
			return;
		}
		
		TextureFactory.factory = factory;
	}
	
	/**
	 * Creates a new texture instance.
	 * @param url URL of the texture.
	 * @return The created texture instance.
	 */
	public abstract Texture newTexture(URL url);
	
	/**
	 * Creates a new animated texture instance.
	 * @param url URL of the texture.
	 * @param frameWidth Width of each animation frame.
	 * @param frameHeight Height of each animation frame.
	 * @param frameSpeed Speed in ticks of each animation frame.
	 * @return The created texture instance.
	 */
	public abstract Texture newTexture(URL url, int frameWidth, int frameHeight, int frameSpeed);
	
}
