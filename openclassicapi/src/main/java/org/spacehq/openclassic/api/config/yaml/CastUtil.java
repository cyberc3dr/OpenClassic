package org.spacehq.openclassic.api.config.yaml;

public class CastUtil {

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
	
	public static Byte castByte(Object o) {
		if(o instanceof Number) {
			return ((Number) o).byteValue();
		}

		return null;
	}
	
	public static Short castShort(Object o) {
		if(o instanceof Number) {
			return ((Number) o).shortValue();
		}

		return null;
	}
	
	public static Integer castInt(Object o) {
		if(o instanceof Number) {
			return ((Number) o).intValue();
		}

		return null;
	}
	
	public static Long castLong(Object o) {
		if(o instanceof Number) {
			return ((Number) o).longValue();
		}

		return null;
	}

	public static Double castDouble(Object o) {
		if(o instanceof Number) {
			return ((Number) o).doubleValue();
		}

		return null;
	}

	public static Float castFloat(Object obj) {
		if(obj instanceof Number) {
			return ((Number) obj).floatValue();
		}

		return null;
	}

}
