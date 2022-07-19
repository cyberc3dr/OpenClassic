package org.spacehq.openclassic.client.math;

import org.spacehq.openclassic.api.block.BlockFace;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.model.Model;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.api.math.Vector;
import org.spacehq.openclassic.client.level.ClientLevel;

public class RayTracer {

	private static final float EPSILON = 0.0000001f;
	
	public static Intersection rayTrace(ClientLevel level, Vector vec1, Vector vec2, boolean selection) {
		if(!Float.isNaN(vec1.getX()) && !Float.isNaN(vec1.getY()) && !Float.isNaN(vec1.getZ()) && !Float.isNaN(vec2.getX()) && !Float.isNaN(vec2.getY()) && !Float.isNaN(vec2.getZ())) {
			int bx2 = (int) Math.floor(vec2.getX());
			int by2 = (int) Math.floor(vec2.getY());
			int bz2 = (int) Math.floor(vec2.getZ());
			int bx1 = (int) Math.floor(vec1.getX());
			int by1 = (int) Math.floor(vec1.getY());
			int bz1 = (int) Math.floor(vec1.getZ());
			for(int total = 20; total >= 0; total--) {
				if(Float.isNaN(vec1.getX()) || Float.isNaN(vec1.getY()) || Float.isNaN(vec1.getZ())) {
					return null;
				}

				if(bx1 == bx2 && by1 == by2 && bz1 == bz2) {
					return null;
				}

				float xmax1 = 999;
				float ymax1 = 999;
				float zmax1 = 999;
				if(bx2 > bx1) {
					xmax1 = bx1 + 1;
				}

				if(bx2 < bx1) {
					xmax1 = bx1;
				}

				if(by2 > by1) {
					ymax1 = by1 + 1;
				}

				if(by2 < by1) {
					ymax1 = by1;
				}

				if(bz2 > bz1) {
					zmax1 = bz1 + 1;
				}

				if(bz2 < bz1) {
					zmax1 = bz1;
				}

				float dxmax = 999;
				float dymax = 999;
				float dzmax = 999;
				float dx = vec2.getX() - vec1.getX();
				float dy = vec2.getY() - vec1.getY();
				float dz = vec2.getZ() - vec1.getZ();
				if(xmax1 != 999) {
					dxmax = (xmax1 - vec1.getX()) / dx;
				}

				if(ymax1 != 999) {
					dymax = (ymax1 - vec1.getY()) / dy;
				}

				if(zmax1 != 999) {
					dzmax = (zmax1 - vec1.getZ()) / dz;
				}

				byte face = 0;
				if(dxmax < dymax && dxmax < dzmax) {
					if(bx2 > bx1) {
						face = 4;
					} else {
						face = 5;
					}

					vec1.setX(xmax1);
					vec1.add(0, dy * dxmax, dz * dxmax);
				} else if(dymax < dzmax) {
					if(by2 > by1) {
						face = 0;
					} else {
						face = 1;
					}

					vec1.setY(ymax1);
					vec1.add(dx * dymax, 0, dz * dymax);
				} else {
					if(bz2 > bz1) {
						face = 2;
					} else {
						face = 3;
					}

					vec1.setZ(zmax1);
					vec1.add(dx * dzmax, dy * dzmax, 0);
				}

				Vector vec = vec1.clone();
				vec.setX((float) Math.floor(vec1.getX()));
				bx1 = (int) vec.getX();
				if(face == 5) {
					bx1--;
					vec.setX(vec.getX() + 1);
				}

				vec.setY((float) Math.floor(vec1.getY()));
				by1 = (int) vec.getY();
				if(face == 1) {
					by1--;
					vec.setY(vec.getY() + 1);
				}

				vec.setZ((float) Math.floor(vec1.getZ()));
				bz1 = (int) vec.getZ();
				if(face == 3) {
					bz1--;
					vec.setZ(vec.getZ() + 1);
				}

				BlockType type = level.getBlockTypeAt(bx1, by1, bz1);
				if(type != null) {
					Intersection collision = rayTraceBlock(level, bx1, by1, bz1, vec1.clone().subtract(bx1, by1, bz1), vec2.clone().subtract(bx1, by1, bz1), selection);
					if(collision != null) {
						collision.getPos().add(bx1, by1, bz1);
						return collision;
					}
				}
			}
		}

		return null;
	}
	
