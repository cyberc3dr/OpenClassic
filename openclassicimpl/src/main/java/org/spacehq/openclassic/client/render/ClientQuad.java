package org.spacehq.openclassic.client.render;

import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.block.model.Vertex;
import org.spacehq.openclassic.game.GameQuad;

public class ClientQuad extends GameQuad {

	public ClientQuad(int id, Texture texture) {
		super(id, texture);
	}
	
	public ClientQuad(int id, Texture texture, Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
		super(id, texture, v1, v2, v3, v4);
	}
	
	@Override
	public void render(float x, float y, float z, float brightness) {
		this.render(x, y, z, brightness, false);
	}
	
	@Override
	public void render(float x, float y, float z, float brightness, boolean batch) {	
		RenderHelper.getHelper().drawQuad(this, x, y, z, brightness, batch);
	}
	
	@Override
	public void renderScaled(float x, float y, float z, float scale, float brightness) {
		RenderHelper.getHelper().drawScaledQuad(this, x, y, z, scale, brightness);
	}
	
}
