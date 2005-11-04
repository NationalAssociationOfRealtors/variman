//$Id: CharBooleanType.java,v 1.8 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;


/** 
 * Superclass for types that map Java boolean to SQL CHAR(1).
 * @author Gavin King
 */
public abstract class CharBooleanType extends BooleanType {
	
	protected abstract String getTrueString();
	protected abstract String getFalseString();
	
	public Object get(ResultSet rs, String name) throws SQLException {
		String code = rs.getString(name);
		if (code==null) {
			return null;
		}
		else {
			return getTrueString().equalsIgnoreCase(code) ? Boolean.TRUE : Boolean.FALSE;
		}
	}
	
	public void set(PreparedStatement st, Object value, int index)
	throws SQLException {
		st.setString( index, toCharacter(value) );
		
	}
	
	public int sqlType() {
		return Types.CHAR;
	}
	
	private String toCharacter(Object value) {
		return ( (Boolean) value ).booleanValue() ? getTrueString() : getFalseString();
	}
	
	public String objectToSQLString(Object value) throws Exception {
		return "'" + toCharacter(value) + "'";
	}
	
	public Object stringToObject(String xml) throws Exception {
		if ( getTrueString().equalsIgnoreCase(xml) ) {
			return Boolean.TRUE;
		}
		else if ( getFalseString().equalsIgnoreCase(xml) ) {
			return Boolean.FALSE;
		}
		else {
			throw new HibernateException("Could not interpret: " + xml);
		}
	}
	
}







