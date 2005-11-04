//$Id: ObjectType.java,v 1.17 2004/07/27 20:12:44 oneovthafew Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.TransientObjectException;
import net.sf.hibernate.engine.Cascades;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.engine.Cascades.CascadeStyle;
import net.sf.hibernate.loader.OuterJoinLoader;
import net.sf.hibernate.persister.Joinable;
import net.sf.hibernate.proxy.HibernateProxyHelper;
import net.sf.hibernate.util.ArrayHelper;

/**
 * Handles "any" mappings and the old deprecated "object" type
 * @author Gavin King
 */
public class ObjectType extends AbstractType implements AbstractComponentType, AssociationType {
	
	private final Type identifierType;
	private final Type metaType;
	
	public ObjectType(Type metaType, Type identifierType) {
		this.identifierType = identifierType;
		this.metaType = metaType;
	}
	
	public ObjectType() {
		this(Hibernate.CLASS, Hibernate.SERIALIZABLE);
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#deepCopy(Object)
	 */
	public Object deepCopy(Object value)
	throws HibernateException {
		return value;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#equals(Object, Object)
	 */
	public boolean equals(Object x, Object y) throws HibernateException {
		return x==y;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#getColumnSpan(Mapping)
	 */
	public int getColumnSpan(Mapping session)
	throws MappingException {
		return 2;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#getName()
	 */
	public String getName() {
		return "object";
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#hasNiceEquals()
	 */
	public boolean hasNiceEquals() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#isMutable()
	 */
	public boolean isMutable() {
		return false;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#nullSafeGet(ResultSet, String, SessionImplementor, Object)
	 */
	public Object nullSafeGet(ResultSet rs,	String name, SessionImplementor session, Object owner)
	throws HibernateException, SQLException {
		
		throw new UnsupportedOperationException("object is a multicolumn type");
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#nullSafeGet(ResultSet, String[], SessionImplementor, Object)
	 */
	public Object nullSafeGet(ResultSet rs,	String[] names,	SessionImplementor session,	Object owner)
	throws HibernateException, SQLException {
		return resolve(
			(Class) metaType.nullSafeGet(rs, names[0], session, owner),
			(Serializable) identifierType.nullSafeGet(rs, names[1], session, owner),
			session
		);
	}
	
	public Object hydrate(ResultSet rs,	String[] names,	SessionImplementor session,	Object owner)
	throws HibernateException, SQLException {
		Class clazz = (Class) metaType.nullSafeGet(rs, names[0], session, owner);
		Serializable id = (Serializable) identifierType.nullSafeGet(rs, names[1], session, owner);
		return new ObjectTypeCacheEntry(clazz, id);
	}
	
	public Object resolveIdentifier(Object value, SessionImplementor session, Object owner)
	throws HibernateException {
		ObjectTypeCacheEntry holder = (ObjectTypeCacheEntry) value;
		return resolve(holder.clazz, holder.id, session);
	}
	
	private Object resolve(Class clazz, Serializable id, SessionImplementor session) 
	throws HibernateException {
		return (clazz==null || id==null) ?
			null :
			session.internalLoad(clazz, id);
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#nullSafeSet(PreparedStatement, Object, int, SessionImplementor)
	 */
	public void nullSafeSet(PreparedStatement st, Object value,	int index, SessionImplementor session)
	throws HibernateException, SQLException {
		
		Serializable id;
		Class clazz;
		if (value==null) {
			id=null;
			clazz=null;
		}
		else {
			id = session.getEntityIdentifierIfNotUnsaved(value);
			clazz = HibernateProxyHelper.getClass(value);
		}
		
		metaType.nullSafeSet(st, clazz, index, session);
		identifierType.nullSafeSet(st, id, index+1, session); // metaType must be single-column type
	}
		
	/**
	 * @see net.sf.hibernate.type.Type#getReturnedClass()
	 */
	public Class getReturnedClass() {
		return Object.class;
	}
	
	//private static final int[] SQL_TYPES = new int[] { Types.VARCHAR, Types.VARBINARY };
	
	/**
	 * @see net.sf.hibernate.type.Type#sqlTypes(Mapping)
	 */
	public int[] sqlTypes(Mapping mapping) throws MappingException {
		return ArrayHelper.join( 
			metaType.sqlTypes(mapping),
			identifierType.sqlTypes(mapping) 
		);
	}
	
	/**
	 */
	public String toString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		return value==null ? 
			"null" : 
			Hibernate.entity( HibernateProxyHelper.getClass(value) )
				.toString(value, factory);
	}
	
	public Object fromString(String xml) throws HibernateException {
		throw new UnsupportedOperationException(); //TODO: is this right??
	}

	public static final class ObjectTypeCacheEntry implements Serializable {
		Class clazz;
		Serializable id;
		ObjectTypeCacheEntry(Class clazz, Serializable id) {
			this.clazz = clazz;
			this.id = id;
		}
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#assemble(Serializable, SessionImplementor, Object)
	 */
	public Object assemble(
		Serializable cached,
		SessionImplementor session,
		Object owner)
	throws HibernateException {
		
		ObjectTypeCacheEntry e = (ObjectTypeCacheEntry) cached;
		return (cached==null) ? null : session.load(e.clazz, e.id);
	}
		
	/**
	 * @see net.sf.hibernate.type.Type#disassemble(Object, SessionImplementor)
	 */
	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		return (value==null) ? 
			null : 
			new ObjectTypeCacheEntry( 
				HibernateProxyHelper.getClass(value), 
				session.getEntityIdentifierIfNotUnsaved(value) 
			);
	}
		
	/**
	 * @see net.sf.hibernate.type.Type#isObjectType()
	 */
	public boolean isObjectType() {
		return true;
	}


	public CascadeStyle cascade(int i) {
		return Cascades.STYLE_NONE;
	}

	public int enableJoinedFetch(int i) {
		return OuterJoinLoader.LAZY;
	}
	
	private static final String[] PROPERTY_NAMES = new String[] { "class", "id" };

	public String[] getPropertyNames() {
		return PROPERTY_NAMES;
	}

	public Object getPropertyValue(Object component, int i, SessionImplementor session)
		throws HibernateException {

		return (i==0) ? 
			HibernateProxyHelper.getClass(component) : 
			id(component, session);
	}

	public Object[] getPropertyValues(Object component, SessionImplementor session)
		throws HibernateException {

		return new Object[] { HibernateProxyHelper.getClass(component), id(component, session) };
	}
	
	private Serializable id(Object component, SessionImplementor session) throws HibernateException {
		try {
			return session.getEntityIdentifierIfNotUnsaved(component);
		}
		catch (TransientObjectException toe) {
			return null;
		}
	}

	public Type[] getSubtypes() {
		return new Type[] { metaType, identifierType };
	}

	public void setPropertyValues(Object component, Object[] values)
		throws HibernateException {

		throw new UnsupportedOperationException();

	}

	public Object[] getPropertyValues(Object component) {
		throw new UnsupportedOperationException();
	}

	public boolean isComponentType() {
		return true;
	}

	public ForeignKeyDirection getForeignKeyDirection() {
		//return AssociationType.FOREIGN_KEY_TO_PARENT; //this is better but causes a transient object exception...
		return ForeignKeyDirection.FOREIGN_KEY_FROM_PARENT;
	}
	
	public boolean isAssociationType() {
		return true;
	}
	
	/**
	 * Not really relevant to ObjectType, since it cannot by "joined"!
	 */
	public boolean usePrimaryKeyAsForeignKey() {
		return false;
	}

	public Joinable getJoinable(SessionFactoryImplementor factory) {
		throw new UnsupportedOperationException("any types do not have a unique referenced persister");
	}

	public String[] getReferencedColumns(SessionFactoryImplementor factory) throws MappingException {
		throw new UnsupportedOperationException("any types do not have unique referenced columns");
	}

	public boolean isModified(Object old, Object current, SessionImplementor session)
	throws HibernateException {
		if (current==null) return old!=null;
		if (old==null) return current!=null;
		ObjectTypeCacheEntry holder = (ObjectTypeCacheEntry) old;
		return holder.clazz != HibernateProxyHelper.getClass(current) || 
			identifierType.isModified(holder.id, id(current, session), session);
	}

	public Class getAssociatedClass(SessionFactoryImplementor factory)
		throws MappingException {
		throw new UnsupportedOperationException("any types do not have a unique referenced persister");
	}

	public boolean equals(Object object) {
		return this==object;
	}

	public int hashCode() {
		return System.identityHashCode(this);
	}
	
}
	
	
	
	
	
	
