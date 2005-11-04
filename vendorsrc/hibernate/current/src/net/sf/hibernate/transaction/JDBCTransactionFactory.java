//$Id: JDBCTransactionFactory.java,v 1.9 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.transaction;

import java.util.Properties;

import net.sf.hibernate.Transaction;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * Factory for <tt>JDBCTransaction</tt>.
 * @see JDBCTransaction
 * @author Anton van Straaten
 */
public final class JDBCTransactionFactory implements TransactionFactory {
	
	public Transaction beginTransaction(SessionImplementor session) throws HibernateException {
		JDBCTransaction tx = new JDBCTransaction(session);
		tx.begin();
		return tx;
	}
	public void configure(Properties props) throws HibernateException {}
	
}





