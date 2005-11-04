//$Id: CustomType.java,v 1.13 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.UserType;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * Adapts <tt>UserType</tt> to the generic <tt>Type</tt> interface.
 *
 * @see net.sf.hibernate.UserType
 * @author Gavin King
 */
public class CustomType extends AbstractType {
	
	private final UserType userType;
	private final String name;
	private final int[] types;
	
	public CustomType(Class userTypeClass) throws MappingException {
		
		name = userTypeClass.getName();

		try {
			userType = (UserType) userTypeClass.newInstance();
		}
		catch (InstantiationException ie) {
			throw new MappingException( "Cannot instantiate custom type: " + userTypeClass.getName() );
		}
		catch (IllegalAccessException iae) {
			throw new MappingException( "IllegalAccessException trying to instantiate custom type: " + userTypeClass.getName() );
		}
		catch (ClassCastException cce) {
			throw new MappingException( userTypeClass.getName() + " must implement net.sf.hibernate.UserType" );
		}
		types = userType.sqlTypes();

		if ( !Serializable.class.isAssignableFrom( userType.returnedClass() ) ) {
			LogFactory.getLog(CustomType.class).warn("custom type does not implement Serializable: " + userTypeClass);
		}
		
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#sqlTypes(Mapping)
	 */
	public int[] sqlTypes(Mapping pi) {
		return types;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#getColumnSpan(Mapping)
	 */
	public int getColumnSpan(Mapping session) {
		return types.length;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#getReturnedClass()
	 */
	public Class getReturnedClass() {
		return userType.returnedClass();
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#equals(Object, Object)
	 */
	public boolean equals(Object x, Object y) throws HibernateException {
		return userType.equals(x, y);
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#nullSafeGet(ResultSet, String[], SessionImplementor, Object)
	 */
	public Object nullSafeGet(
		ResultSet rs,
		String[] names,
		SessionImplementor session,
		Object owner
	) throws HibernateException, SQLException {
			
		return userType.nullSafeGet(rs, names, owner);
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#nullSafeGet(ResultSet, String, SessionImplementor, Object)
	 */
	public Object nullSafeGet(
		ResultSet rs,
		String columnName,
		SessionImplementor session,
		Object owner
	) throws HibernateException, SQLException {
			
		return nullSafeGet(rs, new String[] { columnName }, session, owner);
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#nullSafeSet(PreparedStatement, Object, int, SessionImplementor)
	 */
	public void nullSafeSet(
		PreparedStatement st,
		Object value,
		int index,
		SessionImplementor session
	) throws HibernateException, SQLException {
			
			userType.nullSafeSet(st, value, index);
	}
	
	/**
	 */
	public String toString(Object value, SessionFactoryImplementor factory) {
		return value==null ? "null" : value.toString();
	}
	
	public Object fromString(String xml) {
		throw new UnsupportedOperationException("not yet implemented!"); //TODO: look for constructor
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#deepCopy(Object)
	 */
	public Object deepCopy(Object value) throws HibernateException {
		return userType.deepCopy(value);
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#isMutable()
	 */
	public boolean isMutable() {
		return userType.isMutable();
	}
	
	public boolean hasNiceEquals() {
		return false;
	}

	public boolean equals(Object object) {
		if ( !super.equals(object) ) return false;
		return ( (CustomType) object ).userType.getClass()==userType.getClass();
	}

	public int hashCode() {
		return userType.hashCode();
	}
			
}






