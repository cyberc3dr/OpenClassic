package org.spacehq.openclassic.api;

import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.api.math.Vector;

/**
 * Represents a point in a world.
 */
public class Position implements Cloneable {

	private Level level;
	private float x;
	private float prevX;
	private float y;
	private float prevY;
	private float z;
	private float prevZ;
	private float yaw;
	private float prevYaw;
	private float pitch;
	private float prevPitch;
	
	public Position(Level level, float x, float y, float z) {
		this(level, x, y, z, 0, 0);
	}
	
	public Position(Level level, float x, float y, float z, float yaw, float pitch) {
		this.level = level;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.prevYaw = yaw;
		this.prevPitch = pitch;
	}
	
	/**
	 * Gets the level this position is located in.
	 * @return The level.
	 */
	public Level getLevel() {
		return this.level;
	}
	
	/**
	 * Gets the X coordinate of this position.
	 * @return The X coordinate.
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Gets the Y coordinate of this position.
	 * @return The Y coordinate.
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Gets the Z coordinate of this position.
	 * @return The Z coordinate.
	 */
	public float getZ() {
		return this.z;
	}
	
	/**
	 * Gets the previous X coordinate of this position.
	 * @return The X coordinate.
	 */
	public float getPreviousX() {
		return this.prevX;
	}
	
	/**
	 * Gets the previous Y coordinate of this position.
	 * @return The Y coordinate.
	 */
	public float getPreviousY() {
		return this.prevY;
	}
	
	/**
	 * Gets the previous Z coordinate of this position.
	 * @return The Z coordinate.
	 */
	public float getPreviousZ() {
		return this.prevZ;
	}
	
	/**
	 * Gets the interpolated X coordinate of this position.
	 * @param interpolation Interpolation to use.
	 * @return The interpolated X coordinate.
	 */
	public float getInterpolatedX(float interpolation) {
		return (this.x - this.prevX) * interpolation + this.prevX;
	}
	
	/**
	 * Gets the interpolated Y coordinate of this position.
	 * @param interpolation Interpolation to use.
	 * @return The interpolated Y coordinate.
	 */
	public float getInterpolatedY(float interpolation) {
		return (this.y - this.prevY) * interpolation + this.prevY;
	}
	
	/**
	 * Gets the interpolated Z coordinate of this position.
	 * @param interpolation Interpolation to use.
	 * @return The interpolated Z coordinate.
	 */
	public float getInterpolatedZ(float interpolation) {
		return (this.z - this.prevZ) * interpolation + this.prevZ;
	}
	
	/**
	 * Gets the X coordinate of the block at this position.
	 * @return The block's X coordinate.
	 */
	public int getBlockX() {
		return (int) Math.floor(this.getX());
	}
	
	/**
	 * Gets the Y coordinate of the block at this position.
	 * @return The block's Y coordinate.
	 */
	public int getBlockY() {
		return (int) Math.floor(this.getY());
	}
	
	/**
	 * Gets the Z coordinate of the block at this position.
	 * @return The block's Z coordinate.
	 */
	public int getBlockZ() {
		return (int) Math.floor(this.getZ());
	}
	
	/**
	 * Gets the yaw of this position.
	 * @return The yaw coordinate.
	 */
	public float getYaw() {
		return this.yaw;
	}
	
	/**
	 * Gets the pitch of this position.
	 * @return The pitch.
	 */
	public float getPitch() {
		return this.pitch;
	}
	
	/**
	 * Gets the interpolated yaw of this position.
	 * @param interpolation Interpolation to use.
	 * @return The interpolated yaw coordinate.
	 */
	public float getInterpolatedYaw(float interpolation) {
		return (this.yaw - this.prevYaw) * interpolation + this.prevYaw;
	}
	
	/**
	 * Gets the interpolated pitch of this position.
	 * @param interpolation Interpolation to use.
	 * @return The interpolated pitch.
	 */
	public float getInterpolatedPitch(float interpolation) {
		float inter = (this.pitch - this.prevPitch) * interpolation + this.prevPitch;
		if(inter > 90) inter = 90;
		if(inter < -90) inter = -90;
		return inter;
	}
	
	/**
	 * Sets the level this position is located in.
	 * @param level The level.
	 */
	public void setLevel(Level level) {
		this.level = level;
	}
	
	/**
	 * Sets the X coordinate of this position.
	 * @param x The X coordinate.
	 */
	public void setX(float x) {
		this.prevX = this.x;
		this.x = x;
	}
	
