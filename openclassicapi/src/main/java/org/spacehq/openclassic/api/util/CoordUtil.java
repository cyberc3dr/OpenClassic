package org.spacehq.openclassic.api.util;

import org.spacehq.openclassic.api.level.Level;

public class CoordUtil {

	public static int coordsToBlockIndex(Level level, int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= level.getWidth() || y >= level.getHeight() || z >= level.getDepth())
			return -1;

		return (y * level.getDepth() + z) * level.getWidth() + x;
	}
	
}
