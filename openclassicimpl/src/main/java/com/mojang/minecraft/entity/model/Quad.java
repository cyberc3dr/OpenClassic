package com.mojang.minecraft.entity.model;

public class Quad {

	public Vertex[] vertices;

	private Quad(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public Quad(Vertex[] vertices, int u1, int v1, int u2, int v2) {
		this(vertices);
		vertices[0] = vertices[0].clonePosition(u2 / 64F - 0.0015625F, v1 / 32F + 0.003125F);
		vertices[1] = vertices[1].clonePosition(u1 / 64F + 0.0015625F, v1 / 32F + 0.003125F);
		vertices[2] = vertices[2].clonePosition(u1 / 64F + 0.0015625F, v2 / 32F - 0.003125F);
		vertices[3] = vertices[3].clonePosition(u2 / 64F - 0.0015625F, v2 / 32F - 0.003125F);
	}

	public Quad(Vertex[] vertices, float u1, float v1, float u2, float v2) {
		this(vertices);
		vertices[0] = vertices[0].clonePosition(u2, v1);
		vertices[1] = vertices[1].clonePosition(u1, v1);
		vertices[2] = vertices[2].clonePosition(u1, v2);
		vertices[3] = vertices[3].clonePosition(u2, v2);
	}
	
}
