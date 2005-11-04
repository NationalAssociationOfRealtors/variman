//$Id: TimeZoneType.java,v 1.9 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.util.EqualsHelper;

/**
 * <tt>timezone</tt>: A type that maps an SQL VARCHAR to a 
 * <tt>java.util.TimeZone</tt>
 * @see java.util.TimeZone
 * @author Gavin King
 */
public class TimeZoneType extends ImmutableType implements LiteralType {
	
	public Object get(ResultSet rs, String name)
	throws HibernateException, SQLException {
		String id = (String) Hibernate.STRING.nullSafeGet(rs, name);
		return (id==null) ? null : TimeZone.getTimeZone(id);
	}
	

	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		Hibernate.STRING.set(st, ( (TimeZone) value ).getID(), index);
	}
	
	public int sqlType() {
		return Hibernate.STRING.sqlType();
	}
	
	public String toString(Object value) throws HibernateException {
		return ( (TimeZone) value ).getID();
	}
	
	public Object fromStringValue(String xml) throws HibernateException {
		return TimeZone.getTimeZone(xml);
	}
	
	public Class getReturnedClass() {
		return TimeZone.class;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		return EqualsHelper.equals(x, y);
	}
	
	public String getName() {
		return "timezone";
	}
	
	public String objectToSQLString(Object value) throws Exception {
		return ( (LiteralType) Hibernate.STRING ).objectToSQLString(
			( (TimeZone) value ).getID()
		);
	}
	
}






