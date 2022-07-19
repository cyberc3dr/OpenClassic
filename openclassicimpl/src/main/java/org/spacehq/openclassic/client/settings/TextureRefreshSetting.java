package org.spacehq.openclassic.client.settings;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.block.model.TextureFactory;
import org.spacehq.openclassic.api.settings.BooleanSetting;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.render.ClientTextureFactory;

public class TextureRefreshSetting extends BooleanSetting {

	public TextureRefreshSetting(String name) {
		super(name);
	}
	
	@Override
	public void toggle() {
		super.toggle();
		((ClientTextureFactory) TextureFactory.getFactory()).resetTextures();
		ClientLevel level = (ClientLevel) OpenClassic.getClient().getLevel();
		if(level != null) {
			level.refreshRenderer();
		}
	}

}
