package org.spacehq.openclassic.api.block.model;

import java.util.List;

/**
 * A quad face used in models.
 */
public interface Quad {

	/**
	 * Gets the ID of this quad.
	 * @return This quad's ID.
	 */
	public int getId();
	
	/**
	 * Sets the given vertex id to the given vertex.
	 * @param id Id of the vertex.
	 * @param vertex Vertex to set.
	 */
	public void setVertex(int id, Vertex vertex);
	
	/**
	 * Adds a vertex to this quad.
	 * @param id ID of the vertex.
	 * @param x X of the vertex.
	 * @param y Y of the vertex.
	 * @param z Z of the vertex.
	 */
	public void addVertex(int id, float x, float y, float z);
	
	/**
	 * Removes the vertex with the given ID.
	 * @param id ID of the vertex to remove.
	 */
	public void removeVertex(int id);
	
	/**
	 * Gets the vertex with the given ID.
	 * @param id ID to look for.
	 * @return The vertex with the given ID.
	 */
	public Vertex getVertex(int id);
	
	/**
	 * Gets the quad's vertices.
	 * @return The quad's vertices.
	 */
	public List<Vertex> getVertices();
	
	/**
	 * Gets the quad's texture.
	 * @return The quad's texture.
	 */
	public Texture getTexture();
	
	/**
	 * Renders the quad.
	 * @param x X to render at.
	 * @param y Y to render at.
	 * @param z Z to render at.
	 * @param brightness Brightness to render at.
	 */
	public void render(float x, float y, float z, float brightness);
	
	/**
	 * Renders the quad.
	 * @param x X to render at.
	 * @param y Y to render at.
	 * @param z Z to render at.
	 * @param brightness Brightness to render at.
	 * @param batch Whether this render is part of a level rendering batch.
	 */
	public void render(float x, float y, float z, float brightness, boolean batch);
	
	/**
	 * Renders the quad at the given scale.
	 * @param x X to render at.
	 * @param y Y to render at.
	 * @param z Z to render at.
	 * @param scale Scale to render at.
	 * @param brightness Brightness to render at.
	 */
	public void renderScaled(float x, float y, float z, float scale, float brightness);

	/**
	 * Gets the parent of this quad.
	 * @return The quad's parent.
	 */
	public Model getParent();
	
	/**
	 * Sets the parent of this quad if it does not have one.
	 * @param parent The quad's new parent.
	 */
	public void setParent(Model parent);

	/**
	 * Creates a copy of this quad with reversed vertex order.
	 * @param newId Id of the resulting quad.
	 * @return The resulting quad.
	 */
	public Quad reverseVertices(int newId);
	
}
