package org.spacehq.openclassic.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spacehq.openclassic.client.ClassicClient;
import org.spacehq.openclassic.server.ClassicServer;

public class Main {

	public static void main(String args[]) {
		final List<String> argList = new ArrayList<String>(Arrays.asList(args));
		if(argList.contains("server")) {
			argList.remove("server");
			new Thread("Server") {
				@Override
				public void run() {
					new ClassicServer().start(argList.toArray(new String[argList.size()]));
				}
			}.start();
		} else {
			new Thread("Client") {
				@Override
				public void run() {
					new ClassicClient().start(argList.toArray(new String[argList.size()]));
				}
			}.start();
		}
	}

}
