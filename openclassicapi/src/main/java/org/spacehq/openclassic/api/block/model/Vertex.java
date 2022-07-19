package org.spacehq.openclassic.api.block.model;

import org.spacehq.openclassic.api.math.Vector;

/**
 * Represents a vertex.
 */
public class Vertex {

	private float x;
	private float y;
	private float z;
	
	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets the X of the vertex.
	 * @return The X of the vertex.
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Gets the Y of the vertex.
	 * @return The Y of the vertex.
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Gets the Z of the vertex.
	 * @return The Z of the vertex.
	 */
	public float getZ() {
		return this.z;
	}
	
	/**
	 * Returns this vertex as a vector.
	 * @return This vertex as a vector.
	 */
	public Vector toVector() {
		return new Vector(this.x, this.y, this.z);
	}
	
}
