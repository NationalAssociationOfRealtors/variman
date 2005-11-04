//$Id: BinaryType.java,v 1.11 2004/07/13 14:51:10 turin42 Exp $
package net.sf.hibernate.type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Environment;

/**
 * <tt>binary</tt>: A type that maps an SQL VARBINARY to a Java byte[].
 * @author Gavin King
 */
public class BinaryType extends MutableType {
	
	public void set(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		
		if ( Environment.useStreamsForBinary() ) {
			st.setBinaryStream( index, new ByteArrayInputStream( (byte[]) value ), ( (byte[]) value ).length );
		}
		else {
			st.setBytes( index, (byte[]) value );
		}
	}
	
	public Object get(ResultSet rs, String name) throws HibernateException, SQLException {
		
		if ( Environment.useStreamsForBinary() ) {
			
			InputStream inputStream = rs.getBinaryStream(name);
			
			if (inputStream==null) return null; // is this really necessary?
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
			byte[] buffer = new byte[2048];
			
			try {
				while (true) {
					int amountRead = inputStream.read(buffer);
					if (amountRead == -1) {
						break;
					}
					outputStream.write(buffer, 0, amountRead);
				}
				
				inputStream.close();
				outputStream.close();
			}
			catch (IOException ioe) {
				throw new HibernateException( "IOException occurred reading a binary value", ioe );
			}
			
			return outputStream.toByteArray();
			
		}
		else {
			return rs.getBytes(name);
		}
	}
	
	public int sqlType() {
		return Types.VARBINARY;
	}
	
	public Class getReturnedClass() {
		return byte[].class;
	}
	
	public boolean equals(Object x, Object y) {
		return (x==y) || ( x!=null && y!=null && java.util.Arrays.equals( (byte[]) x, (byte[]) y ) );
	}
	
	public String getName() { return "binary"; }
	
	public String toString(Object val) {
		byte[] bytes = ( byte[] ) val;
		StringBuffer buf = new StringBuffer();
		for ( int i=0; i<bytes.length; i++ ) {
			String hexStr = Integer.toHexString( bytes[i] - Byte.MIN_VALUE );
			if ( hexStr.length()==1 ) buf.append('0');
			buf.append(hexStr);
		}
		return buf.toString();
	}
	
	public Object deepCopyNotNull(Object value) {
		byte[] bytes = (byte[]) value;
		byte[] result = new byte[bytes.length];
		System.arraycopy(bytes, 0, result, 0, bytes.length);
		return result;
	}
	
	public Object fromStringValue(String xml) throws HibernateException {
		if (xml == null)
			return null;
		if (xml.length() % 2 != 0)
			throw new IllegalArgumentException("The string is not a valid xml representation of a binary content.");
		byte[] bytes = new byte[xml.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			String hexStr = xml.substring(i * 2, (i + 1) * 2);
			bytes[i] = (byte) (Integer.parseInt(hexStr, 16) + Byte.MIN_VALUE);
		}
		return bytes;
	}
	
}







