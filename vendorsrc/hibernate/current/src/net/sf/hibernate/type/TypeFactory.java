//$Id: TypeFactory.java,v 1.16 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import net.sf.hibernate.CompositeUserType;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Lifecycle;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.PersistentEnum;
import net.sf.hibernate.UserType;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.util.ReflectHelper;

/**
 * Used internally to obtain instances of <tt>Type</tt>. Applications should
 * use static methods and constants on <tt>net.sf.hibernate.Hibernate</tt>.
 * 
 * @see net.sf.hibernate.Hibernate
 * @author Gavin King
 */
public final class TypeFactory {
	
	private static final Map BASIC_TYPES;
	
	static {
		HashMap basics = new HashMap();
		basics.put( boolean.class.getName(), Hibernate.BOOLEAN);
		basics.put( long.class.getName(), Hibernate.LONG);
		basics.put( short.class.getName(), Hibernate.SHORT);
		basics.put( int.class.getName(), Hibernate.INTEGER);
		basics.put( byte.class.getName(), Hibernate.BYTE);
		basics.put( float.class.getName(), Hibernate.FLOAT);
		basics.put( double.class.getName(), Hibernate.DOUBLE);
		basics.put( char.class.getName(), Hibernate.CHARACTER);
		basics.put( Hibernate.CHARACTER.getName(), Hibernate.CHARACTER);
		basics.put( Hibernate.INTEGER.getName(), Hibernate.INTEGER);
		basics.put( Hibernate.STRING.getName(), Hibernate.STRING);
		basics.put( Hibernate.DATE.getName(), Hibernate.DATE);
		basics.put( Hibernate.TIME.getName(), Hibernate.TIME);
		basics.put( Hibernate.TIMESTAMP.getName(), Hibernate.TIMESTAMP);
		basics.put( Hibernate.LOCALE.getName(), Hibernate.LOCALE);
		basics.put( Hibernate.CALENDAR.getName(), Hibernate.CALENDAR);
		basics.put( Hibernate.CALENDAR_DATE.getName(), Hibernate.CALENDAR_DATE);
		basics.put( Hibernate.CURRENCY.getName(), Hibernate.CURRENCY);
		basics.put( Hibernate.TIMEZONE.getName(), Hibernate.TIMEZONE);
		basics.put( Hibernate.CLASS.getName(), Hibernate.CLASS);
		basics.put( Hibernate.TRUE_FALSE.getName(), Hibernate.TRUE_FALSE);
		basics.put( Hibernate.YES_NO.getName(), Hibernate.YES_NO);
		basics.put( Hibernate.BINARY.getName(), Hibernate.BINARY);
		basics.put( Hibernate.TEXT.getName(), Hibernate.TEXT);
		basics.put( Hibernate.BLOB.getName(), Hibernate.BLOB);
		basics.put( Hibernate.CLOB.getName(), Hibernate.CLOB);
		basics.put( Hibernate.BIG_DECIMAL.getName(), Hibernate.BIG_DECIMAL);
		basics.put( Hibernate.SERIALIZABLE.getName(), Hibernate.SERIALIZABLE);
		basics.put( Hibernate.OBJECT.getName(), Hibernate.OBJECT);
		basics.put( Boolean.class.getName(), Hibernate.BOOLEAN);
		basics.put( Long.class.getName(), Hibernate.LONG);
		basics.put( Short.class.getName(), Hibernate.SHORT);
		basics.put( Integer.class.getName(), Hibernate.INTEGER);
		basics.put( Byte.class.getName(), Hibernate.BYTE);
		basics.put( Float.class.getName(), Hibernate.FLOAT);
		basics.put( Double.class.getName(), Hibernate.DOUBLE);
		basics.put( Character.class.getName(), Hibernate.CHARACTER);
		basics.put( String.class.getName(), Hibernate.STRING);
		basics.put( java.util.Date.class.getName(), Hibernate.TIMESTAMP);
		basics.put( Time.class.getName(), Hibernate.TIME);
		basics.put( Timestamp.class.getName(), Hibernate.TIMESTAMP);
		basics.put( java.sql.Date.class.getName(), Hibernate.DATE);
		basics.put( BigDecimal.class.getName(), Hibernate.BIG_DECIMAL);
		basics.put( Locale.class.getName(), Hibernate.LOCALE);
		basics.put( Calendar.class.getName(), Hibernate.CALENDAR);
		basics.put( GregorianCalendar.class.getName(), Hibernate.CALENDAR);
		if ( CurrencyType.CURRENCY_CLASS!=null) basics.put( CurrencyType.CURRENCY_CLASS.getName(), Hibernate.CURRENCY);
		basics.put( TimeZone.class.getName(), Hibernate.TIMEZONE);
		basics.put( Object.class.getName(), Hibernate.OBJECT);
		basics.put( Class.class.getName(), Hibernate.CLASS);
		basics.put( byte[].class.getName(), Hibernate.BINARY);
		basics.put( Blob.class.getName(), Hibernate.BLOB);
		basics.put( Clob.class.getName(), Hibernate.CLOB);
		basics.put( Serializable.class.getName(), Hibernate.SERIALIZABLE);
		BASIC_TYPES = Collections.unmodifiableMap(basics);
	}
	
