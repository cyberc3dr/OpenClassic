package org.spacehq.openclassic.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.ButtonList;
import org.spacehq.openclassic.api.gui.base.ButtonListCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.TranslucentBackground;
import org.spacehq.openclassic.api.input.InputHelper;
import org.spacehq.openclassic.api.settings.bindings.Bindings;
import org.spacehq.openclassic.api.settings.bindings.KeyBinding;

public class ControlsScreen extends GuiComponent {

	private GuiComponent parent;
	private Bindings bindings;
	private KeyBinding binding = null;

	public ControlsScreen(GuiComponent parent, Bindings bindings) {
		super("controlsscreen");
		this.parent = parent;
		this.bindings = bindings;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		if(OpenClassic.getClient().isInGame()) {
			this.attachComponent(new TranslucentBackground("bg"));
		} else {
			this.attachComponent(new DefaultBackground("bg"));
		}
		
		ButtonList list = new ButtonList("controls", 0, 0, this.getWidth(), (int) (this.getHeight() * 0.8f));
		list.setCallback(new ButtonListCallback() {
			@Override
			public void onButtonListClick(ButtonList list, Button button) {
				int page = list.getCurrentPage();
				getComponent("controls", ButtonList.class).setContents(buildContents());
				list.setCurrentPage(page);
				binding = bindings.getBinding((list.getCurrentPage() * 5) + Integer.parseInt(button.getName().replace("button", "")));
				button.setText("> " + OpenClassic.getGame().getTranslator().translate(binding.getName()) + ": " + InputHelper.getHelper().getKeyName(binding.getKey()) + " <");
			}
		});
		
		this.attachComponent(list);
		this.attachComponent(new Button("done", this.getWidth() / 2 - 200, this.getHeight() - 56, OpenClassic.getGame().getTranslator().translate("gui.done")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 80, OpenClassic.getGame().getTranslator().translate("gui.controls"), true));
		this.getComponent("controls", ButtonList.class).setContents(this.buildContents());
	}
	
	private List<String> buildContents() {
		List<String> contents = new ArrayList<String>();
		for(int index = 0; index < this.bindings.getBindings().size(); index++) {
			KeyBinding binding = this.bindings.getBinding(index);
			contents.add(OpenClassic.getGame().getTranslator().translate(binding.getName()) + ": " + InputHelper.getHelper().getKeyName(binding.getKey()));
		}
		
		return contents;
	}
	
	@Override
	public void onKeyPress(char c, int key) {
		if(this.binding != null) {
			this.binding.setKey(key);
			int page = this.getComponent("controls", ButtonList.class).getCurrentPage();
			this.getComponent("controls", ButtonList.class).setContents(this.buildContents());
			this.getComponent("controls", ButtonList.class).setCurrentPage(page);
			this.binding = null;
		} else {
			super.onKeyPress(c, key);
		}
	}
	
}
