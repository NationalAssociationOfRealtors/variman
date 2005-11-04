//$Id: TrueFalseType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

/**
 * <tt>true_false</tt>: A type that maps an SQL CHAR(1) to a Java Boolean.
 * @author Gavin King
 */
public class TrueFalseType extends CharBooleanType {
	
	protected final String getTrueString() {
		return "T";
	}
	protected final String getFalseString() {
		return "F";
	}
	public String getName() { return "true_false"; }
	
}







