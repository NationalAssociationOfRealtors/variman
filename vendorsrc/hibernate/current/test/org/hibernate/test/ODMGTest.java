//$Id: ODMGTest.java,v 1.2 2004/06/04 01:28:53 steveebersole Exp $
package org.hibernate.test;

import java.util.List;

import org.odmg.Database;
import org.odmg.OQLQuery;
import org.odmg.Transaction;

import net.sf.hibernate.odmg.Implementation;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ODMGTest extends TestCase {
	
	public void testOdmgApi() throws Exception {
		Database db = Implementation.getInstance().newDatabase();
		( (net.sf.hibernate.odmg.Database) db ).open( getSessions() );
		Transaction tx = Implementation.getInstance().newTransaction();
		tx.begin();
		FooProxy foo = new Foo();
		db.bind(foo, "foo");
		tx.commit();
		
		tx = Implementation.getInstance().newTransaction();
		tx.begin();
		foo = (FooProxy) db.lookup("foo");
		foo.setString("foo string");
		tx.commit();
		
		Implementation.getInstance().newTransaction().begin();
		OQLQuery q = Implementation.getInstance().newOQLQuery();
		q.create("from foo in class Foo where foo.string=?");
		q.bind("foo string");
		foo = (FooProxy) ( (List) q.execute() ).get(0);
		assertTrue( foo.getString().equals("foo string") );
		db.deletePersistent(foo);
		Implementation.getInstance().currentTransaction().commit();
		
		tx = Implementation.getInstance().newTransaction();
		tx.begin();
		q = new net.sf.hibernate.odmg.OQLQuery();
		q.create("from foo in class Foo");
		assertTrue( ( (List) q.execute() ).isEmpty() );
		tx.commit();
		db.close();
	}
	
	
	public ODMGTest(String arg) {
		super(arg);
	}
	
	public String[] getMappings() {
		return new String[] {
			"net/sf/hibernate/odmg/Name.hbm.xml",
			"FooBar.hbm.xml",
			"Baz.hbm.xml",
			"Qux.hbm.xml",
			"Glarch.hbm.xml",
			"Fum.hbm.xml",
			"Fumm.hbm.xml",
			"Fo.hbm.xml",
			"One.hbm.xml",
			"Many.hbm.xml",
			"Immutable.hbm.xml",
			"Fee.hbm.xml",
			"Vetoer.hbm.xml",
			"Holder.hbm.xml",
			"Location.hbm.xml",
			"Stuff.hbm.xml",
			"Container.hbm.xml",
			"Simple.hbm.xml"
		};		
	}
	
	public static Test suite() {
		return new TestSuite(ODMGTest.class);
	}
	
}






