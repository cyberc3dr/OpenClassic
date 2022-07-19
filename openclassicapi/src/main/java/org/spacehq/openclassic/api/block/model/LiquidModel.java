package org.spacehq.openclassic.api.block.model;

/**
 * A model used in liquids.
 */
public class LiquidModel extends CuboidModel {
	
	public LiquidModel(Texture texture, int textureIds[], boolean top, int subWidth, int subHeight) {
		super(texture, textureIds, 0, 0, 0, 1, top ? 0.95f : 1, 1, subWidth, subHeight);
		this.setUseCulling(false);
		this.setCollisionBox(null);
		this.setSelectionBox(null);
	}

	public LiquidModel(Texture texture, int textureId, boolean top, int subWidth, int subHeight) {
		this(texture, new int[] { textureId, textureId, textureId, textureId, textureId, textureId }, top, subWidth, subHeight);
	}

}