	/**
	 * Sets the Y coordinate of this position.
	 * @param y The Y coordinate.
	 */
	public void setY(float y) {
		this.prevY = this.y;
		this.y = y;
	}
	
	/**
	 * Sets the Z coordinate of this position.
	 * @param z The Z coordinate.
	 */
	public void setZ(float z) {
		this.prevZ = this.z;
		this.z = z;
	}
	
	/**
	 * Sets the yaw of this position.
	 * @param yaw The yaw.
	 */
	public void setYaw(float yaw) {
		float oldYaw = yaw;
		while(yaw >= 180) {
			yaw -= 360;
		}

		while(yaw < -180) {
			yaw += 360;
		}
		
		this.prevYaw = yaw != oldYaw ? yaw : this.yaw;
		this.yaw = yaw;
	}
	
	/**
	 * Sets the pitch of this position.
	 * @param pitch The pitch.
	 */
	public void setPitch(float pitch) {
		float oldPitch = pitch;
		if(pitch < -90) {
			pitch = -90;
		}
		
		if(pitch > 90) {
			pitch = 90;
		}
		
		this.prevPitch = pitch != oldPitch ? pitch : this.pitch;
		this.pitch = pitch;
	}
	
	/**
	 * Sets this position's coordinates.
	 * @param x X coordinate to set.
	 * @param y Y coordinate to set.
	 * @param z Z coordinate to set.
	 */
	public Position set(float x, float y, float z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		return this;
	}
	
	/**
	 * Sets this position's coordinates and rotation.
	 * @param pos Position to get the coordinates and rotation from.
	 */
	public Position set(Position pos) {
		this.setX(pos.getX());
		this.setY(pos.getY());
		this.setZ(pos.getZ());
		this.setYaw(pos.getYaw());
		this.setPitch(pos.getPitch());
		return this;
	}
	
	/**
	 * Sets this position's coordinates.
	 * @param vec Vector to get the coordinates from.
	 */
	public Position set(Vector vec) {
		this.setX(vec.getX());
		this.setY(vec.getY());
		this.setZ(vec.getZ());
		return this;
	}
	
