package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.ProgressBar;
import org.spacehq.openclassic.api.event.level.LevelUnloadEvent;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.TranslucentBackground;

import com.zachsthings.onevent.EventManager;

public class IngameMenuScreen extends GuiComponent {

	public IngameMenuScreen() {
		super("ingamemenuscreen");
	}
	
	@Override
	public void onAttached(GuiComponent parent) {
		this.setSize(parent.getWidth(), parent.getHeight());
		this.attachComponent(new TranslucentBackground("bg"));
		this.attachComponent(new Button("options", this.getWidth() / 2 - 200, this.getHeight() / 2 - 96, OpenClassic.getGame().getTranslator().translate("gui.menu.options")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new OptionsScreen(IngameMenuScreen.this, OpenClassic.getClient().getSettings()));
			}
		}));
		
		this.attachComponent(new Button("resourcepacks", this.getWidth() / 2 - 200, this.getHeight() / 2 - 48, OpenClassic.getGame().getTranslator().translate("gui.main-menu.resource-packs")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new ResourcePackScreen(IngameMenuScreen.this));
			}
		}));
		
		this.attachComponent(new Button("dumplevel", this.getWidth() / 2 - 200, this.getHeight() / 2, OpenClassic.getGame().getTranslator().translate("gui.menu.dump")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new LevelDumpScreen(IngameMenuScreen.this));
			}
		}));
		
		this.attachComponent(new Button("mainmenu", this.getWidth() / 2 - 200, this.getHeight() / 2 + 48, OpenClassic.getGame().getTranslator().translate("gui.menu.main-menu")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				if(!OpenClassic.getClient().isInMultiplayer()) {
					if(EventManager.callEvent(new LevelUnloadEvent(OpenClassic.getClient().getLevel())).isCancelled()) {
						return;
					}

					ProgressBar progress = OpenClassic.getClient().getProgressBar();
					progress.setVisible(true);
					progress.setTitle(OpenClassic.getGame().getTranslator().translate("progress-bar.singleplayer"));
					progress.setSubtitle(OpenClassic.getGame().getTranslator().translate("level.saving"));
					progress.setText("");
					progress.setProgress(-1);
					progress.render();
					if(!OpenClassic.getClient().saveLevel()) {
						OpenClassic.getClient().getProgressBar().setText(String.format(OpenClassic.getGame().getTranslator().translate("level.save-fail")));
						try {
							Thread.sleep(1000L);
						} catch(InterruptedException e) {
						}
					}
					
					progress.setVisible(false);
				}

				OpenClassic.getClient().exitGameSession();
			}
		}));
		
		this.attachComponent(new Button("back", this.getWidth() / 2 - 200, this.getHeight() / 2 + 120, OpenClassic.getGame().getTranslator().translate("gui.menu.back")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(null);
			}
		}));
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 60, OpenClassic.getGame().getTranslator().translate("gui.menu.title"), true));
		if(!OpenClassic.getClient().isInMultiplayer()) {
			this.getComponent("dumplevel", Button.class).setActive(false);
		}
	}
	
}
