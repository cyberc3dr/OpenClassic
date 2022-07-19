package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.TextBox;
import org.spacehq.openclassic.client.util.ServerDataStore;

public class AddFavoriteScreen extends GuiComponent {

	private GuiComponent parent;
	
	public AddFavoriteScreen(GuiComponent parent) {
		super("addfavoritescreen");
		this.parent = parent;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		this.attachComponent(new Button("add", this.getWidth() / 2 - 200, (int) (this.getHeight() * 0.75f), OpenClassic.getGame().getTranslator().translate("gui.add-favorite.add")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				TextBox name = getComponent("name", TextBox.class);
				TextBox url = getComponent("url", TextBox.class);
				ServerDataStore.addFavorite(name.getText(), url.getText());
				ServerDataStore.saveFavorites();
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new Button("cancel", this.getWidth() / 2 - 200, (int) (this.getHeight() * 0.75f) + 48, OpenClassic.getGame().getTranslator().translate("gui.cancel")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new TextBox("name", this.getWidth() / 2 - 200, this.getHeight() / 2 - 100));
		this.attachComponent(new TextBox("url", this.getWidth() / 2 - 200, this.getHeight() / 2 - 20));
		this.attachComponent(new Label("entername", this.getWidth() / 2, this.getHeight() / 2 - 130, OpenClassic.getGame().getTranslator().translate("gui.add-favorite.enter-name"), true));
		this.attachComponent(new Label("enterurl", this.getWidth() / 2, this.getHeight() / 2 - 50, OpenClassic.getGame().getTranslator().translate("gui.add-favorite.enter-url"), true));
		
		this.getComponent("add", Button.class).setActive(false);
	}

	@Override
	public void onKeyPress(char c, int key) {
		super.onKeyPress(c, key);
		this.getComponent("add", Button.class).setActive(this.getComponent("name", TextBox.class).getText().length() > 0 && this.getComponent("url", TextBox.class).getText().length() > 0);
	}
	
}
