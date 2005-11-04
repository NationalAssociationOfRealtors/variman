//$Id: OneToOneType.java,v 1.12 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * A one-to-one association to an entity
 * @author Gavin King
 */
public class OneToOneType extends EntityType {
	
	private static final int[] NO_INTS = new int[0];
	
	private final ForeignKeyDirection foreignKeyType;
	
	public int getColumnSpan(Mapping session) throws MappingException {
		return 0;
	}
	
	
	public int[] sqlTypes(Mapping session) throws MappingException {
		return NO_INTS;
	}
	
	public OneToOneType(Class persistentClass, ForeignKeyDirection foreignKeyType, String uniqueKeyPropertyName) {
		super(persistentClass, uniqueKeyPropertyName);
		this.foreignKeyType = foreignKeyType;
	}
	
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		//nothing to do
	}
	
	public boolean isOneToOne() {
		return true;
	}
	
	
	public boolean isDirty(Object old, Object current, SessionImplementor session) throws HibernateException {
		return false;
	}
	
	public boolean isModified(Object old, Object current, SessionImplementor session) throws HibernateException {
		return false;
	}
	
	public ForeignKeyDirection getForeignKeyDirection() {
		return foreignKeyType;
	}
	
	public Object hydrate(
		ResultSet rs,
		String[] names,
		SessionImplementor session,
		Object owner) 
	throws HibernateException, SQLException {
		
		return session.getEntityIdentifier(owner);
	}
	
	protected Object resolveIdentifier(Serializable id, SessionImplementor session) throws HibernateException {
		Class clazz = getAssociatedClass();
		return isNullable() ?
			session.internalLoadOneToOne(clazz, id) : //no proxy allowed
			session.internalLoad(clazz, id); //proxy ok
	}
	
	public boolean isNullable() {
		return foreignKeyType==ForeignKeyDirection.FOREIGN_KEY_TO_PARENT;
	}
	
	public boolean usePrimaryKeyAsForeignKey() {
		return true;
	}

	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		return null;
	}
	
	public Object assemble(Serializable oid, SessionImplementor session, Object owner) 
	throws HibernateException {
		return resolveIdentifier( session.getEntityIdentifier(owner), session, owner );
	}
	
}