	private static Intersection rayTraceBlock(ClientLevel level, int x, int y, int z, Vector point, Vector other, boolean selection) {
		Model model = level.getBlockTypeAt(x, y, z).getModel(level, x, y, z);
		BoundingBox box = selection ? model.getSelectionBox(0, 0, 0) : model.getCollisionBox(0, 0, 0);
		if(box == null) {
			return null;
		}
		
		Vector x1 = getXIntersection(point, other, box.getX1());
		Vector x2 = getXIntersection(point, other, box.getX2());
		Vector y1 = getYIntersection(point, other, box.getY1());
		Vector y2 = getYIntersection(point, other, box.getY2());
		Vector z1 = getZIntersection(point, other, box.getZ1());
		Vector z2 = getZIntersection(point, other, box.getZ2());
		if(x1 != null && !xIntersects(selection ? model.getSelectionBox((int) x1.getX(), (int) x1.getY(), (int) x1.getZ()) : model.getCollisionBox((int) x1.getX(), (int) x1.getY(), (int) x1.getZ()), x1)) {
			x1 = null;
		}

		if(x2 != null && !xIntersects(selection ? model.getSelectionBox((int) x2.getX(), (int) x2.getY(), (int) x2.getZ()) : model.getCollisionBox((int) x2.getX(), (int) x2.getY(), (int) x2.getZ()), x2)) {
			x2 = null;
		}

		if(y1 != null && !yIntersects(selection ? model.getSelectionBox((int) y1.getX(), (int) y1.getY(), (int) y1.getZ()) : model.getCollisionBox((int) y1.getX(), (int) y1.getY(), (int) y1.getZ()), y1)) {
			y1 = null;
		}

		if(y2 != null && !yIntersects(selection ? model.getSelectionBox((int) y2.getX(), (int) y2.getY(), (int) y2.getZ()) : model.getCollisionBox((int) y2.getX(), (int) y2.getY(), (int) y2.getZ()), y2)) {
			y2 = null;
		}

		if(z1 != null && !zIntersects(selection ? model.getSelectionBox((int) z1.getX(), (int) z1.getY(), (int) z1.getZ()) : model.getCollisionBox((int) z1.getX(), (int) z1.getY(), (int) z1.getZ()), z1)) {
			z1 = null;
		}

		if(z2 != null && !zIntersects(selection ? model.getSelectionBox((int) z2.getX(), (int) z2.getY(), (int) z2.getZ()) : model.getCollisionBox((int) z2.getX(), (int) z2.getY(), (int) z2.getZ()), z2)) {
			z2 = null;
		}

		Vector result = null;

		if(x1 != null) {
			result = x1;
		}

		if(x2 != null && (result == null || point.distanceSquared(x2) < point.distanceSquared(result))) {
			result = x2;
		}

		if(y1 != null && (result == null || point.distanceSquared(y1) < point.distanceSquared(result))) {
			result = y1;
		}

		if(y2 != null && (result == null || point.distanceSquared(y2) < point.distanceSquared(result))) {
			result = y2;
		}

		if(z1 != null && (result == null || point.distanceSquared(z1) < point.distanceSquared(result))) {
			result = z1;
		}

		if(z2 != null && (result == null || point.distanceSquared(z2) < point.distanceSquared(result))) {
			result = z2;
		}

		if(result == null) {
			return null;
		} else {
			BlockFace face = null;
			if(result == x1) {
				face = BlockFace.SOUTH;
			}

			if(result == x2) {
				face = BlockFace.NORTH;
			}

			if(result == y1) {
				face = BlockFace.DOWN;
			}

			if(result == y2) {
				face = BlockFace.UP;
			}

			if(result == z1) {
				face = BlockFace.WEST;
			}

			if(result == z2) {
				face = BlockFace.EAST;
			}

			return new Intersection(x, y, z, face, result);
		}
	}
	
