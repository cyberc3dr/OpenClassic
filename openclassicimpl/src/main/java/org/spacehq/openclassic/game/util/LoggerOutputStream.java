package org.spacehq.openclassic.game.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.spacehq.openclassic.api.OpenClassic;

public class LoggerOutputStream extends ByteArrayOutputStream {

	private final Level level;

	public LoggerOutputStream(Level level) {
		super();
		this.level = level;
	}

	@Override
	public synchronized void flush() throws IOException {
		super.flush();
		String record = this.toString();
		super.reset();
		if(record.length() > 0 && !record.equals(System.getProperty("line.separator"))) {
			OpenClassic.getLogger().logp(level, "LoggerOutputStream", "log" + level, record);
		}
	}

}
