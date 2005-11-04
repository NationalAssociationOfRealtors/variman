//$Id: ByteType.java,v 1.10 2004/06/22 16:08:35 oneovthafew Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * <tt>byte</tt>: A type that maps an SQL TINYINT to a Java Byte.
 * @author Gavin King
 */
public class ByteType extends PrimitiveType implements DiscriminatorType, VersionType {
	
	private static final Byte ZERO = new Byte( (byte) 0 );
	
	public Object get(ResultSet rs, String name) throws SQLException {
		return new Byte( rs.getByte(name) );
	}
	
	public Class getPrimitiveClass() {
		return byte.class;
	}
	
	public Class getReturnedClass() {
		return Byte.class;
	}
	
	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		st.setByte( index, ( (Byte) value ).byteValue() );
	}
	
	public int sqlType() {
		return Types.TINYINT;
	}
	
	public String getName() { return "byte"; }
	
	public String objectToSQLString(Object value) throws Exception {
		return value.toString();
	}
	
	public Object stringToObject(String xml) throws Exception {
		return new Byte(xml);
	}
	
	public Object fromStringValue(String xml) {
		return new Byte(xml);
	}
	
	public Object next(Object current) {
		return new Byte( (byte) ( ( (Byte) current ).byteValue() + 1 ) );
	}
	
	public Object seed() {
		return ZERO;
	}
	
}





