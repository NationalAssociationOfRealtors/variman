//$Id: LocaleType.java,v 1.9 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.StringTokenizer;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.util.EqualsHelper;
import net.sf.hibernate.util.StringHelper;

/**
 * <tt>locale</tt>: A type that maps an SQL VARCHAR to a Java Locale.
 * @author Gavin King
 */
public class LocaleType extends ImmutableType implements LiteralType {
	
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		return fromString( (String) Hibernate.STRING.get(rs, name) );
	}
	
	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		Hibernate.STRING.set(st, value.toString(), index);
	}
	
	public Object fromStringValue(String string) {
		if (string == null) {
			return null;
		}
		else {
			StringTokenizer tokens = new StringTokenizer(string, "_");
			String language = tokens.hasMoreTokens() ? tokens.nextToken() : StringHelper.EMPTY_STRING;
			String country = tokens.hasMoreTokens() ? tokens.nextToken() : StringHelper.EMPTY_STRING;
			String variant = tokens.hasMoreTokens() ? tokens.nextToken() : StringHelper.EMPTY_STRING;
			return new Locale(language, country, variant);
		}
	}
	
	public int sqlType() {
		return Hibernate.STRING.sqlType();
	}
	
	public String toString(Object value) throws HibernateException {
		return value.toString();
	}
	
	public Class getReturnedClass() {
		return Locale.class;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		return EqualsHelper.equals(x, y);
	}
	
	public String getName() {
		return "locale";
	}
	
	public String objectToSQLString(Object value) throws Exception {
		return ( (LiteralType) Hibernate.STRING ).objectToSQLString( value.toString() );
	}
	
}






