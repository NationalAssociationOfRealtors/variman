//$Id: BlobType.java,v 1.10 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.lob.BlobImpl;

/**
 * <tt>blob</tt>: A type that maps an SQL BLOB to a java.sql.Blob.
 * @author Gavin King
 */
public class BlobType extends ImmutableType {
	
	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if (value instanceof BlobImpl) {
			BlobImpl blob = (BlobImpl) value;
			st.setBinaryStream( index, blob.getBinaryStream(), (int) blob.length() );
		}
		else {
			st.setBlob(index, (Blob) value);
		}
	}
	
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		return rs.getBlob(name);
	}
	
	public int sqlType() {
		return Types.BLOB;
	}
	
	public Class getReturnedClass() {
		return Blob.class;
	}
	
	public boolean hasNiceEquals() {
		return false;
	}
	
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}
	
	public String getName() {
		return "blob";
	}
	
	public String toString(Object val) throws HibernateException {
		return val.toString();
	}
	public Serializable disassemble(Object value, SessionImplementor session)
		throws HibernateException {
		throw new UnsupportedOperationException("Blobs are not cacheable");
	}
	public Object fromStringValue(String xml) {
		throw new UnsupportedOperationException();
	}

}