	/**
	 * Adds the specified position's coordinates to this position's coordinates.
	 * @param pos Position to add.
	 * @return This position after adding.
	 */
	public Position add(Position pos) {
		return this.add(pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * Adds the specified vector's coordinates to this position's coordinates.
	 * @param vec Vector to add.
	 * @return This position after adding.
	 */
	public Position add(Vector vec) {
		return this.add(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Adds the specified coordinates to this position's coordinates.
	 * @param x X to add.
	 * @param y Y to add.
	 * @param z Z to add.
	 * @return This position after adding.
	 */
	public Position add(double x, double y, double z) {
		this.setX(this.x + (float) x);
		this.setY(this.y + (float) y);
		this.setZ(this.z + (float) z);
		
		return this;
	}
	
	/**
	 * Subtracts the specified position's coordinates from this position's coordinates.
	 * @param pos Position to subtract.
	 * @return This position after subtracting.
	 */
	public Position subtract(Position pos) {
		return this.subtract(pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * Subtracts the specified vector's coordinates from this position's coordinates.
	 * @param vec Vector to subtract.
	 * @return This position after subtracting.
	 */
	public Position subtract(Vector vec) {
		return this.subtract(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Subtracts the specified coordinates from this position's coordinates.
	 * @param x X to subtract.
	 * @param y Y to subtract.
	 * @param z Z to subtract.
	 * @return This position after subtracting.
	 */
	public Position subtract(double x, double y, double z) {
		this.setX(this.x - (float) x);
		this.setY(this.y - (float) y);
		this.setZ(this.z - (float) z);
		
		return this;
	}
	
	/**
	 * Multiplies this position's coordinates by the specified position's coordinates.
	 * @param pos Position to multiply by.
	 * @return This position after multiplying.
	 */
	public Position multiply(Position pos) {
		return this.multiply(pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * Multiplies this position's coordinates by the specified vector's coordinates.
	 * @param vec Vector to multiply by.
	 * @return This position after multiplying.
	 */
	public Position multiply(Vector vec) {
		return this.multiply(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Multiplies this position's coordinates by the specified coordinates.
	 * @param x X to multiply by.
	 * @param y Y to multiply by.
	 * @param z Z to multiply by.
	 * @return This position after multiplying.
	 */
	public Position multiply(double x, double y, double z) {
		this.setX(this.x * (float) x);
		this.setY(this.y * (float) y);
		this.setZ(this.z * (float) z);
		
		return this;
	}
	
	/**
	 * Divides this position's coordinates by the specified position's coordinates.
	 * @param pos Position to divide by.
	 * @return This position after dividing.
	 */
	public Position divide(Position pos) {
		return this.divide(pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * Divides this position's coordinates by the specified vector's coordinates.
	 * @param vec Vector to divide by.
	 * @return This position after dividing.
	 */
	public Position divide(Vector vec) {
		return this.divide(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Divides this position's coordinates by the specified coordinates.
	 * @param x X to divide by.
	 * @param y Y to divide by.
	 * @param z Z to divide by.
	 * @return This position after dividing.
	 */
	public Position divide(double x, double y, double z) {
		this.setX(this.x / (float) x);
		this.setY(this.y / (float) y);
		this.setZ(this.z / (float) z);
		
		return this;
	}

    /**
     * Gets the distance between this position and another.
     * WARNING: Very costly.
     * @param pos Other position.
     * @return Distance between the positions.
     */
    public float distance(Position pos) {
        return this.distance(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Gets the distance between this position and another.
     * WARNING: Very costly.
     * @param x X coordinate of the other position.
     * @param y Y coordinate of the other position.
     * @param z Z coordinate of the other position.
     * @return Distance between the positions.
     */
    public float distance(float x, float y, float z) {
        return (float) Math.sqrt(this.distanceSquared(x, y, z));
    }
    
    /**
     * Get the squared distance between this position and another.
     * @param pos Other position.
     * @return Square distance between the positions.
     */
    public float distanceSquared(Position pos) {
    	return this.distanceSquared(pos.getX(), pos.getY(), pos.getZ());
    }
    
    /**
     * Get the squared distance between this position and another.
     * @param x X coordinate of the other position.
     * @param y Y coordinate of the other position.
     * @param z Z coordinate of the other position.
     * @return Square distance between the positions.
     */
    public float distanceSquared(float x, float y, float z) {
        return (float) (Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) + Math.pow(this.z - z, 2));
    }
    
    /**
     * Zeros all the values of this position.
     * @return This position after zeroing.
     */
    public Position zero() {
    	this.x = 0;
    	this.y = 0;
    	this.z = 0;
    	this.prevX = 0;
    	this.prevY = 0;
    	this.prevZ = 0;
    	
    	return this;
    }
    
    /**
     * Converts this position into a position vector.
     * @return The resulting position vector.
     */
    public Vector toPosVector() {
    	return new Vector(this.x, this.y, this.z);
    }
    
    /**
     * Converts this position into a position vector, interpolating the coordinate values.
     * @param interpolation Value to use for interpolation. 
     * @return The resulting position vector.
     */
    public Vector toPosVector(float interpolation) {
    	return new Vector(this.getInterpolatedX(interpolation), this.getInterpolatedY(interpolation), this.getInterpolatedZ(interpolation));
    }
    
    /**
     * Converts this position into a direction vector.
     * @return The resulting direction vector.
     */
    public Vector toDirVector() {
    	return MathHelper.toForwardVec(this.yaw, this.pitch);
    }
	
	/**
	 * Clones this Position.
	 */
	@Override
	public Position clone() {
		Position pos = new Position(this.level, this.x, this.y, this.z, this.yaw, this.pitch);
		pos.prevX = this.prevX;
		pos.prevY = this.prevY;
		pos.prevZ = this.prevZ;
		pos.prevYaw = this.prevYaw;
		pos.prevPitch = this.prevPitch;
		return pos;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Position)) return false;
		if(this == o) return true;
		
		Position pos = (Position) o;
		return Float.floatToIntBits(this.x) == Float.floatToIntBits(pos.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(pos.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(pos.z) && Float.floatToIntBits(this.yaw) == Float.floatToIntBits(pos.yaw) && Float.floatToIntBits(this.pitch) == Float.floatToIntBits(pos.pitch) && (this.level == null || pos.level == null ? this.level == pos.level : this.level.getName().equals(pos.level.getName()));
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + Float.floatToIntBits(this.x);
		hash = 31 * hash + Float.floatToIntBits(this.y);
		hash = 31 * hash + Float.floatToIntBits(this.z);
		hash = 31 * hash + Float.floatToIntBits(this.yaw);
		hash = 31 * hash + Float.floatToIntBits(this.pitch);
		hash = 31 * hash + (this.level == null || this.level.getName() == null ? 0 : this.level.getName().hashCode());
		
		return hash;
	}
	
	@Override
	public String toString() {
		return "Position{level=" + this.level.getName() + ",x=" + this.x + ",y=" + this.y + ",z=" + this.z + ",yaw=" + this.yaw + ",pitch=" + this.pitch;
	}
	
}
