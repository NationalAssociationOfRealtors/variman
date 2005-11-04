//$Id: PersistentCollectionType.java,v 1.20 2004/08/07 14:05:39 oneovthafew Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.collection.QueryableCollection;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.persister.Joinable;

/**
 * A type that handles Hibernate <tt>PersistentCollection</tt>s (including arrays).
 * @author Gavin King
 */
public abstract class PersistentCollectionType extends AbstractType implements AssociationType {
	
	private final String role;
	private static final int[] NO_INTS = {};
	
	public PersistentCollectionType(String role) {
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
	
	public boolean isPersistentCollectionType() {
		return true;
	}
	
	public final boolean equals(Object x, Object y) {
		return x==y ||
			( x instanceof PersistentCollection && ( (PersistentCollection) x ).isWrapper(y) ) ||
			( y instanceof PersistentCollection && ( (PersistentCollection) y ).isWrapper(x) );
	}
	
	public abstract PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) 
	throws HibernateException;
	
	public Object nullSafeGet(ResultSet rs, String name, SessionImplementor session, Object owner) 
	throws HibernateException, SQLException {
		throw new AssertionFailure("bug in PersistentCollectionType");
	}
	
	public Object nullSafeGet(ResultSet rs, String[] name, SessionImplementor session, Object owner) 
	throws HibernateException, SQLException {
		return resolveIdentifier( hydrate(rs, name, session, owner), session, owner );
	}
	
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) 
	throws HibernateException, SQLException {
	}
	
	public int[] sqlTypes(Mapping session) throws MappingException {
		return NO_INTS;
	}
	
	public int getColumnSpan(Mapping session) throws MappingException {
		return 0;
	}
	
	public String toString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		if (value==null) return "null";
		Type elemType = getElementType(factory);
		if ( Hibernate.isInitialized(value) ) {
			List list = new ArrayList();
			Iterator iter = getElementsIterator(value);
			while ( iter.hasNext() ) list.add( elemType.toString( iter.next(), factory ) );
			return list.toString();
		}
		else {
			return "uninitialized";
		}
	}
	
	public Object fromString(String xml) {
		throw new UnsupportedOperationException();
	}
	
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}
	
	public String getName() {
		return getReturnedClass().getName();
	}
	
	public Iterator getElementsIterator(Object collection) {
		return ( (java.util.Collection) collection ).iterator();
	}
	
	public boolean isMutable() {
		return false;
	}
	
	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		
		return null;

	}
	
	public Object assemble(Serializable cached, SessionImplementor session, Object owner) 
	throws HibernateException {
		Serializable id = session.getEntityIdentifier(owner);
		if (id==null) throw new AssertionFailure("owner id unknown when re-assembling collection reference");
		return resolveIdentifier(id, session, owner);
	}
	
	private boolean isOwnerVersioned(SessionImplementor session) throws MappingException {
		Class ownerClass = session.getFactory()
			.getCollectionPersister(role)
			.getOwnerClass();
		return session.getFactory()
			.getPersister(ownerClass)
			.isVersioned();
	}
	
	public boolean isDirty(Object old, Object current, SessionImplementor session)
	throws HibernateException {
		
		// collections don't dirty an unversioned parent entity
		
		//TODO: I don't really like this implementation; it would be better if
		//      this was handled by searchForDirtyCollections()
		return isOwnerVersioned(session) && super.isDirty(old, current, session);
		//return false;
	
	}
	
	public boolean hasNiceEquals() {
		return false;
	}
	
	public abstract PersistentCollection wrap(SessionImplementor session, Object collection);
	
	/**
	 * Note: return true because this type is castable to <tt>AssociationType</tt>. Not because
	 * all collections are associations.
	 */
	public boolean isAssociationType() {
		return true;
	}
	
	public ForeignKeyDirection getForeignKeyDirection() {
		return ForeignKeyDirection.FOREIGN_KEY_TO_PARENT;
	}
	
	public Object hydrate(ResultSet rs, String[] name, SessionImplementor session, Object owner)
	throws HibernateException, SQLException {
		return session.getEntityIdentifier(owner);
	}
	
	public Object resolveIdentifier(Object value, SessionImplementor session, Object owner)
	throws HibernateException {
		return value==null ?
			null :
			session.getCollection(role, (Serializable) value, owner);
	}
	
	public boolean isArrayType() {
		return false;
	}
	
	public boolean usePrimaryKeyAsForeignKey() {
		return true;
	}

	public Joinable getJoinable(SessionFactoryImplementor factory) throws MappingException {
		return (Joinable) factory.getCollectionPersister(role);
	}

	public String[] getReferencedColumns(SessionFactoryImplementor factory) throws MappingException {
		//I really, really don't like the fact that a Type now knows about column mappings!
		//bad seperation of concerns ... could we move this somehow to Joinable interface??
		return getJoinable(factory).getJoinKeyColumnNames();
	}

	public boolean isModified(
		Object old,
		Object current,
		SessionImplementor session)
		throws HibernateException {
		
		return false;
	}

	public Class getAssociatedClass(SessionFactoryImplementor factory) throws MappingException {
		try {
			QueryableCollection collectionPersister = (QueryableCollection) factory.getCollectionPersister(role);
			if ( !collectionPersister.getElementType().isEntityType() ) {
				throw new MappingException( "collection was not an association: " + collectionPersister.getRole() );
			}
			return collectionPersister.getElementPersister().getMappedClass();
		}
		catch (ClassCastException cce) {
			throw new MappingException("collection role is not queryable " + role);
		}
	}

	public boolean equals(Object object) {
		if ( !super.equals(object) ) return false;
		return ( (PersistentCollectionType) object ).getRole().equals(role);
	}

	public int hashCode() {
		return role.hashCode();
	}

	public Object copy(
		Object original,
		Object target,
		SessionImplementor session, Object owner)
		throws HibernateException {
		
		if (original==null) return null;
		if ( original==target || !Hibernate.isInitialized(original) ) return target;
		
		Type elemType = getElementType( session.getFactory() );
		java.util.Collection result = (java.util.Collection) target;
		result.clear();
		Iterator iter = ( (java.util.Collection) original ).iterator();
		while ( iter.hasNext() ) {
			result.add( elemType.copy( iter.next(), null, session, owner ) );
		}
		return result;
	}

	protected final Type getElementType(SessionFactoryImplementor factory) 
	throws MappingException {
		return factory.getCollectionPersister( getRole() ).getElementType();
	}

	public String toString() {
		return super.toString() + " for " + getRole(); 
	}
}





