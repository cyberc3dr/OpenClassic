package com.mojang.minecraft.entity.model;

import org.spacehq.openclassic.api.math.Vector;

public class Vertex {

	public Vector vector;
	public float u;
	public float v;

	public Vertex(float x, float y, float z, float u, float v) {
		this(new Vector(x, y, z), u, v);
	}

	private Vertex(Vector vec, float u, float v) {
		this.vector = vec;
		this.u = u;
		this.v = v;
	}

	public Vertex clonePosition(float u, float v) {
		return new Vertex(this.vector.clone(), u, v);
	}

}
