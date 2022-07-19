package org.spacehq.openclassic.client.player;

import com.zachsthings.onevent.EventManager;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.block.BlockType;
import org.spacehq.openclassic.api.data.NBTData;
import org.spacehq.openclassic.api.event.player.PlayerChatEvent;
import org.spacehq.openclassic.api.event.player.PlayerTeleportEvent;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.permissions.Group;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.plugin.RemotePluginInfo;
import org.spacehq.openclassic.api.util.Constants;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.client.gui.hud.ClientHUDScreen;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.network.ClientSession;
import org.spacehq.openclassic.game.network.msg.PlayerChatMessage;
import org.spacehq.openclassic.game.network.msg.custom.CustomMessage;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public class ClientPlayer implements Player {

	private String name;
	private ClientSession session;
	private com.mojang.minecraft.entity.player.Player handle;
	private BlockType placeMode = null;
	private NBTData data = new NBTData("Player");
	private boolean breakUnbreakables = false;

	public ClientPlayer() {
		this.data.load(OpenClassic.getClient().getDirectory().getPath() + "/player.nbt");
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void sendMessage(String message) {
		this.sendInternal(OpenClassic.getClient().getTranslator().translate(message, this.getLanguage()));
	}
	
	@Override
	public void sendMessage(String message, Object... args) {
		this.sendInternal(String.format(OpenClassic.getClient().getTranslator().translate(message, this.getLanguage()), args));
	}
	
	private void sendInternal(String message) {
		for(String msg : message.split("\n")) {
			OpenClassic.getClient().getHUD().addChat(msg);
		}
	}

	@Override
	public boolean hasPermission(String permission) {
		return true;
	}

	@Override
	public String getCommandPrefix() {
		return "/";
	}

	public ClientSession getSession() {
		return this.session;
	}
	
	public void setSession(ClientSession session) {
		this.session = session;
	}

	@Override
	public Position getPosition() {
		if(this.handle == null) {
			return new Position(null, 0, 0, 0);
		}
		
		return this.handle.pos;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public void setDisplayName(String name) {
	}

	@Override
	public BlockType getPlaceMode() {
		return this.placeMode;
	}

	@Override
	public void setPlaceMode(BlockType type) {
		this.placeMode = type;
	}

	@Override
	public void moveTo(Position pos) {
		this.moveTo(pos.getLevel(), pos.getX(), pos.getY(), pos.getZ(), pos.getYaw(), pos.getPitch());
	}

	@Override
	public void moveTo(float x, float y, float z) {
		this.moveTo(this.handle.pos.getLevel(), x, y, z, (byte) 0, (byte) 0);
	}

	@Override
	public void moveTo(float x, float y, float z, float yaw, float pitch) {
		this.moveTo(this.handle.pos.getLevel(), x, y, z, yaw, pitch);
	}

	@Override
	public void moveTo(Level level, float x, float y, float z) {
		this.moveTo(level, x, y, z, (byte) 0, (byte) 0);
	}

	@Override
	public void moveTo(Level level, float x, float y, float z, float yaw, float pitch) {
		if(this.handle == null) {
			return;
		}
		
		PlayerTeleportEvent event = EventManager.callEvent(new PlayerTeleportEvent(this, this.getPosition(), new Position(level, x, y, z, yaw, pitch)));
		if(event.isCancelled()) {
			return;
		}

		if(event.getTo().getLevel() != null && this.handle.pos.getLevel() != null && !this.handle.pos.getLevel().getName().equals(event.getTo().getLevel().getName())) {
			this.handle.setLevel((ClientLevel) event.getTo().getLevel());
			((ClassicClient) OpenClassic.getClient()).setLevel((ClientLevel) event.getTo().getLevel());
		}

		this.handle.moveTo(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ(), event.getTo().getYaw(), event.getTo().getPitch());
	}

	@Override
	public Group getGroup() {
		return null;
	}

	@Override
	public void setGroup(Group group) {
	}

	@Override
	public String getIp() {
		return this.getAddress().toString().replace("/", "").split(":")[0];
	}

	@Override
	public SocketAddress getAddress() {
		if(this.getSession() == null) {
			InetSocketAddress.createUnresolved("127.0.0.1", 25565);
		}
		
		return this.getSession().getAddress();
	}

	@Override
	public void disconnect(String reason) {
		if(this.getSession() == null) {
			OpenClassic.getClient().exitGameSession();
			return;
		}
		
		this.getSession().disconnect(reason);
	}

	public com.mojang.minecraft.entity.player.Player getHandle() {
		return this.handle;
	}
	
	public void setHandle(com.mojang.minecraft.entity.player.Player handle) {
		this.handle = handle;
		this.placeMode = null;
		this.breakUnbreakables = false;
	}

	@Override
	public boolean hasCustomClient() {
		return true;
	}

	@Override
	public String getClientVersion() {
		return Constants.VERSION;
	}

	@Override
	public NBTData getData() {
		return this.data;
	}

	@Override
	public List<RemotePluginInfo> getPlugins() {
		return ((ClassicClient) OpenClassic.getClient()).getPluginInfo();
	}

	@Override
	public void chat(String message) {
		if(this.getSession() == null) {
			((ClientHUDScreen) OpenClassic.getClient().getHUD()).addChat(message);
			return;
		}
		
		PlayerChatEvent event = EventManager.callEvent(new PlayerChatEvent(OpenClassic.getClient().getPlayer(), message));
		if(event.isCancelled()) {
			return;
		}
		
		this.getSession().send(new PlayerChatMessage((byte) -1, event.getMessage()));
	}

	@Override
	public void hidePlayer(Player player) {
	}

	@Override
	public void showPlayer(Player player) {
	}

	@Override
	public boolean canSee(Player player) {
		return true;
	}

	@Override
	public String getLanguage() {
		return OpenClassic.getGame().getLanguage();
	}

	@Override
	public int getInvulnerableTime() {
		if(this.handle == null) {
			return 0;
		}
		
		return this.handle.invulnerableTime;
	}

	@Override
	public boolean isUnderwater() {
		if(this.handle == null) {
			return false;
		}
		
		return this.handle.isUnderWater();
	}

	@Override
	public int getHealth() {
		if(this.handle == null) {
			return 0;
		}
		
		return this.handle.health;
	}

	@Override
	public void setHealth(int health) {
		if(this.handle == null) {
			return;
		}
		
		if(health < 0) {
			health = 0;
		}
		
		if(health > Constants.MAX_HEALTH) {
			health = Constants.MAX_HEALTH;
		}
		
		this.handle.health = health;
	}
	
	@Override
	public void heal(int health) {
		if(this.handle == null) {
			return;
		}
		
		if(health < 0) {
			health = 0;
		}
		
		if(health > Constants.MAX_HEALTH) {
			health = Constants.MAX_HEALTH;
		}
		
		this.handle.heal(health);
	}
	
	@Override
	public void damage(int damage) {
		if(this.handle == null) {
			return;
		}
		
		if(damage < 0) {
			damage = 0;
		}
		
		if(damage > Constants.MAX_HEALTH) {
			damage = Constants.MAX_HEALTH;
		}
		
		this.handle.hurt(null, damage);
	}

	@Override
	public boolean isDead() {
		if(this.handle == null) {
			return false;
		}
		
		return this.handle.dead;
	}

	@Override
	public int getPreviousHealth() {
		if(this.handle == null) {
			return 0;
		}
		
		return this.handle.lastHealth;
	}

	@Override
	public int getAir() {
		if(this.handle == null) {
			return 0;
		}
		
		return this.handle.airSupply;
	}

	@Override
	public void setAir(int air) {
		if(this.handle == null) {
			return;
		}
		
		if(air < 0) {
			air = 0;
		}
		
		if(air > Constants.MAX_AIR) {
			air = Constants.MAX_AIR;
		}
		
		this.handle.airSupply = air;
	}

	@Override
	public int getScore() {
		if(this.handle == null) {
			return 0;
		}
		
		return this.handle.getScore();
	}

	@Override
	public void setScore(int score) {
		if(this.handle == null) {
			return;
		}
		
		this.handle.score = score;
	}

	@Override
	public int getArrows() {
		if(this.handle == null) {
			return 0;
		}
		
		return this.handle.arrows;
	}

	@Override
	public void setArrows(int arrows) {
		if(this.handle == null) {
			return;
		}
		
		if(arrows < 0) {
			arrows = 0;
		}
		
		if(arrows > Constants.MAX_ARROWS) {
			arrows = Constants.MAX_ARROWS;
		}
		
		this.handle.arrows = arrows;
	}

	@Override
	public int getSelectedSlot() {
		if(this.handle == null) {
			return 0;
		}
		
		return this.handle.inventory.selected;
	}

	@Override
	public int[] getInventoryContents() {
		if(this.handle == null) {
			return new int[9];
		}
		
		return this.handle.inventory.slots;
	}

	@Override
	public int[] getInventoryAmounts() {
		if(this.handle == null) {
			return new int[9];
		}
		
		return this.handle.inventory.count;
	}
	
	@Override
	public void replaceSelected(BlockType block) {
		if(this.handle == null) {
			return;
		}
		
		this.handle.inventory.replaceSlot(block);
	}

	@Override
	public void respawn() {
		if(this.handle == null) {
			return;
		}
		
		this.handle.resetPos();
	}

	@Override
	public boolean canBreakUnbreakables() {
		return this.breakUnbreakables;
	}

	@Override
	public void setCanBreakUnbreakables(boolean canBreak) {
		this.breakUnbreakables = canBreak;
	}

	@Override
	public void sendCustomMessage(String id, byte[] data) {
		if(this.getSession() == null) {
			return;
		}
		
		this.getSession().send(new CustomMessage(id, data));
	}

}
