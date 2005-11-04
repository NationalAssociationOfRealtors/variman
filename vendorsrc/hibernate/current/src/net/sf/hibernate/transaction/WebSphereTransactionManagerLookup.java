//$Id: WebSphereTransactionManagerLookup.java,v 1.9 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.transaction;

import java.util.Properties;

import javax.transaction.TransactionManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.hibernate.HibernateException;

/**
 * TransactionManager lookup strategy for WebSphere (versions 4, 5.0 and 5.1)
 * @author Gavin King
 */
public class WebSphereTransactionManagerLookup implements TransactionManagerLookup {

	private static final Log log = LogFactory.getLog(WebSphereTransactionManagerLookup.class);
	private int version;

	/**
	 * @see net.sf.hibernate.transaction.TransactionManagerLookup#getTransactionManager(Properties)
	 */
	public TransactionManager getTransactionManager(Properties props) throws HibernateException {
		try {
			Class clazz;
			try {
				clazz = Class.forName("com.ibm.ws.Transaction.TransactionManagerFactory");
				version = 5;
				log.info("WebSphere 5.1");
			}
			catch (Exception e) {
				try {
					clazz = Class.forName("com.ibm.ejs.jts.jta.TransactionManagerFactory");
					version = 5;
					log.info("WebSphere 5.0");
				} 
				catch (Exception e2) {
					clazz = Class.forName("com.ibm.ejs.jts.jta.JTSXA");
					version = 4;
					log.info("WebSphere 4");
				}
			}

			return (TransactionManager) clazz.getMethod("getTransactionManager", null).invoke(null, null);
		}
		catch (Exception e) {
			throw new HibernateException( "Could not obtain WebSphere JTSXA instance", e );
		}
	}

	public String getUserTransactionName() {
		return version==5 ?
			"java:comp/UserTransaction":
			"jta/usertransaction";
	}

}