package org.spacehq.openclassic.client.network.handler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.event.player.PlayerJoinEvent;
import org.spacehq.openclassic.api.event.player.PlayerLoginEvent;
import org.spacehq.openclassic.api.event.player.PlayerLoginEvent.Result;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.client.gui.ErrorScreen;
import org.spacehq.openclassic.game.network.ClassicSession;
import org.spacehq.openclassic.game.network.ClassicSession.State;
import org.spacehq.openclassic.game.network.msg.IdentificationMessage;
import org.spacehq.openclassic.game.network.MessageHandler;
import org.spacehq.openclassic.game.util.InternalConstants;

import com.zachsthings.onevent.EventManager;

public class IdentificationMessageHandler extends MessageHandler<IdentificationMessage> {

	@Override
	public void handle(ClassicSession session, Player player, IdentificationMessage message) {
		if(session.getState() == State.IDENTIFYING) {
			PlayerLoginEvent event = EventManager.callEvent(new PlayerLoginEvent(OpenClassic.getClient().getPlayer(), session.getAddress()));
			if(event.getResult() != Result.ALLOWED) {
				OpenClassic.getClient().exitGameSession();
				OpenClassic.getClient().setActiveComponent(new ErrorScreen(OpenClassic.getGame().getTranslator().translate("disconnect.plugin-disallow"), event.getKickMessage()));
				return;
			}
		}
		
		if(message.getVerificationKeyOrMotd().contains("-hax")) {
			((ClassicClient) OpenClassic.getClient()).setHacks(false);
		}

		OpenClassic.getClient().getProgressBar().setVisible(true);
		OpenClassic.getClient().getProgressBar().setSubtitleScaled(false);
		OpenClassic.getClient().getProgressBar().setTitle(OpenClassic.getGame().getTranslator().translate("progress-bar.multiplayer"));
		OpenClassic.getClient().getProgressBar().setSubtitle(message.getUsernameOrServerName());
		OpenClassic.getClient().getProgressBar().setText(message.getVerificationKeyOrMotd());
		byte op = message.getOpOrCustomClient();
		if(op == InternalConstants.OP) {
			player.setCanBreakUnbreakables(true);	
		} else if(op == InternalConstants.NOT_OP) {
			player.setCanBreakUnbreakables(false);
		}
		
		if(session.getState() == State.IDENTIFYING) {
			EventManager.callEvent(new PlayerJoinEvent(OpenClassic.getClient().getPlayer(), "Joined"));
		}

		session.setState(State.PREPARING);
	}

}
