//$Id: ComponentType.java,v 1.24 2004/08/06 02:40:38 oneovthafew Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.reflect.FastClass;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.InstantiationException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.PropertyAccessException;
import net.sf.hibernate.cfg.Environment;
import net.sf.hibernate.engine.Cascades;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.engine.SessionFactoryImplementor;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.property.Getter;
import net.sf.hibernate.property.PropertyAccessor;
import net.sf.hibernate.property.PropertyAccessorFactory;
import net.sf.hibernate.property.Setter;
import net.sf.hibernate.util.ArrayHelper;
import net.sf.hibernate.util.ReflectHelper;
import net.sf.hibernate.util.StringHelper;

/**
 * Handles "component" mappings
 * @author Gavin King
 */
public class ComponentType extends AbstractType implements AbstractComponentType {

	private final Class componentClass;
	private final Constructor constructor;
	private final Type[] propertyTypes;
	private final Getter[] getters;
	private final Setter[] setters;
	private final String[] propertyNames;
	private final int propertySpan;
	private final Cascades.CascadeStyle[] cascade;
	private final int[] joinedFetch;
	private final Setter parentSetter;
	private final Getter parentGetter;
	private final BulkBean optimizer;
	private final FastClass fastClass;
	
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
	
	public int getColumnSpan(Mapping mapping) throws MappingException {
		int span = 0;
		for ( int i=0; i<propertySpan; i++ ) {
			span += propertyTypes[i].getColumnSpan(mapping);
		}
		return span;
	}
	
	public ComponentType(
		Class componentClass,
		String[] propertyNames,
		Getter[] propertyGetters,
		Setter[] propertySetters,
		final boolean foundCustomAccessor,
		Type[] propertyTypes,
		int[] joinedFetch,
		Cascades.CascadeStyle[] cascade,
		String parentProperty
	) throws MappingException {

		this.componentClass = componentClass;
		this.propertyTypes = propertyTypes;
		getters = propertyGetters;
		setters = propertySetters;
		propertySpan = propertyNames.length;
		String[] getterNames = new String[propertySpan];
		String[] setterNames = new String[propertySpan];
		Class[] propTypes = new Class[propertySpan];
		for ( int i=0; i<propertySpan; i++ ) {
			getterNames[i] = getters[i].getMethodName();
			setterNames[i] = setters[i].getMethodName();
			propTypes[i] = getters[i].getReturnType();
		}
		if (parentProperty==null) {
			parentSetter=null;
			parentGetter=null;
		}
		else {
			PropertyAccessor pa = PropertyAccessorFactory.getPropertyAccessor(null);
			parentSetter = pa.getSetter(componentClass, parentProperty);
			parentGetter = pa.getGetter(componentClass, parentProperty);
		}
		this.propertyNames = propertyNames;
		this.cascade = cascade;
		this.joinedFetch = joinedFetch;
		constructor = ReflectHelper.getDefaultConstructor(componentClass);
		fastClass = ReflectHelper.getFastClass(componentClass);
		optimizer = !foundCustomAccessor && Environment.useReflectionOptimizer() ? 
			ReflectHelper.getBulkBean(componentClass, getterNames, setterNames, propTypes, fastClass) :
			null;
	}
	
	public final boolean isComponentType() {
		return true;
	}
	
