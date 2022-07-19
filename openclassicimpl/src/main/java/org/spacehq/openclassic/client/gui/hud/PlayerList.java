package org.spacehq.openclassic.client.gui.hud;

import java.util.List;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.base.FadingBox;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.player.Player;

public class PlayerList extends GuiComponent {

	private ClientHUDScreen parent;

	public PlayerList(String name, int x, int y, int width, int height, ClientHUDScreen parent) {
		super(name, x, y, width, height);
		this.parent = parent;
	}

	@Override
	public void onAttached(GuiComponent oparent) {
		this.attachComponent(new FadingBox("bg", this.getX(), this.getY(), this.getWidth(), this.getHeight(), -1878719232, -1070583712));
		this.attachComponent(new Label("title", this.getX() + this.getWidth() / 2, this.getY() + 8, OpenClassic.getGame().getTranslator().translate("gui.hud.player-list-title"), true));
	}
	
	@Override
	public void update(int mouseX, int mouseY) {
		if(OpenClassic.getClient().getLevel() != null) {
			List<Player> players = OpenClassic.getClient().getLevel().getPlayers();
			for(int count = 0; count < players.size(); count++) {
				int x = 6 + ((count % 2) * 250);
				int y = 32 + ((count / 2) * 20);
				Label normal = this.getComponent("normal" + count, Label.class);
				Label hover = this.getComponent("hover" + count, Label.class);
				if(normal == null) {
					normal = new Label("normal" + count, this.getX() + x, this.getY() + y, players.get(count).getName());
					normal.setVisible(false);
					this.attachComponent(normal);
				}
				
				if(hover == null) {
					hover = new Label("hover" + count, this.getX() + x + 4, this.getY() + y, players.get(count).getName());
					hover.setVisible(false);
					this.attachComponent(hover);
				}
				
				if(OpenClassic.getClient().getActiveComponent() != null && mouseX >= x && mouseY >= y && mouseX < x + 250 && mouseY < y + 16) {
					this.parent.setHoveredPlayer(players.get(count).getName());
					normal.setVisible(false);
					hover.setVisible(true);
				} else {
					normal.setVisible(true);
					hover.setVisible(false);
				}
			}
		}
	}
	
}
