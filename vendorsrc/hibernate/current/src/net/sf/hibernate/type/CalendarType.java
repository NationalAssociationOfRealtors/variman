//$Id: CalendarType.java,v 1.13 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Environment;

/**
 * <tt>calendar</tt>: A type mapping for a <tt>Calendar</tt> object that 
 * represents a datetime.
 * @author Gavin King
 */
public class CalendarType extends MutableType implements VersionType {
	
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		
		Timestamp ts = rs.getTimestamp(name);
		if (ts!=null) {
			Calendar cal = new GregorianCalendar();
			if ( Environment.jvmHasTimestampBug() ) {
				cal.setTime( new Date( ts.getTime() + ts.getNanos() / 1000000 ) );
			}
			else {
				cal.setTime(ts);
			}
			return cal;
		}
		else {
			return null;
		}
		
	}
	
	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		
		st.setTimestamp( index,  new Timestamp( ( (Calendar) value ).getTime().getTime() ) );
	}
	
	public int sqlType() {
		return Types.TIMESTAMP;
	}
	
	public String toString(Object value) throws HibernateException {
		return Hibernate.TIMESTAMP.toString( ( (Calendar) value ).getTime() );
	}
	
	public Object fromStringValue(String xml) throws HibernateException {
		Calendar result = new GregorianCalendar();
		result.setTime( ( (Date) Hibernate.TIMESTAMP.fromString(xml) ) );
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
		
		return calendar1.get(Calendar.MILLISECOND) == calendar2.get(Calendar.MILLISECOND)
		&& calendar1.get(Calendar.SECOND) == calendar2.get(Calendar.SECOND)
		&& calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE)
		&& calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY)
		&& calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
		&& calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
		&& calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
	}
	
	public String getName() {
		return "calendar";
	}
	
	public Object next(Object current) {
		return seed();
	}

	public Object seed() {
		return Calendar.getInstance();
	}
	
}






