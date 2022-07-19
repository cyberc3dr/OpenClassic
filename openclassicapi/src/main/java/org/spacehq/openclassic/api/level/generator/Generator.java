package org.spacehq.openclassic.api.level.generator;

import java.util.Random;

import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.level.Level;

/**
 * Represents a map generator.
 */
public abstract class Generator {

	protected static Random rand = new Random();
	
	/**
	 * Generates the level.
	 * @param level Level to generate.
	 * @param blocks Generated block IDs.
	 */
	public abstract void generate(Level level, byte blocks[]);
	
	/**
	 * Finds a spawn for the level.
	 * @param level The level to find a spawn for.
	 * @return The spawn found for the level.
	 */
	public Position findSpawn(Level level) {
		int attempts = 0;
		int x = 0;
		int z = 0;
		int y = 0;
		while(y <= level.getWaterLevel()) {
			attempts++;
			x = rand.nextInt(level.getWidth() / 2) + level.getWidth() / 4;
			y = level.getHighestBlockY(x, z) + 1;
			z = rand.nextInt(level.getDepth() / 2) + level.getDepth() / 4;
			if(attempts == 10000) {
				y = level.getGroundLevel();
				break;
			}
		}
		
		return new Position(level, x, y, z);	
	}
	
}
