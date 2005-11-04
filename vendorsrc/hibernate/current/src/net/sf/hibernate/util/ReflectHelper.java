//$Id: ReflectHelper.java,v 1.19 2004/08/06 02:40:38 oneovthafew Exp $
package net.sf.hibernate.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.beans.BulkBeanException;
import net.sf.cglib.reflect.FastClass;
import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.PropertyNotFoundException;
import net.sf.hibernate.property.BasicPropertyAccessor;
import net.sf.hibernate.property.DirectPropertyAccessor;
import net.sf.hibernate.property.Getter;
import net.sf.hibernate.property.PropertyAccessor;
import net.sf.hibernate.type.PrimitiveType;
import net.sf.hibernate.type.Type;
import net.sf.hibernate.type.TypeFactory;


public final class ReflectHelper {
	
	private static final Log log = LogFactory.getLog(ReflectHelper.class);
	//TODO: this dependency is kinda Bad
	private static final PropertyAccessor BASIC_PROPERTY_ACCESSOR = new BasicPropertyAccessor();
	private static final PropertyAccessor DIRECT_PROPERTY_ACCESSOR = new DirectPropertyAccessor();
	
	private static final Class[] NO_CLASSES = new Class[0];
	private static final Class[] OBJECT = new Class[] { Object.class };
	private static final Class[] NO_PARAM = new Class[] { };
	private static final Method OBJECT_EQUALS;
	private static final Method OBJECT_HASHCODE;
	
	static {
		Method eq;
		Method hash;
		try {
			eq = Object.class.getMethod("equals", OBJECT);
			hash = Object.class.getMethod("hashCode", NO_PARAM);
		}
		catch (Exception e) {
			throw new AssertionFailure("Could not find Object.equals() or Object.hashCode()", e);
		}
		OBJECT_EQUALS = eq;
		OBJECT_HASHCODE = hash;
	}
	
	public static boolean overridesEquals(Class clazz) {
		Method equals;
		try {
			equals = clazz.getMethod("equals", OBJECT);
		}
		catch (NoSuchMethodException nsme) {
			return false; //its an interface so we can't really tell anything...
		}
		return !OBJECT_EQUALS.equals(equals);
	}
	
	public static boolean overridesHashCode(Class clazz) {
		Method hashCode;
		try {
			hashCode = clazz.getMethod("hashCode", NO_PARAM);
		}
		catch (NoSuchMethodException nsme) {
			return false; //its an interface so we can't really tell anything...
		}
		return !OBJECT_HASHCODE.equals(hashCode);
	}

	private static Getter getter(Class clazz, String name) throws MappingException {
		try {
			return BASIC_PROPERTY_ACCESSOR.getGetter(clazz, name);
		}
		catch (PropertyNotFoundException pnfe) {
			return DIRECT_PROPERTY_ACCESSOR.getGetter(clazz, name);
		}
	}
	
	public static Type reflectedPropertyType(Class clazz, String name) throws MappingException {
		return TypeFactory.heuristicType( getter(clazz, name).getReturnType().getName() );
	}
	
	public static Class reflectedPropertyClass(Class clazz, String name) throws MappingException {
		return getter(clazz, name).getReturnType();
	}
	
	public static Getter getGetter(Class theClass, String name) throws MappingException {
		return BASIC_PROPERTY_ACCESSOR.getGetter(theClass, name);
	}
	
	public static Class classForName(String name) throws ClassNotFoundException {
		try {
			return Thread.currentThread().getContextClassLoader().loadClass(name);
		}
		catch (Exception e) {
			return Class.forName(name);
		}
	}
	
	public static boolean isPublic(Class clazz, Member member) {
		return Modifier.isPublic( member.getModifiers() ) && Modifier.isPublic( clazz.getModifiers() );
	}
	
	public static Object getConstantValue(String name) {
		Class clazz;
		try {
			clazz = classForName( StringHelper.qualifier(name) );
		}
		catch(ClassNotFoundException cnfe) {
			return null;
		}
		try {
			return clazz.getField( StringHelper.unqualify(name) ).get(null);
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static Constructor getDefaultConstructor(Class clazz) throws PropertyNotFoundException {
		
		if ( isAbstractClass(clazz) ) return null;
		
		try {
			Constructor constructor = clazz.getDeclaredConstructor(NO_CLASSES);
			if ( !isPublic(clazz, constructor) ) {
				constructor.setAccessible(true);
			}
			return constructor;
		}
		catch (NoSuchMethodException nme) {
			throw new PropertyNotFoundException(
				"Object class " + clazz.getName() +
				" must declare a default (no-argument) constructor"
			);
		}
		
	}
	
	public static boolean isAbstractClass(Class clazz) {
		int modifier = clazz.getModifiers();
		return Modifier.isAbstract(modifier) || Modifier.isInterface(modifier);
	}
	
	public static FastClass getFastClass(Class clazz) {
		try {
			return FastClass.create(clazz);
		}
		catch (Throwable t) {
			return null;
		}
	}

	public static BulkBean getBulkBean(Class clazz, String[] getterNames, String[] setterNames, Class[] types, FastClass fastClass) {
		try {
			BulkBean optimizer = BulkBean.create(clazz, getterNames, setterNames, types);
			if ( !clazz.isInterface() && !Modifier.isAbstract( clazz.getModifiers() ) ) {
				if (fastClass==null) return null;
				//test out the optimizer:
				Object instance = fastClass.newInstance();
				optimizer.setPropertyValues( instance, optimizer.getPropertyValues(instance) );
			}
			//if working:
			return optimizer;
		}
		catch (Throwable t) {
			String message =
			"reflection optimizer disabled for: " + 
			clazz.getName() + 
			", " + 
			StringHelper.unqualify( t.getClass().getName() ) + 
			": " + 
			t.getMessage();
			if (t instanceof BulkBeanException) {
				int index = ( (BulkBeanException) t ).getIndex();
				if (index >= 0) {
					message += " (property " + setterNames[index] + ")";
				}
			}
			log.info(message);
			return null;
		}
	}
	
	public static Constructor getConstructor(Class clazz, Type[] types) throws PropertyNotFoundException {
		final Constructor[] candidates = clazz.getConstructors();
		for ( int i=0; i<candidates.length; i++ ) {
			final Constructor constructor = candidates[i];
			final Class[] params = constructor.getParameterTypes();
			if ( params.length==types.length ) {
				boolean found = true;
				for ( int j=0; j<params.length; j++ ) {
					final boolean ok = params[j].isAssignableFrom( types[j].getReturnedClass() ) || (
						types[j] instanceof PrimitiveType &&
						params[j] == ( (PrimitiveType) types[j] ).getPrimitiveClass() 
					);
					if (!ok) {
						found = false;
						break;
					}
				}
				if (found) {
					if ( !isPublic(clazz, constructor) ) constructor.setAccessible(true);
					return constructor;
				}
			}
		}
		throw new PropertyNotFoundException( "no appropriate constructor in class: " + clazz.getName() );
	}

	public static String getPropertyName(Throwable t, BulkBean optimizer) {
		if (t instanceof BulkBeanException) {
			return optimizer.getSetters()[ ( (BulkBeanException) t ).getIndex() ];
		}
		else {
			return "?";
		}
	}

	private ReflectHelper() {}
	
	public static final String PROPERTY_ACCESS_EXCEPTION = 
		"exception setting property value with CGLIB (set hibernate.cglib.use_reflection_optimizer=false for more info)";
	
}






