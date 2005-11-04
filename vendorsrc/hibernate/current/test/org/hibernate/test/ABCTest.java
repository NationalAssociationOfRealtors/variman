//$Id: ABCTest.java,v 1.5 2004/06/07 16:42:27 steveebersole Exp $
package org.hibernate.test;


import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public class ABCTest extends TestCase {
	
	public ABCTest(String arg0) {
		super(arg0);
	}

	public void testHigherLevelIndexDefinition() throws Throwable {
		String[] commands = getCfg().generateSchemaCreationScript( getDialect() );
		int max = commands.length;
		boolean found = false;
		for (int indx = 0; indx < max; indx++) {
			System.out.println("Checking command : " + commands[indx]);
			found = commands[indx].indexOf("create index indx_a_name") >= 0;
			if (found)
				break;
		}
		assertTrue("Unable to locate indx_a_name index creation", found);
	}

	public void testSubselect() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		B b = new B();
		Map map = new HashMap();
		map.put("a", "a");
		map.put("b", "b");
		b.setMap(map);
		s.save(b);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		b = (B) s.createQuery("from B").uniqueResult();
		t.commit();
		s.close();
	}
	
	public void testSubclassing() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		C1 c1 = new C1();
		D d = new D();
		d.setAmount(213.34f);
		c1.setAddress("foo bar");
		c1.setCount(23432);
		c1.setName("c1");
		c1.setD(d);
		s.save(c1);
		d.setId( c1.getId() );
		s.save(d);
		
		assertTrue( s.find("from c in class C2 where 1=1 or 1=1").size()==0 );
		
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		c1 = (C1) s.load( A.class, c1.getId() );
		assertTrue(
			c1.getAddress().equals("foo bar") &&
			(c1.getCount()==23432) &&
			c1.getName().equals("c1") &&
			c1.getD().getAmount()>213.3f
		);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		c1 = (C1) s.load( B.class, c1.getId() );
		assertTrue(
			c1.getAddress().equals("foo bar") &&
			(c1.getCount()==23432) &&
			c1.getName().equals("c1") &&
			c1.getD().getAmount()>213.3f
		);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		c1 = (C1) s.load( C1.class, c1.getId() );
		assertTrue(
			c1.getAddress().equals("foo bar") &&
			(c1.getCount()==23432) &&
			c1.getName().equals("c1") &&
			c1.getD().getAmount()>213.3f
		);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		s.find("from b in class B");
		t.commit();
		s.close();
	}
	

	public String[] getMappings() {
		return new String[] { "ABC.hbm.xml", "ABCExtends.hbm.xml" };
	}

	public static Test suite() {
		return new TestSuite(ABCTest.class);
	}
	
	public static void main(String[] args) throws Exception {
		TestRunner.run( suite() );
	}

}