	private TypeFactory() { throw new UnsupportedOperationException(); }
	
	/**
	 * A one-to-one association type for the given class
	 */
	public static Type oneToOne(Class persistentClass, ForeignKeyDirection foreignKeyType) {
		return oneToOne(persistentClass, foreignKeyType, null);
	}	
	/**
	 * A one-to-one association type for the given class
	 */
	public static Type oneToOne(Class persistentClass, ForeignKeyDirection foreignKeyType, String uniqueKeyPropertyName) {
		return new OneToOneType(persistentClass, foreignKeyType, uniqueKeyPropertyName);
	}
	
	/**
	 * A many-to-one association type for the given class
	 */
	public static Type manyToOne(Class persistentClass) {
		return manyToOne(persistentClass, null);
	}
	/**
	 * A many-to-one association type for the given class
	 */
	public static Type manyToOne(Class persistentClass, String uniqueKeyPropertyName) {
		return new ManyToOneType(persistentClass, uniqueKeyPropertyName);
	}

	/**
	 * Given the name of a Hibernate basic type, return an instance
	 * of <tt>net.sf.hibernate.type.Type</tt>.
	 */
	public static Type basic(String name) {
		return (Type) BASIC_TYPES.get(name);
	}
	
	/**
	 * Uses heuristics to deduce a Hibernate type given a string naming
	 * the type or Java class. Return an instance of
	 * <tt>net.sf.hibernate.type.Type</tt>.
	 */
	public static Type heuristicType(String typeName) throws MappingException {
		Type type = TypeFactory.basic(typeName);
		if (type==null) {
			Class typeClass;
			try {
				typeClass = ReflectHelper.classForName(typeName);
			}
			catch (ClassNotFoundException cnfe) {
				typeClass = null;
			}
			if (typeClass!=null) {
				if ( Type.class.isAssignableFrom(typeClass) ) {
					try {
						type = (Type) typeClass.newInstance();
					}
					catch (Exception e) {
						throw new MappingException("Could not instantiate Type " + typeClass.getName() + ": " + e);
					}
				}
				else if ( CompositeUserType.class.isAssignableFrom(typeClass) ) {
					type = new CompositeCustomType(typeClass);
				}
				else if ( UserType.class.isAssignableFrom(typeClass) ) {
					type = new CustomType(typeClass);
				}
				else if ( Lifecycle.class.isAssignableFrom(typeClass) ) {
					type = Hibernate.entity(typeClass);
				}
				else if ( PersistentEnum.class.isAssignableFrom(typeClass) ) {
					type = Hibernate.enum(typeClass);
				}
				else if  ( Serializable.class.isAssignableFrom(typeClass) ) {
					type = Hibernate.serializable(typeClass);
				}
			}
		}
		return type;
		
	}
	
	// Collection Types:
	
