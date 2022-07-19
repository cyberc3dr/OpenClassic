package org.spacehq.openclassic.client.level.particle;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.model.Model;
import org.spacehq.openclassic.api.block.model.Quad;

public class TerrainParticle extends Particle {

	private Model model;
	
	public TerrainParticle(Position pos, float xd, float yd, float zd, BlockType block) {
		super(pos, xd, yd, zd);
		this.model = block.getModel();
		Quad quad = block.getModel().getQuads().size() > 2 ? block.getModel().getQuad(2) : block.getModel().getQuads().size() > 0 ? block.getModel().getQuad(block.getModel().getQuads().size() - 1) : null;
		if(quad != null) {
			this.texture = quad.getTexture();
		}

		this.textureSizeDivider = 4;
		this.textureOffset = ((float) Math.random() * 3) / 64;
		this.gravity = 1;
		this.rCol = 0.6f;
		this.gCol = 0.6f;
		this.bCol = 0.6f;
		if(OpenClassic.getClient().getSettings().getIntSetting("options.graphics").getValue() == 1) {
			this.setSize(0.125f);
		}
	}
	
	@Override
	public void render(float dt, float xmod, float ymod, float zmod, float xdir, float zdir) {
		if(OpenClassic.getClient().getSettings().getIntSetting("options.graphics").getValue() == 1) {
            float brightness = this.pos.getLevel().getBrightness(this.pos.getBlockX(), this.pos.getBlockY(), this.pos.getBlockZ());
            this.model.renderScaled(this.pos.getInterpolatedX(dt), this.pos.getInterpolatedY(dt), this.pos.getInterpolatedZ(dt), this.bbSize, brightness);
		} else {
			super.render(dt, xmod, ymod, zmod, xdir, zdir);
		}
	}
	
}
