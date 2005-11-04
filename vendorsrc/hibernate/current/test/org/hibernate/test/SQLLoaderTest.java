//$Id: SQLLoaderTest.java,v 1.4 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.dialect.MySQLDialect;
import net.sf.hibernate.dialect.Oracle9Dialect;

public class SQLLoaderTest extends TestCase {

	static int nextInt = 1;
	static long nextLong = 1;
	public SQLLoaderTest(String arg) {
		super(arg);
	}
	
	public void testTS() throws Exception {
		if (getDialect() instanceof Oracle9Dialect) return;
		Session session = openSession();
		Simple sim = new Simple();
		sim.setDate( new Date() );
		session.save( sim, new Long(1) );
		Query q = session.createSQLQuery("select {sim.*} from Simple {sim} where {sim}.date_ = ?", "sim", Simple.class);
		q.setTimestamp( 0, sim.getDate() );
		assertTrue ( q.list().size()==1 );
		session.delete(sim);
		session.flush();
		session.connection().commit();
		session.close();    
	} 
	

	public void testFindBySQLStar() throws HibernateException, SQLException {
		Session session = openSession();
		session.delete("from Assignable");
		session.delete("from Category");
		session.delete("from Simple");
		session.delete("from A");

		Category s = new Category();
		s.setName(String.valueOf(nextLong++));
		session.save(s);

		Simple simple = new Simple();
		simple.init();
		session.save(simple, new Long(nextLong++));

		A a = new A();
		session.save(a);

		B b = new B();
		session.save(b);
		session.flush();

		session.createSQLQuery("select {category.*} from Category {category}", "category", Category.class).list();
		session.createSQLQuery("select {simple.*} from Simple {simple}", "simple", Simple.class).list();
		session.createSQLQuery("select {a.*} from A {a}", "a", A.class).list();

		session.connection().commit();
		session.close();

	}

	public void testFindBySQLProperties() throws HibernateException, SQLException {
			Session session = openSession();
			session.delete("from Category");
			
			Category s = new Category();
			s.setName(String.valueOf(nextLong++));
			session.save(s);

			s = new Category();
			s.setName("WannaBeFound");
			session.flush();

			Query query = session.createSQLQuery("select {category.*} from Category {category} where {category}.name = :name", "category", Category.class);
			
			query.setProperties(s);
			//query.setParameter("name", s.getName());
			
			query.list();

			session.connection().commit();
			session.close();

		}
	
	public void testFindBySQLAssociatedObjects() throws HibernateException, SQLException {
		Session s = openSession();
		s.delete("from Assignable");
		s.delete("from Category");

		Category c = new Category();
		c.setName("NAME");
		Assignable assn = new Assignable();
		assn.setId("i.d.");
		List l = new ArrayList();
		l.add(c);
		assn.setCategories(l);
		c.setAssignable(assn);
		s.save(assn);
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		List list = s.createSQLQuery("select {category.*} from Category {category}", "category", Category.class).list();
		list.get(0);
		s.connection().commit();
		s.close();
	}

	public void testFindBySQLMultipleObject() throws HibernateException, SQLException {
		Session s = openSession();
		s.delete("from Assignable");
		s.delete("from Category");
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		Category c = new Category();
		c.setName("NAME");
		Assignable assn = new Assignable();
		assn.setId("i.d.");
		List l = new ArrayList();
		l.add(c);
		assn.setCategories(l);
		c.setAssignable(assn);
		s.save(assn);
		s.flush();
		c = new Category();
		c.setName("NAME2");
		assn = new Assignable();
		assn.setId("i.d.2");
		l = new ArrayList();
		l.add(c);
		assn.setCategories(l);
		c.setAssignable(assn);
		s.save(assn);
		s.flush();

		assn = new Assignable();
		assn.setId("i.d.3");
		s.save(assn);
		s.flush();
		s.connection().commit();
		s.close();
		
		if ( getDialect() instanceof MySQLDialect ) return;

		s = openSession();
		List list = s.createSQLQuery("select {category.*}, {assignable.*} from Category {category}, \"assign able\" {assignable}", new String[] { "category", "assignable" }, new Class[] { Category.class, Assignable.class }).list();

		assertTrue(list.size() == 6); // crossproduct of 2 categories x 3 assignables
		assertTrue(list.get(0) instanceof Object[]);
		s.connection().commit();
		s.close();
	}