	public static Intersection rayTrace(BoundingBox box, Vector point, Vector other) {
		Vector x1 = getXIntersection(point, other, box.getX1());
		Vector x2 = getXIntersection(point, other, box.getX2());
		Vector y1 = getYIntersection(point, other, box.getY1());
		Vector y2 = getYIntersection(point, other, box.getY2());
		Vector z1 = getZIntersection(point, other, box.getZ1());
		Vector z2 = getZIntersection(point, other, box.getZ2());
		if(!xIntersects(box, x1)) {
			x1 = null;
		}

		if(!xIntersects(box, x2)) {
			x2 = null;
		}

		if(!yIntersects(box, y1)) {
			y1 = null;
		}

		if(!yIntersects(box, y2)) {
			y2 = null;
		}

		if(!zIntersects(box, z1)) {
			z1 = null;
		}

		if(!zIntersects(box, z2)) {
			z2 = null;
		}

		Vector result = null;

		if(x1 != null) {
			result = x1;
		}

		if(x2 != null && (result == null || point.distanceSquared(x2) < point.distanceSquared(result))) {
			result = x2;
		}

		if(y1 != null && (result == null || point.distanceSquared(y1) < point.distanceSquared(result))) {
			result = y1;
		}

		if(y2 != null && (result == null || point.distanceSquared(y2) < point.distanceSquared(result))) {
			result = y2;
		}

		if(z1 != null && (result == null || point.distanceSquared(z1) < point.distanceSquared(result))) {
			result = z1;
		}

		if(z2 != null && (result == null || point.distanceSquared(z2) < point.distanceSquared(result))) {
			result = z2;
		}

		if(result == null) {
			return null;
		} else {
			BlockFace face = null;
			if(result == x1) {
				face = BlockFace.SOUTH;
			}

			if(result == x2) {
				face = BlockFace.NORTH;
			}

			if(result == y1) {
				face = BlockFace.DOWN;
			}

			if(result == y2) {
				face = BlockFace.UP;
			}

			if(result == z1) {
				face = BlockFace.WEST;
			}

			if(result == z2) {
				face = BlockFace.EAST;
			}

			return new Intersection(0, 0, 0, face, result);
		}
	}
	
	private static Vector getXIntersection(Vector vec, Vector other, float intersectX) {
		float x = other.getX() - vec.getX();
		float y = other.getY() - vec.getY();
		float z = other.getZ() - vec.getZ();
		float ln = (intersectX - vec.getX()) / x;
		return x * x < EPSILON ? null : (ln >= 0 && ln <= 1 ? new Vector(vec.getX() + x * ln, vec.getY() + y * ln, vec.getZ() + z * ln) : null);
	}

	private static Vector getYIntersection(Vector vec, Vector other, float intersectY) {
		float x = other.getX() - vec.getX();
		float y = other.getY() - vec.getY();
		float z = other.getZ() - vec.getZ();
		float ln = (intersectY - vec.getY()) / y;
		return y * y < EPSILON ? null : (ln >= 0 && ln <= 1 ? new Vector(vec.getX() + x * ln, vec.getY() + y * ln, vec.getZ() + z * ln) : null);
	}

	private static Vector getZIntersection(Vector vec, Vector other, float intersectZ) {
		float x = other.getX() - vec.getX();
		float y = other.getY() - vec.getY();
		float z = other.getZ() - vec.getZ();
		float ln = (intersectZ - vec.getZ()) / z;
		return z * z < EPSILON ? null : (ln >= 0 && ln <= 1 ? new Vector(vec.getX() + x * ln, vec.getY() + y * ln, vec.getZ() + z * ln) : null);
	}

	private static boolean xIntersects(BoundingBox box, Vector point) {
		return point != null && point.getY() >= box.getY1() && point.getY() <= box.getY2() && point.getZ() >= box.getZ1() && point.getZ() <= box.getZ2();
	}

	private static boolean yIntersects(BoundingBox box, Vector point) {
		return point != null && point.getX() >= box.getX1() && point.getX() <= box.getX2() && point.getZ() >= box.getZ1() && point.getZ() <= box.getZ2();
	}

	private static boolean zIntersects(BoundingBox box, Vector point) {
		return point != null && point.getX() >= box.getX1() && point.getX() <= box.getX2() && point.getY() >= box.getY1() && point.getY() <= box.getY2();
	}
	
}
