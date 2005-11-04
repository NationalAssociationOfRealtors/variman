//$Id: JDBCException.java,v 1.7 2004/06/04 05:43:44 steveebersole Exp $
package net.sf.hibernate;

import java.sql.SQLException;

import net.sf.hibernate.util.JDBCExceptionReporter;

import org.apache.commons.logging.LogFactory;

/**
 * Wraps an <tt>SQLException</tt>. Indicates that an exception
 * occurred during a JDBC call.
 * 
 * @see java.sql.SQLException
 * @author Gavin King
 */
public class JDBCException extends HibernateException {
	
	private SQLException sqle;

	/**
	 * Constructor for JDBCException.
	 * @param root
	 */
	public JDBCException(SQLException root) {
		this("SQLException occurred", root);
	}

	/**
	 * Constructor for JDBCException.
	 * @param string
	 * @param root
	 */
	public JDBCException(String string, SQLException root) {
		super(string, root);
		sqle=root;
		JDBCExceptionReporter.logExceptions(root);
		LogFactory.getLog(JDBCExceptionReporter.class).error(string, root);
	}
	
	/**
	 * Get the SQLState of the underlying <tt>SQLException</tt>.
	 * @see java.sql.SQLException
	 * @return String
	 */
	public String getSQLState() {
		return sqle.getSQLState();
	}

	/**
	 * Get the <tt>errorCode</tt> of the underlying <tt>SQLException</tt>.
	 * @see java.sql.SQLException
	 * @return int the error code
	 */
	public int getErrorCode() {
		return sqle.getErrorCode();
	}
	
	/**
	 * Get the underlying <tt>SQLException</tt>.
	 * @return SQLException
	 */
	public SQLException getSQLException() {
		return sqle;
	}

}
