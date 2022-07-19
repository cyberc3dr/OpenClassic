package org.spacehq.openclassic.client.level.particle;

import java.util.ArrayList;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.model.Texture;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.render.Renderer;

public abstract class Particle {

	protected Position pos;
	protected BoundingBox bb;
	protected float xd;
	protected float yd;
	protected float zd;
	protected boolean onGround = false;
	protected boolean removed = false;
	protected boolean noPhysics = false;

	protected Texture texture;
	protected float slowDown = 0.98f;
	protected float textureSizeDivider = 1;
	protected float textureOffset = 0;
	protected int age = 0;
	protected int lifetime = 0;
	protected float size;
	protected float bbSize = 0.2f;
	protected float gravity;
	protected float rCol;
	protected float gCol;
	protected float bCol;

	public Particle(Position pos, float xd, float yd, float zd) {
		this.pos = pos;
		float center = this.bbSize / 2;
		this.bb = new BoundingBox(pos.getX() - center, pos.getY() - center, pos.getZ() - center, pos.getX() + center, pos.getY() + center, pos.getZ() + center);

		this.rCol = 1;
		this.gCol = 1;
		this.bCol = 1;
		this.xd = xd + (float) (Math.random() * 2 - 1) * 0.4f;
		this.yd = yd + (float) (Math.random() * 2 - 1) * 0.4f;
		this.zd = zd + (float) (Math.random() * 2 - 1) * 0.4f;
		float multiplier = (float) (Math.random() + Math.random() + 1) * 0.15f;
		float len = (float) Math.sqrt(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
		this.xd = this.xd / len * multiplier * 0.4f;
		this.yd = this.yd / len * multiplier * 0.4f + 0.1f;
		this.zd = this.zd / len * multiplier * 0.4f;
		this.size = (float) (Math.random() * 0.5 + 0.5);
		this.lifetime = (int) (4 / (Math.random() * 0.9 + 0.1));
		this.age = 0;
	}

	public Particle setPower(float power) {
		this.xd *= power;
		this.yd = (this.yd - 0.1f) * power + 0.1f;
		this.zd *= power;
		return this;
	}

	public Particle scale(float scale) {
		this.setSize(0.2f * scale);
		this.size *= scale;
		return this;
	}

	public Particle setSize(float size) {
		this.bbSize = size;
		return this;
	}

	public void tick() {
		// Reset previous values by setting pos to itself.
		this.pos.set(this.pos);
		if(this.age++ >= this.lifetime) {
			this.removed = true;
		}

		this.yd = this.yd - 0.04f * this.gravity;
		this.move(this.xd, this.yd, this.zd);
		this.xd *= this.slowDown;
		this.yd *= this.slowDown;
		this.zd *= this.slowDown;
		if(this.onGround) {
			this.xd *= 0.7f;
			this.zd *= 0.7f;
		}
	}

	public void render(float dt, float xmod, float ymod, float zmod, float xdir, float zdir) {
		if(this.texture == null) {
			return;
		}
		
		this.texture.bind();
		float tminX = (this.texture.getX() / (float) this.texture.getFullWidth()) + this.textureOffset;
		float tmaxX = tminX + this.texture.getWidth() / this.textureSizeDivider / this.texture.getFullWidth();
		float tminY = (this.texture.getY() / (float) this.texture.getFullHeight()) + this.textureOffset;
		float tmaxY = tminY + this.texture.getHeight() / this.textureSizeDivider / this.texture.getFullHeight();
		float size = 0.1f * this.size;
		float x = this.pos.getInterpolatedX(dt);
		float y = this.pos.getInterpolatedY(dt);
		float z = this.pos.getInterpolatedZ(dt);
		float brightness = this.getBrightness(dt);
		Renderer.get().begin();
		Renderer.get().color(this.rCol * brightness, this.gCol * brightness, this.bCol * brightness);
		Renderer.get().vertexuv(x - xmod * size - xdir * size, y - ymod * size, z - zmod * size - zdir * size, tminX, tmaxY);
		Renderer.get().vertexuv(x - xmod * size + xdir * size, y + ymod * size, z - zmod * size + zdir * size, tminX, tminY);
		Renderer.get().vertexuv(x + xmod * size + xdir * size, y + ymod * size, z + zmod * size + zdir * size, tmaxX, tminY);
		Renderer.get().vertexuv(x + xmod * size - xdir * size, y - ymod * size, z + zmod * size - zdir * size, tmaxX, tmaxY);
		Renderer.get().end();
	}

	public void move(float x, float y, float z) {
		if(this.noPhysics) {
			this.bb.move(x, y, z);
			this.pos.set((this.bb.getX1() + this.bb.getX2()) / 2, this.bb.getY1() + 0.1f, (this.bb.getZ1() + this.bb.getZ2()) / 2);
		} else {
			float oldX = x;
			float oldY = y;
			float oldZ = z;
			ArrayList<BoundingBox> cubes = ((ClientLevel) this.pos.getLevel()).getBoxes(this.bb.expand(x, y, z));
			for(BoundingBox cube : cubes) {
				y = cube.clipYCollide(this.bb, y);
			}

			this.bb.move(0, y, 0);
			for(BoundingBox cube : cubes) {
				x = cube.clipXCollide(this.bb, x);
			}

			this.bb.move(x, 0, 0);
			for(BoundingBox cube : cubes) {
				z = cube.clipZCollide(this.bb, z);
			}

			this.bb.move(0, 0, z);
			this.onGround = oldY != y && oldY < 0;
			if(oldX != x) {
				this.xd = 0;
			}

			if(oldY != y) {
				this.yd = 0;
			}

			if(oldZ != z) {
				this.zd = 0;
			}

			this.pos.set((this.bb.getX1() + this.bb.getX2()) / 2, this.bb.getY1() + 0.1f, (this.bb.getZ1() + this.bb.getZ2()) / 2);
		}
	}

	protected float getBrightness(float dt) {
		return this.pos.getLevel().getBrightness((int) this.pos.getX(), (int) (this.pos.getY() - 0.45f), (int) this.pos.getZ());
	}

}
