package org.spacehq.openclassic.client.settings;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.settings.BooleanSetting;
import org.spacehq.openclassic.client.gui.hud.ClientHUDScreen;

public class MinimapSetting extends BooleanSetting {

	public MinimapSetting(String name) {
		super(name);
	}
	
	@Override
	public void toggle() {
		super.toggle();
		if(OpenClassic.getClient().isInGame()) {
			if(this.getValue()) {
				((ClientHUDScreen) OpenClassic.getClient().getHUD()).addMinimap();
			} else {
				((ClientHUDScreen) OpenClassic.getClient().getHUD()).removeMinimap();
			}
		}
	}

}
