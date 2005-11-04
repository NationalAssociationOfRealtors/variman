//$Id: ArrayType.java,v 1.15 2004/07/27 20:12:44 oneovthafew Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.collection.ArrayHolder;
import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * A type for persistent arrays.
 * @author Gavin King
 */
public class ArrayType extends PersistentCollectionType {
	
	private final Class elementClass;
	private final Class arrayClass;
	
	public ArrayType(String role, Class elementClass) {
		super(role);
		this.elementClass = elementClass;
		arrayClass = Array.newInstance(elementClass, 0).getClass();
	}
	
	public Class getReturnedClass() {
		return arrayClass;
	}
	
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) throws HibernateException {
		return new ArrayHolder(session, persister);
	}
	
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
	throws HibernateException, SQLException {
		super.nullSafeSet( st, session.getArrayHolder(value), index, session );
	}
	
	/**
	 * Not defined for collections of primitive type
	 */
	public Iterator getElementsIterator(Object collection) {
		return Arrays.asList( (Object[]) collection ).iterator();
	}
	
	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		
		if (value==null) {
			return null;
		}
		else {
			return session.getLoadedCollectionKey( session.getArrayHolder(value) );
		}
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object array) {
		return new ArrayHolder(session, array);
	}
	
	public boolean isArrayType() {
		return true;
	}

	public String toString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		if (value==null) return "null";
		int length = Array.getLength(value);
		List list = new ArrayList(length);
		Type elemType = getElementType(factory);
		for ( int i=0; i<length; i++ ) {
			list.add( elemType.toString( Array.get(value, i), factory ) );
		}
		return list.toString();
	}

	public Object copy(
		Object original,
		Object target,
		SessionImplementor session, 
		Object owner)
	throws HibernateException {
		if (original==null) return null;
		if (original==target) return target;
		int length = Array.getLength(original);
		Type elemType = getElementType( session.getFactory() );
		Object result = Array.newInstance(elementClass, length);
		for ( int i=0; i<length; i++ ) {
			Array.set( result, i, elemType.copy( Array.get(original, i), null, session, owner ) );
		}
		return result;
	}
	
}
