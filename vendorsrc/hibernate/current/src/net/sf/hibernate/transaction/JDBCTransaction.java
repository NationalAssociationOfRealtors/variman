//$Id: JDBCTransaction.java,v 1.10 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.transaction;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.FlushMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.TransactionException;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * Implements a basic transaction strategy for JDBC connections.This is the
 * default <tt>Transaction</tt> implementation used if none is explicitly
 * specified.
 * @author Anton van Straaten, Gavin King
 */
public class JDBCTransaction implements Transaction {
	private SessionImplementor session;
	private boolean toggleAutoCommit;
	private boolean rolledBack;
	private boolean committed;
	private boolean begun;
	private boolean commitFailed;
	
	private static final Log log = LogFactory.getLog(JDBCTransaction.class);
	
	public JDBCTransaction(SessionImplementor session) throws HibernateException {
		this.session = session;
	}
	
	public void begin() throws HibernateException {
		
		log.debug("begin");
		
		try {
			toggleAutoCommit = session.connection().getAutoCommit();
			if ( log.isDebugEnabled() ) log.debug("current autocommit status:" + toggleAutoCommit);
			if (toggleAutoCommit) {
				log.debug("disabling autocommit");
				session.connection().setAutoCommit(false);
			}
		}
		catch (SQLException e) {
			log.error("Begin failed", e);
			throw new TransactionException("Begin failed with SQL exception: ", e);
		}
		
		begun = true;
	}
	
	public void commit() throws HibernateException {
		
		if (!begun) throw new TransactionException("Transaction not successfully started");
		
		log.debug("commit");
		
		if ( session.getFlushMode()!=FlushMode.NEVER ) session.flush();
		try {
			session.connection().commit();
			committed = true;
			session.afterTransactionCompletion(true);
		}
		catch (SQLException e) {
			log.error("Commit failed", e);
			session.afterTransactionCompletion(false);
			commitFailed = true;
			throw new TransactionException("Commit failed with SQL exception: ", e);
		}
		finally {
			toggleAutoCommit();
		}
	}
	
	public void rollback() throws HibernateException {
		
		if (!begun) throw new TransactionException("Transaction not successfully started");
		
		log.debug("rollback");
		
		if (!commitFailed) {
			try {
				session.connection().rollback();
				rolledBack = true;
			}
			catch (SQLException e) {
				log.error("Rollback failed", e);
				throw new TransactionException("Rollback failed with SQL exception: ", e);
			}
			finally {
				session.afterTransactionCompletion(false);
				toggleAutoCommit();
			}
		}
	}
	
	private void toggleAutoCommit() {
		try {
			if (toggleAutoCommit) {
				log.debug("re-enabling autocommit");
				session.connection().setAutoCommit(true);
			}
		}
		catch (Exception sqle) {
			log.error("Could not toggle autocommit", sqle);
			//swallow it (the transaction _was_ successful or successfully rolled back)
		}
	}
	
	public boolean wasRolledBack() {
		return rolledBack;
	}
	public boolean wasCommitted() {
		return committed;
	}

}





