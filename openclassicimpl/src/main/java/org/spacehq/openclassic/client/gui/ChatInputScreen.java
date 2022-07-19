package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.TextBox;
import org.spacehq.openclassic.api.input.Keyboard;

public class ChatInputScreen extends GuiComponent {
	
	public ChatInputScreen() {
		super("chatinputscreen");
	}

	@Override
	public void onAttached(GuiComponent parent) {
		this.setSize(parent.getWidth(), parent.getHeight());
		this.attachComponent(new TextBox("chat", 4, this.getHeight() - 28, this.getWidth() - 8, 24, true));
		this.getComponent("chat", TextBox.class).setFocused(true);
	}

	@Override
	public void onKeyPress(char c, int key) {
		if(key == Keyboard.KEY_RETURN) {
			String message = this.getComponent("chat", TextBox.class).getText().trim();
			if(message.length() > 0) {
				if(OpenClassic.getClient().isInMultiplayer()) {
					OpenClassic.getClient().getPlayer().chat(message);
				} else if(message.startsWith("/")) {
					OpenClassic.getClient().processCommand(OpenClassic.getClient().getPlayer(), message.substring(1));
				}
			}

			OpenClassic.getClient().setActiveComponent(null);
		}

		super.onKeyPress(c, key);
	}

	@Override
	public void onMouseClick(int x, int y, int button) {
		TextBox text = this.getComponent("chat", TextBox.class);
		String clickedPlayer = OpenClassic.getClient().getHUD().getHoveredPlayer();
		if(button == 0 && clickedPlayer != null) {
			if(text.getText().length() > 0 && !text.getText().endsWith(" ")) {
				text.setText(text.getText() + " ");
			}

			text.setText(text.getText() + clickedPlayer);
			int length = clickedPlayer.length();
			if(text.getText().length() > 62 - length) {
				text.setText(text.getText().substring(0, 62 - length));
			}
		}

		super.onMouseClick(x, y, button);
	}
	
}
