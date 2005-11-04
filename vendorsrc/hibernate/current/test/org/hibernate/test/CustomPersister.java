//$Id: CustomPersister.java,v 1.4 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;
import java.util.Hashtable;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.cache.CacheConcurrencyStrategy;
import net.sf.hibernate.engine.Cascades;
import net.sf.hibernate.engine.Key;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.id.IdentifierGenerator;
import net.sf.hibernate.id.CounterGenerator;
import net.sf.hibernate.mapping.PersistentClass;
import net.sf.hibernate.metadata.ClassMetadata;
import net.sf.hibernate.persister.ClassPersister;
import net.sf.hibernate.type.Type;
import net.sf.hibernate.type.VersionType;
import net.sf.hibernate.util.EqualsHelper;

public class CustomPersister implements ClassPersister {
	
	private static final Hashtable INSTANCES = new Hashtable();
	private static final IdentifierGenerator GENERATOR = new CounterGenerator();
	
	public CustomPersister(PersistentClass model, SessionFactoryImplementor factory) {}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#postInstantiate(SessionFactoryImplementor)
	 */
	public void postInstantiate()
	throws MappingException {
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getMappedClass()
	 */
	public Class getMappedClass() {
		return Custom.class;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getClassName()
	 */
	public String getClassName() {
		return Custom.class.getName();
	}

	/**
	 * @see net.sf.hibernate.persister.ClassPersister#implementsPersistentLifecycle()
	 */
	public boolean implementsPersistentLifecycle() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#implementsLifecycle()
	 */
	public boolean implementsLifecycle() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#implementsValidatable()
	 */
	public boolean implementsValidatable() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#hasProxy()
	 */
	public boolean hasProxy() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#hasCollections()
	 */
	public boolean hasCollections() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#hasCascades()
	 */
	public boolean hasCascades() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#isMutable()
	 */
	public boolean isMutable() {
		return true;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#isIdentifierAssignedByInsert()
	 */
	public boolean isIdentifierAssignedByInsert() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#isUnsaved(Serializable)
	 */
	public boolean isUnsaved(Object object) {
		return ( (Custom) object ).id==0;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#setPropertyValues(Object, Object[])
	 */
	public void setPropertyValues(Object object, Object[] values)
	throws HibernateException {
		Custom c = (Custom) object;
		c.name = (String) values[0];
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getPropertyValues(Object)
	 */
	public Object[] getPropertyValues(Object object)
	throws HibernateException {
		
		Custom c = (Custom) object;
		return new Object[] { c.name };
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getPropertyValue(Object, int)
	 */
	public Object getPropertyValue(Object obj, int i)
	throws HibernateException {
		return ( (Custom) obj ).name;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#setPropertyValue(Object, int, Object)
	 */
	public void setPropertyValue(Object obj, int i, Object val)
	throws HibernateException {
		( (Custom) obj ).name = (String) val;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#isDirty(Object[], Object[], Object, SessionImplementor)
	 */
	public int[] findDirty(
		Object[] x,
		Object[] y,
		Object owner,
		SessionImplementor session
	) throws HibernateException {
		if ( !EqualsHelper.equals( x[0], y[0] ) ) {
			return new int[] { 0 };
		}
		else {
			return null;
		}
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#isDirty(Object[], Object[], Object, SessionImplementor)
	 */
	public int[] findModified(
		Object[] x,
		Object[] y,
		Object owner,
		SessionImplementor session
	) throws HibernateException {
		if ( !EqualsHelper.equals( x[0], y[0] ) ) {
			return new int[] { 0 };
		}
		else {
			return null;
		}
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#hasIdentifierProperty()
	 */
	public boolean hasIdentifierProperty() {
		return true;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getIdentifier(Object)
	 */
	public Serializable getIdentifier(Object object)
	throws HibernateException {
		return new Long( ( (Custom) object ).id );
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#setIdentifier(Object, Serializable)
	 */
	public void setIdentifier(Object object, Serializable id)
	throws HibernateException {
		
		( (Custom) object ).id = ( (Long) id ).longValue();
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#isVersioned()
	 */
	public boolean isVersioned() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getVersionType()
	 */
	public VersionType getVersionType() {
		return null;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getVersionProperty()
	 */
	public int getVersionProperty() {
		return 0;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getVersion(Object)
	 */
	public Object getVersion(Object object) throws HibernateException {
		return null;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#instantiate(Serializable)
	 */
	public Object instantiate(Serializable id) throws HibernateException {
		Custom c = new Custom();
		c.id = ( (Long) id ).longValue();
		return c;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getIdentifierGenerator()
	 */
	public IdentifierGenerator getIdentifierGenerator()
	throws HibernateException {
		return GENERATOR;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#load(Serializable, Object, boolean, SessionImplementor)
	 */
	public Object load(
		Serializable id,
		Object optionalObject,
		LockMode lockMode,
		SessionImplementor session
	) throws HibernateException {
			
		// fails when optional object is supplied
		
		Custom clone = null;
		Custom obj = (Custom) INSTANCES.get(id);
		if (obj!=null) {
			clone = (Custom) obj.clone();
			session.addUninitializedEntity( new Key(id, this), clone, LockMode.NONE );
			session.postHydrate(this, id, new String[] { obj.name }, clone, LockMode.NONE);
			session.initializeEntity(clone);
		}
		return clone;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#lockForUpdate(Serializable, Object, SessionImplementor)
	 */
	public void lock(
		Serializable id,
		Object version,
		Object object,
		LockMode lockMode,
		SessionImplementor session
	) throws HibernateException {
			
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#insert(Serializable, Object[], SessionImplementor)
	 */
	public void insert(
		Serializable id,
		Object[] fields,
		Object object,
		SessionImplementor session
	) throws HibernateException {
			
		INSTANCES.put(id, ( (Custom) object ).clone() );
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#insert(Object[], SessionImplementor)
	 */
	public Serializable insert(Object[] fields, Object object, SessionImplementor session)
	throws HibernateException {
		
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#delete(Serializable, Object, SessionImplementor)
	 */
	public void delete(
		Serializable id,
		Object version,
		Object object,
		SessionImplementor session
	) throws HibernateException {
			
		INSTANCES.remove(id);
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#update(Serializable, Object[], Object, SessionImplementor)
	 */
	public void update(
		Serializable id,
		Object[] fields,
		int[] dirtyFields,
		Object[] oldFields,
		Object oldVersion,
		Object object,
		SessionImplementor session
	) throws HibernateException {
			
		INSTANCES.put( id, ( (Custom) object ).clone() );
		
	}
	
	private static final Type[] TYPES = new Type[] { Hibernate.STRING };
	private static final String[] NAMES = new String[] { "name" };
	private static final boolean[] MUTABILITY = new boolean[] { true };
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getPropertyTypes()
	 */
	public Type[] getPropertyTypes() {
		return TYPES;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getPropertyNames()
	 */
	public String[] getPropertyNames() {
		return NAMES;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getPropertyCascadeStyles()
	 */
	public Cascades.CascadeStyle[] getPropertyCascadeStyles() {
		return null;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getIdentifierType()
	 */
	public Type getIdentifierType() {
		return Hibernate.LONG;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getIdentifierPropertyName()
	 */
	public String getIdentifierPropertyName() {
		return "id";
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#hasCache()
	 */
	public boolean hasCache() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getCache()
	 */
	public CacheConcurrencyStrategy getCache() {
		return null;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getIdentifierSpace()
	 */
	public Serializable getIdentifierSpace() {
		return "CUSTOMS";
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getPropertySpaces(Object)
	 */
	public Serializable[] getPropertySpaces() {
		return new String[] { "CUSTOMS" };
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getClassMetadata()
	 */
	public ClassMetadata getClassMetadata() {
		return null;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#hasSubclasses()
	 */
	public boolean hasSubclasses() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getConcreteProxyClass()
	 */
	public Class getConcreteProxyClass() {
		return Custom.class;
	}
					
	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getPropertyMutability()
	 */
	public boolean[] getPropertyUpdateability() {
		return MUTABILITY;
	}

	/**
	 * @see net.sf.hibernate.persister.ClassPersister#getPropertyInsertability()
	 */
	public boolean[] getPropertyInsertability() {
		return MUTABILITY;
	}

	public boolean hasIdentifierPropertyOrEmbeddedCompositeIdentifier() {
		return true;
	}

	public boolean isBatchLoadable() {
		return false;
	}

	public Object[] getCurrentPersistentState(
		Serializable id,
		Object version, SessionImplementor session)
		throws HibernateException {

		return null;
	}

	public Type getPropertyType(String propertyName) {
		throw new UnsupportedOperationException();
	}

	public Object getPropertyValue(Object object, String propertyName)
		throws HibernateException {
		throw new UnsupportedOperationException();
	}

	public Object createProxy(Serializable id, SessionImplementor session)
		throws HibernateException {
		throw new UnsupportedOperationException("no proxy for this class");
	}

	public Object getCurrentVersion(
		Serializable id,
		SessionImplementor session)
		throws HibernateException {
		
		return INSTANCES.get(id);
	}

	public boolean[] getPropertyNullability() {
		return MUTABILITY;
	}

	public boolean isCacheInvalidationRequired() {
		return false;
	}

}
						
						
						
						
						
						
