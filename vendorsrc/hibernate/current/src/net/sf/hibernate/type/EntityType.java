//$Id: EntityType.java,v 1.14 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.util.ReflectHelper;
import net.sf.hibernate.util.StringHelper;
import net.sf.hibernate.persister.ClassPersister;
import net.sf.hibernate.persister.Joinable;
import net.sf.hibernate.persister.UniqueKeyLoadable;
import net.sf.hibernate.proxy.HibernateProxyHelper;

/**
 * A reference to an entity class
 * @author Gavin King
 */
public abstract class EntityType extends AbstractType implements AssociationType {
	
	private final Class associatedClass;
	private final boolean niceEquals;
	protected final String uniqueKeyPropertyName;
	
	public final boolean isEntityType() {
		return true;
	}
	
	public final Class getAssociatedClass() {
		return associatedClass;
	}
	
	public final boolean equals(Object x, Object y) {
		return x==y;
	}
	
	protected EntityType(Class persistentClass, String uniqueKeyPropertyName) {
		this.associatedClass = persistentClass;
		niceEquals = !ReflectHelper.overridesEquals(persistentClass);
		this.uniqueKeyPropertyName = uniqueKeyPropertyName;
	}
	
	public Object nullSafeGet(ResultSet rs, String name, SessionImplementor session, Object owner) throws HibernateException, SQLException {
		return nullSafeGet( rs, new String[] {name}, session, owner );
	}
	
	/**
	 * This returns the wrong class for an entity with a proxy. Theoretically
	 * it should return the proxy class, but it doesn't.
	 */
	public final Class getReturnedClass() {
		return associatedClass;
	}
	
	/*protected final Object getActualIdentifier(Object value, SessionImplementor session) throws HibernateException {
		return session.getEntityIdentifierIfNotUnsaved(value); //tolerates nulls
	}*/
	
	protected final Object getIdentifier(Object value, SessionImplementor session) throws HibernateException {
		if (uniqueKeyPropertyName==null) {
			return session.getEntityIdentifierIfNotUnsaved(value); //tolerates nulls
		}
		else if (value==null) {
			return null;
		}
		else {
			return session.getFactory()
				.getPersister( getAssociatedClass() )
				.getPropertyValue(value, uniqueKeyPropertyName);
		}
	}
	
	public String toString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		ClassPersister persister = factory.getPersister(associatedClass);
		if (value==null) return "null";
		StringBuffer result = new StringBuffer()
			.append( StringHelper.unqualify( HibernateProxyHelper.getClass(value).getName() ) );
		if ( persister.hasIdentifierProperty() ) {
			result.append('#')
				.append( persister.getIdentifierType().toString( HibernateProxyHelper.getIdentifier(value, persister), factory ) );
		}
		return result.toString();
	}
	
	public Object fromString(String xml) throws HibernateException {
		throw new UnsupportedOperationException(); //TODO: is this correct???
	}
	
	public String getName() { return associatedClass.getName(); }
	
	public Object deepCopy(Object value) {
		return value; //special case ... this is the leaf of the containment graph, even though not immutable
	}
	
	public boolean isMutable() {
		return false;
	}
	
	public abstract boolean isOneToOne();
	
	public Object copy(Object original, Object target, SessionImplementor session, Object owner) 
	throws HibernateException {
		if (original==null) return null;
		if (original==target) return target;
		Object id = getIdentifier(original, session);
		if (id==null) throw new AssertionFailure("cannot copy a reference to an object with a null id");
		id = getIdentifierOrUniqueKeyType( session.getFactory() ).copy(id, null, session, owner);
		return resolveIdentifier(id, session, owner);
	}
	
	public boolean hasNiceEquals() {
		return niceEquals;
	}
	
	public boolean isAssociationType() {
		return true;
	}
	
	public final Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) 
	throws HibernateException, SQLException {
		return resolveIdentifier( hydrate(rs, names, session, owner), session, owner );
	}
	
	public abstract Object hydrate(ResultSet rs, String[] names, SessionImplementor session, Object owner)
	throws HibernateException, SQLException;
		
	public boolean isDirty(Object old, Object current, SessionImplementor session) throws HibernateException {
		
		//TODO: currently broken for unique-key references (does not detect 
		//      change to unique key property of the associated object)
		
		if ( equals(old, current) ) return false;
		
		Object oldid = getIdentifier(old, session);
		Object newid = getIdentifier(current, session);
		return !session.getFactory().getIdentifierType(associatedClass).equals(oldid, newid);

	}
	
	public boolean isUniqueKeyReference() {
		return uniqueKeyPropertyName!=null;
	}

	public Joinable getJoinable(SessionFactoryImplementor factory) throws MappingException {
		return (Joinable) factory.getPersister(associatedClass);
	}
	
	public String[] getReferencedColumns(SessionFactoryImplementor factory) throws MappingException {
		//I really, really don't like the fact that a Type now knows about column mappings!
		//bad seperation of concerns ... could we move this somehow to Joinable interface??
		if (uniqueKeyPropertyName==null) {
			return getJoinable(factory).getJoinKeyColumnNames();
		}
		else {
			return ( (UniqueKeyLoadable) getJoinable(factory) ).getUniqueKeyColumnNames(uniqueKeyPropertyName);
		}
	}
	
	Type getIdentifierType(SessionImplementor session) throws MappingException {
		return session.getFactory().getIdentifierType(associatedClass);
	}
	
	public final Type getIdentifierOrUniqueKeyType(SessionFactoryImplementor factory) throws MappingException {
  		if (uniqueKeyPropertyName==null) {
  			return factory.getIdentifierType(associatedClass);
		}
		else {
			return factory.getPersister(associatedClass).getPropertyType(uniqueKeyPropertyName);
		}
	}

	public final String getIdentifierOrUniqueKeyPropertyName(SessionFactoryImplementor factory) 
	throws MappingException {
		if (uniqueKeyPropertyName==null) {
			return factory.getIdentifierPropertyName(associatedClass);
		}
		else {
			return uniqueKeyPropertyName;
		}
	}
	
	/**
	 * Resolve an identifier
	 */
	protected abstract Object resolveIdentifier(Serializable id, SessionImplementor session) throws HibernateException;
	
	/**
	 * Resolve an identifier or unique key value
	 */
	public Object resolveIdentifier(Object value, SessionImplementor session, Object owner)
	throws HibernateException {
		
		if (value==null) {
			return null;
		}
		else {
			Serializable id = (Serializable) value;
			if (uniqueKeyPropertyName==null) {
				return resolveIdentifier(id, session);
			}
			else {
				return session.loadByUniqueKey( getAssociatedClass(), uniqueKeyPropertyName, id );
			}
		}
	}
	
	public Class getAssociatedClass(SessionFactoryImplementor factory) {
		return getAssociatedClass();
	}
	
	/**
	 * @deprecated use <tt>getAssociationClass()</tt>
	 */
	public Class getPersistentClass() {
		return associatedClass;
	}

	public boolean equals(Object object) {
		if ( !super.equals(object) ) return false;
		return ( (EntityType) object ).getAssociatedClass()==associatedClass;
	}

	public int hashCode() {
		return associatedClass.hashCode();
	}

}
	
	
	
	
	
