package org.spacehq.openclassic.server.level;

import java.util.ArrayList;
import java.util.List;

import org.spacehq.openclassic.api.level.LevelInfo;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.level.ClassicLevel;
import org.spacehq.openclassic.game.network.msg.BlockChangeMessage;
import org.spacehq.openclassic.game.network.msg.Message;
import org.spacehq.openclassic.game.network.msg.PlayerDespawnMessage;
import org.spacehq.openclassic.game.network.msg.custom.LevelPropertyMessage;
import org.spacehq.openclassic.server.player.ServerPlayer;

public class ServerLevel extends ClassicLevel {

	private List<Player> players = new ArrayList<Player>();

	public ServerLevel() {
		super();
	}

	public ServerLevel(LevelInfo info) {
		super(info);
	}

	@Override
	public List<Player> getPlayers() {
		return new ArrayList<Player>(this.players);
	}
	
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	public void removePlayer(String name) {
		for(Player player : this.getPlayers()) {
			if(player.getName().equalsIgnoreCase(name)) {
				this.players.remove(player);
				this.sendToAllExcept(player, new PlayerDespawnMessage(((ServerPlayer) player).getPlayerId()));
			}
		}
	}

	public void removePlayer(byte id) {
		for(Player player : this.getPlayers()) {
			if(((ServerPlayer) player).getPlayerId() == id) {
				this.players.remove(player);
				this.sendToAllExcept(player, new PlayerDespawnMessage(((ServerPlayer) player).getPlayerId()));
			}
		}
	}
	
	@Override
	public boolean setBlockIdAt(int x, int y, int z, byte type, boolean physics) {
		if(super.setBlockIdAt(x, y, z, type, physics)) {
			this.sendToAll(new BlockChangeMessage((short) x, (short) y, (short) z, type));
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setSkyColor(int color) {
		super.setSkyColor(color);
		this.sendToAll(new LevelPropertyMessage("sky", color));
	}

	@Override
	public void setFogColor(int color) {
		super.setFogColor(color);
		this.sendToAll(new LevelPropertyMessage("fog", color));
	}

	@Override
	public void setCloudColor(int color) {
		super.setCloudColor(color);
		this.sendToAll(new LevelPropertyMessage("cloud", color));
	}
	
	@Override
	public void setRaining(boolean raining) {
		super.setRaining(raining);
		this.sendToAll(new LevelPropertyMessage("raining", raining ? 1 : 0));
	}
	
	public void sendToAll(Message message) {
		for(Player player : this.getPlayers()) {
			((ServerPlayer) player).getSession().send(message);
		}
	}

	public void sendToAllExcept(Player skip, Message message) {
		for(Player player : this.getPlayers()) {
			if(((ServerPlayer) player).getPlayerId() == ((ServerPlayer) player).getPlayerId()) {
				continue;
			}

			((ServerPlayer) player).getSession().send(message);
		}
	}

}
