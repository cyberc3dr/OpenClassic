package org.spacehq.openclassic.client.gui;

import java.io.File;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.DefaultBackground;
import org.spacehq.openclassic.api.gui.base.Label;

public class ConfirmDeleteScreen extends GuiComponent {

	private GuiComponent parent;
	private String name;
	private File file;

	public ConfirmDeleteScreen(GuiComponent parent, String name, File file) {
		super("confirmdeletescreen");
		this.parent = parent;
		this.name = name;
		this.file = file;
	}
	
	@Override
	public void onAttached(GuiComponent oparent) {
		this.setSize(oparent.getWidth(), oparent.getHeight());
		this.attachComponent(new DefaultBackground("bg"));
		this.attachComponent(new Button("yes", this.getWidth() / 2 - 204, this.getHeight() / 2 + 104, 200, 40, OpenClassic.getGame().getTranslator().translate("gui.yes")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				try {
					file.delete();
				} catch(SecurityException e) {
					e.printStackTrace();
				}

				File file = new File(new File(OpenClassic.getClient().getDirectory(), "levels"), name + ".nbt");
				if(file.exists()) {
					try {
						file.delete();
					} catch(SecurityException e) {
						e.printStackTrace();
					}
				}
				
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new Button("no", this.getWidth() / 2 + 4, this.getHeight() / 2 + 104, 200, 40, OpenClassic.getGame().getTranslator().translate("gui.no")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().setActiveComponent(parent);
			}
		}));
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4 - 60, String.format(OpenClassic.getGame().getTranslator().translate("gui.delete.level"), this.file.getName().substring(0, this.file.getName().indexOf("."))), true));
	}
	
}
