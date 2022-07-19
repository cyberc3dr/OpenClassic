package org.spacehq.openclassic.client.level.particle;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.client.render.Textures;

public class RainParticle extends Particle {

	public RainParticle(Position pos) {
		super(pos, 0, 0, 0);
		this.xd *= 0.3f;
		this.yd = (float) Math.random() * 0.2f + 0.1f;
		this.zd *= 0.3f;
		this.texture = Textures.RAIN_PARTICLE;
		this.setSize(0.01f);
		this.lifetime = (int) (8 / (Math.random() * 0.8 + 0.2));
		this.gravity = 1.5f;
	}

	@Override
	public void tick() {
		super.tick();
		if(this.onGround && Math.random() < 0.5) {
			this.removed = true;
		}
	}

}
