//$Id: TimeType.java,v 1.9 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.hibernate.HibernateException;

/**
 * <tt>time</tt>: A type that maps an SQL TIME to a Java 
 * java.util.Date or java.sql.Time.
 * @author Gavin King
 */
public class TimeType extends MutableType implements LiteralType {
	
	public Object get(ResultSet rs, String name) throws SQLException {
		return rs.getTime(name);
	}
	public Class getReturnedClass() {
		return java.util.Date.class;
	}
	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		
		Time time;
		if (value instanceof Time) {
			time = (Time) value;
		}
		else {
			time = new Time( ( (java.util.Date) value ).getTime() );
		}
		st.setTime(index, time);
	}
	
	public int sqlType() {
		return Types.TIME;
	}
	public String getName() { return "time"; }
	
	public String toString(Object val) {
		return new SimpleDateFormat("HH:mm:ss").format( (java.util.Date) val );
	}
	public boolean equals(Object x, Object y) {
		
		if (x==y) return true;
		if (x==null || y==null) return false;
		
		Calendar calendar1 = java.util.Calendar.getInstance();
		Calendar calendar2 = java.util.Calendar.getInstance();
		calendar1.setTime( (java.util.Date) x );
		calendar2.setTime( (java.util.Date) y );
		
		return calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY)
		&& calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE)
		&& calendar1.get(Calendar.SECOND) == calendar2.get(Calendar.SECOND)
		&& calendar1.get(Calendar.MILLISECOND) == calendar2.get(Calendar.MILLISECOND);
	}
	
	public Object deepCopyNotNull(Object value) {
		return  /*value;*/ new Time( ( (java.util.Date) value ).getTime() );
	}
	
	public boolean hasNiceEquals() {
		return true;
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





