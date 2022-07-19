package org.spacehq.openclassic.client.gui;

import java.io.File;
import java.util.Arrays;

import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.ButtonList;
import org.spacehq.openclassic.api.gui.base.ButtonListCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;

public class LevelLoadScreen extends GuiComponent {

	private GuiComponent parent;
	private String[] levels = null;

	private boolean delete = false;

	public LevelLoadScreen(GuiComponent parent) {
		super("levelloadscreen");
		this.parent = parent;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		ButtonList list = new ButtonList("levels", 0, 0, this.getWidth(), (int) (this.getHeight() * 0.8f), true);
		list.setCallback(new ButtonListCallback() {
			@Override
			public void onButtonListClick(ButtonList list, Button button) {
				if(delete) {
					File file = null;
					for(File f : (new File(OpenClassic.getGame().getDirectory(), "levels")).listFiles()) {
						if(f != null && (f.getName().equals(button.getText() + ".mine") || f.getName().equals(button.getText() + ".map") || f.getName().equals(button.getText() + ".oclvl") || f.getName().equals(button.getText() + ".lvl") || f.getName().equals(button.getText() + ".dat") || f.getName().equals(button.getText() + ".mclevel"))) {
							file = f;
							break;
						}
					}

					if(file == null) return;

					OpenClassic.getClient().setActiveComponent(new ConfirmDeleteScreen(LevelLoadScreen.this, button.getText(), file));
					delete = false;
					getComponent("title", Label.class).setText(OpenClassic.getGame().getTranslator().translate("gui.load-level.title"));
				} else {
					OpenClassic.getClient().openLevel(button.getText());
				}
			}
		});
		
		this.attachComponent(list);
		this.attachComponent(new Button("create", this.getWidth() / 2 - 312, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.load-level.new")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(new LevelCreateScreen(LevelLoadScreen.this));
			}
		}));
		
		this.attachComponent(new Button("delete", this.getWidth() / 2 - 104, (int) (this.getHeight() * 0.8f), 200, 40, OpenClassic.getGame().getTranslator().translate("gui.load-level.delete")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				if(delete) {
					getComponent("title", Label.class).setText(OpenClassic.getGame().getTranslator().translate("gui.load-level.title"));
					delete = false;
				} else {
					getComponent("title", Label.class).setText(Color.RED + OpenClassic.getGame().getTranslator().translate("gui.load-level.title-delete"));
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
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 80, OpenClassic.getGame().getTranslator().translate("gui.load-level.title"), true));
		
		StringBuilder levels = new StringBuilder();
		for(String file : (new File(OpenClassic.getGame().getDirectory(), "levels").list())) {
			if(!file.endsWith(".map") && !file.endsWith(".mine") && !file.endsWith(".mclevel") && !file.endsWith(".oclvl") && !file.endsWith(".dat") && !file.endsWith(".lvl")) continue;
			if(levels.length() != 0) levels.append(";");
			levels.append(file.substring(0, file.indexOf(".")));
		}

		this.levels = levels.toString().split(";");
		this.getComponent("levels", ButtonList.class).setContents(Arrays.asList(this.levels));
	}
	
}
