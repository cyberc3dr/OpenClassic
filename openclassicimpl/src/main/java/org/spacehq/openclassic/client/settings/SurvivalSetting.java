package org.spacehq.openclassic.client.settings;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.settings.IntSetting;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.client.gamemode.CreativeGameMode;
import org.spacehq.openclassic.client.gamemode.SurvivalGameMode;
import org.spacehq.openclassic.client.level.ClientLevel;

public class SurvivalSetting extends IntSetting {
	
	public SurvivalSetting(String name, String[] stringVals) {
		super(name, stringVals);
	}
	
	@Override
	public boolean isVisible() {
		return !OpenClassic.getClient().isInMultiplayer();
	}
	
	@Override
	public void toggle() {
		super.toggle();
		((ClassicClient) OpenClassic.getClient()).setMode(this.getValue() > 0 ? new SurvivalGameMode() : new CreativeGameMode());
		if(OpenClassic.getClient().getLevel() != null) {
			((ClassicClient) OpenClassic.getClient()).getMode().apply((ClientLevel) OpenClassic.getClient().getLevel());
		}

		((ClassicClient) OpenClassic.getClient()).getMode().apply(OpenClassic.getClient().getPlayer());
	}

}
