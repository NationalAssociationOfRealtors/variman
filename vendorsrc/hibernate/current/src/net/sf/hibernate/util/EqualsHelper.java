//$Id: EqualsHelper.java,v 1.6 2004/06/04 05:43:49 steveebersole Exp $
package net.sf.hibernate.util;

/**
 * @author Gavin King
 */
public final class EqualsHelper {

	public static boolean equals(Object x, Object y) {
		return x==null ? 
			y==null : 
			x==y || x.equals(y);
	}
	private EqualsHelper() {}

}
