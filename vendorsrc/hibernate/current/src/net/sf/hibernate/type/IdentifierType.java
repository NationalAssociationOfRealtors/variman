//$Id: IdentifierType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

/**
 * A <tt>Type</tt> that may be used as an identifier.
 * @author Gavin King
 */
public interface IdentifierType extends Type {
	
	/**
	 * Convert the value from the mapping file to a Java object.
	 * @param xml the value of <tt>discriminator-value</tt> or <tt>unsaved-value</tt> attribute
	 * @return Object
	 * @throws Exception
	 */
	public Object stringToObject(String xml) throws Exception;
	
}






