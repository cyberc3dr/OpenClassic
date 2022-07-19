package org.spacehq.openclassic.client.level.particle;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.api.player.Player;

public class ParticleManager {

	public List<Particle> particles = new ArrayList<Particle>();

	public void spawnParticle(Particle particle) {
		this.particles.add(particle);
	}

	public void tick() {
		for(int index = 0; index < this.particles.size(); index++) {
			Particle particle = this.particles.get(index);
			particle.tick();
			if(particle.removed) {
				this.particles.remove(particle);
			}
		}
	}

	public void render(float delta, Player player) {
		float xmod = -MathHelper.cos(player.getPosition().getYaw() * MathHelper.DEG_TO_RAD);
		float zmod = -MathHelper.sin(player.getPosition().getYaw() * MathHelper.DEG_TO_RAD);
		float xdir = -zmod * MathHelper.sin(player.getPosition().getPitch() * MathHelper.DEG_TO_RAD);
		float zdir = xmod * MathHelper.sin(player.getPosition().getPitch() * MathHelper.DEG_TO_RAD);
		float ymod = MathHelper.cos(player.getPosition().getPitch() * MathHelper.DEG_TO_RAD);
		for(int index = 0; index < this.particles.size(); index++) {
			Particle particle = this.particles.get(index);
			particle.render(delta, xmod, ymod, zmod, xdir, zdir);
		}
	}
	
}
