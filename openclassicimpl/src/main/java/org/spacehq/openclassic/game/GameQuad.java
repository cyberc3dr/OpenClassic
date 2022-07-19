package org.spacehq.openclassic.game;

import org.spacehq.openclassic.api.block.model.Model;
import org.spacehq.openclassic.api.block.model.Quad;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.Vertex;
import org.spacehq.openclassic.client.render.ClientQuad;

import java.util.Arrays;
import java.util.List;

public class GameQuad implements Quad {

	protected int id;
	private Vertex vertices[] = new Vertex[4];
	private Texture texture;
	private Model parent;
	
	public GameQuad(int id, Texture texture) {
		this.texture = texture;
		this.id = id;
	}
	
	public GameQuad(int id, Texture texture, Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		this(id, texture);
		this.setVertex(0, v1);
		this.setVertex(1, v2);
		this.setVertex(2, v3);
		this.setVertex(3, v4);
	}
	
	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public void setVertex(int id, Vertex vertex) {
		if(id < 0 || id > 3) {
			throw new IllegalArgumentException("Quad can only have 4 vertices with IDs 0 - 3!");
		}

		this.vertices[id] = vertex;
	}
	
	@Override
	public void addVertex(int id, float x, float y, float z) {
		this.setVertex(id, new Vertex(x, y, z));
	}
	
	@Override
	public void removeVertex(int id) {
		if(id < 0 || id > 3) {
			throw new IllegalArgumentException("Quad can only have 4 vertices with IDs 0 - 3!");
		}

		this.vertices[id] = null;
	}
	
	@Override
	public Vertex getVertex(int id) {
		if(id < 0 || id > 3) {
			throw new IllegalArgumentException("Quad can only have 4 vertices with IDs 0 - 3!");
		}

		return this.vertices[id];
	}
	
	@Override
	public List<Vertex> getVertices() {
		return Arrays.asList(this.vertices);
	}
	
	@Override
	public Texture getTexture() {
		return this.texture;
	}
	
	@Override
	public void render(float x, float y, float z, float brightness) {
	}
	
	@Override
	public void render(float x, float y, float z, float brightness, boolean batch) {
	}
	
	@Override
	public void renderScaled(float x, float y, float z, float scale, float brightness) {
	}

	@Override
	public Model getParent() {
		return this.parent;
	}
	
	@Override
	public void setParent(Model parent) {
		this.parent = parent;
	}

	@Override
	public Quad reverseVertices(int newId) {
		return new ClientQuad(newId, this.texture, this.vertices[3], this.vertices[2], this.vertices[1], this.vertices[0]);
	}
	
}
