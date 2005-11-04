//$Id: DynamicComponentType.java,v 1.6 2004/06/04 05:43:49 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.engine.Cascades;
import net.sf.hibernate.util.ArrayHelper;

/**
 * Handles "dynamic" components, represented as <tt>Map</tt>s
 * @author Gavin King
 */
public class DynamicComponentType extends AbstractType implements AbstractComponentType {
	
	private String[] propertyNames;
	private Type[] propertyTypes;
	private int propertySpan;
	private final Cascades.CascadeStyle[] cascade;
	private final int[] joinedFetch;
	
	public DynamicComponentType(
		String[] propertyNames, 
		Type[] propertyTypes,
		int[] joinedFetch, 
		Cascades.CascadeStyle[] cascade 
	) {
		this.propertyNames = propertyNames;
		this.propertyTypes = propertyTypes;
		this.joinedFetch = joinedFetch;
		this.cascade = cascade;
		propertySpan = propertyTypes.length;
	}


	public Cascades.CascadeStyle cascade(int i) {
		return cascade[i];
	}

	public int enableJoinedFetch(int i) {
		return joinedFetch[i];
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public Object getPropertyValue(Object component, int i, SessionImplementor session) 
		throws HibernateException {
		return getPropertyValue(component, i);
	}

	public Object[] getPropertyValues(Object component, SessionImplementor session) 
		throws HibernateException {
		return getPropertyValues(component);
	}

	public Object getPropertyValue(Object component, int i)
		throws HibernateException {
		return ( (Map) component).get( propertyNames[i] );
	}

	public Object[] getPropertyValues(Object component) throws HibernateException {
			
		Map bean = (Map) component;
		Object[] result = new Object[propertySpan];
		for (int i=0; i<propertySpan; i++) {
			result[i] = bean.get( propertyNames[i] );
		}
		return result;
	}

	public Type[] getSubtypes() {
		return propertyTypes;
	}

	public Object instantiate() throws HibernateException {
		return new HashMap();
	}

	public void setPropertyValues(Object component, Object[] values)
		throws HibernateException {
			Map map = (Map) component;
			for (int i=0; i<propertySpan; i++) {
				map.put( propertyNames[i], values[i] );
			}		
	}

	public Object deepCopy(Object component) throws HibernateException {
		if (component==null) return null;
		
		Object[] values = getPropertyValues(component);
		for ( int i=0; i<propertySpan; i++ ) {
			values[i] = propertyTypes[i].deepCopy( values[i] );
		}
		Object result = instantiate();
		setPropertyValues(result, values);
		return result;
	}

	public Object copy(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
		if (original==null) return null;
		if (original==target) return target;
		
		Object[] values = TypeFactory.copy(
			getPropertyValues(original), getPropertyValues(target), propertyTypes, session, owner
		);
		
		Object result = target==null ? 
			instantiate() : 
			target;
		setPropertyValues(result, values);
		return result;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x==y) return true;
		if (x==null || y==null) return false;
		Map xbean = (Map) x;
		Map ybean = (Map) y;
		for ( int i=0; i<propertySpan; i++ ) {
			if ( !propertyTypes[i].equals( xbean.get( propertyNames[i] ), ybean.get( propertyNames[i] ) ) ) return false;
		}
		return true;
	}

	public boolean isDirty(Object x, Object y, SessionImplementor session) throws HibernateException {
		if (x==y) return false;
		if (x==null || y==null) return true;
		Map xbean = (Map) x;
		Map ybean = (Map) y;
		for ( int i=0; i<propertySpan; i++ ) {
			if ( propertyTypes[i].isDirty( xbean.get( propertyNames[i] ), ybean.get( propertyNames[i] ), session ) ) return true;
		}
		return false;
	}

	public int getColumnSpan(Mapping mapping) throws MappingException {
		int span = 0;
		for ( int i=0; i<propertySpan; i++ ) {
			span += propertyTypes[i].getColumnSpan(mapping);
		}
		return span;
	}

	public String getName() {
		//TODO:
		return Map.class.getName();
	}

	public boolean hasNiceEquals() {
		return false;
	}

	public boolean isMutable() {
		return true;
	}

	private Object[] nullSafeGetValues(Object value) throws HibernateException {
		if ( value==null ) {
			return new Object[propertySpan];
		}
		else {
			return getPropertyValues(value);
		}
	}
	
	public Object nullSafeGet(
		ResultSet rs,
		String name,
		SessionImplementor session,
		Object owner)
		throws HibernateException, SQLException {
		return nullSafeGet(rs, new String[] {name}, session, owner);
	}

	public Object nullSafeGet(
		ResultSet rs,
		String[] names,
		SessionImplementor session,
		Object owner)
		throws HibernateException, SQLException {

		int begin=0;
		boolean notNull=false;
		Object[] values = new Object[propertySpan];
		for ( int i=0; i<propertySpan; i++ ) {
			int length = propertyTypes[i].getColumnSpan( session.getFactory() );
			String[] range = ArrayHelper.slice(names, begin, length); //cache this
			Object val = propertyTypes[i].nullSafeGet(rs, range, session, owner);
			if (val!=null) notNull=true;
			values[i] = val;
			begin+=length;
		}
	
		if (notNull) {
			Map result = (Map) instantiate();
			for ( int i=0; i<propertySpan; i++ ) {
				result.put( propertyNames[i], values[i] );
			}
			return result;
		}
		else {
			return null;
		}
	}

	public void nullSafeSet(
		PreparedStatement st,
		Object value,
		int begin,
		SessionImplementor session)
		throws HibernateException, SQLException {
			
		Object[] subvalues = nullSafeGetValues(value);
		
		for ( int i=0; i<propertySpan; i++ ) {
			propertyTypes[i].nullSafeSet(st, subvalues[i], begin, session);
			begin += propertyTypes[i].getColumnSpan( session.getFactory() );
		}
	}

	public Class getReturnedClass() {
		return Map.class;
	}

	public int[] sqlTypes(Mapping mapping) throws MappingException {
		//Not called at runtime so doesn't matter if its slow :)
		int[] sqlTypes = new int[ getColumnSpan(mapping) ];
		int n=0;
		for ( int i=0; i<propertySpan; i++ ) {
			int[] subtypes = propertyTypes[i].sqlTypes(mapping);
			for ( int j=0; j<subtypes.length; j++ ) {
				sqlTypes[n++] = subtypes[j];
			}
		}
		return sqlTypes;
	}

	public String toString(Object value, SessionFactoryImplementor factory)
		throws HibernateException {
		return (value==null) ? "null" : value.toString();
	}

	public boolean equals(Object object) {
		return this==object;
	}
	public Object fromString(String xml) throws HibernateException {
		throw new UnsupportedOperationException();
	}

	public boolean isComponentType() {
		return true;
	}
	public int hashCode() {
		return System.identityHashCode(this);
	}
	

}
