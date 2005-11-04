//$Id: ResinTransactionManagerLookup.java,v 1.7 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.transaction;

/**
 * TransactionManager lookup strategy for Resin
 * @author Aapo Laakkonen
 */
public class ResinTransactionManagerLookup
extends JNDITransactionManagerLookup {
	
	/**
	 * @see net.sf.hibernate.transaction.JNDITransactionManagerLookup#getName()
	 */
	protected String getName() {
		return "java:comp/TransactionManager";
	}
	
	public String getUserTransactionName() {
		return "java:comp/UserTransaction";
	}
	
}






