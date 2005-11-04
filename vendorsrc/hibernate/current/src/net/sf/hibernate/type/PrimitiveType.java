//$Id: PrimitiveType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import net.sf.hibernate.util.EqualsHelper;

/**
 * Superclass of primitive / primitive wrapper types.
 * @author Gavin King
 */
public abstract class PrimitiveType extends ImmutableType implements LiteralType {
	
	public abstract Class getPrimitiveClass();
	
	public boolean equals(Object x, Object y) {
		return EqualsHelper.equals(x, y);
	}
	
	public String toString(Object value) {
		return value.toString();
	}
	
}





