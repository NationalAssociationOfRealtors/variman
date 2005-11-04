//$Id: ClobType.java,v 1.10 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.lob.ClobImpl;

/**
 * <tt>clob</tt>: A type that maps an SQL CLOB to a java.sql.Clob.
 * @author Gavin King
 */
public class ClobType extends ImmutableType {
	
	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if (value instanceof ClobImpl) {
			ClobImpl clob = (ClobImpl) value;
			st.setCharacterStream( index, clob.getCharacterStream(), (int) clob.length() );
		}
		else {
			st.setClob(index, (Clob) value);
		}
	}
	
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		return rs.getClob(name);
	}
	
	public int sqlType() {
		return Types.CLOB;
	}
	
	public Class getReturnedClass() {
		return Clob.class;
	}
	
	public boolean hasNiceEquals() {
		return false;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}
	
	public String getName() {
		return "clob";
	}
	
	public String toString(Object val) throws HibernateException {
		return val.toString();
	}
	public Serializable disassemble(Object value, SessionImplementor session)
		throws HibernateException {
		throw new UnsupportedOperationException("Clobs are not cacheable");
	}
	public Object fromStringValue(String xml) {
		throw new UnsupportedOperationException();
	}
	

}






