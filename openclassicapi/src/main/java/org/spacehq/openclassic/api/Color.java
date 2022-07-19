package org.spacehq.openclassic.api;

/**
 * Represents a chat color.
 */
public enum Color {

	/** Represents the color black. */
	BLACK('0', 0, 0, 0),
	/** Represents the color dark blue. */
	DARK_BLUE('1', 0, 0, 170),
	/** Represents the color dark green. */
	DARK_GREEN('2', 0, 170, 0),
	/** Represents the color cyan. */
	CYAN('3', 0, 170, 170),
	/** Represents the color dark red. */
	DARK_RED('4', 170, 0, 0),
	/** Represents the color purple. */
	PURPLE('5', 170, 0, 170),
	/** Represents the color gold. */
	GOLD('6', 255, 170, 0),
	/** Represents the color gray. */
	GRAY('7', 170, 170, 170),
	/** Represents the color dark gray. */
	DARK_GRAY('8', 85, 85, 85),
	/** Represents the color blue. */
	BLUE('9', 85, 85, 255),
	/** Represents the color green. */
	GREEN('a', 85, 255, 85),
	/** Represents the color aqua. */
	AQUA('b', 85, 255, 255),
	/** Represents the color red. */
	RED('c', 255, 85, 85),
	/** Represents the color pink. */
	PINK('d', 255, 85, 255),
	/** Represents the color yellow. */
	YELLOW('e', 255, 255, 85),
	/** Represents the color white. */
	WHITE('f', 255, 255, 255),
	/** Represents the color orange. Only available on OpenClassic clients. */
	ORANGE('g', 220, 120, 60),
	/** Represents the color orange. Only available on OpenClassic clients. */
	BROWN('h', 90, 60, 30);
	
	/** Represents the color code prefix. */
	public static final char COLOR_CHARACTER = '&';
	/** Represents the color code prefix as a String. */
	public static final String COLOR_CHARACTER_STRING = String.valueOf(COLOR_CHARACTER);
	/** A string containing all color chars, with each as an uppercase and lowercase version. */
	public static final String CHARS = buildChars();
	
	private char code;
	private int r;
	private int g;
	private int b;
	
	private Color(char code, int r, int g, int b) {
		this.code = code;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Gets the color code char.
	 * @return The color code char.
	 */
	public char getCode() {
		return this.code;
	}
	
	/**
	 * Gets the red of this color.
	 * @return This color's red value.
	 */
	public int getRed() {
		return this.r;
	}
	
	/**
	 * Gets the green of this color.
	 * @return This color's green value.
	 */
	public int getGreen() {
		return this.g;
	}
	
	/**
	 * Gets the blue of this color.
	 * @return This color's blue value.
	 */
	public int getBlue() {
		return this.b;
	}
	
	@Override
	public String toString() {
		return COLOR_CHARACTER_STRING + this.code;
	}
	
	/**
	 * Strips a message of all color codes.
	 * @param message Message to strip from.
	 * @return Message without color codes.
	 */
	public static String stripColor(String message) {
		if(message == null) {
			throw new IllegalArgumentException("Message cannot be null.");
		}

		StringBuilder builder = new StringBuilder();
		for(int index = 0; index < message.length(); index++) {
			char curr = message.charAt(index);
			
			if(curr == COLOR_CHARACTER) {
				index++;
				continue;
			}
			
			builder.append(curr);
		}
		
		return builder.toString();
	}

	/**
	 * Gets the color from the given character code.
	 * @param c Code to search with.
	 * @return Color with the given code.
	 */
	public static Color getByChar(char c) {
		for(Color color : values()) {
			if(color.getCode() == c) {
				return color;
			}
		}
		
		return null;
	}
	
	/**
	 * Translates any colors prefixed with the given character to working
	 * color codes.
	 * @param prefix Prefix to look for.
	 * @param text Text to translate.
	 * @return The resulting translated text.
	 */
	public static String translate(char prefix, String text) {
		char[] chars = text.toCharArray();
		for(int index = 0; index < chars.length - 1; index++) {
			if(chars[index] == prefix && CHARS.indexOf(chars[index + 1]) > -1) {
				chars[index] = COLOR_CHARACTER;
				chars[index + 1] = Character.toLowerCase(chars[index + 1]);
			}
		}

		return new String(chars);
	}
	
	private static String buildChars() {
		StringBuilder build = new StringBuilder();
		for(Color color : values()) {
			build.append(String.valueOf(color.getCode()).toUpperCase());
			build.append(color.getCode());
		}
		
		return build.toString();
	}

}
