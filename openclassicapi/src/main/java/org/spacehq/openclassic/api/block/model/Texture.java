package org.spacehq.openclassic.api.block.model;

import java.net.URL;

/**
 * A texture for use in rendering.
 */
public interface Texture {

	/**
	 * Gets the path of this texture, mainly used as an identifier.
	 * @return The texture's path.
	 */
	public String getPath();
	
	/**
	 * Gets the URL of this texture.
	 * @return The texture's URL.
	 */
	public URL getURL();
	
	/**
	 * Gets the RGBA values of this texture.
	 * @return The texture's pixels.
	 */
	public int[] getRGBA();
	
	/**
	 * Gets the RGBA value at the given texture coordinates.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return The RGBA value at the given coordinates.
	 */
	public int getRGB(int x, int y);
	
	/**
	 * Gets the rendering X of this texture relative to the X of the first non-subtexture parent of this texture.
	 * @return The texture's X.
	 */
	public int getX();
	
	/**
	 * Gets the rendering Y of this texture relative to the X of the first non-subtexture parent of this texture.
	 * @return The texture's Y.
	 */
	public int getY();
	
	/**
	 * Gets the width of this texture.
	 * @return The texture's width.
	 */
	public int getWidth();
	
	/**
	 * Gets the height of this texture.
	 * @return The texture's height.
	 */
	public int getHeight();
	
	/**
	 * Gets the full width of this texture, returning this texture's width if it is a full texture or the width of the first non-subtexture parent of this texture if it is a sub texture.
	 * @return The texture's full width.
	 */
	public int getFullWidth();
	
	/**
	 * Gets the full height of this texture, returning this texture's height if it is a full texture or the height of the first non-subtexture parent of this texture if it is a sub texture.
	 * @return The texture's full height.
	 */
	public int getFullHeight();
	
	/**
	 * Gets a sub texture from this texture.
	 * @param id Id to derive coordinates from. ((id % (getWidth() / width)) * (getWidth() / width), (id / (getHeight() / height)) * (getHeight() / height))
	 * @param width Width of sub texture.
	 * @param height Height of the sub texture.
	 * @return The resulting sub texture.
	 */
	public Texture getSubTexture(int id, int width, int height);
	
	/**
	 * Gets a sub texture from this texture.
	 * @param x X of the sub texture.
	 * @param y Y of the sub texture.
	 * @param width Width of sub texture.
	 * @param height Height of the sub texture.
	 * @return The resulting sub texture.
	 */
	public Texture getSubTexture(int x, int y, int width, int height);
	
	/**
	 * Binds this texture for rendering.
	 */
	public void bind();
	
	/**
	 * Gets the frame width of this texture, or -1 if it is not animated.
	 * @return The texture's frame width.
	 */
	public int getFrameWidth();
	
	/**
	 * Gets the frame height of this texture, or -1 if it is not animated.
	 * @return The texture's frame height.
	 */
	public int getFrameHeight();
	
	/**
	 * Gets the frame speed of this texture, or -1 if it is not animated.
	 * @return The texture's frame speed.
	 */
	public int getFrameSpeed();
	
}
