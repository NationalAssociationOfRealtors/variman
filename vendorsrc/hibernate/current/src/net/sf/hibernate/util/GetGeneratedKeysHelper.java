//$Id: GetGeneratedKeysHelper.java,v 1.6 2004/06/04 05:43:49 steveebersole Exp $
package net.sf.hibernate.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.hibernate.AssertionFailure;

/**
 * @author Gavin King
 */
public final class GetGeneratedKeysHelper {

	private GetGeneratedKeysHelper() {}
	
	private static final int RETURN_GENERATED_KEYS;
	private static final Method PREPARE_STATEMENT_METHOD;
	private static final Method GET_GENERATED_KEYS_METHOD;
	
	static {
		try {
			RETURN_GENERATED_KEYS = Statement.class.getDeclaredField("RETURN_GENERATED_KEYS").getInt(PreparedStatement.class);
			PREPARE_STATEMENT_METHOD = Connection.class.getMethod( "prepareStatement", new Class[] {String.class, Integer.TYPE} );
			GET_GENERATED_KEYS_METHOD = Statement.class.getDeclaredMethod("getGeneratedKeys", null);
		} 
		catch (Exception e) {
			throw new AssertionFailure("could not initialize getGeneratedKeys() support", e);
		}
	}
	
	public static PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {
		Object[] args = new Object[] { sql, new Integer(RETURN_GENERATED_KEYS) } ;
		try {
			return (PreparedStatement) PREPARE_STATEMENT_METHOD.invoke(conn, args);
		}
		catch (InvocationTargetException ite) {
			if ( ite.getTargetException() instanceof SQLException ) {
				throw (SQLException) ite.getTargetException();
			}
			else if ( ite.getTargetException() instanceof RuntimeException ) {
				throw (RuntimeException) ite.getTargetException();
			}
			else {
				throw new AssertionFailure("InvocationTargetException", ite);
			}
		}
		catch (IllegalAccessException iae) {
			throw new AssertionFailure("IllegalAccessException", iae);
		}
	}
	
	public static ResultSet getGeneratedKey(PreparedStatement ps) throws SQLException {
		try {
			return (ResultSet) GET_GENERATED_KEYS_METHOD.invoke(ps, null);
		}
		catch (InvocationTargetException ite) {
			if ( ite.getTargetException() instanceof SQLException ) {
				throw (SQLException) ite.getTargetException();
			}
			else if ( ite.getTargetException() instanceof RuntimeException ) {
				throw (RuntimeException) ite.getTargetException();
			}
			else {
				throw new AssertionFailure("InvocationTargetException", ite);
			}
		}
		catch (IllegalAccessException iae) {
			throw new AssertionFailure("IllegalAccessException", iae);
		}
	}

}
