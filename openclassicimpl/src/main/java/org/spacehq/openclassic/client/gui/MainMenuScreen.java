package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Image;
import org.spacehq.openclassic.client.render.Textures;

public class MainMenuScreen extends GuiComponent {

	public MainMenuScreen() {
		super("mainmenuscreen");
	}

	@Override
	public void onAttached(GuiComponent parent) {
		this.setSize(parent.getWidth(), parent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		this.attachComponent(new Button("singleplayer", this.getWidth() / 2 - 200, this.getHeight() / 2 - 88, OpenClassic.getGame().getTranslator().translate("gui.main-menu.singleplayer")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new LevelLoadScreen(MainMenuScreen.this));
			}
		}));
		
		this.attachComponent(new Button("multiplayer", this.getWidth() / 2 - 200, this.getHeight() / 2 - 40, OpenClassic.getGame().getTranslator().translate("gui.main-menu.multiplayer")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new ServerListScreen(MainMenuScreen.this));
			}
		}));
		
		this.attachComponent(new Button("options", this.getWidth() / 2 - 200, this.getHeight() / 2 + 8, OpenClassic.getGame().getTranslator().translate("gui.main-menu.options")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new OptionsScreen(MainMenuScreen.this, OpenClassic.getClient().getSettings()));
			}
		}));
		
		this.attachComponent(new Button("resourcepacks", this.getWidth() / 2 - 200, this.getHeight() / 2 + 56, OpenClassic.getGame().getTranslator().translate("gui.main-menu.resource-packs")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new ResourcePackScreen(MainMenuScreen.this));
			}
		}));
		
		this.attachComponent(new Button("languages", this.getWidth() / 2 - 200, this.getHeight() / 2 + 104, OpenClassic.getGame().getTranslator().translate("gui.main-menu.language")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new LanguageScreen(MainMenuScreen.this));
			}
		}));
		
		this.attachComponent(new Button("about", this.getWidth() / 2 - 204, this.getHeight() / 2 + 168, 200, 40, OpenClassic.getGame().getTranslator().translate("gui.main-menu.about")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new AboutScreen(MainMenuScreen.this));
			}
		}));
		
		this.attachComponent(new Button("quit", this.getWidth() / 2 + 4, this.getHeight() / 2 + 168, 200, 40, OpenClassic.getGame().getTranslator().translate("gui.main-menu.quit")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().shutdown();
			}
		}));
		
		this.attachComponent(new Image("logo", this.getWidth() / 2 - Textures.LOGO.getWidth() / 2, this.getHeight() / 12 - 40, Textures.LOGO.getSubTexture(0, 0, Textures.LOGO.getWidth(), Textures.LOGO.getHeight())));
		OpenClassic.getGame().getAudioManager().playMusic("menu", true);
		if(OpenClassic.getClient().getPlayer().getName() == null || OpenClassic.getClient().getSettings().getIntSetting("options.survival").getValue() > 0) {
			this.getComponent("multiplayer", Button.class).setActive(false);
		}
	}
	
}
