//$Id: SunONETransactionManagerLookup.java,v 1.6 2004/06/04 05:43:48 steveebersole Exp $
package net.sf.hibernate.transaction;

/**
 * TransactionManager lookup strategy for Sun ONE Application Server 7
 * @author Robert Davidson
 */
public class SunONETransactionManagerLookup extends JNDITransactionManagerLookup {

	protected String getName() {
		return "java:/TransactionManager";
	}

	public String getUserTransactionName() {
		return "UserTransaction";
	}
}
