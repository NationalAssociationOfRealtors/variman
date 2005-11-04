//$Id: VersionType.java,v 1.7 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;
/**
 * A <tt>Type</tt> that may be used to version data.
 * @author Gavin King
 */
public interface VersionType extends Type {
	/**
	 * Generate an initial version.
	 * @return an instance of the type
	 */
	public Object seed();
	/**
	 * Increment the version.
	 * @param current the current version
	 * @return an instance of the type
	 */
	public Object next(Object current);
}






