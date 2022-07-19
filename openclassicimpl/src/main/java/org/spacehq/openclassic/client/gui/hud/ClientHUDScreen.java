package org.spacehq.openclassic.client.gui.hud;

import com.zachsthings.onevent.EventManager;
import org.lwjgl.opengl.Display;
import org.spacehq.openclassic.api.Color;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.block.Blocks;
import org.spacehq.openclassic.api.event.game.ChatDisplayEvent;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.HUDComponent;
import org.spacehq.openclassic.api.gui.base.BlockPreview;
import org.spacehq.openclassic.api.gui.base.ComponentHelper;
import org.spacehq.openclassic.api.gui.base.Image;
import org.spacehq.openclassic.api.gui.base.Label;
import org.spacehq.openclassic.api.input.InputHelper;
import org.spacehq.openclassic.api.input.Keyboard;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.util.Constants;
import org.spacehq.openclassic.client.gui.ChatInputScreen;
import org.spacehq.openclassic.client.gui.ChatLine;
import org.spacehq.openclassic.client.gui.Minimap;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.client.render.Textures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientHUDScreen extends HUDComponent {

	private Random rand = new Random();
	
	private List<ChatLine> chatHistory = new ArrayList<ChatLine>();
	private String hoveredPlayer = null;
	private String debugInfo = "";

	public ClientHUDScreen() {
		super("clienthudscreen", 0, 0, Display.getWidth(), Display.getHeight());
	}
	
	@Override
	public void onAttached(GuiComponent parent) {
		if(OpenClassic.getClient().getSettings().getBooleanSetting("options.minimap").getValue()) {
			this.addMinimap();
		}
		
		this.attachComponent(new Label("version", 4, 4, Constants.VERSION));
		this.attachComponent(new Label("debuginfo", 4, 24, ""));
		this.attachComponent(new Label("position", 4, 44, ""));
		this.attachComponent(new Label("score", 0, 0, ""));
		this.attachComponent(new Label("arrows", this.getWidth() / 2 + 64, this.getHeight() - 66, ""));
		this.attachComponent(new Image("crosshair", this.getWidth() / 2 - 16, this.getHeight() / 2 - 16, Textures.CROSSHAIR));
		this.attachComponent(new Image("quickbar", this.getWidth() / 2 - 182, this.getHeight() - 44, Textures.QUICK_BAR));
		this.updateSlots();
		Player player = OpenClassic.getClient().getPlayer();
		this.attachComponent(new Image("selection", this.getWidth() / 2 - 184 + player.getSelectedSlot() * 40, this.getHeight() - 46, Textures.SELECTION));
		this.attachComponent(new PlayerList("tabmenu", this.getWidth() / 2 - 256, this.getHeight() / 2 - 148, 512, 296, this));
	}
	
	@Override
	public void update(int mouseX, int mouseY) {
		for(int index = 0; index < this.chatHistory.size(); index++) {
			this.chatHistory.get(index).incrementTime();
		}
		
		this.updateSlots();
		this.updateSurvivalData();
		this.updateDebugInfo();
		this.updateChat();
		this.updateTabMenu();
		super.update(mouseX, mouseY);
	}
	
	private void updateSlots() {
		Player player = OpenClassic.getClient().getPlayer();
		Image selection = this.getComponent("selection", Image.class);
		if(selection != null) {
			selection.setPos(this.getWidth() / 2 - 184 + player.getSelectedSlot() * 40, this.getHeight() - 46);
		}
		
		for(int count = 0; count < player.getInventoryContents().length; count++) {
			BlockPreview block = this.getComponent("block" + count, BlockPreview.class);
			Label amount = this.getComponent("amount" + count, Label.class);
			boolean filled = false;
			if(player.getInventoryContents()[count] > 0) {
				BlockType b = Blocks.fromId(player.getInventoryContents()[count]);
				if(b != null) {
					int bx = this.getWidth() / 2 - 170 + count * 40;
					int by = this.getHeight() - 22;
					if(block == null) {
						block = new BlockPreview("block" + count, bx, by, b);
						this.attachComponent(block);
					} else {
						block.setBlock(b);
					}
					
					if(((ClientPlayer) player).getHandle().inventory.pop[count]) {
						((ClientPlayer) player).getHandle().inventory.pop[count] = false;
						block.pop();
					}
					
					if(count == player.getSelectedSlot() && OpenClassic.getClient().getSettings().getIntSetting("options.graphics").getValue() == 1) {
						block.setRotating(true);
					} else {
						block.setRotating(false);
					}
					
					if(player.getInventoryAmounts()[count] > 1) {
						int ax = bx + 26 - ComponentHelper.getHelper().getStringWidth(String.valueOf(player.getInventoryAmounts()[count]), false);
						int ay = by;
						if(amount == null) {
							amount = new Label("amount" + count, ax, ay, String.valueOf(player.getInventoryAmounts()[count]));
							this.attachComponent(amount);
						} else {
							amount.setVisible(true);
							if(!amount.getText().equals(String.valueOf(player.getInventoryAmounts()[count]))) {
								amount.setPos(ax, ay);
								amount.setText(String.valueOf(player.getInventoryAmounts()[count]));
							}
						}
					} else if(amount != null) {
						amount.setText("");
						amount.setVisible(false);
					}
					
					filled = true;
				}
			}
			
			if(!filled) {
				if(block != null) {
					block.setBlock(null);
				}
				
				if(amount != null) {
					amount.setText("");
					amount.setVisible(false);
				}
			}
		}
	}
	
	private void updateDebugInfo() {
		Position pos = OpenClassic.getClient().getPlayer().getPosition();
		boolean visible = OpenClassic.getClient().getSettings().getBooleanSetting("options.show-info").getValue();
		Label debug = this.getComponent("debuginfo", Label.class);
		Label posLabel = this.getComponent("position", Label.class);
		debug.setText(this.debugInfo);
		posLabel.setText("Position: " + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ());
		debug.setVisible(visible);
		posLabel.setVisible(visible);
	}
	
	private void updateTabMenu() {
		this.getComponent("tabmenu").setVisible(InputHelper.getHelper().isKeyDown(Keyboard.KEY_TAB) && OpenClassic.getClient().isInMultiplayer());
	}
	
	private void updateChat() {
		List<ChatLine> visible = this.getVisible();
		for(int count = 0; count < 20; count++) {
			Label line = this.getComponent("line" + count, Label.class);
			if(visible.size() > count) {
				ChatLine chat = visible.get(count);
				if(line == null) {
					line = new Label("line" + count, 4, this.getHeight() - 56 - count * 18, chat.getMessage());
					this.attachComponent(line);
				} else {
					line.setVisible(true);
					line.setText(chat.getMessage());
				}
			} else if(line != null) {
				line.setVisible(false);
			}
		}
	}
	
	private void updateSurvivalData() {
		Player player = OpenClassic.getClient().getPlayer();
		
		Label score = this.getComponent("score", Label.class);
		Label arrows = this.getComponent("arrows", Label.class);
		score.setVisible(OpenClassic.getClient().isInSurvival());
		arrows.setVisible(OpenClassic.getClient().isInSurvival());
		String scoreString = "Score: " + Color.YELLOW + player.getScore();
		score.setText(scoreString);
		score.setPos(this.getWidth() - ComponentHelper.getHelper().getStringWidth(scoreString, false) - 4, 4);
		arrows.setText("Arrows: " + Color.YELLOW + player.getArrows());

		boolean flash = player.getInvulnerableTime() / 3 % 2 != 0 && player.getInvulnerableTime() >= 10;
		for(int count = 0; count < 10; count++) {
			Image heart = this.getComponent("heart" + count, Image.class);
			Image heartBg = this.getComponent("heartbg" + count, Image.class);
			if(OpenClassic.getClient().isInSurvival()) {
				int heartX = this.getWidth() / 2 - 182 + (count << 3) * 2;
				int heartY = this.getHeight() - 66;
				if(player.getHealth() <= 4) {
					heartY += this.rand.nextInt(2) * 2;
				}
				
				if(heartBg == null) {
					heartBg = new Image("heartbg" + count, heartX, heartY, Textures.EMPTY_HEART);
					this.attachComponent(heartBg);
				} else {
					heartBg.setPos(heartX, heartY);
				}
				
				if(heart == null) {
					heart = new Image("heart" + count, heartX, heartY, Textures.FULL_HEART);
					this.attachComponent(heart);
				} else {
					heart.setPos(heartX, heartY);
				}
				
				heart.setVisible(true);
				heartBg.setVisible(true);
				heartBg.setTexture(flash ? Textures.EMPTY_HEART_FLASH : Textures.EMPTY_HEART, true);
				boolean set = false;
				if(flash) {
					if(count * 2 + 1 < player.getPreviousHealth()) {
						heart.setTexture(Textures.FULL_HEART_FLASH, true);
						set = true;
					}
					
					if(count * 2 + 1 == player.getPreviousHealth()) {
						heart.setTexture(Textures.HALF_HEART_FLASH, true);
						set = true;
					}
				}

				if(count * 2 + 1 < player.getHealth()) {
					heart.setTexture(Textures.FULL_HEART, true);
					set = true;
				}
				
				if(count * 2 + 1 == player.getHealth()) {
					heart.setTexture(Textures.HALF_HEART, true);
					set = true;
				}
				
				if(!set) {
					heart.setVisible(false);
				}
			} else if(heart != null) {
				heart.setVisible(false);
				heartBg.setVisible(false);
			}
		}
		
		for(int count = 0; count < 10; count++) {
			Image bubble = this.getComponent("bubble" + count, Image.class);
			boolean show = false;
			if(player.isUnderwater() && OpenClassic.getClient().isInSurvival()) {
				int full = (int) Math.ceil((player.getAir() - 2) * 10.0D / 300.0D);
				int pop = (int) Math.ceil(player.getAir() * 10.0D / 300.0D) - full;
				if(count < full + pop) {
					if(bubble == null) {
						bubble = new Image("bubble" + count, this.getWidth() / 2 - 182 + (count << 3) * 2, this.getHeight() - 84, Textures.BUBBLE);
						this.attachComponent(bubble);
					}
					
					show = true;
					bubble.setVisible(true);
					if(count < full) {
						if(bubble.getTexture() != Textures.BUBBLE) {
							bubble.setTexture(Textures.BUBBLE, true);
						}
					} else {
						if(bubble.getTexture() != Textures.POPPING_BUBBLE) {
							bubble.setTexture(Textures.POPPING_BUBBLE, true);
						}
					}
				}
			}
			
			if(!show && bubble != null) {
				bubble.setVisible(false);
			}
		}
	}
	
	public void addMinimap() {
		this.attachComponent(new Minimap("minimap", this.getWidth() - 170, 20, 150, 150));
	}
	
	public void removeMinimap() {
		this.removeComponent("minimap");
	}
	
	private List<ChatLine> getVisible() {
		int max = 10;
		boolean all = false;
		if(OpenClassic.getClient().getActiveComponent() instanceof ChatInputScreen) {
			max = 20;
			all = true;
		}
		
		List<ChatLine> result = new ArrayList<ChatLine>();
		for(ChatLine chat : this.chatHistory) {
			if(max <= 0) break;
			if(chat.getTime() < 200 || all) {
				result.add(chat);
				max--;
			} else {
				break;
			}
		}

		return result;
	}

	@Override
	public void addChat(String message) {
		ChatDisplayEvent event = EventManager.callEvent(new ChatDisplayEvent(message));
		if(event.isCancelled()) {
			return;
		}
		
		this.chatHistory.add(0, new ChatLine(event.getMessage()));
		while(this.chatHistory.size() > 50) {
			this.chatHistory.remove(this.chatHistory.size() - 1);
		}
	}

	@Override
	public String getHoveredPlayer() {
		return this.hoveredPlayer;
	}

	@Override
	public List<String> getChat() {
		List<String> result = new ArrayList<String>();
		for(ChatLine line : this.chatHistory) {
			result.add(line.getMessage());
		}

		return result;
	}

	@Override
	public String getChatMessage(int index) {
		if(this.chatHistory.size() <= index) return null;
		return this.chatHistory.get(index).getMessage();
	}

	@Override
	public String getLastChat() {
		if(this.chatHistory.size() <= 0) return null;
		return this.chatHistory.get(0).getMessage();
	}

	protected void setHoveredPlayer(String hoveredPlayer) {
		this.hoveredPlayer = hoveredPlayer;
	}
	
	public void setDebugInfo(String info) {
		this.debugInfo = info;
	}
	
}
