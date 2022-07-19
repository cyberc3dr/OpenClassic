package com.mojang.minecraft.entity.model;

import org.lwjgl.opengl.GL11;

import org.spacehq.openclassic.api.math.Vector;
import org.spacehq.openclassic.client.render.Renderer;

public class ModelPart {

	public Vertex[] vertices;
	public Quad[] quads;
	private int u;
	private int v;
	public float x;
	public float y;
	public float z;
	public float pitch;
	public float yaw;
	public float roll;
	public boolean hasList = false;
	public int list = 0;
	public boolean mirror = false;
	public boolean render = true;

	public ModelPart(int u, int v) {
		this.u = u;
		this.v = v;
	}

	public void setBounds(float x, float y, float z, int width, int height, int depth, float offset) {
		this.vertices = new Vertex[8];
		this.quads = new Quad[6];
		float x2 = x + width + offset;
		float y2 = y + height + offset;
		float z2 = z + depth + offset;
		x -= offset;
		y -= offset;
		z -= offset;
		if(this.mirror) {
			offset = x2;
			x2 = x;
			x = offset;
		}

		Vertex v0 = new Vertex(x, y, z, 0.0F, 0.0F);
		Vertex v1 = new Vertex(x2, y, z, 0.0F, 8.0F);
		Vertex v2 = new Vertex(x2, y2, z, 8.0F, 8.0F);
		Vertex v3 = new Vertex(x, y2, z, 8.0F, 0.0F);
		Vertex v4 = new Vertex(x, y, z2, 0.0F, 0.0F);
		Vertex v5 = new Vertex(x2, y, z2, 0.0F, 8.0F);
		Vertex v6 = new Vertex(x2, y2, z2, 8.0F, 8.0F);
		Vertex v7 = new Vertex(x, y2, z2, 8.0F, 0.0F);
		this.vertices[0] = v0;
		this.vertices[1] = v1;
		this.vertices[2] = v2;
		this.vertices[3] = v3;
		this.vertices[4] = v4;
		this.vertices[5] = v5;
		this.vertices[6] = v6;
		this.vertices[7] = v7;
		this.quads[0] = new Quad(new Vertex[] { v5, v1, v2, v6 }, this.u + depth + width, this.v + depth, this.u + depth + width + depth, this.v + depth + height);
		this.quads[1] = new Quad(new Vertex[] { v0, v4, v7, v3 }, this.u, this.v + depth, this.u + depth, this.v + depth + height);
		this.quads[2] = new Quad(new Vertex[] { v5, v4, v0, v1 }, this.u + depth, this.v, this.u + depth + width, this.v + depth);
		this.quads[3] = new Quad(new Vertex[] { v2, v3, v7, v6 }, this.u + depth + width, this.v, this.u + depth + width + width, this.v + depth);
		this.quads[4] = new Quad(new Vertex[] { v1, v0, v3, v2 }, this.u + depth, this.v + depth, this.u + depth + width, this.v + depth + height);
		this.quads[5] = new Quad(new Vertex[] { v4, v5, v6, v7 }, this.u + depth + width + depth, this.v + depth, this.u + depth + width + depth + width, this.v + depth + height);
		if(this.mirror) {
			for(int q = 0; q < this.quads.length; q++) {
				Quad quad = this.quads[q];
				Vertex[] vecs = new Vertex[quad.vertices.length];
				for(int vert = 0; vert < quad.vertices.length; vert++) {
					vecs[vert] = quad.vertices[quad.vertices.length - vert - 1];
				}

				quad.vertices = vecs;
			}
		}

	}

	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void render(float scale) {
		if(this.render) {
			if(!this.hasList) {
				this.generateList(scale);
			}

			if(this.pitch == 0 && this.yaw == 0 && this.roll == 0) {
				if(this.x == 0 && this.y == 0 && this.z == 0) {
					GL11.glCallList(this.list);
				} else {
					GL11.glTranslatef(this.x * scale, this.y * scale, this.z * scale);
					GL11.glCallList(this.list);
					GL11.glTranslatef(-this.x * scale, -this.y * scale, -this.z * scale);
				}
			} else {
				GL11.glPushMatrix();
				GL11.glTranslatef(this.x * scale, this.y * scale, this.z * scale);
				if(this.roll != 0) {
					GL11.glRotatef(this.roll * 57.295776F, 0.0F, 0.0F, 1.0F);
				}

				if(this.yaw != 0) {
					GL11.glRotatef(this.yaw * 57.295776F, 0.0F, 1.0F, 0.0F);
				}

				if(this.pitch != 0) {
					GL11.glRotatef(this.pitch * 57.295776F, 1.0F, 0.0F, 0.0F);
				}

				GL11.glCallList(this.list);
				GL11.glPopMatrix();
			}
		}
	}

	public void generateList(float scale) {
		this.list = GL11.glGenLists(1);
		GL11.glNewList(this.list, GL11.GL_COMPILE);
		Renderer.get().begin();
		for(int q = 0; q < this.quads.length; q++) {
			Quad quad = this.quads[q];
			Vector min = quad.vertices[1].vector.clone().subtract(quad.vertices[0].vector).normalize();
			Vector max = quad.vertices[1].vector.clone().subtract(quad.vertices[2].vector).normalize();
			Vector normal = new Vector(min.getY() * max.getZ() - min.getZ() * max.getY(), min.getZ() * max.getX() - min.getX() * max.getZ(), min.getX() * max.getY() - min.getY() * max.getX()).normalize();
			Renderer.get().normal(normal.getX(), normal.getY(), normal.getZ());
			for(int vertex = 0; vertex < 4; vertex++) {
				Vertex vert = quad.vertices[vertex];
				Renderer.get().vertexuv(vert.vector.getX() * scale, vert.vector.getY() * scale, vert.vector.getZ() * scale, vert.u, vert.v);
			}
		}

		Renderer.get().end();
		GL11.glEndList();
		this.hasList = true;
	}
	
}
