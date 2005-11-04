//$Id: FloatType.java,v 1.9 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * <tt>float</tt>: A type that maps an SQL FLOAT to a Java Float.
 * @author Gavin King
 */
public class FloatType extends PrimitiveType {
	
	public Object get(ResultSet rs, String name) throws SQLException {
		return new Float( rs.getFloat(name) );
	}
	
	public Class getPrimitiveClass() {
		return float.class;
	}
	
	public Class getReturnedClass() {
		return Float.class;
	}
	
	public void set(PreparedStatement st, Object value, int index)
	throws SQLException {
		
		st.setFloat( index, ( (Float) value ).floatValue() );
	}
	
	public int sqlType() {
		return Types.FLOAT;
	}
	
	public String getName() { return "float"; }
	
	public String objectToSQLString(Object value) throws Exception {
		return value.toString();
	}

	public Object fromStringValue(String xml) {
		return new Float(xml);
	}
	
}





