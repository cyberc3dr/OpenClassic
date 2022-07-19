package org.spacehq.openclassic.game.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class DateOutputFormatter extends Formatter {

	private SimpleDateFormat date;
	private MessageFormatter formatter;

	public DateOutputFormatter(SimpleDateFormat date, MessageFormatter formatter) {
		this.date = date;
		this.formatter = formatter;
	}

	@Override
	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder();

		builder.append(date.format(record.getMillis()));
		builder.append(" [");
		builder.append(record.getLevel().getLocalizedName().toUpperCase());
		builder.append("] ");
		builder.append(this.formatter.format(this.formatMessage(record)));
		builder.append('\n');

		if(record.getThrown() != null) {
			StringWriter writer = new StringWriter();
			record.getThrown().printStackTrace(new PrintWriter(writer));
			builder.append(writer.toString());
		}

		return builder.toString();
	}

}