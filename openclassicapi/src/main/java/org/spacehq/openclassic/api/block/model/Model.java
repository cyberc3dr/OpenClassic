package org.spacehq.openclassic.api.block.model;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.math.BoundingBox;

/**
 * Represents a block's model.
 */
public class Model {

	private List<Quad> quads = new ArrayList<Quad>();
	private BoundingBox collision;
	private BoundingBox selection;
	private boolean culling = true;
	
	/**
	 * Adds a quad to this model.
	 * @param quad Quad to add.
	 */
	public void addQuad(Quad quad) {
		while(quad.getId() > this.quads.size()) {
			this.quads.add(this.quads.size(), null);
		}
		
		this.quads.add(quad.getId(), quad);
		quad.setParent(this);
	}
	
	/**
	 * Removes a quad from this model.
	 * @param id ID of the quad to remove.
	 */
	public void removeQuad(int id) {
		Quad quad = this.quads.remove(id);
		quad.setParent(null);
	}
	
	/**
	 * Gets the quad with the given ID.
	 * @param id ID of the quad.
	 * @return The quad with the given ID.
	 */
	public Quad getQuad(int id) {
		return this.quads.get(id);
	}
	
	/**
	 * Gets all quads that belong to this model.
	 * @return The quads belonging to this model.
	 */
	public List<Quad> getQuads() {
		return new ArrayList<Quad>(this.quads);
	}
	
	/**
	 * Gets this model's collision box.
	 * @param x X of the model.
	 * @param y Y of the model.
	 * @param z Z of the model.
	 * @return This model's collision box.
	 */
	public BoundingBox getCollisionBox(int x, int y, int z) {
		if(this.collision == null) {
			return null;
		}
		
		BoundingBox bb = this.collision.clone();
		bb.move(x, y, z);
		return bb;
	}
	
	/**
	 * Gets this model's default collision box.
	 * @return This model's default collision box.
	 */
	public BoundingBox getDefaultCollisionBox() {
		return this.collision;
	}
	
	/**
	 * Sets this model's collision box.
	 * @param box The new collision box.
	 */
	public void setCollisionBox(BoundingBox box) {
		this.collision = box;
	}
	
	/**
	 * Sets this model's collision box.
	 * @param x1 X of the first point.
	 * @param y1 Y of the first point.
	 * @param z1 Z of the first point.
	 * @param x2 X of the second point.
	 * @param y2 Y of the second point.
	 * @param z2 Z of the second point.
	 */
	public void setCollisionBox(float x1, float y1, float z1, float x2, float y2, float z2) {
		this.setCollisionBox(new BoundingBox(x1, y1, z1, x2, y2, z2));
	}
	
	/**
	 * Gets this model's selection box.
	 * @param x X of the model.
	 * @param y Y of the model.
	 * @param z Z of the model.
	 * @return This model's selection box.
	 */
	public BoundingBox getSelectionBox(int x, int y, int z) {
		if(this.selection == null) {
			return null;
		}
		
		BoundingBox bb = this.selection.clone();
		bb.move(x, y, z);
		return bb;
	}
	
	/**
	 * Gets this model's default selection box.
	 * @return This model's default selection box.
	 */
	public BoundingBox getDefaultSelectionBox() {
		return this.selection;
	}
	
	/**
	 * Sets this model's selection box.
	 * @param box The new selection box.
	 */
	public void setSelectionBox(BoundingBox box) {
		this.selection = box;
	}
	
	/**
	 * Sets this model's selection box.
	 * @param x1 X of the first point.
	 * @param y1 Y of the first point.
	 * @param z1 Z of the first point.
	 * @param x2 X of the second point.
	 * @param y2 Y of the second point.
	 * @param z2 Z of the second point.
	 */
	public void setSelectionBox(float x1, float y1, float z1, float x2, float y2, float z2) {
		this.setSelectionBox(new BoundingBox(x1, y1, z1, x2, y2, z2));
	}
	
	/**
	 * Gets whether this model should have face culling enabled.
	 * @return Whether this model should use culling.
	 */
	public boolean useCulling() {
		return this.culling;
	}
	
	/**
	 * Sets whether this model should have face culling enabled.
	 * @return Whether this model should use culling.
	 */
	public void setUseCulling(boolean culling) {
		this.culling = culling;
	}
	
	/**
	 * Clears the quads in a model.
	 */
	public void clearQuads() {
		this.quads.clear();
	}
	
	/**
	 * Renders the model.
	 * @param x X to render at.
	 * @param y Y to render at.
	 * @param z Z to render at.
	 * @param brightness Brightness to render at.
	 * @return Whether anything was rendered.
	 */
	public void render(float x, float y, float z, float brightness) {
		this.render(x, y, z, brightness, false);
	}
	
	/**
	 * Renders the model.
	 * @param x X to render at.
	 * @param y Y to render at.
	 * @param z Z to render at.
	 * @param brightness Brightness to render at.
	 * @param batch Whether this model is part of an internal batch.
	 * @return Whether anything was rendered.
	 */
	public void render(float x, float y, float z, float brightness, boolean batch) {
		for(Quad quad : this.quads) {
			if(quad != null) {
				quad.render(x, y, z, brightness, batch);
			}
		}
	}

	/**
	 * Renders all sides, ignoring the model's rendering conditions.
	 * @param x X to render at.
	 * @param y Y to render at.
	 * @param z Z to render at.
	 * @param brightness Brightness to render at.
	 */
	public void renderAll(float x, float y, float z, float brightness) {
		this.render(x, y, z, brightness);
	}

	/**
	 * Renders the model at the given scale.
	 * @param x X to render at.
	 * @param y Y to render at.
	 * @param z Z to render at.
	 * @param scale Scale to render at.
	 * @param brightness Brightness to render at.
	 */
	public void renderScaled(float x, float y, float z, float scale, float brightness) {
		for(Quad quad : this.quads) {
			quad.renderScaled(x, y, z, scale, brightness);
		}
	}
	
}
