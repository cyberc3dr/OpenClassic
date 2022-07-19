package org.spacehq.openclassic.client.input;

import org.spacehq.openclassic.api.input.InputHelper;

public class ClientInputHelper extends InputHelper {

	@Override
	public boolean isKeyDown(int key) {
		return Input.isKeyDown(key);
	}

	@Override
	public void enableRepeatEvents(boolean enabled) {
		Input.enableRepeatEvents(enabled);
	}

	@Override
	public String getKeyName(int key) {
		return Input.getKeyName(key);
	}

}
