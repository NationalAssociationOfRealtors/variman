//$Id: AbstractType.java,v 1.15 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * Abstract superclass of the built in Type hierarchy.
 * @author Gavin King
 */
public abstract class AbstractType implements Type {
	
	public boolean isAssociationType() {
		return false;
	}
	
	public boolean isPersistentCollectionType() {
		return false;
	}
	
	public boolean isComponentType() {
		return false;
	}
	
	public boolean isEntityType() {
		return false;
	}
	
	
	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		
		if (value==null) {
			return null;
		}
		else {
			return (Serializable) deepCopy(value);
		}
	}
	
	public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
		if ( cached==null ) {
			return null;
		}
		else {
			return deepCopy(cached);
		}
	}
	
	public boolean isDirty(Object old, Object current, SessionImplementor session) throws HibernateException {
		return !equals(old, current);
	}
	
	public Object hydrate(
		ResultSet rs,
		String[] names,
		SessionImplementor session,
		Object owner)
	throws HibernateException, SQLException {
		// TODO: this is very suboptimal for some subclasses (namely components), 
		// since it does not take advantage of two-phase-load
		return nullSafeGet(rs, names, session, owner);
	}
		
	public Object resolveIdentifier(Object value, SessionImplementor session, Object owner)
	throws HibernateException {
		return value;
	}
		

	public boolean isObjectType() {
		return false;
	}

	public boolean isModified(
		Object old,
		Object current,
		SessionImplementor session)
		throws HibernateException {
		return isDirty(old, current, session);
	}
	
	public Object copy(Object original, Object target, SessionImplementor session, Object owner) 
	throws HibernateException {
		if (original==null) return null;
		return assemble( disassemble(original, session), session, owner );
	}
	
	public boolean equals(Object object) {
		return object==this || ( object!=null && object.getClass()==getClass() );
	}
	public int hashCode() {
		return getClass().hashCode();
	}

}
	
	
	
	
	
	
