package net.sf.hibernate.transaction;

import java.util.Properties;

import javax.transaction.TransactionManager;

import net.sf.hibernate.HibernateException;

/**
 * TransactionManager lookup strategy for JOnAS
 * @author ?
 */
public class JOnASTransactionManagerLookup implements TransactionManagerLookup {

	/**
	 * @see net.sf.hibernate.transaction.TransactionManagerLookup#getTransactionManager(Properties)
	 */
	public TransactionManager getTransactionManager(Properties props) throws HibernateException {
		try {
			Class clazz = Class.forName("org.objectweb.jonas_tm.Current");
			return (TransactionManager) clazz.getMethod("getTransactionManager", null).invoke(null, null);
		}
		catch (Exception e) {
			throw new HibernateException( "Could not obtain JOnAS transaction manager instance", e );
		}
	}

	public String getUserTransactionName() {
		return "java:comp/UserTransaction";
	}

}

