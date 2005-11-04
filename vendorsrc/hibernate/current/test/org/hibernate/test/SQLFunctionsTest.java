//$Id: SQLFunctionsTest.java,v 1.5 2004/07/23 15:21:47 steveebersole Exp $
package org.hibernate.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.ScrollableResults;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.dialect.DB2Dialect;
import net.sf.hibernate.dialect.HSQLDialect;
import net.sf.hibernate.dialect.InterbaseDialect;
import net.sf.hibernate.dialect.MckoiDialect;
import net.sf.hibernate.dialect.MySQLDialect;
import net.sf.hibernate.dialect.Oracle9Dialect;
import net.sf.hibernate.dialect.PostgreSQLDialect;
import net.sf.hibernate.dialect.SybaseDialect;
import net.sf.hibernate.dialect.OracleDialect;
import net.sf.hibernate.dialect.SQLFunction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SQLFunctionsTest extends TestCase {

	private static final Log log = LogFactory.getLog(SQLFunctionsTest.class);

	public SQLFunctionsTest(String arg0) {
		super(arg0);
	}

	public void testDialectSQLFunctions() throws Exception {

		Session s = openSession();
		Transaction t = s.beginTransaction();

		Iterator iter = s.iterate("select max(s.count) from s in class Simple");

		if ( getDialect() instanceof MySQLDialect ) assertTrue( iter.hasNext() && iter.next()==null );

		Simple simple = new Simple();
		simple.setName("Simple Dialect Function Test");
		simple.setAddress("Simple Address");
		simple.setPay(new Float(45.8));
		simple.setCount(2);
		s.save(simple, new Long(10) );

		// Test to make sure allocating an specified object operates correctly.
		assertTrue(
			s.find("select new org.hibernate.test.S(s.count, s.address) from s in class Simple").size() == 1
		);

		// Quick check the base dialect functions operate correctly
		assertTrue(
			s.find("select max(s.count) from s in class Simple").size() == 1
		);
		assertTrue(
			s.find("select count(*) from s in class Simple").size() == 1
		);

		if ( getDialect() instanceof OracleDialect) {
			// Check Oracle Dialect mix of dialect functions - no args (no parenthesis and single arg functions
			java.util.List rset = s.find("select s.name, sysdate, trunc(s.pay), round(s.pay) from s in class Simple");
			assertNotNull("Name string should have been returned",(((Object[])rset.get(0))[0]));
			assertNotNull("Todays Date should have been returned",(((Object[])rset.get(0))[1]));
			assertEquals("trunc(45.8) result was incorrect ", new Float(45), ( (Object[]) rset.get(0) )[2] );
			assertEquals("round(45.8) result was incorrect ", new Float(46), ( (Object[]) rset.get(0) )[3] );

			simple.setPay(new Float(-45.8));
			s.update(simple);

			// Test type conversions while using nested functions (Float to Int).
			rset = s.find("select abs(round(s.pay)) from s in class Simple");
			assertEquals("abs(round(-45.8)) result was incorrect ", new Float(46), rset.get(0));

			// Test a larger depth 3 function example - Not a useful combo other than for testing
			assertTrue(
				s.find("select trunc(round(sysdate)) from s in class Simple").size() == 1
			);

			// Test the oracle standard NVL funtion as a test of multi-param functions...
			simple.setPay(null);
			s.update(simple);
			Integer value = (Integer) s.find("select MOD( NVL(s.pay, 5000), 2 ) from Simple as s where s.id = 10").get(0);
			assertTrue( 0 == value.intValue() );
		}

		if ( (getDialect() instanceof HSQLDialect) ) {
			// Test the hsql standard MOD funtion as a test of multi-param functions...
			Integer value = (Integer) s.find("select MOD(s.count, 2) from Simple as s where s.id = 10" ).get(0);
			assertTrue( 0 == value.intValue() );
		}

		s.delete(simple);
		t.commit();
		s.close();
	}

	public void testSetProperties() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Simple simple = new Simple();
		simple.setName("Simple 1");
		s.save(simple, new Long(10) );
		Query q = s.createQuery("from s in class Simple where s.name=:name and s.count=:count");
		q.setProperties(simple);
		assertTrue( q.list().get(0)==simple );
		s.delete(simple);
		t.commit();
		s.close();
	}

	public void testBroken() throws Exception {
		if (getDialect() instanceof Oracle9Dialect) return;
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Broken b = new Fixed();
		b.setId( new Long(123));
		b.setOtherId("foobar");
		s.save(b);
		s.flush();
		b.setTimestamp( new Date() );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		s.update(b);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		b = (Broken) s.load( Broken.class, b );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		s.delete(b);
		t.commit();
		s.close();
	}

	public void testNothinToUpdate() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Simple simple = new Simple();
		simple.setName("Simple 1");
		s.save( simple, new Long(10) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		s.update( simple, new Long(10) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		s.update( simple, new Long(10) );
		s.delete(simple);
		t.commit();
		s.close();
	}

	public void testCachedQuery() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Simple simple = new Simple();
		simple.setName("Simple 1");
		s.save( simple, new Long(10) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Query q = s.createQuery("from Simple s where s.name=?");
		q.setCacheable(true);
		q.setString(0, "Simple 1");
		assertTrue( q.list().size()==1 );
		assertTrue( q.list().size()==1 );
		assertTrue( q.list().size()==1 );
		q = s.createQuery("from Simple s where s.name=:name");
		q.setCacheable(true);
		q.setString("name", "Simple 1");
		assertTrue( q.list().size()==1 );
		simple = (Simple) q.list().get(0);

		q.setString("name", "Simple 2");
		assertTrue( q.list().size()==0 );
		assertTrue( q.list().size()==0 );
		simple.setName("Simple 2");
		assertTrue( q.list().size()==1 );
		assertTrue( q.list().size()==1 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		q = s.createQuery("from Simple s where s.name=:name");
		q.setString("name", "Simple 2");
		q.setCacheable(true);
		assertTrue( q.list().size()==1 );
		assertTrue( q.list().size()==1 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		s.update( simple, new Long(10) );
		s.delete(simple);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		q = s.createQuery("from Simple s where s.name=?");
		q.setCacheable(true);
		q.setString(0, "Simple 1");
		assertTrue( q.list().size()==0 );
		assertTrue( q.list().size()==0 );
		t.commit();
		s.close();
	}

	public void testSqlFunctionAsAlias() throws Exception {
		String functionName = locateAppropriateDialectFunctionNameForAliasTest();
		if (functionName == null) {
			log.info("Dialect does not list any no-arg functions");
			return;
		}

		log.info("Using function named [" + functionName + "] for 'function as alias' test");
		String query = "select " + functionName + " from Simple as " + functionName + " where " + functionName + ".id = 10";

		Session s = openSession();
		Transaction t = s.beginTransaction();
		Simple simple = new Simple();
		simple.setName("Simple 1");
		s.save( simple, new Long(10) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		List result = s.find(query);
		assertTrue(
		        "Unexpected result type [" + result.get(0).getClass().getName() + "]",
		        result.get(0) instanceof Simple
		);
		s.delete( result.get(0) );
		t.commit();
		s.close();
	}

	private String locateAppropriateDialectFunctionNameForAliasTest() {
		for (Iterator itr = getDialect().getFunctions().entrySet().iterator(); itr.hasNext(); ) {
			final Map.Entry entry = (Map.Entry) itr.next();
			final SQLFunction function = (SQLFunction) entry.getValue();
			if ( !function.hasArguments() && !function.hasParenthesesIfNoArguments() ) {
				return (String) entry.getKey();
			}
		}
		return null;
	}

	public void testCachedQueryOnInsert() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Simple simple = new Simple();
		simple.setName("Simple 1");
		s.save( simple, new Long(10) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Query q = s.createQuery("from Simple s");
		List list = q.setCacheable(true).list();
		assertTrue( list.size()==1 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		q = s.createQuery("from Simple s");
		list = q.setCacheable(true).list();
		assertTrue( list.size()==1 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Simple simple2 = new Simple();
		simple2.setCount(133);
		s.save( simple2, new Long(12) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		q = s.createQuery("from Simple s");
		list = q.setCacheable(true).list();
		assertTrue( list.size()==2 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		q = s.createQuery("from Simple s");
		list = q.setCacheable(true).list();
		assertTrue( list.size()==2 );
		Iterator i = list.iterator();
		while ( i.hasNext() ) s.delete( i.next() );
		t.commit();
		s.close();

	}

	public void testCachedQueryRegion() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Simple simple = new Simple();
		simple.setName("Simple 1");
		s.save( simple, new Long(10) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		Query q = s.createQuery("from Simple s where s.name=?");
		q.setCacheRegion("foo");
		q.setCacheable(true);
		q.setString(0, "Simple 1");
		assertTrue( q.list().size()==1 );
		assertTrue( q.list().size()==1 );
		assertTrue( q.list().size()==1 );
		q = s.createQuery("from Simple s where s.name=:name");
		q.setCacheRegion("foo");
		q.setCacheable(true);
		q.setString("name", "Simple 1");
		assertTrue( q.list().size()==1 );
		simple = (Simple) q.list().get(0);

		q.setString("name", "Simple 2");
		assertTrue( q.list().size()==0 );
		assertTrue( q.list().size()==0 );
		simple.setName("Simple 2");
		assertTrue( q.list().size()==1 );
		assertTrue( q.list().size()==1 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		s.update( simple, new Long(10) );
		s.delete(simple);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		q = s.createQuery("from Simple s where s.name=?");
		q.setCacheRegion("foo");
		q.setCacheable(true);
		q.setString(0, "Simple 1");
		assertTrue( q.list().size()==0 );
		assertTrue( q.list().size()==0 );
		t.commit();
		s.close();
	}

	public void testSQLFunctions() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Simple simple = new Simple();
		simple.setName("Simple 1");
		s.save(simple, new Long(10) );

		if ( getDialect() instanceof DB2Dialect) {
			s.find("from s in class Simple where repeat('foo', 3) = 'foofoofoo'");
			s.find("from s in class Simple where repeat(s.name, 3) = 'foofoofoo'");
			s.find("from s in class Simple where repeat( lower(s.name), 3 + (1-1) / 2) = 'foofoofoo'");
		}

		assertTrue(
			s.find("from s in class Simple where upper( s.name ) ='SIMPLE 1'").size()==1
		);
		if ( !( getDialect() instanceof HSQLDialect ) ) {
			assertTrue(
				s.find("from s in class Simple where not( upper( s.name ) ='yada' or 1=2 or 'foo'='bar' or not('foo'='foo') or 'foo' like 'bar' )").size()==1
			);
		}
		if ( !(getDialect() instanceof MySQLDialect) && !(getDialect() instanceof SybaseDialect) && !(getDialect() instanceof MckoiDialect) && !(getDialect() instanceof InterbaseDialect) ) { //My SQL has a funny concatenation operator
			assertTrue(
				s.find("from s in class Simple where lower( s.name || ' foo' ) ='simple 1 foo'").size()==1
			);
		}
		if ( (getDialect() instanceof SybaseDialect) ) {
			assertTrue(
				s.find("from s in class Simple where lower( s.name + ' foo' ) ='simple 1 foo'").size()==1
			);
		}
		if ( (getDialect() instanceof MckoiDialect) ) {
			assertTrue(
				s.find("from s in class Simple where lower( concat(s.name, ' foo') ) ='simple 1 foo'").size()==1
			);
		}

		Simple other = new Simple();
		other.setName("Simple 2");
		other.setCount(12);
		simple.setOther(other);
		s.save( other, new Long(20) );
		//s.find("from s in class Simple where s.name ## 'cat|rat|bag'");
		assertTrue(
			s.find("from s in class Simple where upper( s.other.name ) ='SIMPLE 2'").size()==1
		);
		assertTrue(
			s.find("from s in class Simple where not ( upper( s.other.name ) ='SIMPLE 2' )").size()==0
		);
		assertTrue(
			s.find("select distinct s from s in class Simple where ( ( s.other.count + 3 ) = (15*2)/2 and s.count = 69) or ( ( s.other.count + 2 ) / 7 ) = 2").size()==1
		);
		assertTrue(
			s.find("select s from s in class Simple where ( ( s.other.count + 3 ) = (15*2)/2 and s.count = 69) or ( ( s.other.count + 2 ) / 7 ) = 2 order by s.other.count").size()==1
		);
		Simple min = new Simple();
		min.setCount(-1);
		s.save(min, new Long(30) );
		if ( ! (getDialect() instanceof MySQLDialect) && ! (getDialect() instanceof HSQLDialect) ) { //My SQL has no subqueries
			assertTrue(
				s.find("from s in class Simple where s.count > ( select min(sim.count) from sim in class Simple )").size()==2
			);
			t.commit();
			t = s.beginTransaction();
			assertTrue(
				s.find("from s in class Simple where s = some( select sim from sim in class Simple where sim.count>=0 ) and s.count >= 0").size()==2
			);
			assertTrue(
				s.find("from s in class Simple where s = some( select sim from sim in class Simple where sim.other.count=s.other.count ) and s.other.count > 0").size()==1
			);
		}

		Iterator iter = s.iterate("select sum(s.count) from s in class Simple group by s.count having sum(s.count) > 10");
		assertTrue( iter.hasNext() );
		assertTrue( new Integer(12).equals( iter.next() ) );
		assertTrue( !iter.hasNext() );
		if ( ! (getDialect() instanceof MySQLDialect) ) {
			iter = s.iterate("select s.count from s in class Simple group by s.count having s.count = 12");
			assertTrue( iter.hasNext() );
		}

		s.iterate("select s.id, s.count, count(t), max(t.date) from s in class Simple, t in class Simple where s.count = t.count group by s.id, s.count order by s.count");

		Query q = s.createQuery("from s in class Simple");
		q.setMaxResults(10);
		assertTrue( q.list().size()==3 );
		q = s.createQuery("from s in class Simple");
		q.setMaxResults(1);
		assertTrue( q.list().size()==1 );
		q = s.createQuery("from s in class Simple");
		assertTrue( q.list().size()==3 );
		q = s.createQuery("from s in class Simple where s.name = ?");
		q.setString(0, "Simple 1");
		assertTrue( q.list().size()==1 );
		q = s.createQuery("from s in class Simple where s.name = ? and upper(s.name) = ?");
		q.setString(1, "SIMPLE 1");
		q.setString(0, "Simple 1");
		q.setFirstResult(0);
		assertTrue( q.iterate().hasNext() );
		q = s.createQuery("from s in class Simple where s.name = :foo and upper(s.name) = :bar or s.count=:count or s.count=:count + 1");
		q.setParameter("bar", "SIMPLE 1");
		q.setString("foo", "Simple 1");
		q.setInteger("count", 69);
		q.setFirstResult(0);
		assertTrue( q.iterate().hasNext() );
		q = s.createQuery("select s.id from s in class Simple");
		q.setFirstResult(1);
		q.setMaxResults(2);
		iter = q.iterate();
		int i=0;
		while ( iter.hasNext() ) {
			assertTrue( iter.next() instanceof Long );
			i++;
		}
		assertTrue(i==2);
		q = s.createQuery("select all s, s.other from s in class Simple where s = :s");
		q.setParameter("s", simple);
		assertTrue( q.list().size()==1 );


		q = s.createQuery("from s in class Simple where s.name in (:name_list) and s.count > :count");
		HashSet set = new HashSet();
		set.add("Simple 1"); set.add("foo");
		q.setParameterList( "name_list", set );
		q.setParameter("count", new Integer(-1) );
		assertTrue( q.list().size()==1 );

		ScrollableResults sr = s.createQuery("select s.id from Simple s").scroll();
		sr.next();
		sr.getLong(0);

		s.delete(other);
		s.delete(simple);
		s.delete(min);
		t.commit();
		s.close();

	}

	public void testBlobClob() throws Exception {

		if (
			getDialect() instanceof MySQLDialect ||
			getDialect() instanceof PostgreSQLDialect ||
			getDialect() instanceof HSQLDialect ||
			getDialect() instanceof OracleDialect
		) return;

		Session s = openSession();
		Blobber b = new Blobber();
		b.setBlob( Hibernate.createBlob( "foo/bar/baz".getBytes() ) );
		b.setClob( Hibernate.createClob("foo/bar/baz") );
		s.save(b);
		//s.refresh(b);
		//assertTrue( b.getClob() instanceof ClobImpl );
		s.flush();
		s.refresh(b);
		//b.getBlob().setBytes( 2, "abc".getBytes() );
		b.getClob().getSubString(2, 3);
		//b.getClob().setString(2, "abc");
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		b = (Blobber) s.load( Blobber.class, new Integer( b.getId() ) );
		Blobber b2 = new Blobber();
		s.save(b2);
		b2.setBlob( b.getBlob() );
		b.setBlob(null);
		//assertTrue( b.getClob().getSubString(1, 3).equals("fab") );
		b.getClob().getSubString(1, 6);
		//b.getClob().setString(1, "qwerty");
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		b = (Blobber) s.load( Blobber.class, new Integer( b.getId() ) );
		b.setClob( Hibernate.createClob("xcvfxvc xcvbx cvbx cvbx cvbxcvbxcvbxcvb") );
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		b = (Blobber) s.load( Blobber.class, new Integer( b.getId() ) );
		assertTrue( b.getClob().getSubString(1, 7).equals("xcvfxvc") );
		//b.getClob().setString(5, "1234567890");
		s.flush();
		s.connection().commit();
		s.close();


		/*InputStream is = getClass().getClassLoader().getResourceAsStream("jdbc20.pdf");
		s = sessionsopenSession();
		b = (Blobber) s.load( Blobber.class, new Integer( b.getId() ) );
		System.out.println( is.available() );
		int size = is.available();
		b.setBlob( Hibernate.createBlob( is, is.available() ) );
		s.flush();
		s.connection().commit();
		ResultSet rs = s.connection().createStatement().executeQuery("select datalength(blob_) from blobber where id=" + b.getId() );
		rs.next();
		assertTrue( size==rs.getInt(1) );
		rs.close();
		s.close();

		s = sessionsopenSession();
		b = (Blobber) s.load( Blobber.class, new Integer( b.getId() ) );
		File f = new File("C:/foo.pdf");
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		Blob blob = b.getBlob();
		byte[] bytes = blob.getBytes( 1, (int) blob.length() );
		System.out.println( bytes.length );
		fos.write(bytes);
		fos.flush();
		fos.close();
		s.close();*/

	}


	public String[] getMappings() {
		return new String[] {
			"AltSimple.hbm.xml",
			"Broken.hbm.xml",
			"Blobber.hbm.xml"
		};
	}

	public static Test suite() {
		return new TestSuite(SQLFunctionsTest.class);
	}

	public static void main(String[] args) throws Exception {
		TestRunner.run( suite() );
	}

}







