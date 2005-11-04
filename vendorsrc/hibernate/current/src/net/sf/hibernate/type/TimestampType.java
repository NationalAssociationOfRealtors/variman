//$Id: TimestampType.java,v 1.11 2004/07/13 15:17:09 turin42 Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.hibernate.HibernateException;

/**
 * <tt>timestamp</tt>: A type that maps an SQL TIMESTAMP to a Java 
 * java.util.Date or java.sql.Timestamp.
 * @author Gavin King
 */
public class TimestampType extends MutableType implements VersionType, LiteralType {

	private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public Object get(ResultSet rs, String name) throws SQLException {
		return rs.getTimestamp(name);
	}
	public Class getReturnedClass() {
		return java.util.Date.class;
	}
	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		Timestamp ts;
		if (value instanceof Timestamp) {
			ts = (Timestamp) value;
		}
		else {
			ts = new Timestamp( ( (java.util.Date) value ).getTime() );
		}
		st.setTimestamp(index, ts);
	}
	
	public int sqlType() {
		return Types.TIMESTAMP;
	}
	public String getName() { return "timestamp"; }
	
	public String toString(Object val) {
		return new SimpleDateFormat(TIMESTAMP_FORMAT).format( (java.util.Date) val );
	}
	
	public Object deepCopyNotNull(Object value) {
		if ( value instanceof Timestamp ) {
			Timestamp orig = (Timestamp) value;
			Timestamp ts = new Timestamp( orig.getTime() );
			ts.setNanos( orig.getNanos() );
			return ts;
		}
		else {
			java.util.Date orig = (java.util.Date) value;
			return new java.util.Date( orig.getTime() );
		}
	}
	
	public boolean equals(Object x, Object y) {
		
		if (x==y) return true;
		if (x==null || y==null) return false;
		
		long xTime = ( (java.util.Date) x ).getTime();
		long yTime = ( (java.util.Date) y ).getTime();
		boolean xts = x instanceof Timestamp;
		boolean yts = y instanceof Timestamp;
		int xNanos = xts ? ( (Timestamp) x ).getNanos() : 0;
		int yNanos = yts ? ( (Timestamp) y ).getNanos() : 0;
		xTime += xNanos / 1000000;
		yTime += yNanos / 1000000;
		if ( xTime!=yTime ) return false;
		if (xts && yts) {
			// both are Timestamps
			int xn = xNanos % 1000000;
			int yn = yNanos % 1000000;
			return xn==yn;
		}
		else {
			// at least one is a plain old Date
			return true;
		}
		
	}
	
	public boolean hasNiceEquals() {
		return true;
	}
	
	public Object next(Object current) {
		return seed();
	}
	
	public Object seed() {
		return new Timestamp( System.currentTimeMillis() );
	}
	
	public String objectToSQLString(Object value) throws Exception {
		return '\'' + value.toString() + '\'';
	}
	
	public Object fromStringValue(String xml) throws HibernateException {
		try {
			return new Timestamp(new SimpleDateFormat(TIMESTAMP_FORMAT).parse(xml).getTime());
		}
		catch (ParseException pe) {
			throw new HibernateException("could not parse XML", pe);
		}
	}
	
}





