package org.spacehq.openclassic.server.network;

import java.util.logging.Level;

import javax.swing.DefaultListModel;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.event.player.PlayerQuitEvent;
import org.spacehq.openclassic.game.network.msg.Message;
import org.spacehq.openclassic.server.ClassicServer;
import org.spacehq.openclassic.server.level.ServerLevel;
import org.spacehq.openclassic.server.player.ServerSession;
import org.spacehq.openclassic.server.ui.GuiConsoleManager;

import com.zachsthings.onevent.EventManager;

public class ServerHandler extends SimpleChannelUpstreamHandler {

	@Override
	public void channelConnected(ChannelHandlerContext context, ChannelStateEvent event) {
		Channel channel = event.getChannel();
		((ClassicServer) OpenClassic.getGame()).getChannelGroup().add(channel);

		ServerSession session = new ServerSession(channel);
		((ClassicServer) OpenClassic.getGame()).getSessionRegistry().add(session);
		context.setAttachment(session);

		OpenClassic.getLogger().info("Channel connected: " + channel + ".");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void channelDisconnected(ChannelHandlerContext context, ChannelStateEvent event) {
		Channel channel = event.getChannel();
		((ClassicServer) OpenClassic.getGame()).getChannelGroup().remove(channel);

		ServerSession session = (ServerSession) context.getAttachment();
		((ClassicServer) OpenClassic.getGame()).getSessionRegistry().remove(session);

		if(session.getPlayer() != null) {
			OpenClassic.getServer().broadcastMessage(EventManager.callEvent(new PlayerQuitEvent(session.getPlayer(), String.format(OpenClassic.getGame().getTranslator().translate("player.logout"), session.getPlayer().getDisplayName()))).getMessage());
			if(((ClassicServer) OpenClassic.getServer()).getConsoleManager() instanceof GuiConsoleManager) {
				DefaultListModel model = ((GuiConsoleManager) ((ClassicServer) OpenClassic.getServer()).getConsoleManager()).getFrame().players;
				if(model.indexOf(session.getPlayer().getName()) != -1) {
					model.remove(model.indexOf(session.getPlayer().getName()));
				}
			}

			((ServerLevel) session.getPlayer().getPosition().getLevel()).removePlayer(session.getPlayer().getName());
			if(!session.disconnectMsgSent) session.getPlayer().getData().save(OpenClassic.getServer().getDirectory().getPath() + "/players/" + session.getPlayer().getName() + ".nbt");
		} else {
			if(!session.disconnectMsgSent) OpenClassic.getLogger().info(channel.getRemoteAddress() + " disconnected.");
		}

		OpenClassic.getLogger().info("Channel disconnected: " + channel + ".");
	}

	@Override
	public void messageReceived(ChannelHandlerContext context, MessageEvent event) {
		ServerSession session = (ServerSession) context.getAttachment();
		session.messageReceived((Message) event.getMessage());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent event) {
		Channel channel = event.getChannel();

		if(channel.isOpen()) {
			if(!(event.getCause().getMessage() != null && (event.getCause().getMessage().equals("Connection reset by peer") || event.getCause().getMessage().equals("Connection timed out")))) OpenClassic.getLogger().log(Level.WARNING, "Exception caught, closing channel: " + channel + "...", event.getCause());
			channel.close();
		}
	}

}
