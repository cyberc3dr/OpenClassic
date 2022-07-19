package org.spacehq.openclassic.api.block.model;

/**
 * A model used in plant blocks.
 */
public class PlantModel extends Model {
	
	public PlantModel(Texture texture, int textureId, int subWidth, int subHeight) {
		this.setSelectionBox(0.2F, 0, 0.2F, 0.8F, 0.6F, 0.8F);
		this.setUseCulling(false);
		
		Quad face1 = QuadFactory.getFactory().newQuad(0, texture.getSubTexture(textureId, subWidth, subHeight));
		face1.addVertex(0, 0.85F, 0, 0.85F);
		face1.addVertex(1, 0.85F, 1, 0.85F);
		face1.addVertex(2, 0.15F, 1, 0.15F);
		face1.addVertex(3, 0.15F, 0, 0.15F);
		this.addQuad(face1);
		
		Quad face2 = QuadFactory.getFactory().newQuad(1, texture.getSubTexture(textureId, subWidth, subHeight));
		face2.addVertex(0, 0.85F, 0, 0.15F);
		face2.addVertex(1, 0.85F, 1, 0.15F);
		face2.addVertex(2, 0.15F, 1, 0.85F);
		face2.addVertex(3, 0.15F, 0, 0.85F);
		this.addQuad(face2);
	}
	
}
