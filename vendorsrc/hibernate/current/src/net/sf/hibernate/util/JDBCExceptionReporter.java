//$Id: JDBCExceptionReporter.java,v 1.8 2004/06/04 01:28:53 steveebersole Exp $
package net.sf.hibernate.util;

import java.sql.SQLException;
import java.sql.SQLWarning;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class JDBCExceptionReporter {
	
	private static final Log log = LogFactory.getLog(JDBCExceptionReporter.class);
	
	private JDBCExceptionReporter() {}
	
	public static void logWarnings(SQLWarning warning) {
		if ( log.isWarnEnabled() ) {
			if ( log.isDebugEnabled() && warning!=null ) log.debug( "SQL Warning", warning );
			while (warning != null) {
				log.warn(
				new StringBuffer(30)
					.append("SQL Warning: ")
					.append( warning.getErrorCode() )
					.append(", SQLState: ")
					.append( warning.getSQLState() )
					.toString()
				);
				log.warn( warning.getMessage() );
				warning = warning.getNextWarning();
			}
		}
	}

	public static void logExceptions(SQLException ex) {
		if ( log.isErrorEnabled() ) {
			if ( log.isDebugEnabled() ) log.debug( "SQL Exception", ex );
			while (ex != null) {
				log.warn( 
				new StringBuffer(30)
					.append("SQL Error: ")
					.append( ex.getErrorCode() )
					.append(", SQLState: ")
					.append( ex.getSQLState() )
					.toString()
				);
				log.error( ex.getMessage() );
				ex = ex.getNextException();
			}
		}
	}

}






