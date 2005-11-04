//$Id: BigDecimalType.java,v 1.9 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;

/**
 * <tt>big_decimal</tt>: A type that maps an SQL NUMERIC to a 
 * <tt>java.math.BigDecimal</tt>
 * @see java.math.BigDecimal
 * @author Gavin King
 */
public class BigDecimalType extends ImmutableType {
	
	/**
	 * @see net.sf.hibernate.type.NullableType#get(ResultSet, String)
	 */
	public Object get(ResultSet rs, String name)
	throws HibernateException, SQLException {
		return rs.getBigDecimal(name);
	}
	
	/**
	 * @see net.sf.hibernate.type.NullableType#set(PreparedStatement, Object, int)
	 */
	public void set(PreparedStatement st, Object value, int index)
	throws HibernateException, SQLException {
		st.setBigDecimal(index, (BigDecimal) value);
	}
	
	/**
	 * @see net.sf.hibernate.type.NullableType#sqlType()
	 */
	public int sqlType() {
		return Types.NUMERIC;
	}
	
	/**
	 */
	public String toString(Object value) throws HibernateException {
		return value.toString();
	}
	
	
	/**
	 * @see net.sf.hibernate.type.Type#getReturnedClass()
	 */
	public Class getReturnedClass() {
		return BigDecimal.class;
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#equals(Object, Object)
	 */
	public boolean equals(Object x, Object y) throws HibernateException {
		return x==y || ( x!=null && y!=null && ( (BigDecimal) x ).compareTo( (BigDecimal) y )==0 );
	}
	
	/**
	 * @see net.sf.hibernate.type.Type#getName()
	 */
	public String getName() {
		return "big_decimal";
	}

	public Object fromStringValue(String xml) {
		return new BigDecimal(xml);
	}
	
	
}






