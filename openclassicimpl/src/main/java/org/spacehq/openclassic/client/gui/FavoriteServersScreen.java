package org.spacehq.openclassic.client.gui;

import java.util.ArrayList;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.ButtonList;
import org.spacehq.openclassic.api.gui.base.ButtonListCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.client.util.ServerDataStore;

public class FavoriteServersScreen extends GuiComponent {

	private GuiComponent parent;
	private boolean delete = false;

	public FavoriteServersScreen(GuiComponent parent) {
		super("favoriteserversscreen");
		this.parent = parent;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		ButtonList list = new ButtonList("favorites", 0, 0, this.getWidth(), (int) (this.getHeight() * 0.8f));
		list.setCallback(new ButtonListCallback() {
			@Override
			public void onButtonListClick(ButtonList list, Button button) {
				if(delete) {
					OpenClassic.getClient().setActiveComponent(new ConfirmDeleteServerScreen(FavoriteServersScreen.this, button.getText()));
					getComponent("title", Label.class).setText(OpenClassic.getGame().getTranslator().translate("gui.favorites.select"));
					delete = false;
				} else {
					OpenClassic.getClient().joinServer(ServerDataStore.getFavorites().get(button.getText()));
				}
			}
		});
		
		this.attachComponent(list);
		this.attachComponent(new Button("add", this.getWidth() / 2 - 312, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.add-favorite.add")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new AddFavoriteScreen(FavoriteServersScreen.this));
			}
		}));
		
		this.attachComponent(new Button("delete", this.getWidth() / 2 - 104, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.add-favorite.remove")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				Label label = getComponent("title", Label.class);
				if(delete) {
					label.setText(OpenClassic.getGame().getTranslator().translate("gui.favorites.select"));
					delete = false;
				} else {
					label.setText(Color.RED + OpenClassic.getGame().getTranslator().translate("gui.favorites.delete"));
					delete = true;
				}
			}
		}));
		
		this.attachComponent(new Button("back", this.getWidth() / 2 + 104, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.back")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 80, OpenClassic.getGame().getTranslator().translate("gui.favorites.select"), true));
		this.getComponent("favorites", ButtonList.class).setContents(new ArrayList<String>(ServerDataStore.getFavorites().keySet()));
	}
	
}
