//$Id: SerializableType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.util.SerializationHelper;

/**
 * <tt>serializable</tt>: A type that maps an SQL VARBINARY to a 
 * serializable Java object.
 * @author Gavin King
 */
public class SerializableType extends MutableType {
	
	private final Class serializableClass;
	
	public SerializableType(Class serializableClass) {
		this.serializableClass = serializableClass;
	}
	
	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		Hibernate.BINARY.set(st, toBytes(value), index);
	}
	
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		
		byte[] bytes = (byte[]) Hibernate.BINARY.get(rs, name);
		if ( bytes==null ) {
			return null;
		}
		else {
			return fromBytes(bytes);
		}
	}
	
	public Class getReturnedClass() {
		return serializableClass;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x==y) return true;
		if (x==null || y==null) return false;
		return Hibernate.BINARY.equals( toBytes(x), toBytes(y) );
	}
	
	public String toString(Object value) throws HibernateException {
		return Hibernate.BINARY.toString( toBytes(value) );
	}
	
	public Object fromStringValue(String xml) throws HibernateException {
		return fromBytes( (byte[]) Hibernate.BINARY.fromString(xml) );
	}
	
	public String getName() {
		return (serializableClass==Serializable.class) ? "serializable" : serializableClass.getName();
	}
	
	public Object deepCopyNotNull(Object value) throws HibernateException {
		return fromBytes( toBytes(value) );
	}
	
	private static byte[] toBytes(Object object) throws SerializationException {
		return SerializationHelper.serialize( (Serializable) object );
	}
	
	private static Object fromBytes( byte[] bytes ) throws SerializationException {
		return SerializationHelper.deserialize(bytes);		
	}
	
	public int sqlType() {
		return Hibernate.BINARY.sqlType();
	}
	
	public Object assemble(Serializable cached, SessionImplementor session, Object owner)
	throws HibernateException {
		return (cached==null) ? null : fromBytes( (byte[]) cached );
	}
	
	public Serializable disassemble(Object value, SessionImplementor session)
	throws HibernateException {
		return (value==null) ? null : toBytes(value);
	}
	
	public boolean equals(Object object) {
		return super.equals(object) ?
			( (SerializableType) object ).serializableClass==serializableClass :
			false;
	}

	public int hashCode() {
		return serializableClass.hashCode();
	}

}







