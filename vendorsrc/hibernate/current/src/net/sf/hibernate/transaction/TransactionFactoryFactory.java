package net.sf.hibernate.transaction;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Environment;
import net.sf.hibernate.util.ReflectHelper;

/**
 * @author Gavin King
 */
public final class TransactionFactoryFactory {

	private static final Log log = LogFactory.getLog(TransactionFactoryFactory.class);
	
	/**
	 * Obtain a TransactionFactory with the transaction handling strategy
	 * specified by the given Properties.
	 *
	 * @param transactionProps transaction properties
	 * @return TransactionFactory
	 * @throws HibernateException
	 */
	public static TransactionFactory buildTransactionFactory(Properties transactionProps) throws HibernateException {
		
		String strategyClassName = transactionProps.getProperty(Environment.TRANSACTION_STRATEGY);
		if (strategyClassName==null) return new JDBCTransactionFactory();
		log.info("Transaction strategy: " + strategyClassName);
		TransactionFactory factory;
		try {
			factory = (TransactionFactory) ReflectHelper.classForName(strategyClassName).newInstance();
		}
		catch (ClassNotFoundException e) {
			log.error("TransactionFactory class not found", e);
			throw new HibernateException("TransactionFactory class not found: " + strategyClassName);
		}
		catch (IllegalAccessException e) {
			log.error("Failed to instantiate TransactionFactory", e);
			throw new HibernateException("Failed to instantiate TransactionFactory: " + e);
		}
		catch (java.lang.InstantiationException e) {
			log.error("Failed to instantiate TransactionFactory", e);
			throw new HibernateException("Failed to instantiate TransactionFactory: " + e);
		}
		factory.configure(transactionProps);
		return factory;
	}
	
	private TransactionFactoryFactory() {}
}
