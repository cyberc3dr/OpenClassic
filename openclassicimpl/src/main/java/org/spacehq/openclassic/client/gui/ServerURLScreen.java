package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.TextBox;

public class ServerURLScreen extends GuiComponent {

	private GuiComponent parent;

	public ServerURLScreen(GuiComponent parent) {
		super("serverurlscreen");
		this.parent = parent;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		this.attachComponent(new Button("connect", this.getWidth() / 2 - 200, (int) (this.getHeight() * 0.75f), OpenClassic.getGame().getTranslator().translate("gui.servers.connect")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().joinServer(getComponent("url", TextBox.class).getText());
			}
		}));
		
		this.attachComponent(new Button("back", this.getWidth() / 2 - 200, (int) (this.getHeight() * 0.75f) + 48, OpenClassic.getGame().getTranslator().translate("gui.cancel")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new TextBox("url", this.getWidth() / 2 - 200, this.getHeight() / 2 - 20));
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 2 - 160, OpenClassic.getGame().getTranslator().translate("gui.add-favorite.enter-url"), true));
		this.getComponent("connect", Button.class).setActive(false);
	}

	@Override
	public void onKeyPress(char c, int key) {
		super.onKeyPress(c, key);
		this.getComponent("connect", Button.class).setActive(this.getComponent("url", TextBox.class).getText().length() > 0);
	}
	
}
