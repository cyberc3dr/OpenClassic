package org.spacehq.openclassic.api.math;

import org.spacehq.openclassic.api.block.model.Vertex;

public class BoundingBox implements Cloneable {
	
	private float x1;
	private float y1;
	private float z1;
	private float x2;
	private float y2;
	private float z2;
	
	public BoundingBox(float x1, float y1, float z1, float x2, float y2, float z2) {
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}
	
	/**
	 * Gets the X of the first point of the bounding box.
	 * @return The X of the first point.
	 */
	public float getX1() {
		return this.x1;
	}
	
	
	/**
	 * Gets the Y of the first point of the bounding box.
	 * @return The Y of the first point.
	 */
	public float getY1() {
		return this.y1;
	}
	
	/**
	 * Gets the Z of the first point of the bounding box.
	 * @return The Z of the first point.
	 */
	public float getZ1() {
		return this.z1;
	}
	
	/**
	 * Gets the X of the second point of the bounding box.
	 * @return The X of the second point.
	 */
	public float getX2() {
		return this.x2;
	}
	
	/**
	 * Gets the Y of the second point of the bounding box.
	 * @return The Y of the second point.
	 */
	public float getY2() {
		return this.y2;
	}
	
	/**
	 * Gets the Z of the second point of the bounding box.
	 * @return The Z of the second point.
	 */
	public float getZ2() {
		return this.z2;
	}
	
	public BoundingBox expand(float x, float y, float z) {
		float x0 = this.x1;
		float y0 = this.y1;
		float z0 = this.z1;
		float x1 = this.x2;
		float y1 = this.y2;
		float z1 = this.z2;
		
		if (x < 0.0F) {
			x0 += x;
		}

		if (x > 0.0F) {
			x1 += x;
		}

		if (y < 0.0F) {
			y0 += y;
		}

		if (y > 0.0F) {
			y1 += y;
		}

		if (z < 0.0F) {
			z0 += z;
		}

		if (z > 0.0F) {
			z1 += z;
		}

		return new BoundingBox(x0, y0, z0, x1, y1, z1);
	}

	public BoundingBox grow(float x, float y, float z) {
		float x0 = this.x1 - x;
		float y0 = this.y1 - y;
		float z0 = this.z1 - z;
		float x1 = this.x2 + x;
		float y1 = this.y2 + y;
		float z1 = this.z2 + z;
		
		return new BoundingBox(x0, y0, z0, x1, y1, z1);
	}

	public BoundingBox cloneMove(float x, float y, float z) {
		return new BoundingBox(this.x1 + z, this.y1 + y, this.z1 + z, this.x2 + x, this.y2 + y, this.z2 + z);
	}

	public float clipXCollide(BoundingBox bounding, float x) {
		if (bounding.getY2() > this.y1 && bounding.getY1() < this.y2) {
			if (bounding.getZ2() > this.z1 && bounding.getZ1() < this.z2) {
				float collX = this.x1 - bounding.getX2();
				if (x > 0 && bounding.getX2() <= this.x1 && collX < x) {
					return collX;
				}
				
				collX = this.x2 - bounding.getX1();
				if (x < 0 && bounding.getX1() >= this.x2 && collX > x) {
					return collX;
				}
			}
		}
		
		return x;
	}

	public float clipYCollide(BoundingBox bounding, float y) {
		if (bounding.getX2() > this.x1 && bounding.getX1() < this.x2) {
			if (bounding.getZ2() > this.z1 && bounding.getZ1() < this.z2) {
				float collY = this.y1 - bounding.getY2();
				if (y > 0 && bounding.getY2() <= this.y1 && collY < y) {
					return collY;
				}

				collY = this.y2 - bounding.getY1();
				if (y < 0 && bounding.getY1() >= this.y2 && collY > y) {
					return collY;
				}
			}
		}
		
		return y;
	}

	public float clipZCollide(BoundingBox bounding, float z) {
		if (bounding.getX2() > this.x1 && bounding.getX1() < this.x2) {
			if (bounding.getY2() > this.y1 && bounding.getY1() < this.y2) {
				float collZ = this.z1 - bounding.getZ2();
				if (z > 0 && bounding.getZ2() <= this.z1 && collZ < z) {
					return collZ;
				}

				collZ = this.z2 - bounding.getZ1(); 
				if (z < 0 && bounding.getZ1() >= this.z2 && collZ > z) {
					return collZ;
				}
			}
		}
		
		return z;
	}

	public boolean intersects(BoundingBox bounding) {
		return bounding.getX2() > this.x1 && bounding.getX1() < this.x2 && bounding.getY2() > this.y1 && bounding.getY1() < this.y2 && bounding.getZ2() > this.z1 && bounding.getZ1() < this.z2;
	}

	public boolean intersectsInner(BoundingBox bounding) {
		return bounding.getX2() >= this.x1 && bounding.getX1() <= this.x2 && bounding.getY2() >= this.y1 && bounding.getY1() <= this.y2 && bounding.getZ2() >= this.z1 && bounding.getZ1() <= this.z2;
	}

	public void move(float x, float y, float z) {
		this.x1 += x;
		this.y1 += y;
		this.z1 += z;
		this.x2 += x;
		this.y2 += y;
		this.z2 += z;
	}

	public boolean intersects(float x1, float y1, float z1, float x2, float y2, float z2) {
		return x2 > this.x1 && x1 < this.x2 && y2 > this.y1 && y1 < this.y2 && z2 > this.z1 && z1 < this.z2;
	}

	public boolean contains(Vertex point) {
		return point.getX() > this.x1 && point.getX() < this.x2 && point.getY() > this.y1 && point.getY() < this.y2 && point.getZ() > this.z1 && point.getZ() < this.z2;
	}

	public float getSize() {
		float length = this.x2 - this.x1;
		float height = this.y2 - this.y1;
		float depth = this.z2 - this.z1;
		return (length + height + depth) / 3.0F;
	}

	public BoundingBox shrink(float x, float y, float z) {
		float x1 = this.x1;
		float y1 = this.y1;
		float z1 = this.z1;
		float x2 = this.x2;
		float y2 = this.y2;
		float z2 = this.z2;
		
		if (x < 0) {
			x1 -= x;
		}

		if (x > 0) {
			x2 -= x;
		}

		if (y < 0) {
			y1 -= y;
		}

		if (y > 0) {
			y2 -= y;
		}

		if (z < 0) {
			z1 -= z;
		}

		if (z > 0) {
			z2 -= z;
		}

		return new BoundingBox(x1, y1, z1, x2, y2, z2);
	}
	
	public BoundingBox scale(float scale) {
		return new BoundingBox(this.x1 * scale, this.y1 * scale, this.z1 * scale, this.x2 * scale, this.y2 * scale, this.z2 * scale);
	}

	@Override
	public BoundingBox clone() {
		return new BoundingBox(this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
	}
	
	@Override
	public String toString() {
		return "BoundingBox{x1=" + this.x1 + ",y1=" + this.y1 + ",z1=" + this.z1 + ",x2=" + this.x2 + ",y2=" + this.y2 + ",z2=" + this.z2 + "}";
	}
	
}
