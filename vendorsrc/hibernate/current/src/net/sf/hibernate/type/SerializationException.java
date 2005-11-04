//$Id: SerializationException.java,v 1.7 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import net.sf.hibernate.HibernateException;

/**
 * Thrown when a property cannot be serializaed/deserialized
 * @author Gavin King
 */
public class SerializationException extends HibernateException {
	
	public SerializationException(String message, Exception root) {
		super(message, root);
	}
	
}






