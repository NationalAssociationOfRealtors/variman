//$Id: JTATransaction.java,v 1.11 2004/08/02 19:20:26 turin42 Exp $
package net.sf.hibernate.transaction;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.FlushMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.TransactionException;
import net.sf.hibernate.engine.CacheSynchronization;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * Implements a basic transaction strategy for JTA transactions. Instances check to
 * see if there is an existing JTA transaction. If none exists, a new transaction
 * is started. If one exists, all work is done in the existing context. The
 * following properties are used to locate the underlying <tt>UserTransaction</tt>:
 * <br><br>
 * <table>
 * <tr><td><tt>hibernate.jndi.url</tt></td><td>JNDI initial context URL</td></tr>
 * <tr><td><tt>hibernate.jndi.class</tt></td><td>JNDI provider class</td></tr>
 * <tr><td><tt>jta.UserTransaction</tt></td><td>JNDI name</td></tr>
 * </table>
 * @author Gavin King
 */
public class JTATransaction implements Transaction {
	
	private final SessionImplementor session;
	private UserTransaction ut;
	private boolean newTransaction;
	private boolean synchronization;
	private boolean begun;
	private boolean commitFailed;
	
	private static final Log log = LogFactory.getLog(JTATransaction.class);
	
	public void commit() throws HibernateException {
		
		if (!begun) throw new TransactionException("Transaction not successfully started");
		
		log.debug("commit");
		
		if ( session.getFlushMode()!=FlushMode.NEVER ) session.flush();
		if (newTransaction) {
			try {
				log.debug("Committing UserTransaction started by Hibernate");
				ut.commit();
			}
			catch (Exception e) {
				commitFailed = true; // so the transaction is already rolled back, by JTA spec
				log.error("Commit failed", e);
				throw new TransactionException("Commit failed with exception: ", e);
			}
			finally {
				afterCommitRollback();
			}
		}
		else {
			// this one only really needed for badly-behaved applications!
			// (if the TransactionManager has a Sychronization registered, 
			// its a noop)
			// (actually we do need it for downgrading locks)
			afterCommitRollback(); 
		}
		
	}
	
	public void rollback() throws HibernateException {
		
		if (!begun) throw new TransactionException("Transaction not successfully started");
		
		log.debug("rollback");
		
		try {
			if (newTransaction) {
				if (!commitFailed) ut.rollback();
			}
			else {
				ut.setRollbackOnly();
			}
		}
		catch (Exception e) {
			log.error("Rollback failed", e);
			throw new TransactionException("Rollback failed with exception", e);
		}
		finally {
			afterCommitRollback();
		}
	}
	
	private static final int NULL = Integer.MIN_VALUE;
	
	private void afterCommitRollback() throws TransactionException {

		if (!synchronization) { // this method is a noop if there is a Synchronization!

			if (!newTransaction) log.warn("You should set hibernate.transaction.manager_lookup_class if cache is enabled");
			int status=NULL;
			try {
				status = ut.getStatus();
			}
			catch (Exception e) {
				log.error("Could not determine transaction status after commit", e);
				throw new TransactionException("Could not determine transaction status after commit", e);
			}
			finally {
				/*if (status!=Status.STATUS_COMMITTED && status!=Status.STATUS_ROLLEDBACK) {
					log.warn("Transaction not complete - you should set hibernate.transaction.manager_lookup_class if cache is enabled");
					//throw exception??
				}*/
				session.afterTransactionCompletion(status==Status.STATUS_COMMITTED);
			}
			
		} 
	}

	public JTATransaction(SessionImplementor session) {
		this.session = session;
	}
	
	public void begin(InitialContext context, String utName, TransactionManager transactionManager) throws HibernateException {
		log.debug("Looking for UserTransaction under: " + utName);
		try {
			ut = (UserTransaction) context.lookup(utName);
		}
		catch (NamingException ne) {
			log.error("Could not find UserTransaction in JNDI", ne);
			throw new TransactionException("Could not find UserTransaction in JNDI: ", ne);
		}
		if (ut==null) {
			throw new AssertionFailure("A naming service lookup returned null");
		}
		
		log.debug("Obtained UserTransaction");
		
		try {
			newTransaction = ut.getStatus() == Status.STATUS_NO_TRANSACTION;
			if (newTransaction) {
				log.debug("beginning new transaction");
				ut.begin();
			}
		}
		catch (Exception e) {
			log.error("Begin failed", e);
			throw new TransactionException("Begin failed with exception", e);
		}
		
		if (newTransaction) {
			// don't need a synchronization since we are committing 
			// or rolling back the transaction ourselves - assuming 
			// that we do no work in beforeTransactionCompletion()
			synchronization = false;
		}
		else if (transactionManager==null) {
			// this is only ok if there is no cache
			synchronization = false;
			if (!newTransaction) log.warn("You should set hibernate.transaction.manager_lookup_class if cache is enabled");
		}
		else {
			try {
				transactionManager.getTransaction().registerSynchronization( new CacheSynchronization(session) );
			}
			catch (Exception se) {
				log.error("Could not register Synchronization", se);
				throw new TransactionException("Could not register Synchronization", se);
			}
			synchronization = true;
		}
		
		begun = true;
	}
	
	public boolean wasRolledBack() throws TransactionException {
		
		if (!begun) return false;
		if (commitFailed) return true;
		
		final int status;
		try {
			status = ut.getStatus();
		}
		catch (SystemException se) {
			log.error("Could not determine transaction status", se);
			throw new TransactionException("Could not determine transaction status", se);
		}
		if (status==Status.STATUS_UNKNOWN) {
			throw new TransactionException("Could not determine transaction status");
		}
		else {
			return status==Status.STATUS_MARKED_ROLLBACK ||
			status == Status.STATUS_ROLLING_BACK ||
			status == Status.STATUS_ROLLEDBACK;
		}
	}
	
	public boolean wasCommitted() throws TransactionException {
		
		if (!begun || commitFailed) return false;
		
		final int status;
		try {
			status = ut.getStatus();
		}
		catch (SystemException se) {
			log.error("Could not determine transaction status", se);
			throw new TransactionException("Could not determine transaction status: ", se);
		}
		if (status==Status.STATUS_UNKNOWN) {
			throw new TransactionException("Could not determine transaction status");
		}
		else {
			return status==Status.STATUS_COMMITTED;
		}
	}
	
}






