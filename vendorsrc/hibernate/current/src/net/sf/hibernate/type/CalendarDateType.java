//$Id: CalendarDateType.java,v 1.10 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;

/**
 * <tt>calendar_date</tt>: A type mapping for a <tt>Calendar</tt> 
 * object that represents a date.
 * @author Gavin King
 */
public class CalendarDateType extends MutableType {
	
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		
		Date date = rs.getDate(name);
		if (date!=null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal;
		}
		else {
			return null;
		}
		
	}
	
	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		
		st.setDate( index,  new Date( ( (Calendar) value ).getTime().getTime() ) );
	}
	
	public int sqlType() {
		return Types.DATE;
	}
	
	public String toString(Object value) throws HibernateException {
		return Hibernate.DATE.toString( ( (Calendar) value ).getTime() );
	}
	
	public Object fromStringValue(String xml) throws HibernateException {
		Calendar result = new GregorianCalendar();
		result.setTime( ( (Date) Hibernate.DATE.fromString(xml) ) );
		return result;
	}
	
	public Object deepCopyNotNull(Object value) throws HibernateException {
		return ( (Calendar) value ).clone();
	}
	
	public Class getReturnedClass() {
		return Calendar.class;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x==y) return true;
		if (x==null || y==null) return false;
		
		Calendar calendar1 = (Calendar) x;
		Calendar calendar2 = (Calendar) y;
		
		return calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
		&& calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
		&& calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
	}
	
	public String getName() {
		return "calendar_date";
	}
	
}






