//$Id: HibernateIterator.java,v 1.6 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.engine;

import java.sql.SQLException;
import java.util.Iterator;

/**
 * An iterator that may be "closed"
 * @see net.sf.hibernate.Hibernate#close(java.util.Iterator)
 * @author Gavin King
 */
public interface HibernateIterator extends Iterator {
	public void close() throws SQLException;
}
