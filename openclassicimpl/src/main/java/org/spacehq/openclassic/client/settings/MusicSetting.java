package org.spacehq.openclassic.client.settings;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.settings.BooleanSetting;

public class MusicSetting extends BooleanSetting {

	public MusicSetting(String name) {
		super(name);
	}
	
	@Override
	public void toggle() {
		super.toggle();
		if(!this.getValue()) {
			OpenClassic.getGame().getAudioManager().stopMusic();
		}
	}

}
