//$Id: IntegerType.java,v 1.9 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * <tt>integer</tt>: A type that maps an SQL INT to a Java Integer.
 * @author Gavin King
 */
public class IntegerType extends PrimitiveType implements DiscriminatorType, VersionType {
	
	private static final Integer ZERO = new Integer(0);
	
	public Object get(ResultSet rs, String name) throws SQLException {
		return new Integer( rs.getInt(name) );
	}
	
	public Class getPrimitiveClass() {
		return int.class;
	}
	
	public Class getReturnedClass() {
		return Integer.class;
	}
	
	public void set(PreparedStatement st, Object value, int index)
	throws SQLException {
		st.setInt( index, ( (Integer) value ).intValue() );
	}
	
	public int sqlType() {
		return Types.INTEGER;
	}
	
	public String getName() { return "integer"; }
	
	public String objectToSQLString(Object value) throws Exception {
		return value.toString();
	}
	
	public Object stringToObject(String xml) throws Exception {
		return new Integer(xml);
	}
	
	public Object next(Object current) {
		return new Integer( ( (Integer) current ).intValue() + 1 );
	}
	
	public Object seed() {
		return ZERO;
	}
	
	public Object fromStringValue(String xml) {
		return new Integer(xml);
	}
	
}





