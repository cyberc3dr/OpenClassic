package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.StateButton;
import org.spacehq.openclassic.api.gui.base.TextBox;
import org.spacehq.openclassic.api.level.LevelInfo;

public class LevelCreateScreen extends GuiComponent {

	private GuiComponent parent;
	private int type = 0;

	public LevelCreateScreen(GuiComponent parent) {
		super("levelcreatescreen");
		this.parent = parent;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		this.attachComponent(new StateButton("type", this.getWidth() / 2 - 200, this.getHeight() / 2 - 24, OpenClassic.getGame().getTranslator().translate("gui.level-create.type")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				type++;
				if(type >= OpenClassic.getGame().getGenerators().size()) {
					type = 0;
				}

				String generators[] = OpenClassic.getGame().getGenerators().keySet().toArray(new String[OpenClassic.getGame().getGenerators().keySet().size()]);
				((StateButton) button).setState(generators[type]);
			}
		}));
		
		this.getComponent("type", StateButton.class).setState("normal");
		this.attachComponent(new Button("small", this.getWidth() / 2 - 200, this.getHeight() / 2 + 24, OpenClassic.getGame().getTranslator().translate("gui.level-create.small")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				generate(getComponent("name", TextBox.class).getText(), (short) 0);
			}
		}));
		
		this.attachComponent(new Button("medium", this.getWidth() / 2 - 200, this.getHeight() / 2 + 72, OpenClassic.getGame().getTranslator().translate("gui.level-create.normal")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				generate(getComponent("name", TextBox.class).getText(), (short) 1);
			}
		}));
		
		this.attachComponent(new Button("large", this.getWidth() / 2 - 200, this.getHeight() / 2 + 120, OpenClassic.getGame().getTranslator().translate("gui.level-create.huge")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				generate(getComponent("name", TextBox.class).getText(), (short) 2);
			}
		}));
		
		this.attachComponent(new Button("back", this.getWidth() / 2 - 200, this.getHeight() / 2 + 168, OpenClassic.getGame().getTranslator().translate("gui.cancel")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new TextBox("name", this.getWidth() / 2 - 200, this.getHeight() / 2 - 90, 30));
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 60, OpenClassic.getGame().getTranslator().translate("gui.level-create.name"), true));
		
		this.getComponent("small", Button.class).setActive(false);
		this.getComponent("medium", Button.class).setActive(false);
		this.getComponent("large", Button.class).setActive(false);
	}

	@Override
	public void onKeyPress(char c, int key) {
		super.onKeyPress(c, key);
		TextBox text = this.getComponent("name", TextBox.class);
		this.getComponent("small", Button.class).setActive(text.getText().trim().length() > 0);
		this.getComponent("medium", Button.class).setActive(text.getText().trim().length() > 0);
		this.getComponent("large", Button.class).setActive(text.getText().trim().length() > 0);
	}
	
	private void generate(String name, short sizeId) {
		short size = (short) (128 << sizeId);
		OpenClassic.getClient().getProgressBar().setVisible(true);
		OpenClassic.getClient().getProgressBar().setTitle(OpenClassic.getGame().getTranslator().translate("progress-bar.singleplayer"));
		OpenClassic.getClient().getProgressBar().setSubtitle(OpenClassic.getGame().getTranslator().translate("level.generating"));
		OpenClassic.getClient().getProgressBar().setText("");
		OpenClassic.getClient().getProgressBar().setProgress(-1);
		OpenClassic.getClient().getProgressBar().render();
		OpenClassic.getClient().createLevel(new LevelInfo(name, null, size, (short) 128, size), OpenClassic.getGame().getGenerator(this.getComponent("type", StateButton.class).getState()));
		OpenClassic.getClient().saveLevel();
		OpenClassic.getClient().getProgressBar().setVisible(false);
		OpenClassic.getClient().setActiveComponent(null);
	}
	
}
