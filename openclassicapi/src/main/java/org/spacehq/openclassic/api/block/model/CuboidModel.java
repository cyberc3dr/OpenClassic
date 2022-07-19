package org.spacehq.openclassic.api.block.model;

import org.apache.commons.lang3.Validate;

/**
 * A cuboid-shaped model.
 */
public class CuboidModel extends Model {

	private boolean cube = false;
	private float x1;
	private float y1;
	private float z1;
	private float x2;
	private float y2;
	private float z2;
	private int subWidth;
	private int subHeight;

	public CuboidModel(Texture texture, int[] textureIds, float x1, float y1, float z1, float x2, float y2, float z2, int subWidth, int subHeight) {
		Validate.isTrue(textureIds.length == 6, "Texture ID array must have length of 6!");
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.subWidth = subWidth;
		this.subHeight = subHeight;
		if(x1 == 0 && y1 == 0 && z1 == 0 && x2 == 1 && y2 == 1 && z2 == 1) {
			this.cube = true;
		}

		this.setCollisionBox(x1, y1, z1, x2, y2, z2);
		this.setSelectionBox(x1, y1, z1, x2, y2, z2);

		Quad bottom = QuadFactory.getFactory().newQuad(0, texture.getSubTexture(textureIds[0], 16, 16));
		bottom.addVertex(0, x1, y1, z1);
		bottom.addVertex(1, x2, y1, z1);
		bottom.addVertex(2, x2, y1, z2);
		bottom.addVertex(3, x1, y1, z2);
		this.addQuad(bottom);

		Quad top = QuadFactory.getFactory().newQuad(1, texture.getSubTexture(textureIds[1], 16, 16));
		top.addVertex(0, x1, y2, z1);
		top.addVertex(1, x1, y2, z2);
		top.addVertex(2, x2, y2, z2);
		top.addVertex(3, x2, y2, z1);
		this.addQuad(top);

		Quad face1 = QuadFactory.getFactory().newQuad(2, texture.getSubTexture(textureIds[2], 16, 16));
		face1.addVertex(0, x1, y1, z1);
		face1.addVertex(1, x1, y2, z1);
		face1.addVertex(2, x2, y2, z1);
		face1.addVertex(3, x2, y1, z1);
		this.addQuad(face1);

		Quad face2 = QuadFactory.getFactory().newQuad(3, texture.getSubTexture(textureIds[3], 16, 16));
		face2.addVertex(0, x2, y1, z2);
		face2.addVertex(1, x2, y2, z2);
		face2.addVertex(2, x1, y2, z2);
		face2.addVertex(3, x1, y1, z2);
		this.addQuad(face2);

		Quad face3 = QuadFactory.getFactory().newQuad(4, texture.getSubTexture(textureIds[4], 16, 16));
		face3.addVertex(0, x1, y1, z2);
		face3.addVertex(1, x1, y2, z2);
		face3.addVertex(2, x1, y2, z1);
		face3.addVertex(3, x1, y1, z1);
		this.addQuad(face3);

		Quad face4 = QuadFactory.getFactory().newQuad(5, texture.getSubTexture(textureIds[5], 16, 16));
		face4.addVertex(0, x2, y1, z1);
		face4.addVertex(1, x2, y2, z1);
		face4.addVertex(2, x2, y2, z2);
		face4.addVertex(3, x2, y1, z2);
		this.addQuad(face4);
	}

	public CuboidModel(Texture texture, int textureId, float x1, float y1, float z1, float x2, float y2, float z2, int subWidth, int subHeight) {
		this(texture, new int[] { textureId, textureId, textureId, textureId, textureId, textureId }, x1, y1, z1, x2, y2, z2, subWidth, subHeight);
	}

	/**
	 * Gets whether the model is a full cube.
	 * @return Whether the model is a full cube.
	 */
	public boolean isFullCube() {
		return this.cube;
	}

	/**
	 * Gets the model's first x coordinate.
	 * @return The model's first x.
	 */
	public float getX1() {
		return this.x1;
	}

	/**
	 * Gets the model's first y coordinate.
	 * @return The model's first y.
	 */
	public float getY1() {
		return this.y1;
	}

	/**
	 * Gets the model's first z coordinate.
	 * @return The model's first z.
	 */
	public float getZ1() {
		return this.z1;
	}

	/**
	 * Gets the model's second x coordinate.
	 * @return The model's second x.
	 */
	public float getX2() {
		return this.x2;
	}

	/**
	 * Gets the model's second y coordinate.
	 * @return The model's second y.
	 */
	public float getY2() {
		return this.y2;
	}

	/**
	 * Gets the model's second z coordinate.
	 * @return The model's second z.
	 */
	public float getZ2() {
		return this.z2;
	}
	
	/**
	 * Gets the model's sub texture width.
	 * @return The model's sub texture width.
	 */
	public int getSubWidth() {
		return this.subWidth;
	}

	/**
	 * Gets the model's sub texture height.
	 * @return The model's sub texture height.
	 */
	public int getSubHeight() {
		return this.subHeight;
	}

}
