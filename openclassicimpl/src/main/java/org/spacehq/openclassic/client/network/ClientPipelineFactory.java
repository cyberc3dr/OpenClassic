package org.spacehq.openclassic.client.network;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import org.spacehq.openclassic.game.network.ClassicDecoder;
import org.spacehq.openclassic.game.network.ClassicEncoder;

public class ClientPipelineFactory implements ChannelPipelineFactory {

	private Timer timer = new HashedWheelTimer();
	private ClientSession session;

	public ClientPipelineFactory(ClientSession session) {
		this.session = session;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		return Channels.pipeline(new ClassicDecoder(), new ClassicEncoder(), new ReadTimeoutHandler(this.timer, 40), new ClientHandler(this.session));
	}

}
