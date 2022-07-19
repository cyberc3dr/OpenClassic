package org.spacehq.openclassic.client.settings;

import org.spacehq.openclassic.client.render.MipmapMode;
import org.spacehq.openclassic.client.render.RenderHelper;

public class SmoothingSetting extends TextureRefreshSetting {

	public SmoothingSetting(String name) {
		super(name);
	}
	
	@Override
	public boolean isVisible() {
		return RenderHelper.getHelper().getMipmapMode() != MipmapMode.NONE;
	}

}
