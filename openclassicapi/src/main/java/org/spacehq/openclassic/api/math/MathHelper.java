package org.spacehq.openclassic.api.math;

/**
 * A math utility class.
 */
public class MathHelper {

	public static final float PI = 3.1415927f;
	public static final float TWO_PI = PI * 2;
	public static final float HALF_PI = PI / 2;
	public static final float ONE_AND_HALF_PI = PI + HALF_PI;
	public static final double DTWO_PI = Math.PI * 2;

	private static final int SIN_BITS = 14;
	private static final int SIN_MASK = ~(-1 << SIN_BITS);
	private static final int SIN_COUNT = SIN_MASK + 1;
	private static final float[] SIN_TABLE = new float[SIN_COUNT];

	private static final float radFull = TWO_PI;
	private static final float degFull = 360;
	private static final float radToIndex = SIN_COUNT / radFull;
	private static final float degToIndex = SIN_COUNT / degFull;

	public static final float RAD_TO_DEG = 180 / PI;
	public static final double DRAD_TO_DEG = 180 / Math.PI;
	public static final float DEG_TO_RAD = PI / 180;
	public static final double DDEG_TO_RAD = Math.PI / 180;

	static {
		for(int i = 0; i < SIN_COUNT; i++) {
			SIN_TABLE[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
		}

		for(int i = 0; i < 360; i += 90) {
			SIN_TABLE[(int) (i * degToIndex) & SIN_MASK] = (float) Math.sin(i * DEG_TO_RAD);
		}
	}

	/**
	 * Gets the sine of an angle.
	 * @param radians Measure of the angle.
	 * @return The sine of the angle.
	 */
	public static float sin(float radians) {
		return SIN_TABLE[(int) (radians * radToIndex) & SIN_MASK];
	}

	/**
	 * Gets the cosine of an angle.
	 * @param radians Measure of the angle.
	 * @return The cosine of the angle.
	 */
	public static float cos(float radians) {
		return SIN_TABLE[(int) ((radians + PI / 2) * radToIndex) & SIN_MASK];
	}

	/**
	 * Converts yaw and pitch values to a forward vector.
	 * @param yaw Yaw to convert.
	 * @param pitch Pitch to convert.
	 * @return The resulting forward vector.
	 */
	public static Vector toForwardVec(float yaw, float pitch) {
		float xzLen = MathHelper.cos(-pitch * DEG_TO_RAD);
		float x = (MathHelper.sin(-yaw * DEG_TO_RAD - (float) Math.PI) * xzLen);
		float y = MathHelper.sin(-pitch * DEG_TO_RAD);
		float z = (MathHelper.cos(-yaw * DEG_TO_RAD - (float) Math.PI) * xzLen);
		return new Vector(x, y, z);
	}

	/**
	 * Casts the given object to an integer if applicable.
	 * @param o Object to cast.
	 * @return The resulting integer.
	 */
	public static Integer castInt(Object o) {
		if(o instanceof Number) {
			return ((Number) o).intValue();
		}

		return null;
	}

	/**
	 * Casts the given object to a double if applicable.
	 * @param o Object to cast.
	 * @return The resulting double.
	 */
	public static Double castDouble(Object o) {
		if(o instanceof Number) {
			return ((Number) o).doubleValue();
		}

		return null;
	}

	/**
	 * Casts the given object to a float if applicable.
	 * @param o Object to cast.
	 * @return The resulting float.
	 */
	public static Float castFloat(Object obj) {
		if(obj instanceof Number) {
			return ((Number) obj).floatValue();
		}

		return null;
	}

	/**
	 * Casts the given object to a boolean if applicable.
	 * @param o Object to cast.
	 * @return The resulting boolean.
	 */
	public static Boolean castBoolean(Object o) {
		if(o instanceof Boolean) {
			return (Boolean) o;
		} else if(o instanceof String) {
			try {
				return Boolean.parseBoolean((String) o);
			} catch (IllegalArgumentException e) {
				return null;
			}
		}

		return null;
	}

}