	public Class getReturnedClass() {
		return componentClass;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x==y) return true;
		if (x==null || y==null) return false;
		for ( int i=0; i<propertySpan; i++ ) {
			if ( !propertyTypes[i].equals( getters[i].get(x), getters[i].get(y) ) ) return false;
		}
		return true;
	}
	
	public boolean isDirty(Object x, Object y, SessionImplementor session) throws HibernateException {
		if (x==y) return false;
		if (x==null || y==null) return true;
		for ( int i=0; i<getters.length; i++ ) {
			if ( propertyTypes[i].isDirty( getters[i].get(x), getters[i].get(y), session ) ) return true;
		}
		return false;
	}
	
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
	throws HibernateException, SQLException {
		
		return resolveIdentifier( hydrate(rs, names, session, owner), session, owner );
	}
	
	public void nullSafeSet(PreparedStatement st, Object value, int begin, SessionImplementor session)
	throws HibernateException, SQLException {
		
		Object[] subvalues = nullSafeGetValues(value);
		
		for ( int i=0; i<propertySpan; i++ ) {
			propertyTypes[i].nullSafeSet(st, subvalues[i], begin, session);
			begin += propertyTypes[i].getColumnSpan( session.getFactory() );
		}
	}
	
	private Object[] nullSafeGetValues(Object value) throws HibernateException {
		if ( value==null ) {
			return new Object[propertySpan];
		}
		else {
			return getPropertyValues(value);
		}
	}
	
	public Object nullSafeGet(ResultSet rs, String name, SessionImplementor session, Object owner)
	throws HibernateException, SQLException {
		
		return nullSafeGet(rs, new String[] {name}, session, owner);
	}
	
	public Object getPropertyValue(Object component, int i, SessionImplementor session) throws HibernateException {
		return getPropertyValue(component, i);
	}
	
	public Object getPropertyValue(Object component, int i) throws HibernateException {
		return getters[i].get(component);
	}
	
	public Object[] getPropertyValues(Object component, SessionImplementor session) throws HibernateException {
		return getPropertyValues(component);
	}
	
	public Object[] getPropertyValues(Object component) throws HibernateException {
		
		if (optimizer!=null) {
			try {
				return optimizer.getPropertyValues(component);
			}
			catch (Throwable t){
				throw new PropertyAccessException(
					t, 
					ReflectHelper.PROPERTY_ACCESS_EXCEPTION, 
					false, 
					componentClass, 
					ReflectHelper.getPropertyName(t, optimizer)
				);
			}
		}
		else {
			Object[] values = new Object[propertySpan];
			for ( int i=0; i<propertySpan; i++ ) {
				values[i] = getPropertyValue(component, i);
			}
			return values;
		}
	}
	
	public void setPropertyValues(Object component, Object[] values) throws HibernateException {

		if (optimizer!=null) {
			try {
				optimizer.setPropertyValues(component, values);
				return;
			}
			catch (Throwable t) {
				throw new PropertyAccessException(
					t, 
					ReflectHelper.PROPERTY_ACCESS_EXCEPTION, 
					true, 
					componentClass, 
					ReflectHelper.getPropertyName(t, optimizer)
				);
			}
		}
		else {
			for ( int i=0; i<propertySpan; i++ ) {
				setters[i].set( component, values[i] );
			}
		}
	}
	
	public Type[] getSubtypes() {
		return propertyTypes;
	}
	
	public String getName() { 
		return componentClass.getName();
	}
	
	public String toString(Object value, SessionFactoryImplementor factory) throws HibernateException {
		if (value==null) return "null";
		Map result = new HashMap();
		Object[] values = getPropertyValues(value);
		for ( int i=0; i<propertyTypes.length; i++ ) {
			result.put( propertyNames[i], propertyTypes[i].toString( values[i], factory ) );
		}
		return StringHelper.unqualify( getName() ) + result.toString();
	}
	
	public Object fromString(String xml) throws HibernateException {
		throw new UnsupportedOperationException();
	}
	
	public String[] getPropertyNames() {
		return propertyNames;
	}
	
	public Object deepCopy(Object component) throws HibernateException {
		if (component==null) return null;
		
		Object[] values = getPropertyValues(component);
		for ( int i=0; i<propertySpan; i++ ) {
			values[i] = propertyTypes[i].deepCopy( values[i] );
		}
		
		Object result = instantiate();
		setPropertyValues(result, values);
		
		//not absolutely necessary, but helps for some
		//equals()/hashCode() implementations
		if (parentGetter!=null) {
			parentSetter.set( result, parentGetter.get(component) );
		}
		
		return result;
	}
	
	public Object copy(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
		if (original==null) return null;
		if (original==target) return target;
		
		final Object result = target==null ? 
			instantiate(owner, session) : 
			target;
		
		Object[] values = TypeFactory.copy(
			getPropertyValues(original), getPropertyValues(result), propertyTypes, session, owner
		);
		
		setPropertyValues(result, values);
		return result;
	}
	
	/**
	 * This method does not populate the component parent
	 */
	private Object instantiate() throws HibernateException {
		if(optimizer!=null) {
			try {
				return fastClass.newInstance();
			}
			catch(Throwable t) {
				throw new InstantiationException("Could not instantiate component with CGLIB: ", componentClass, t);
			}
		}
		else {
			try {
				return constructor.newInstance(null);
			}
			catch (Exception e) {
				throw new InstantiationException("Could not instantiate component: ", componentClass, e);
			}
		}
	}
	
	public Object instantiate(Object parent, SessionImplementor session) throws HibernateException {
		Object result = instantiate();
		if (parentSetter!=null && parent!=null) parentSetter.set( result, session.proxyFor(parent) );
		return result;
	}
	
	public Cascades.CascadeStyle cascade(int i) {
		return cascade[i];
	}
	
	public boolean isMutable() {
		return true;
	}
	
	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		
		if (value==null) {
			return null;
		}
		else {
			Object[] values = getPropertyValues(value);
			for ( int i=0; i<propertyTypes.length; i++ ) {
				values[i] = propertyTypes[i].disassemble(values[i], session);
			}
			return values;
		}
	}
	
	public Object assemble(Serializable object, SessionImplementor session, Object owner) 
	throws HibernateException {
		
		if ( object==null ) {
			return null;
		}
		else  {
			Object[] values = (Object[]) object;
			Object[] assembled = new Object[values.length];
			for ( int i=0; i<propertyTypes.length; i++ ) {
				assembled[i] = propertyTypes[i].assemble( (Serializable) values[i], session, owner );
			}
			Object result = instantiate(owner, session);
			setPropertyValues(result, assembled);
			return result;
		}
	}
	
	public boolean hasNiceEquals() {
		return false;
	}
	
	public int enableJoinedFetch(int i) {
		return joinedFetch[i];
	}

	public Object hydrate(
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
			Object val = propertyTypes[i].hydrate(rs, range, session, owner);
			if (val!=null) notNull=true;
			values[i] = val;
			begin+=length;
		}
	
		return notNull ? values : null;
	}

	public Object resolveIdentifier(
		Object value,
		SessionImplementor session,
		Object owner)
		throws HibernateException {

		if (value!=null) {
			Object result = instantiate(owner, session);
			Object[] values = (Object[]) value;
			for (int i=0; i<values.length; i++) values[i] = propertyTypes[i].resolveIdentifier( values[i], session, owner );
			setPropertyValues(result, values);
			return result;
		}
		else {
			return null;
		}
	}


	public boolean isModified(Object old, Object current, SessionImplementor session)
	throws HibernateException {
		if (current==null) return old!=null;
		if (old==null) return current!=null;
		Object[] currentValues = getPropertyValues(current, session);
		Object[] oldValues = (Object[]) old;
		for ( int i=0; i<currentValues.length; i++ ) {
			if ( propertyTypes[i].isModified(oldValues[i], currentValues[i], session) ) return true;
		}
		return false;
	}

	public boolean equals(Object object) {
		return this==object;
	}
	
	public int hashCode() {
		return System.identityHashCode(this);
	}

}