	public void testFindBySQLParameters() throws HibernateException, SQLException {
		Session s = openSession();
		s.delete("from Assignable");
		s.delete("from Category");
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		Category c = new Category();
		c.setName("Good");
		Assignable assn = new Assignable();
		assn.setId("i.d.");
		List l = new ArrayList();
		l.add(c);
		assn.setCategories(l);
		c.setAssignable(assn);
		s.save(assn);
		s.flush();
		c = new Category();
		c.setName("Best");
		assn = new Assignable();
		assn.setId("i.d.2");
		l = new ArrayList();
		l.add(c);
		assn.setCategories(l);
		c.setAssignable(assn);
		s.save(assn);
		s.flush();
		c = new Category();
		c.setName("Better");
		assn = new Assignable();
		assn.setId("i.d.7");
		l = new ArrayList();
		l.add(c);
		assn.setCategories(l);
		c.setAssignable(assn);
		s.save(assn);
		s.flush();

		assn = new Assignable();
		assn.setId("i.d.3");
		s.save(assn);
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		Query basicParam = s.createSQLQuery("select {category.*} from Category {category} where {category}.name = 'Best'", "category", Category.class);
		List list = basicParam.list();
		assertEquals(1, list.size());

		Query unnamedParam = s.createSQLQuery("select {category.*} from Category {category} where {category}.name = ? or {category}.name = ?", "category", Category.class);
		unnamedParam.setString(0, "Good");
		unnamedParam.setString(1, "Best");
		list = unnamedParam.list();
		assertEquals(2, list.size());
		
		Query namedParam = s.createSQLQuery("select {category.*} from Category {category} where ({category}.name=:firstCat or {category}.name=:secondCat)", "category", Category.class);
		namedParam.setString("firstCat", "Better");
		namedParam.setString("secondCat", "Best");
		list = namedParam.list();
		assertEquals(2, list.size());
		
		s.connection().commit();
		s.close();
	}

	public void testEscapedJDBC() throws HibernateException, SQLException {
		Session session = openSession();
		session.delete("from A");
		A savedA = new A();
		savedA.setName("Max");
		session.save(savedA);

		B savedB = new B();
		session.save(savedB);
		session.flush();

		int count = session.createQuery("from A").list().size();
		session.close();

		session = openSession();

		Query query = session.createSQLQuery("select identifier_column as {a.id}, clazz_discriminata as {a.class}, count_ as {a.count}, name as {a.name} from A where {fn ucase(name)} like {fn ucase('max')}", "a", A.class);
		List list = query.list();

		assertNotNull(list);
		assertEquals(1, list.size());
		session.connection().commit();
		session.close();				
	}
	
	public void testDoubleAliasing() throws HibernateException, SQLException {
		Session session = openSession();
		session.delete("from A");
		A savedA = new A();
		savedA.setName("Max");
		session.save(savedA);

		B savedB = new B();
		session.save(savedB);
		session.flush();

		int count = session.createQuery("from A").list().size();
		session.close();

		session = openSession();

		Query query = session.createSQLQuery("select a.identifier_column as {a1.id}, a.clazz_discriminata as {a1.class}, a.count_ as {a1.count}, a.name as {a1.name} " +
											", b.identifier_column as {a2.id}, b.clazz_discriminata as {a2.class}, b.count_ as {a2.count}, b.name as {a2.name} " +
											" from A a, A b" +
											" where a.identifier_column = b.identifier_column", new String[] {"a1", "a2" }, new Class[] {A.class, A.class});
		List list = query.list();

		assertNotNull(list);
		assertEquals(2, list.size());
		session.connection().commit();
		session.close();						
	}
	
	// TODO: compositeid's - how ? (SingleSeveral.hbm.xml test)
	public void testEmbeddedCompositeProperties() throws HibernateException, SQLException {
	   Session session = openSession();
	   
	   Single s = new Single();
	   s.setId("my id");
	   s.setString("string 1");
	   session.save(s);
	   session.flush();
	   session.connection().commit();
	   
	   session.clear();
	   
	   Query query = session.createSQLQuery("select {sing.*} from Single {sing}", "sing", Single.class);
	   
	   List list = query.list();
	   
	   assertTrue(list.size()==1);
	   
	   session.clear();
	   
	   query = session.createSQLQuery("select {sing.*} from Single {sing} where sing.id = ?", "sing", Single.class);
	   query.setString(0, "my id");
	   list = query.list();
	   
	   assertTrue(list.size()==1);
	   
	   session.clear();
	  
	   query = session.createSQLQuery("select s.id as {sing.id}, s.string_ as {sing.string}, s.prop as {sing.prop} from Single s where s.id = ?", "sing", Single.class);
	   query.setString(0, "my id");
	   list = query.list();
	   
	   assertTrue(list.size()==1);
	   
	   
	   session.clear();
	   
	   query = session.createSQLQuery("select s.id as {sing.id}, s.string_ as {sing.string}, s.prop as {sing.prop} from Single s where s.id = ?", "sing", Single.class);
	   query.setString(0, "my id");
	   list = query.list();
	   
	   assertTrue(list.size()==1);
	   
	   session.connection().commit();
	   session.close();
	   
	}

