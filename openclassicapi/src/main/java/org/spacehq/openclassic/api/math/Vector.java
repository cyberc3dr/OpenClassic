package org.spacehq.openclassic.api.math;

/**
 * Represents a vector containing 3 floats.
 */
public class Vector implements Cloneable {

	private float x;
	private float y;
	private float z;
	
	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Gets the X coordinate of this vector.
	 * @return The X coordinate.
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Gets the Y coordinate of this vector.
	 * @return The Y coordinate.
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Gets the Z coordinate of this vector.
	 * @return The Z coordinate.
	 */
	public float getZ() {
		return this.z;
	}
	
	/**
	 * Sets the X coordinate of this vector.
	 * @param x The X coordinate.
	 */
	public void setX(float x) {
		this.x = x;
	}
	
	/**
	 * Sets the Y coordinate of this vector.
	 * @param y The Y coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * Sets the Z coordinate of this vector.
	 * @param z The Z coordinate.
	 */
	public void setZ(float z) {
		this.z = z;
	}
	
	/**
	 * Sets this vector's coordinates.
	 * @param x X coordinate to set.
	 * @param y Y coordinate to set.
	 * @param z Z coordinate to set.
	 */
	public Vector set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Sets this vector's coordinates.
	 * @param vec Vector to get the coordinates from.
	 */
	public Vector set(Vector vec) {
		return this.set(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Adds the specified vector's coordinates to this vector's coordinates.
	 * @param vec Vector to add.
	 * @return This vector after adding.
	 */
	public Vector add(Vector vec) {
		return this.add(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Adds the specified coordinates to this vector's coordinates.
	 * @param x X to add.
	 * @param y Y to add.
	 * @param z Z to add.
	 * @return This vector after adding.
	 */
	public Vector add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		
		return this;
	}
	
	/**
	 * Subtracts the specified vector's coordinates from this vector's coordinates.
	 * @param vec Vector to subtract.
	 * @return This vector after subtracting.
	 */
	public Vector subtract(Vector vec) {
		return this.subtract(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Subtracts the specified coordinates from this vector's coordinates.
	 * @param x X to subtract.
	 * @param y Y to subtract.
	 * @param z Z to subtract.
	 * @return This vector after subtracting.
	 */
	public Vector subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		
		return this;
	}
	
	/**
	 * Multiplies this vector's coordinates by the specified vector's coordinates.
	 * @param vec Vector to multiply by.
	 * @return This vector after multiplying.
	 */
	public Vector multiply(Vector vec) {
		return this.multiply(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Multiplies this vector's coordinates by the specified coordinates.
	 * @param x X to multiply by.
	 * @param y Y to multiply by.
	 * @param z Z to multiply by.
	 * @return This vector after multiplying.
	 */
	public Vector multiply(double x, double y, double z) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		
		return this;
	}
	
	/**
	 * Divides this vector's coordinates by the specified vector's coordinates.
	 * @param vec Vector to divide by.
	 * @return This vector after dividing.
	 */
	public Vector divide(Vector vec) {
		return this.divide(vec.getX(), vec.getY(), vec.getZ());
	}
	
	/**
	 * Divides this vector's coordinates by the specified coordinates.
	 * @param x X to divide by.
	 * @param y Y to divide by.
	 * @param z Z to divide by.
	 * @return This vector after dividing.
	 */
	public Vector divide(double x, double y, double z) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		
		return this;
	}

    /**
     * Gets the distance between this vector and another.
     * WARNING: Very costly.
     * @param vec Other vector.
     * @return Distance between the vectors.
     */
    public float distance(Vector vec) {
        return (float) Math.sqrt(this.distanceSquared(vec));
    }

    /**
     * Get the squared distance between this vector and another.
     * @param vec Other vector.
     * @return Square distance between the vectors.
     */
    public float distanceSquared(Vector vec) {
        return (float) (Math.pow(this.x - vec.x, 2) + Math.pow(this.y - vec.y, 2) + Math.pow(this.z - vec.z, 2));
    }
    
    /**
     * Gets the length of this vector.
     * @return The length of this vector.
     */
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    /**
     * Get the squared length of this vector.
     * @return Square length of this vector.
     */
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    /**
     * Zeros all the values of this vector.
     * @return This vector after zeroing.
     */
    public Vector zero() {
    	this.x = 0;
    	this.y = 0;
    	this.z = 0;
    	
    	return this;
    }
    
    /**
     * Normalizes this vector.
     */
    public Vector normalize() {
    	double length = this.length();
    	this.divide(length, length, length);
    	return this;
    }
	
	/**
	 * Clones this vector.
	 */
	@Override
	public Vector clone() {
		return new Vector(this.x, this.y, this.z);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Vector)) return false;
		if(this == o) return true;
		
		Vector vec = (Vector) o;
		return Float.floatToIntBits(this.x) == Float.floatToIntBits(vec.x) && Float.floatToIntBits(this.y) == Float.floatToIntBits(vec.y) && Float.floatToIntBits(this.z) == Float.floatToIntBits(vec.z);
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + Float.floatToIntBits(this.x);
		hash = 31 * hash + Float.floatToIntBits(this.y);
		hash = 31 * hash + Float.floatToIntBits(this.z);

		return hash;
	}
	
	@Override
	public String toString() {
		return "Vector{x=" + this.x + ",y=" + this.y + ",z=" + this.z + "}";
	}
	
}
