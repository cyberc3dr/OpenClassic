package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.TranslucentBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.TextBox;

public class LevelDumpScreen extends GuiComponent {

	private GuiComponent parent;

	public LevelDumpScreen(GuiComponent parent) {
		super("leveldumpscreen");
		this.parent = parent;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		this.attachComponent(new TranslucentBackground("bg"));
		this.attachComponent(new TextBox("name", this.getWidth() / 2 - 200, this.getHeight() / 2 - 60, 30));
		this.attachComponent(new Button("save", this.getWidth() / 2 - 200, (int) (this.getHeight() * 0.75f), OpenClassic.getGame().getTranslator().translate("gui.level-dump.dump")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().saveLevel(getComponent("name", TextBox.class).getText());
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new Button("back", this.getWidth() / 2 - 200, (int) (this.getHeight() * 0.75f) + 48, OpenClassic.getGame().getTranslator().translate("gui.cancel")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 60, OpenClassic.getGame().getTranslator().translate("gui.level-dump.name"), true));
		this.getComponent("save", Button.class).setActive(false);
	}

	@Override
	public void onKeyPress(char c, int key) {
		super.onKeyPress(c, key);
		this.getComponent("save", Button.class).setActive(this.getComponent("name", TextBox.class).getText().trim().length() > 0);
	}
	
}
