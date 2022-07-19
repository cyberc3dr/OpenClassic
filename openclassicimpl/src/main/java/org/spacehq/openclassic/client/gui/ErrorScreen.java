package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.input.Keyboard;

public class ErrorScreen extends GuiComponent {

	private final String title;
	private final String message;

	public ErrorScreen(String title, String message) {
		super("errorscreen");
		this.title = title;
		this.message = message;
	}

	@Override
	public void onAttached(GuiComponent parent) {
		this.setSize(parent.getWidth(), parent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		this.attachComponent(new Button("mainmenu", this.getWidth() / 2 - 200, this.getHeight() / 2 + 104, OpenClassic.getGame().getTranslator().translate("gui.error.main-menu")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().exitGameSession();
			}
		}));
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 60, this.title, true));
		this.attachComponent(new Label("message", this.getWidth() / 2, this.getHeight() / 2 - 20, this.message, true));
	}

	@Override
	public void onKeyPress(char c, int key) {
		if(key != Keyboard.KEY_ESCAPE) {
			super.onKeyPress(c, key);
		}
	}
	
}
