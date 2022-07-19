package org.spacehq.openclassic.api.block.model;

/**
 * A factory for creating quad instances.
 */
public abstract class QuadFactory {

	private static QuadFactory factory;
	
	/**
	 * Gets the quad factory instance.
	 * @return The quad factory instance.
	 */
	public static QuadFactory getFactory() {
		return QuadFactory.factory;
	}
	
	/**
	 * Sets the quad factory instance if it is not already set.
	 * @param factory Quad factory to use.
	 */
	public static void setFactory(QuadFactory factory) {
		if(QuadFactory.factory != null) {
			return;
		}
		
		QuadFactory.factory = factory;
	}
	
	/**
	 * Creates a new quad.
	 * @param id Id of the quad.
	 * @param texture Texture of the quad.
	 * @return The created quad.
	 */
	public abstract Quad newQuad(int id, Texture texture);
	
	/**
	 * Creates a new quad.
	 * @param id Id of the quad.
	 * @param texture Texture of the quad.
	 * @param v1 First vertex of the quad.
	 * @param v2 Second vertex of the quad.
	 * @param v3 Third vertex of the quad.
	 * @param v4 Fourth vertex of the quad.
	 * @return The created quad.
	 */
	public abstract Quad newQuad(int id, Texture texture, Vertex v1, Vertex v2, Vertex v3, Vertex v4);
	
}