	public void testFindSimpleBySQL() throws Exception {
		if ( getDialect() instanceof MySQLDialect ) return;
		Session session = openSession();
		Category s = new Category();
		s.setName(String.valueOf(nextLong++));
		session.save(s);
		session.flush();

		Query query = session.createSQLQuery("select s.category_key_col as {category.id}, s.name as {category.name}, s.\"assign able id\" as {category.assignable} from {category} s", "category", Category.class);
		List list = query.list();

		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertTrue(list.get(0) instanceof Category);
		session.connection().commit();
		session.close();
		// How do we handle objects with composite id's ? (such as Single)
	}

	public void testFindBySQLSimpleByDiffSessions() throws Exception {
		Session session = openSession();
		Category s = new Category();
		s.setName(String.valueOf(nextLong++));
		session.save(s);
		session.flush();
		session.connection().commit();
		session.close();
		
		if ( getDialect() instanceof MySQLDialect ) return;

		session = openSession();

		Query query = session.createSQLQuery("select s.category_key_col as {category.id}, s.name as {category.name}, s.\"assign able id\" as {category.assignable} from {category} s", "category", Category.class);
		List list = query.list();

		assertNotNull(list);
		assertTrue(list.size() > 0);
		assertTrue(list.get(0) instanceof Category);

		// How do we handle objects that does not have id property (such as Simple ?)
		// How do we handle objects with composite id's ? (such as Single)
		session.connection().commit();
		session.close();
	}

	public void testFindBySQLDiscriminatedSameSession() throws Exception {
		Session session = openSession();
		session.delete("from A");
		A savedA = new A();
		session.save(savedA);

		B savedB = new B();
		session.save(savedB);
		session.flush();

		Query query = session.createSQLQuery("select identifier_column as {a.id}, clazz_discriminata as {a.class}, name as {a.name}, count_ as {a.count} from {a} s", "a", A.class);
		List list = query.list();

		assertNotNull(list);
		assertEquals(2, list.size());

		A a1 = (A) list.get(0);
		A a2 = (A) list.get(1);

		assertTrue((a2 instanceof B) || (a1 instanceof B));
		assertFalse(a1 instanceof B && a2 instanceof B);

		if (a1 instanceof B) {
			assertSame(a1, savedB);
			assertSame(a2, savedA);
		} 
		else {
			assertSame(a2, savedB);
			assertSame(a1, savedA);
		}
		
		session.connection().commit();
		session.close();
	}

	public void testFindBySQLDiscriminatedDiffSession() throws Exception {
		Session session = openSession();
		session.delete("from A");
		A savedA = new A();
		session.save(savedA);

		B savedB = new B();
		session.save(savedB);
		session.flush();

		int count = session.createQuery("from A").list().size();
		session.close();

		session = openSession();

		Query query = session.createSQLQuery("select identifier_column as {a.id}, clazz_discriminata as {a.class}, count_ as {a.count}, name as {a.name} from A", "a", A.class);
		List list = query.list();

		assertNotNull(list);
		assertEquals(count, list.size());
		session.connection().commit();
		session.close();
	}
	
	public void testNamedSQLQuery() throws HibernateException, SQLException {
		if ( getDialect() instanceof MySQLDialect ) return;
		Session s = openSession();
		
		Query query = s.getNamedQuery("namedsql");
		assertNotNull(query);
		assertNotNull(query.list());
		s.connection().commit();
		s.close();
	}

	
	public String[] getMappings() {
		return new String[] { 
			"ABC.hbm.xml", 
			"Category.hbm.xml", 
			"Simple.hbm.xml", 
			"Fo.hbm.xml", 
			"SingleSeveral.hbm.xml"
		};
	}

	public static Test suite() {
		return new TestSuite(SQLLoaderTest.class);
	}

	public static void main(String[] args) throws Exception {
		TestRunner.run( suite() );
	}

}
