//$Id: UUIDHexGenerator.java,v 1.10 2004/06/04 01:27:40 steveebersole Exp $
package net.sf.hibernate.id;

import java.io.Serializable;
import java.util.Properties;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.dialect.Dialect;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.type.Type;
import net.sf.hibernate.util.PropertiesHelper;
import net.sf.hibernate.util.StringHelper;

/**
 * <b>uuid.hex</b><br>
 * <br>
 * A <tt>UUIDGenerator</tt> that returns a string of length 32,
 * This string will consist of only hex digits. Optionally, 
 * the string may be generated with seperators between each 
 * component of the UUID.
 *
 * @see UUIDStringGenerator
 * @author Gavin King
 */

public class UUIDHexGenerator extends UUIDGenerator implements Configurable {
	
	private String sep = "";
	
	protected String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace( 8-formatted.length(), 8, formatted );
		return buf.toString();
	}
	
	protected String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace( 4-formatted.length(), 4, formatted );
		return buf.toString();
	}
	
	public Serializable generate(SessionImplementor cache, Object obj) {
		return new StringBuffer(36)
		.append( format( getIP() ) ).append(sep)
		.append( format( getJVM() ) ).append(sep)
		.append( format( getHiTime() ) ).append(sep)
		.append( format( getLoTime() ) ).append(sep)
		.append( format( getCount() ) )
		.toString();
	}
	
	public static void main( String[] args ) throws Exception {
		Properties props = new Properties();
		props.setProperty("seperator", "/");
		IdentifierGenerator gen = new UUIDHexGenerator();
		( (Configurable) gen ).configure(Hibernate.STRING, props, null);
		IdentifierGenerator gen2 = new UUIDHexGenerator();
		( (Configurable) gen2 ).configure(Hibernate.STRING, props, null);
		
		for ( int i=0; i<10; i++) {
			String id = (String) gen.generate(null, null);
			System.out.println( id + ": " +  id.length() );
			String id2 = (String) gen2.generate(null, null);
			System.out.println( id2 + ": " +  id2.length() );
		}
	}
	
	
	public void configure(Type type, Properties params, Dialect d) {
		sep = PropertiesHelper.getString("seperator", params, StringHelper.EMPTY_STRING);
	}

}






