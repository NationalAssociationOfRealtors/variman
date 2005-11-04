//$Id: JBossTransactionManagerLookup.java,v 1.7 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.transaction;

/**
 * A <tt>TransactionManager</tt> lookup strategy for JBoss
 * @author Gavin King
 */
public final class JBossTransactionManagerLookup extends JNDITransactionManagerLookup {
	
	protected String getName() {
		return "java:/TransactionManager";
	}
	
	public String getUserTransactionName() {
		return "UserTransaction";
	}
	
}






