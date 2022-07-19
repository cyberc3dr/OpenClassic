package org.spacehq.openclassic.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.ButtonList;
import org.spacehq.openclassic.api.gui.base.ButtonListCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.client.util.Server;
import org.spacehq.openclassic.client.util.ServerDataStore;

public class ServerListScreen extends GuiComponent {

	private GuiComponent parent;
	private boolean select = false;

	public ServerListScreen(GuiComponent parent) {
		super("serverlistscreen");
		this.parent = parent;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		List<String> contents = new ArrayList<String>();
		for(Server server : ServerDataStore.getServers()) {
			contents.add(server.name);
		}
		
		this.attachComponent(new DefaultBackground("bg"));
		ButtonList list = new ButtonList("servers", 0, 0, this.getWidth(), (int) (this.getHeight() * 0.8f), true);
		list.setCallback(new ButtonListCallback() {
			@Override
			public void onButtonListClick(ButtonList list, Button button) {
				String text = button.getText();
				int index = 0;
				for(String t : list.getContents()) {
					if(text.equals(t)) {
						break;
					}
					
					index++;
				}
				
				Server server = ServerDataStore.getServers().get(index);
				if(select) {
					getComponent("title", Label.class).setText(OpenClassic.getGame().getTranslator().translate("gui.favorites.select"));
					select = false;
					ServerDataStore.addFavorite(server.name, server.getUrl());
					ServerDataStore.saveFavorites();
				} else {
					OpenClassic.getClient().joinServer(server.getUrl());
				}
			}
		});
		
		this.attachComponent(list);
		this.attachComponent(new Button("favorites", this.getWidth() / 2 - 412, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.servers.favorites")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new FavoriteServersScreen(ServerListScreen.this));
			}
		}));
		
		this.attachComponent(new Button("addfavorite", this.getWidth() / 2 - 204, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.add-favorite.add")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				Label label = getComponent("title", Label.class);
				if(select) {
					label.setText(OpenClassic.getGame().getTranslator().translate("gui.favorites.select"));
					select = false;
				} else {
					label.setText(Color.GREEN + OpenClassic.getGame().getTranslator().translate("gui.servers.select-fav"));
					select = true;
				}
			}
		}));
		
		this.attachComponent(new Button("url", this.getWidth() / 2 + 4, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.servers.enter-url")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new ServerURLScreen(ServerListScreen.this));
			}
		}));
		
		this.attachComponent(new Button("back", this.getWidth() / 2 + 212, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.back")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 80, OpenClassic.getGame().getTranslator().translate("gui.favorites.select"), true));
		this.getComponent("servers", ButtonList.class).setContents(contents);
	}

}
