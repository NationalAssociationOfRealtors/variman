//$Id: WeblogicTransactionManagerLookup.java,v 1.8 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.transaction;

/**
 * TransactionManager lookup strategy for WebLogic
 * @author Gavin King
 */
public final class WeblogicTransactionManagerLookup extends JNDITransactionManagerLookup {
	
	/**
	 * @see net.sf.hibernate.transaction.JNDITransactionManagerLookup#getName()
	 */
	protected String getName() {
		return "javax.transaction.TransactionManager";
	}
	
	public String getUserTransactionName() {
		return "javax.transaction.UserTransaction";
	}
	
}






