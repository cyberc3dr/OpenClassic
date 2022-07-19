package org.spacehq.openclassic.api.level.generator.noise;

import java.util.Random;

/**
 * Perlin noise used in generating a normal level.
 */
public class PerlinNoise extends Noise {

	private int[] permutations;

	public PerlinNoise() {
		this(new Random());
	}

	public PerlinNoise(Random rand) {
		this.permutations = new int[512];
		for(int count = 0; count < 256; count++) {
			this.permutations[count] = count;
		}

		for(int count = 0; count < 256; count++) {
			int ind = rand.nextInt(256 - count) + count;
			int val = this.permutations[count];
			this.permutations[count] = this.permutations[ind];
			this.permutations[ind] = val;
			this.permutations[count + 256] = this.permutations[count];
		}
	}

	private static double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private static double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}

	private static double grad(int hash, double x, double y, double z) {
		int h = hash & 15;
		double u = h < 8 ? x : y;
		double v = h < 4 ? y : (h != 12 && h != 14 ? z : x);
		return ((hash & 1) == 0 ? u : -u) + ((hash & 2) == 0 ? v : -v);
	}

	@Override
	public double compute(double x, double z) {
		int fx = (int) Math.floor(x) & 255;
		int fz = (int) Math.floor(z) & 255;
		x -= Math.floor(x);
		z -= Math.floor(z);
		double u = fade(x);
		double w = fade(z);
		int a = this.permutations[this.permutations[fx] + fz + 1];
		int b = this.permutations[this.permutations[fx] + fz];
		int px = this.permutations[this.permutations[fx + 1] + fz + 1];
		int pz = this.permutations[this.permutations[px + 1] + fz];
		return lerp(0, lerp(w, lerp(u, grad(this.permutations[b], x, z, 0), grad(this.permutations[pz], x - 1.0D, z, 0)), lerp(u, grad(this.permutations[a], x, z - 1.0D, 0), grad(this.permutations[px], x - 1.0D, z - 1.0D, 0))), lerp(w, lerp(u, grad(this.permutations[b + 1], x, z, -1), grad(this.permutations[pz + 1], x - 1.0D, z, -1)), lerp(u, grad(this.permutations[a + 1], x, z - 1.0D, -1), grad(this.permutations[px + 1], x - 1.0D, z - 1.0D, -1))));
	}

}
