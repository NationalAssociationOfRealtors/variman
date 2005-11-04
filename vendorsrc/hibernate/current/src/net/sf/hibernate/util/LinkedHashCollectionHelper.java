//$Id: LinkedHashCollectionHelper.java,v 1.7 2004/06/04 01:28:53 steveebersole Exp $
package net.sf.hibernate.util;

import java.util.Map;
import java.util.Set;

import net.sf.hibernate.AssertionFailure;

public final class LinkedHashCollectionHelper {
	
	private static final Class SET_CLASS;
	private static final Class MAP_CLASS;
	static {
		Class setClass;
		Class mapClass;
		try {
			setClass = Class.forName("java.util.LinkedHashSet");
			mapClass = Class.forName("java.util.LinkedHashMap");
		}
		catch (ClassNotFoundException cnfe) {
			setClass = null;
			mapClass = null;
		}
		SET_CLASS = setClass;
		MAP_CLASS = mapClass;
	}
	
	public static Set createLinkedHashSet() {
		try {
			return (Set) SET_CLASS.newInstance();
		}
		catch (Exception e) {
			throw new AssertionFailure("Could not instantiate LinkedHashSet", e);
		}
	}
	
	public static Map createLinkedHashMap() {
		try {
			return (Map) MAP_CLASS.newInstance();
		}
		catch (Exception e) {
			throw new AssertionFailure("Could not instantiate LinkedHashMap", e);
		}
	}
	
	private LinkedHashCollectionHelper() {}
	
}






