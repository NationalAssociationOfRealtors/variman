//$Id: ImmutableType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * Superclass of nullable immutable types.
 * @author Gavin King
 */
public abstract class ImmutableType extends NullableType {
	
	public final Object deepCopyNotNull(Object value) throws HibernateException {
		return value;
	}
	
	public final boolean isMutable() {
		return false;
	}
	
	public boolean hasNiceEquals() {
		return true;
	}

	public Object copy(
		Object original,
		Object target,
		SessionImplementor session,
		Object owner)
		throws HibernateException {
		
		return original;
	}


}