	public static PersistentCollectionType array(String role, Class elementClass) {
		return new ArrayType(role, elementClass);
	}
	public static PersistentCollectionType list(String role) {
		return new ListType(role);
	}
	public static PersistentCollectionType bag(String role) {
		return new BagType(role);
	}
	public static PersistentCollectionType idbag(String role) {
		return new IdentifierBagType(role);
	}
	public static PersistentCollectionType map(String role) {
		return new MapType(role);
	}
	public static PersistentCollectionType set(String role) {
		return new SetType(role);
	}
	public static PersistentCollectionType sortedMap(String role, Comparator comparator) {
		return new SortedMapType(role, comparator);
	}
	public static PersistentCollectionType sortedSet(String role, Comparator comparator) {
		return new SortedSetType(role, comparator);
	}
	
	/**
	 * Deep copy values in the first array into the second
	 */
	public static void deepCopy(Object[] values, Type[] types, boolean[] copy, Object[] target) throws HibernateException {
		for ( int i=0; i<types.length; i++ ) {
			if ( copy[i] ) target[i] = types[i].deepCopy( values[i] );
		}
	}
	
	/**
	 * Determine if any of the given field values are dirty, returning an array containing indexes of
	 * the dirty fields or <tt>null</tt> if no fields are dirty.
	 */
	public static int[] findDirty(Type[] types, Object[] x, Object[] y, boolean[] check, SessionImplementor session) 
	throws HibernateException {
		int[] results = null;
		int count = 0;
		for (int i=0; i<types.length; i++) {
			if ( check[i] && types[i].isDirty( x[i], y[i], session ) ) {
				if (results==null) results = new int[ types.length ];
				results[count++]=i;
			}
		}
		if (count==0) {
			return null;
		}
		else {
			int[] trimmed = new int[count];
			System.arraycopy(results, 0, trimmed, 0, count);
			return trimmed;
		}
	}
	
	/**
	 * Determine if any of the given field values are modified, returning an array containing indexes of
	 * the dirty fields or <tt>null</tt> if no fields are dirty.
	 */
	public static int[] findModified(Type[] types, Object[] old, Object[] current, boolean[] check, SessionImplementor session) 
	throws HibernateException {
		int[] results = null;
		int count = 0;
		for (int i=0; i<types.length; i++) {
			if ( check[i] && types[i].isModified( old[i], current[i], session ) ) {
				if (results==null) results = new int[ types.length ];
				results[count++]=i;
			}
		}
		if (count==0) {
			return null;
		}
		else {
			int[] trimmed = new int[count];
			System.arraycopy(results, 0, trimmed, 0, count);
			return trimmed;
		}
	}

	public static Object[] assemble(Serializable[] row, Type[] types, SessionImplementor session, Object owner) throws HibernateException {
		Object[] assembled = new Object[row.length];
		for ( int i=0; i<types.length; i++ ) {
			assembled[i] = types[i].assemble(row[i], session, owner);
		}
		return assembled;
	}

	public static Serializable[] disassemble(Object[] row, Type[] types, SessionImplementor session) throws HibernateException {
		Serializable[] disassembled = new Serializable[row.length];
		for ( int i=0; i<types.length; i++ ) {
			disassembled[i] = types[i].disassemble(row[i], session);
		}
		return disassembled;
	}
	
	public static Object[] copy(Object[] original, Object[] target, Type[] types, SessionImplementor session, Object owner) 
	throws HibernateException {
		Object[] copied = new Object[original.length];
		for ( int i=0; i<types.length; i++ ) {
			copied[i] = types[i].copy( original[i], target[i], session, owner );
		}
		return copied;
	}
	
	/**
	 * Return <tt>-1</tt> if non-dirty, or the index of the first dirty value otherwise
	 */
	/*public static int findDirty(Type[] types, Object[] x, Object[] y, Object owner, SessionFactoryImplementor factory) throws HibernateException {
		for (int i=0; i<types.length; i++) {
			if ( types[i].isDirty( x[i], y[i], owner, factory ) ) {
				return i;
			}
		}
		return -1;
	}*/
	
}







