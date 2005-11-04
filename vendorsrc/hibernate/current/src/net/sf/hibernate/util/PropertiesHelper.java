//$Id: PropertiesHelper.java,v 1.11 2004/07/13 14:27:00 turin42 Exp $
package net.sf.hibernate.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;


public final class PropertiesHelper {
	
	public static boolean getBoolean(String property, Properties properties) {
		return Boolean.valueOf( properties.getProperty(property) ).booleanValue();
	}
	
	public static boolean getBoolean(String property, Properties properties, boolean defaultValue) {
		String setting = properties.getProperty(property);
		return (setting==null) ? defaultValue : Boolean.valueOf(setting).booleanValue();
	}
	
	public static int getInt(String property, Properties properties, int defaultValue) {
		String propValue = properties.getProperty(property);
		return (propValue==null) ? defaultValue : Integer.parseInt(propValue);
	}
	
	public static long getLong(String property, Properties properties, long defaultValue) {
		String propValue = properties.getProperty(property);
		return (propValue==null) ? defaultValue : Long.parseLong(propValue);
	}
	
	public static String getString(String property, Properties properties, String defaultValue) {
		String propValue = properties.getProperty(property);
		return (propValue==null) ? defaultValue : propValue;
	}
	
	public static Integer getInteger(String property, Properties properties) {
		String propValue = properties.getProperty(property);
		return (propValue==null) ? null : Integer.valueOf(propValue);
	}

	public static byte getByte(String property, Properties properties, byte defaultValue) {
		String propValue = properties.getProperty(property);
		return (propValue == null) ? defaultValue : Byte.parseByte(propValue);
	}	

	public static Map toMap(String property, String delim, Properties properties) {
		Map map = new HashMap();
		String propValue = properties.getProperty(property);
		if (propValue!=null) {
			StringTokenizer tokens = new StringTokenizer(propValue, delim);
			while ( tokens.hasMoreTokens() ) {
				map.put(
					tokens.nextToken(),
					tokens.hasMoreElements() ? tokens.nextToken() : StringHelper.EMPTY_STRING
				);
			}
		}
		return map;
	}
	
	public static String[] toStringArray(String property, String delim, Properties properties) {
		return toStringArray( properties.getProperty(property), delim );
	}
	
	public static String[] toStringArray(String propValue, String delim) {
		if (propValue!=null) {
			return StringHelper.split(delim, propValue);
		}
		else {
			return ArrayHelper.EMPTY_STRING_ARRAY;
		}
	}
	
	private PropertiesHelper() {}
}






