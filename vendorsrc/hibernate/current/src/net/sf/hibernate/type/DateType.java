//$Id: DateType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.hibernate.HibernateException;

/**
 * <tt>date</tt>: A type that maps an SQL DATE to a Java Date.
 * @author Gavin King
 */
public class DateType extends MutableType implements IdentifierType, LiteralType {
	
	public Object get(ResultSet rs, String name) throws SQLException {
		return rs.getDate(name);
	}
	
	public Class getReturnedClass() {
		return java.util.Date.class;
	}
	
	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		
		Date sqlDate;
		if ( value instanceof Date) {
			sqlDate = (Date) value;
		}
		else {
			sqlDate = new Date( ( (java.util.Date) value ).getTime() );
		}
		st.setDate(index, sqlDate);
	}
	
	public int sqlType() {
		return Types.DATE;
	}
	
	public boolean equals(Object x, Object y) {
		
		if (x==y) return true;
		if (x==null || y==null) return false;
		
		Calendar calendar1 = java.util.Calendar.getInstance();
		Calendar calendar2 = java.util.Calendar.getInstance();
		calendar1.setTime( (java.util.Date) x );
		calendar2.setTime( (java.util.Date) y );
		
		return calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
		&& calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
		&& calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
	}
	public String getName() { return "date"; }
	
	public String toString(Object val) {
		return new SimpleDateFormat("dd MMMM yyyy").format( (java.util.Date) val );
	}
	
	public Object deepCopyNotNull(Object value) {
		return  /*value;*/ new Date( ( (java.util.Date) value ).getTime() );
	}
	
	public boolean hasNiceEquals() {
		return true;
	}
	
	public Object stringToObject(String xml) throws Exception {
		return DateFormat.getDateInstance().parse(xml);
	}
	
	public String objectToSQLString(Object value) throws Exception {
		return '\'' + value.toString() + '\'';
	}
	
	public Object fromStringValue(String xml) throws HibernateException {
		try {
			return new SimpleDateFormat().parse(xml);
		}
		catch (ParseException pe) {
			throw new HibernateException("could not parse XML", pe);
		}
	}

}





