//$Id: PersistentEnumType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.PersistentEnum;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.util.ReflectHelper;

/**
 * A type for Hibernate <tt>PersistentEnum</tt>
 * @see net.sf.hibernate.PersistentEnum
 * @author Gavin King
 * @deprecated Support for PersistentEnums will be removed in 2.2
 */
public class PersistentEnumType extends ImmutableType implements LiteralType {
	
	private static final Class[] INTEGER_ARG = new Class[] { int.class };
	private final Class enumClass;
	private final Method method;
	
	public PersistentEnumType(Class enumClass) throws MappingException {
		this.enumClass = enumClass;
		try {
			method = enumClass.getDeclaredMethod("fromInt", INTEGER_ARG);
			if ( !ReflectHelper.isPublic(enumClass, method) ) method.setAccessible(true);
		}
		catch (NoSuchMethodException nme) {
			throw new MappingException("PersistentEnum class did not implement fromInt(int): " + enumClass.getName() );
		}
	}
	
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		int code = rs.getInt(name);
		if ( rs.wasNull() ) {
			return null;
		}
		else {
			return getInstance( new Integer(code) );
		}
	}
	
	public Object getInstance(Integer code) throws HibernateException {
		try {
			return method.invoke( null, new Object[] { code } );
		}
		catch (IllegalArgumentException iae) {
			throw new AssertionFailure("Could not invoke fromInt() from PersistentEnumType", iae);
		}
		catch (InvocationTargetException ite) {
			throw new HibernateException( "InvocationTargetException occurred inside fromInt()", ite );
		}
		catch (IllegalAccessException iae) {
			throw new HibernateException( "IllegalAccessException occurred calling fromInt()", iae );
		}
	}
	
	
	public boolean equals(Object x, Object y) {
		return (x==y) || ( x!=null && y!=null && x.getClass()==y.getClass() && ( (PersistentEnum) x ).toInt()==( (PersistentEnum) y ).toInt() );
	}
	
	public Class getReturnedClass() {
		return enumClass;
	}
	
	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		
		if (value==null) {
			st.setNull( index, Types.SMALLINT );
		}
		else {
			st.setInt( index, ( (PersistentEnum) value ).toInt() );
		}
	}
	
	public int sqlType() {
		return Types.SMALLINT;
	}
	public String getName() { return enumClass.getName(); }
	
	public String toString(Object value) {
		return Integer.toString( ( (PersistentEnum) value ).toInt() );
	}
	public Object fromStringValue(String xml) throws HibernateException {
		return getInstance( new Integer(xml) );
	}
	public Object assemble(Serializable cached,	SessionImplementor session, Object owner)
	throws HibernateException {
		if (cached==null) {
			return null;
		}
		else {
			return getInstance( (Integer) cached );
		}
	}
	
	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		return (value==null) ? null : new Integer( ( (PersistentEnum) value ).toInt() );
	}
	
	
	public String objectToSQLString(Object value) throws Exception {
		return Integer.toString( ( (PersistentEnum) value ).toInt() );
	}
	
	public boolean equals(Object object) {
		if ( !super.equals(object) ) return false;
		return ( (PersistentEnumType) object ).enumClass==enumClass;
	}
	
	public int hashCode() {
		return enumClass.hashCode();
	}
	
}





