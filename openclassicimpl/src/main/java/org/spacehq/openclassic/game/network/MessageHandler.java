package org.spacehq.openclassic.game.network;

import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.game.network.msg.Message;

public abstract class MessageHandler<T extends Message> {

	public abstract void handle(ClassicSession session, Player player, T message);

}
