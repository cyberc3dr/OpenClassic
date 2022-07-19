package org.spacehq.openclassic.api.level.generator.noise;

import java.util.Random;

/**
 * Octave noise used in generating a normal level.
 */
public class OctaveNoise extends Noise {

	private PerlinNoise[] algs;
	private int count;

	public OctaveNoise(Random rand, int algs) {
		this.count = algs;
		this.algs = new PerlinNoise[algs];
		for(int count = 0; count < algs; ++count) {
			this.algs[count] = new PerlinNoise(rand);
		}
	}

	@Override
	public double compute(double x, double z) {
		double result = 0;
		double amp = 1;
		for(int count = 0; count < this.count; count++) {
			result += this.algs[count].compute(x / amp, z / amp) * amp;
			amp *= 2;
		}

		return result;
	}

}
