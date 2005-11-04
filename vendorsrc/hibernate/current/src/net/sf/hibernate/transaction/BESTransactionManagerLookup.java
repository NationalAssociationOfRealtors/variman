//$Id: BESTransactionManagerLookup.java,v 1.1 2004/06/20 19:33:56 turin42 Exp $
package net.sf.hibernate.transaction;

/**
 * A <tt>TransactionManager</tt> lookup strategy for Borland ES.
 * @author Etienne Hardy
 */
public final class BESTransactionManagerLookup extends JNDITransactionManagerLookup {

	protected String getName() {
		return "java:comp/UserTransaction";
	}
	
	public String getUserTransactionName() {
		return "java:pm/TransactionManager";
	}
	
}






