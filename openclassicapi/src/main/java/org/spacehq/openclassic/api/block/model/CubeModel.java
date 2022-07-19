package org.spacehq.openclassic.api.block.model;

/**
 * A model shaped like a cube.
 */
public class CubeModel extends CuboidModel {

	public CubeModel(Texture texture, int textureIds[], int subWidth, int subHeight) {
		super(texture, textureIds, 0, 0, 0, 1, 1, 1, subWidth, subHeight);
	}
	
	public CubeModel(Texture texture, int textureId, int subWidth, int subHeight) {
		this(texture, new int[] { textureId, textureId, textureId, textureId, textureId, textureId }, subWidth, subHeight);
	}
	
}
