package org.spacehq.openclassic.game.util;

public class InternalConstants {

	public static final byte PROTOCOL_VERSION = 7;
	public static final byte OPENCLASSIC_PROTOCOL_VERSION = 9;
	public static final int TICKS_PER_SECOND = 20;
	public static final float TICK_MILLISECONDS = 1000f / TICKS_PER_SECOND;
	public static final int PHYSICS_PER_SECOND = 10;
	public static final byte NOT_OP = 0x00;
	public static final byte OP = 0x64;
	public static final String MINECRAFT_URL_HTTPS = "https://minecraft.net/";
	public static final String MINECRAFT_URL_HTTP = "http://minecraft.net/";
	public static final double[] SENSITIVITY_VALUE = new double[] { 0.05D, 0.15D, 0.3D, 0.5D, 0.75D };
	
}
