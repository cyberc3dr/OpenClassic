package org.spacehq.openclassic.game;

import java.awt.image.BufferedImage;

import org.spacehq.openclassic.api.block.model.Texture;

public interface TextureBase extends Texture {

	public BufferedImage getImage();
	
}
