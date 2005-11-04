//$Id: CurrencyType.java,v 1.9 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.util.EqualsHelper;

/**
 * <tt>currency</tt>: A type that maps an SQL VARCHAR to a 
 * <tt>java.util.Currency</tt>
 * @see java.util.Currency
 * @author Gavin King
 */
public class CurrencyType extends ImmutableType implements LiteralType {
	
	public static final Class CURRENCY_CLASS;
	private static final Method CURRENCY_GET_INSTANCE;
	private static final Method CURRENCY_GET_CODE;
	
	static {
		Class clazz;
		try {
			clazz = Class.forName("java.util.Currency");
		}
		catch (ClassNotFoundException cnfe) {
			clazz = null;
		}
		if (clazz==null) {
			CURRENCY_CLASS = null;
			CURRENCY_GET_INSTANCE = null;
			CURRENCY_GET_CODE = null;
		}
		else {
			CURRENCY_CLASS = clazz;
			try {
				CURRENCY_GET_INSTANCE = clazz.getMethod("getInstance", new Class[] { String.class } );
				CURRENCY_GET_CODE = clazz.getMethod("getCurrencyCode", new Class[0] );
			}
			catch (Exception e) {
				throw new AssertionFailure("Exception in static initializer of CurrencyType", e);
			}
		}
	}
	
	/**
	 * @see net.sf.hibernate.type.NullableType#get(ResultSet, String)
	 */
	public Object get(ResultSet rs, String name)
	throws HibernateException, SQLException {
		String code = (String) Hibernate.STRING.nullSafeGet(rs, name);
		try {
			return (code==null) ? null : CURRENCY_GET_INSTANCE.invoke(null, new Object[] { code } );
		}
		catch (Exception e) {
			throw new HibernateException("Could not resolve currency code: " + code);
		}
	}
	
	/**
	 * @see net.sf.hibernate.type.NullableType#set(PreparedStatement, Object, int)
	 */
	public void set(PreparedStatement st, Object value, int index)
	throws HibernateException, SQLException {
		Object code;
		try {
			code = CURRENCY_GET_CODE.invoke(value, null);
		}
		catch (Exception e) {
			throw new HibernateException("Could not get Currency code", e);
		}
		Hibernate.STRING.set(st, code, index);
	}
	
	/**
	 * @see net.sf.hibernate.type.NullableType#sqlType()
	 */
	public int sqlType() {
		return Hibernate.STRING.sqlType();
	}
	
	/**
	 */
	public String toString(Object value) throws HibernateException {
		try {
			return (String) CURRENCY_GET_CODE.invoke(value, null);
		}
		catch (Exception e) {
			throw new HibernateException("Could not get Currency code", e);
		}
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#getReturnedClass()
	 */
	public Class getReturnedClass() {
		return CURRENCY_CLASS;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#equals(Object, Object)
	 */
	public boolean equals(Object x, Object y) throws HibernateException {
		return EqualsHelper.equals(x, y);
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#getName()
	 */
	public String getName() {
		return "currency";
	}
	
	/**
	 * @see net.sf.hibernate.type.LiteralType#objectToSQLString(Object)
	 */
	public String objectToSQLString(Object value) throws Exception {
		String code;
		try {
			code = (String) CURRENCY_GET_CODE.invoke(value, null);
		}
		catch (Exception e) {
			throw new HibernateException("Could not get Currency code", e);
		}
		return ( (LiteralType) Hibernate.STRING ).objectToSQLString(code);
	}
	
	public Object fromStringValue(String xml) throws HibernateException {
		try {
			return CURRENCY_GET_INSTANCE.invoke( null, new Object[] {xml} );
		}
		catch (Exception e) {
			throw new HibernateException("Could not resolve currency code: " + xml);
		}
	}

}






