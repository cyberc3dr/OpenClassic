package org.spacehq.openclassic.client.gui;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.Button;
import org.spacehq.openclassic.api.gui.base.ButtonCallback;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.gui.base.TranslucentBackground;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.util.Constants;

public class GameOverScreen extends GuiComponent {

	public GameOverScreen() {
		super("gameoverscreen");
	}
	
	@Override
	public void onAttached(GuiComponent parent) {
		this.setSize(parent.getWidth(), parent.getHeight());
		this.attachComponent(new TranslucentBackground("bg"));
		this.attachComponent(new Button("respawn", this.getWidth() / 2 - 200, this.getHeight() / 2 + 24, OpenClassic.getGame().getTranslator().translate("gui.game-over.respawn")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				Player player = OpenClassic.getClient().getPlayer();
				for(int slot = 0; slot < 9; slot++) {
					player.getInventoryContents()[slot] = -1;
					player.getInventoryAmounts()[slot] = 0;
				}

				player.setAir(20);
				player.setArrows(20);
				player.setHealth(Constants.MAX_HEALTH);
				player.respawn();

				OpenClassic.getClient().setActiveComponent(null);
			}
		}));
		
		this.attachComponent(new Button("mainmenu", this.getWidth() / 2 - 200, this.getHeight() / 2 + 72, OpenClassic.getGame().getTranslator().translate("gui.game-over.main-menu")).setCallback(new ButtonCallback() {
			@Override
			public void onButtonClick(Button button) {
				OpenClassic.getClient().exitGameSession();
			}
		}));
		
		this.attachComponent(new Label("title", this.getWidth() / 2, this.getHeight() / 4, OpenClassic.getGame().getTranslator().translate("gui.game-over.game-over"), true, true));
		
		Player player = OpenClassic.getClient().getPlayer();
		this.attachComponent(new Label("score", this.getWidth() / 2, this.getHeight() / 2 - 40, String.format(OpenClassic.getGame().getTranslator().translate("gui.game-over.score"), player.getScore()), true));
	}
	
}
