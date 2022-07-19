package org.spacehq.openclassic.client.level.particle;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.client.render.Textures;

public class SmokeParticle extends Particle {

	public SmokeParticle(Position pos) {
		super(pos, 0, 0, 0);
		this.xd *= 0.1f;
		this.yd *= 0.1f;
		this.zd *= 0.1f;
		this.rCol = (float) (Math.random() * 0.3D);
		this.gCol = this.rCol;
		this.bCol = this.rCol;
		this.lifetime = (int) (8 / (Math.random() * 0.8 + 0.2));
		this.noPhysics = true;
		this.texture = Textures.SMOKE_PARTICLE_1;
		this.gravity = -0.1f;
		this.slowDown = 0.96f;
	}

	@Override
	public void tick() {
		super.tick();
		int tex = 7 - (this.age << 3) / this.lifetime;
		switch(tex) {
			case 0:
				this.texture = Textures.SMOKE_PARTICLE_1;
				break;
			case 1:
				this.texture = Textures.SMOKE_PARTICLE_2;
				break;
			case 2:
				this.texture = Textures.SMOKE_PARTICLE_3;
				break;
			case 3:
				this.texture = Textures.SMOKE_PARTICLE_4;
				break;
			case 4:
				this.texture = Textures.SMOKE_PARTICLE_5;
				break;
			case 5:
				this.texture = Textures.SMOKE_PARTICLE_6;
				break;
			case 6:
				this.texture = Textures.SMOKE_PARTICLE_7;
				break;
			default:
				this.texture = Textures.SMOKE_PARTICLE_8;
		}
	}
}
