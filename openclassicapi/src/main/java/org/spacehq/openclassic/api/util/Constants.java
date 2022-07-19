package org.spacehq.openclassic.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.spacehq.openclassic.api.OpenClassic;

/**
 * Constant variables used by OpenClassic.
 */
public class Constants {
	
	static {
		VERSION = getVersion();
	}
	
	/**
	 * OpenClassic's current version.
	 */
	public static final String VERSION;

	/**
	 * The distance between the player's feet and the player's eyes.
	 */
	public static final float FOOT_EYE_DISTANCE = 1.59375f;

	/**
	 * Max health of a player.
	 */
	public static final int MAX_HEALTH = 20;
	
	/**
	 * Max air of a player.
	 */
	public static final int MAX_AIR = 300;
	
	/**
	 * Max arrows of a player.
	 */
	public static final int MAX_ARROWS = 99;
	
	/**
	 * Default private constructor
	 */
	private Constants() {
	}

	private static String getVersion() {
		String result = "Unknown";
		InputStream stream = OpenClassic.class.getClassLoader().getResourceAsStream("META-INF/maven/org.spacehq/openclassicapi/pom.properties");
		Properties properties = new Properties();
		if(stream != null) {
			try {
				properties.load(stream);
				result = properties.getProperty("version");
			} catch(IOException e) {
				OpenClassic.getLogger().severe("Could not get OpenClassic version!");
				e.printStackTrace();
			}
		}

		return result;
	}

}
