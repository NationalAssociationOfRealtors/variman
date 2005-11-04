//$Id: JNDITransactionManagerLookup.java,v 1.7 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.transaction;

import java.util.Properties;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.util.NamingHelper;

/**
 * Locates a <tt>TransactionManager</tt> in JNDI.
 * @author Gavin King
 */
public abstract class JNDITransactionManagerLookup implements TransactionManagerLookup {
	
	/**
	 * @see net.sf.hibernate.transaction.TransactionManagerLookup#getTransactionManager(java.util.Properties)
	 */
	public TransactionManager getTransactionManager(Properties props) throws HibernateException {
		try {
			return (TransactionManager) NamingHelper.getInitialContext(props).lookup( getName() );
		}
		catch (NamingException ne) {
			throw new HibernateException( "Could not locate TransactionManager", ne );
		}
	}
	
	protected abstract String getName();
	
}






