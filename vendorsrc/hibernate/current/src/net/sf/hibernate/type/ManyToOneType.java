//$Id: ManyToOneType.java,v 1.11 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * A many-to-one association to an entity
 * @author Gavin King
 */
public class ManyToOneType extends EntityType {
	
	private Type getReferencedType(Mapping mapping) throws MappingException {
		if (uniqueKeyPropertyName==null) {
			return mapping.getIdentifierType( getAssociatedClass() );
		}
		else {
			return mapping.getPropertyType( getAssociatedClass(), uniqueKeyPropertyName );
		}
	}

	public int getColumnSpan(Mapping mapping) throws MappingException {
		return getReferencedType(mapping).getColumnSpan(mapping);
	}
	
	public int[] sqlTypes(Mapping mapping) throws MappingException {
		return getReferencedType(mapping).sqlTypes(mapping);
	}
	
	public ManyToOneType(Class persistentClass) {
		this(persistentClass, null);
	}
	
	public ManyToOneType(Class persistentClass, String uniqueKeyPropertyName) {
		super(persistentClass, uniqueKeyPropertyName);
	}
	
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
		getIdentifierOrUniqueKeyType( session.getFactory() ).nullSafeSet(st, getIdentifier(value, session), index, session);
	}
	
	public boolean isOneToOne() {
		return false;
	}
	
	public ForeignKeyDirection getForeignKeyDirection() {
		return ForeignKeyDirection.FOREIGN_KEY_FROM_PARENT;
	}
	
	public Object hydrate(ResultSet rs, String[] names, SessionImplementor session, Object owner)
	throws HibernateException, SQLException {
		
		Serializable id = (Serializable) getIdentifierOrUniqueKeyType( session.getFactory() )
			.nullSafeGet(rs, names, session, owner);
		
		if (id!=null) session.scheduleBatchLoad( getAssociatedClass(), id );
		return id;
	}
		
	protected Object resolveIdentifier(Serializable id, SessionImplementor session) throws HibernateException {
		return session.internalLoad( getAssociatedClass(), id );
	}
		
	public boolean usePrimaryKeyAsForeignKey() {
		return false;
	}

	public boolean isModified(Object old, Object current, SessionImplementor session)
	throws HibernateException {
			
		if (current==null) return old!=null;
		if (old==null) return current!=null;
		return getIdentifierOrUniqueKeyType( session.getFactory() ).isModified( old, getIdentifier(current, session), session );
	}

	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		
		if (value==null) {
			return null;
		}
		else {
			// cache the actual id of the object, not the value of the
			// property-ref, which might not be initialized
			Object id = session.getEntityIdentifierIfNotUnsaved(value);
			if (id==null) {
				throw new AssertionFailure("cannot cache a reference to an object with a null id: " + getAssociatedClass().getName() );
			}
			return getIdentifierType(session).disassemble(id, session);
		}
	}
	
	public Object assemble(Serializable oid, SessionImplementor session, Object owner) 
	throws HibernateException {
		Serializable id = (Serializable) getIdentifierType(session).assemble(oid, session, owner);
		if (id==null) {
			return null;
		}
		else {
			return resolveIdentifier(id, session);
		}
	}
	
}
	
	
	
	
	
