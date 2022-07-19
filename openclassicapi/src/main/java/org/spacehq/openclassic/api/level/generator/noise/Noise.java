package org.spacehq.openclassic.api.level.generator.noise;

/**
 * Noise used in generating a normal level.
 */
public abstract class Noise {

	/**
	 * Computes a noise value for the given x and z.
	 * @param x X coordinate.
	 * @param z Z coordinate.
	 * @return The computed noise value.
	 */
	public abstract double compute(double x, double z);

}
