package org.spacehq.openclassic.client.render;

import org.spacehq.openclassic.api.block.model.Quad;
import org.spacehq.openclassic.api.block.model.QuadFactory;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.Vertex;

public class ClientQuadFactory extends QuadFactory {

	@Override
	public Quad newQuad(int id, Texture texture) {
		return new ClientQuad(id, texture);
	}

	@Override
	public Quad newQuad(int id, Texture texture, Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		return new ClientQuad(id, texture, v1, v2, v3, v4);
	}

}
