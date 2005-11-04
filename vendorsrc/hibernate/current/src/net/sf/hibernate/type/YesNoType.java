//$Id: YesNoType.java,v 1.7 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

/**
 * <tt>yes_no</tt>: A type that maps an SQL CHAR(1) to a Java Boolean.
 * @author Gavin King
 */
public class YesNoType extends CharBooleanType {
	
	protected final String getTrueString() {
		return "Y";
	}
	protected final String getFalseString() {
		return "N";
	}
	public String getName() { return "yes_no"; }
	
}







