package org.spacehq.openclassic.server.network;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.spacehq.openclassic.server.player.ServerSession;

public class SessionRegistry {

	private final Queue<ServerSession> pending = new ArrayDeque<ServerSession>();
	private final List<ServerSession> sessions = new ArrayList<ServerSession>();

	public void tick() {
		synchronized(this.pending) {
			ServerSession session;
			while((session = this.pending.poll()) != null) {
				this.sessions.add(session);
			}
		}

		for(Iterator<ServerSession> iter = this.sessions.iterator(); iter.hasNext();) {
			ServerSession session = iter.next();

			if(!session.tick()) {
				iter.remove();
				session.dispose();
			}
		}
	}

	public void add(ServerSession session) {
		synchronized(this.pending) {
			pending.add(session);
		}
	}

	public void remove(ServerSession session) {
		session.flagForRemoval();
	}

	public int size() {
		return this.sessions.size();
	}

}
