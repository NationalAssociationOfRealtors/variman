//$Id: JTATransactionFactory.java,v 1.10 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.transaction;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Environment;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.util.NamingHelper;

/**
 * Factory for <tt>JTATransaction</tt>.
 * @see JTATransaction
 * @author Gavin King
 */
public class JTATransactionFactory implements TransactionFactory {
	
	private static final Log log = LogFactory.getLog(JTATransactionFactory.class);
	
	private static final String DEFAULT_USER_TRANSACTION_NAME = "java:comp/UserTransaction";
	
	private InitialContext context;
	private String utName;
	private TransactionManager transactionManager;
	
	public void configure(Properties props) throws HibernateException {
		
		try {
			context = NamingHelper.getInitialContext(props);
		}
		catch (NamingException ne) {
			log.error("Could not obtain initial context", ne);
			throw new HibernateException( "Could not obtain initial context", ne );
		}
		
		utName = props.getProperty(Environment.USER_TRANSACTION);

		TransactionManagerLookup lookup = TransactionManagerLookupFactory.getTransactionManagerLookup(props);
		if (lookup!=null) {
			transactionManager = lookup.getTransactionManager(props);
			if (utName==null) utName = lookup.getUserTransactionName();
		}
		
		if (utName==null) utName = DEFAULT_USER_TRANSACTION_NAME;
		
	}
	
	public Transaction beginTransaction(SessionImplementor session) throws HibernateException {
		JTATransaction tx = new JTATransaction(session);
		tx.begin(context, utName, transactionManager);
		return tx; 
	}
	
}






