//$Id: LiteralType.java,v 1.7 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

/**
 * A type that may appear as an SQL literal
 * @author Gavin King
 */
public interface LiteralType {
	/**
	 * String representation of the value, suitable for embedding in
	 * an SQL statement.
	 * @param value
	 * @return String
	 * @throws Exception
	 */
	public String objectToSQLString(Object value) throws Exception;
	
}






