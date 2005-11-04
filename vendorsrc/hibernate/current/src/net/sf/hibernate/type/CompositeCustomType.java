//$Id: CompositeCustomType.java,v 1.9 2004/06/04 05:43:49 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.CompositeUserType;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.engine.Cascades;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.engine.Cascades.CascadeStyle;
import net.sf.hibernate.loader.OuterJoinLoader;

/**
 * Adapts <tt>CompositeUserType</tt> to <tt>Type</tt> interface
 * @author Gavin King
 */
public class CompositeCustomType extends AbstractType
	implements AbstractComponentType {
	
	private final CompositeUserType userType;
	private final String name;

	public CompositeCustomType(Class userTypeClass) throws MappingException {
		name = userTypeClass.getName();
		
		try {
			userType = (CompositeUserType) userTypeClass.newInstance();
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
	
		if ( !Serializable.class.isAssignableFrom( userType.returnedClass() ) ) {
			LogFactory.getLog(CustomType.class).warn("custom type does not implement Serializable: " + userTypeClass);
		}
	}

	public Type[] getSubtypes() {
		return userType.getPropertyTypes();
	}

	public String[] getPropertyNames() {
		return userType.getPropertyNames();
	}

	public Object[] getPropertyValues(Object component, SessionImplementor session)
		throws HibernateException {
		return getPropertyValues(component);
	}
	
	public Object[] getPropertyValues(Object component)
		throws HibernateException {

		int len = getSubtypes().length;
		Object[] result = new Object[len];
		for ( int i=0; i<len; i++ ) {
			result[i] = getPropertyValue(component, i);
		}
		return result;
	}

	public void setPropertyValues(Object component, Object[] values)
		throws HibernateException {
		
		for (int i=0; i<values.length; i++) {
			userType.setPropertyValue( component, i, values[i] );
		}
	}

	public Object getPropertyValue(Object component, int i, SessionImplementor session)
		throws HibernateException {
		return getPropertyValue(component, i);
	}
	
	public Object getPropertyValue(Object component, int i)
		throws HibernateException {
		return userType.getPropertyValue(component, i);
	}

	public CascadeStyle cascade(int i) {
		return Cascades.STYLE_NONE;
	}

	public int enableJoinedFetch(int i) {
		return OuterJoinLoader.AUTO;
	}

	public boolean isComponentType() {
		return true;
	}

	public Object assemble(
		Serializable cached,
		SessionImplementor session,
		Object owner)
		throws HibernateException {

		return userType.assemble(cached, session, owner);
	}

	public Object deepCopy(Object value) throws HibernateException {
		return userType.deepCopy(value);
	}

	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		return userType.disassemble(value, session);
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		return userType.equals(x, y);
	}

	public int getColumnSpan(Mapping mapping) throws MappingException {
		Type[] types = userType.getPropertyTypes();
		int n=0;
		for (int i=0; i<types.length; i++) {
			n+=types[i].getColumnSpan(mapping);
		}
		return n;
	}

	public String getName() {
		return name;
	}

	public Class getReturnedClass() {
		return userType.returnedClass();
	}

	public boolean hasNiceEquals() {
		return false;
	}

	public boolean isMutable() {
		return userType.isMutable();
	}

	public Object nullSafeGet(
		ResultSet rs,
		String columnName,
		SessionImplementor session,
		Object owner)
		throws HibernateException, SQLException {

		return userType.nullSafeGet(rs, new String[] {columnName}, session, owner);
	}

	public Object nullSafeGet(
		ResultSet rs,
		String[] names,
		SessionImplementor session,
		Object owner)
		throws HibernateException, SQLException {

		return userType.nullSafeGet(rs, names, session, owner);
	}

	public void nullSafeSet(
		PreparedStatement st,
		Object value,
		int index,
		SessionImplementor session)
		throws HibernateException, SQLException {
		
		userType.nullSafeSet(st, value, index, session);

	}

	public int[] sqlTypes(Mapping mapping) throws MappingException {
		Type[] types = userType.getPropertyTypes();
		int[] result = new int[ getColumnSpan(mapping) ];
		int n=0;
		for (int i=0; i<types.length; i++) {
			int[] sqlTypes = types[i].sqlTypes(mapping);
			for ( int k=0; k<sqlTypes.length; k++ ) result[n++] = sqlTypes[k];
		}
		return result;
	}

	public String toString(Object value, SessionFactoryImplementor factory)
		throws HibernateException {
		
		return value==null ? "null" : value.toString();
	}

	public Object fromString(String xml) {
		throw new UnsupportedOperationException();
	}
	
	public boolean equals(Object object) {
		if ( !super.equals(object) ) return false;
		return ( (CompositeCustomType) object ).userType.getClass()==userType.getClass();
	}
	
	public int hashCode() {
		return userType.hashCode();
	}
	
}
