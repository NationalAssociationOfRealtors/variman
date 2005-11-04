//$Id: MasterDetailTest.java,v 1.4 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.dialect.HSQLDialect;
import net.sf.hibernate.dialect.MckoiDialect;
import net.sf.hibernate.dialect.MySQLDialect;
import net.sf.hibernate.dialect.SAPDBDialect;
import net.sf.hibernate.dialect.SybaseDialect;
import net.sf.hibernate.expression.Example;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.mapping.MetaAttribute;
import net.sf.hibernate.mapping.PersistentClass;

public class MasterDetailTest extends TestCase {
	
	public MasterDetailTest(String arg) {
		super(arg);
	}
	
	public void testParentChildren() throws Exception {

	      Session session = openSession();

	      M parent = new M();
	      N n = new N();
	      n.setString(Integer.toString(0));
	      parent.addChildren(n);
	      session.saveOrUpdate(parent);
	      parent.removeChildren(n);
	      
	      session.flush();
	      session.connection().commit();

	      session.close();
	      
	      session = openSession();
	      assertTrue( session.find("from N").size()==0 );
	      session.delete("from M");
	      session.flush();
	      session.connection().commit();

	      session.close();
	}
	
	public void testOuterJoin() throws Exception {
		Session s = openSession();
		Eye e = new Eye();
		e.setName("Eye Eye");
		Jay jay = new Jay(e);
		e.setJay(jay);
		s.saveOrUpdate(e);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		e = (Eye) s.createCriteria(Eye.class).uniqueResult();
		assertTrue( Hibernate.isInitialized( e.getJay() ) );
		assertTrue( Hibernate.isInitialized( e.getJays() ) );
		s.connection().commit();
		s.close();
		
		s = openSession();
		jay = (Jay) s.createQuery("select new Jay(eye) from Eye eye").uniqueResult();
		assertTrue( "Eye Eye".equals( jay.getEye().getName() ) );
		s.delete( jay.getEye() );
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testMeta() throws Exception {
		PersistentClass clazz = getCfg().getClassMapping(Master.class);
		MetaAttribute meta = clazz.getMetaAttribute("foo");
		assertTrue( "foo".equals( meta.getValue() ) );
		meta = clazz.getProperty("name").getMetaAttribute("bar");
		assertTrue( meta.isMultiValued() );
	}
	
	public void testPolymorphicCriteria() throws Exception {
		Session s = openSession();
		Category f = new Category();
		Single b = new Single();
		b.setId("asdfa");
		b.setString("asdfasdf");
		s.save(f);
		s.save(b);
		List list = s.createCriteria(Object.class).list();
		assertTrue( list.size()==2 );
		assertTrue( list.contains(f) && list.contains(b) );
		s.delete(f);
		s.delete(b);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCopy() throws Exception {
		Category catWA = new Category();
		catWA.setName("HSQL workaround");
		Category cat = new Category();
		cat.setName("foo");
		Category subCatBar = new Category();
		subCatBar.setName("bar");
		Category subCatBaz = new Category();
		subCatBaz.setName("baz");
		cat.getSubcategories().add(subCatBar);
		cat.getSubcategories().add(subCatBaz);
		Session s = openSession();
		s.save(catWA);
		s.save(cat);
		s.flush();
		s.connection().commit();
		s.close();
		
		cat.setName("new foo");
		subCatBar.setName("new bar");
		cat.getSubcategories().remove(subCatBaz);
		Category newCat = new Category();
		newCat.setName("new");
		cat.getSubcategories().add(newCat);
		Category newSubCat = new Category();
		newSubCat.setName("new sub");
		newCat.getSubcategories().add(newSubCat);
		
		s = openSession();
		s.saveOrUpdateCopy(cat);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		cat = (Category) s.createQuery("from Category cat where cat.name='new foo'").uniqueResult();
		newSubCat = (Category) s.createQuery("from Category cat where cat.name='new sub'").uniqueResult();
		newSubCat.getSubcategories().add(cat);
		subCatBaz = (Category) s.saveOrUpdateCopy( newSubCat, new Long( subCatBaz.getId() ) );
		assertTrue( subCatBaz.getName().equals("new sub") );
		assertTrue( subCatBaz.getSubcategories().size()==1 && subCatBaz.getSubcategories().get(0)==cat );
		newSubCat.getSubcategories().remove(cat);
		s.delete(cat);
		s.delete(subCatBaz);
		s.delete(catWA);
		s.flush();
		s.connection().commit();
		s.close();
		
	}
	
	public void testNotNullDiscriminator() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Up up = new Up();
		up.setId1("foo");
		up.setId2(123l);
		Down down = new Down();
		down.setId1("foo");
		down.setId2(321l);
		down.setValue(12312312l);
		s.save(up);
		s.save(down);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		List list = s.find("from Up up order by up.id2 asc");
		assertTrue( list.size()==2 );
		assertFalse( list.get(0) instanceof Down );
		assertTrue( list.get(1) instanceof Down );
		list = s.find("from Down down");
		assertTrue( list.size()==1 );
		assertTrue( list.get(0) instanceof Down );
		//list = s.find("from Up down where down.class = Down");
		assertTrue( list.size()==1 );
		assertTrue( list.get(0) instanceof Down );
		s.delete("from Up up");
		t.commit();
		s.close();
		
	}
	
	public void testSelfManyToOne() throws Exception {
		
		//if (dialect instanceof HSQLDialect) return;
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Master m = new Master();
		m.setOtherMaster(m);
		s.save(m);
		t.commit();
		s.close();
		s = openSession();
		t = s.beginTransaction();
		Iterator i = s.iterate("from m in class Master");
		m = (Master) i.next();
		assertTrue( m.getOtherMaster()==m );
		if (getDialect() instanceof HSQLDialect) { m.setOtherMaster(null); s.flush(); }
		s.delete(m);
		t.commit();
		s.close();
	}
	
	public void testExample() throws Exception {
				
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Master m = new Master();
		m.setName("name");
		m.setX(5);
		m.setOtherMaster(m);
		s.save(m);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		Master m1 = (Master) s.createCriteria(Master.class)
			.add( Example.create(m).enableLike().ignoreCase() )
			.uniqueResult();
		assertTrue( m1.getOtherMaster()==m1 );
		m1 = (Master) s.createCriteria(Master.class)
			.add( Expression.eq("name", "foobar") )
			.uniqueResult();
		assertTrue( m1==null );
		m1 = (Master) s.createCriteria(Master.class)
			.add( Example.create(m) )
			.createCriteria("otherMaster")
				.add( Example.create(m).excludeZeroes() )
			.uniqueResult();
		assertTrue( m1.getOtherMaster()==m1 );
		Master m2 = (Master) s.createCriteria(Master.class)
			.add( Example.create(m).excludeNone() )
			.uniqueResult();
		assertTrue( m2==m1 );
		m.setName(null);
		m2 = (Master) s.createCriteria(Master.class)
			.add( Example.create(m).excludeNone() )
			.uniqueResult();
		assertTrue( null==m2 );
		if (getDialect() instanceof HSQLDialect) { m1.setOtherMaster(null); s.flush(); }
		s.delete(m1);
		t.commit();
		s.close();
	}
	
	public void testNonLazyBidirectional() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Single sin = new Single();
		sin.setId("asdfds");
		sin.setString("adsa asdfasd");
		Several sev = new Several();
		sev.setId("asdfasdfasd");
		sev.setString("asd ddd");
		sin.getSeveral().add(sev);
		sev.setSingle(sin);
		s.save(sin);
		t.commit();
		s.close();
		s = openSession();
		t = s.beginTransaction();
		sin = (Single) s.load( Single.class, sin );
		t.commit();
		s.close();
		s = openSession();
		t = s.beginTransaction();
		sev = (Several) s.load( Several.class, sev );
		t.commit();
		s.close();
		s = openSession();
		t = s.beginTransaction();
		s.find("from s in class Several");
		t.commit();
		s.close();
		s = openSession();
		t = s.beginTransaction();
		s.find("from s in class Single");
		t.commit();
		s.close();
	}
	
	public void testCollectionQuery() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		if ( !(getDialect() instanceof MySQLDialect) && !(getDialect() instanceof SAPDBDialect) && !(getDialect() instanceof MckoiDialect) ) {
			s.iterate("FROM m IN CLASS Master WHERE NOT EXISTS ( FROM d IN m.details.elements WHERE NOT d.i=5 )");
			s.iterate("FROM m IN CLASS Master WHERE NOT 5 IN ( SELECT d.i FROM d IN m.details.elements )");
		}
		s.iterate("SELECT m FROM m IN CLASS Master, d IN m.details.elements WHERE d.i=5");
		s.find("SELECT m FROM m IN CLASS Master, d IN m.details.elements WHERE d.i=5");
		s.find("SELECT m.id FROM m IN CLASS Master, d IN m.details.elements WHERE d.i=5");
		t.commit();
		s.close();
	}
	
	public void testMasterDetail() throws Exception {
		
		if (getDialect() instanceof HSQLDialect) return;
		
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Master master = new Master();
		assertTrue( "save returned native id", s.save(master)!=null );
		Serializable mid = s.getIdentifier(master);
		Detail d1 = new Detail();
		d1.setMaster(master);
		Serializable did = s.save(d1);
		Detail d2 = new Detail();
		d2.setI(12);
		d2.setMaster(master);
		assertTrue( "generated id returned", s.save(d2)!=null);
		master.addDetail(d1);
		master.addDetail(d2);
		if ( !(getDialect() instanceof MySQLDialect) && !(getDialect() instanceof SAPDBDialect) && !(getDialect() instanceof MckoiDialect) ) {
			assertTrue(
				"query",
				s.find("from d in class Detail, m in class Master where m = d.master and m.outgoing.size = 0 and m.incoming.size = 0").size()==2
			);
		}
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		master = new Master();
		s.load(master, mid);
		assertTrue( master.getDetails().size()==2 );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		master = (Master) s.load(Master.class, mid);
		Iterator iter = master.getDetails().iterator();
		int i=0;
		while ( iter.hasNext() ) {
			Detail d = (Detail) iter.next();
			assertTrue( "master-detail", d.getMaster()==master );
			i++;
		}
		assertTrue( "master-detail", i==2 );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		assertTrue( s.find("select elements(master.details) from Master master").size()==2 );
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		List list = s.find("from Master m left join fetch m.details");
		Master m = (Master) list.get(0);
		assertTrue( Hibernate.isInitialized( m.getDetails() ) );
		assertTrue( m.getDetails().size()==2 );
		list = s.find("from Detail d inner join fetch d.master");
		Detail dt = (Detail) list.get(0);
		Serializable dtid = s.getIdentifier(dt);
		assertTrue( dt.getMaster()==m );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		list = s.find("select m from Master m1, Master m left join fetch m.details where m.name=m1.name");
		assertTrue( Hibernate.isInitialized( ( (Master) list.get(0) ).getDetails() ) );
		dt = (Detail) s.load(Detail.class, dtid);
		assertTrue( ( (Master) list.get(0) ).getDetails().contains(dt) );
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		list = s.find("select m, m1.name from Master m1, Master m left join fetch m.details where m.name=m1.name");
		assertTrue( Hibernate.isInitialized( ( (Master) ( (Object[]) list.get(0) )[0] ).getDetails() ) );
		dt = (Detail) s.load(Detail.class, dtid);
		assertTrue( ( (Master) ( (Object[]) list.get(0) )[0] ).getDetails().contains(dt) );
		//list = s.find("select m from Master m, Master m2 left join fetch m.details");
		list = s.find("select m.id from Master m inner join fetch m.details");
		t.commit();
		s.close();


		s = openSession();
		t = s.beginTransaction();
		Detail dd = (Detail) s.load(Detail.class, did);
		master = dd.getMaster();
		assertTrue( "detail-master", master.getDetails().contains(dd) );
		assertTrue( s.filter( master.getDetails(), "order by this.i desc").size()==2 );
		assertTrue( s.filter( master.getDetails(), "select this where this.id > -1").size()==2 );
		Query q = s.createFilter( master.getDetails(), "where this.id > :id" );
		q.setInteger("id", -1);
		assertTrue( q.list().size()==2 );
		q = s.createFilter( master.getDetails(), "where this.id > :id1 and this.id < :id2" );
		q.setInteger("id1", -1);
		q.setInteger("id2", 99999999);
		assertTrue( q.list().size()==2 );
		q.setInteger("id2", -1);
		assertTrue( q.list().size()==0 );
		q = s.createFilter( master.getDetails(), "where this.id in (:ids)" );
		list = new ArrayList();
		list.add(did);
		list.add( new Long(-1) );
		q.setParameterList("ids", list);
		assertTrue( q.list().size()==1 );
		assertTrue( q.iterate().hasNext() );
		assertTrue( s.filter( master.getDetails(), "where this.id > -1").size()==2 );
		assertTrue( s.filter( master.getDetails(), "select this.master where this.id > -1").size()==2 );
		assertTrue( s.filter( master.getDetails(), "select m from m in class Master where this.id > -1 and this.master=m").size()==2 );
		assertTrue( s.filter( master.getIncoming(), "where this.id > -1 and this.name is not null").size()==0 );
		
		assertTrue( s.createFilter( master.getDetails(), "select max(this.i)" ).iterate().next() instanceof Integer );
		assertTrue( s.createFilter( master.getDetails(), "select max(this.i) group by this.id" ).iterate().next() instanceof Integer );
		assertTrue( s.createFilter( master.getDetails(), "select count(*)" ).iterate().next() instanceof Integer );
		
		assertTrue( s.createFilter( master.getDetails(), "select this.master" ).list().size()==2 );
		assertTrue( s.filter( master.getMoreDetails(), "" ).size()==0 );
		assertTrue( s.filter( master.getIncoming(), "" ).size()==0 );
		
		Query f = s.createFilter( master.getDetails(), "select max(this.i) where this.i < :top and this.i>=:bottom" );
		f.setInteger("top", 100);
		f.setInteger("bottom", 0);
		assertEquals( f.iterate().next(), new Integer(12) );
		f.setInteger("top", 2);
		assertEquals( f.iterate().next(), new Integer(0) );
		
		f = s.createFilter( master.getDetails(), "select max(this.i) where this.i not in (:list)" );
		Collection coll = new ArrayList();
		coll.add( new Integer(-666) );
		coll.add( new Integer(22) );
		coll.add( new Integer(0) );
		f.setParameterList("list", coll);
		assertEquals( f.iterate().next(), new Integer(12) );

		f = s.createFilter( master.getDetails(), "select max(this.i) where this.i not in (:list) and this.master.name = :listy2" );
		f.setParameterList("list", coll);
		f.setParameter( "listy2", master.getName() );
		assertEquals( f.iterate().next(), new Integer(12) );
		
		iter = master.getDetails().iterator();
		i=0;
		while ( iter.hasNext() ) {
			Detail d = (Detail) iter.next();
			assertTrue( "master-detail", d.getMaster()==master );
			s.delete(d);
			i++;
		}
		assertTrue( "master-detail", i==2 );
		s.delete(master);
		t.commit();
		s.close();
	}
	
	public void testIncomingOutgoing() throws Exception {
		
		if (getDialect() instanceof HSQLDialect) return;
		
		Session s = openSession();
		Master master1 = new Master();
		Master master2 = new Master();
		Master master3 = new Master();
		s.save(master1);
		s.save(master2);
		s.save(master3);
		master1.addIncoming(master2);
		master2.addOutgoing(master1);
		master1.addIncoming(master3);
		master3.addOutgoing(master1);
		Serializable m1id = s.getIdentifier(master1);
		assertTrue( s.filter( master1.getIncoming(), "where this.id > 0 and this.name is not null").size()==2 );
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		master1 = (Master) s.load(Master.class, m1id);
		Iterator iter = master1.getIncoming().iterator();
		int i=0;
		while ( iter.hasNext() ) {
			Master m = (Master) iter.next();
			assertTrue( "outgoing", m.getOutgoing().size()==1 );
			assertTrue( "outgoing", m.getOutgoing().contains(master1) );
			s.delete(m);
			i++;
		}
		assertTrue( "incoming-outgoing", i==2 );
		s.delete(master1);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCascading() throws Exception {
		
		if (getDialect() instanceof HSQLDialect) return;
		
		Session s = openSession();
		Detail d1 = new Detail();
		Detail d2 = new Detail();
		d2.setI(22);
		Master m = new Master();
		Master m0 = new Master();
		Serializable m0id = s.save(m0);
		m0.addDetail(d1); m0.addDetail(d2);
		d1.setMaster(m0); d2.setMaster(m0);
		m.getMoreDetails().add(d1);
		m.getMoreDetails().add(d2);
		Serializable mid = s.save(m);
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		m = (Master) s.load(Master.class, mid);
		assertTrue( "cascade save", m.getMoreDetails().size()==2 );
		assertTrue( "cascade save", ( (Detail) m.getMoreDetails().iterator().next() ).getMaster().getDetails().size()==2 );
		
		s.delete(m);
		s.delete( s.load(Master.class, m0id) );
		
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	/*public void testCascading2() throws Exception {
		Session s = sessionsopenSession();
		Detail d1 = new Detail();
		Detail d2 = new Detail();
		Master m = new Master();
		m.getMoreDetails().add(d1);
		m.getMoreDetails().add(d2);
		Serializable mid = s.save(m);
		s.flush();
		s.connection().commit();
		s.close();
		s = sessionsopenSession();
		m = (Master) s.load(Master.class, mid);
		assertTrue( m.getMoreDetails().size()==2, "cascade save" );
		s.delete(m);
		s.flush();
		s.connection().commit();
		s.close();
	}*/
	
	public void testNamedQuery() throws Exception {
		Session s = openSession();
		Query q = s.getNamedQuery("all_details");
		q.list();
		s.connection().commit();
		s.close();
	}
	
	
	public void testSerialization() throws Exception {
		
		if (getDialect() instanceof HSQLDialect) return;
		
		Session s = openSession();
		Master m = new Master();
		Detail d1 = new Detail();
		Detail d2 = new Detail();
		Serializable mid = s.save(m);
		d1.setMaster(m);
		d2.setMaster(m);
		m.addDetail(d1);
		m.addDetail(d2);
		if (getDialect() instanceof SybaseDialect) {
			s.save(d1);
		}
		else {
			s.save( d1, new Long(666) );
		}
		//s.save(d2);
		s.flush();
		s.connection().commit();
		s.disconnect();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(s);
		byte[] bytes = os.toByteArray();
		System.out.println(bytes.length);
		s = (Session) new ObjectInputStream( new ByteArrayInputStream(bytes) ).readObject();
		s.reconnect();
		Master m2 = (Master) s.load(Master.class, mid);
		assertTrue( "serialized state", m2.getDetails().size()==2 );
		Iterator iter = m2.getDetails().iterator();
		while ( iter.hasNext() ) {
			Detail d = (Detail) iter.next();
			assertTrue( "deserialization", d.getMaster()==m2 );
			try {
				s.getIdentifier(d);
				s.delete(d);
			}
			catch (Exception e) {}
		}
		s.delete(m2);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		mid = s.save( new Master() );
		Serializable mid2 = s.save( new Master() );
		s.flush();
		s.connection().commit();
		s.disconnect();
		os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(s);
		bytes = os.toByteArray();
		System.out.println(bytes.length);
		s = (Session) new ObjectInputStream( new ByteArrayInputStream(bytes) ).readObject();
		s.reconnect();
		s.delete( s.load(Master.class, mid) );
		s.delete( s.load(Master.class, mid2) );
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		s.connection(); //force session to grab a connection
		try {
			os = new ByteArrayOutputStream();
			new ObjectOutputStream(os).writeObject(s);
		}
		catch (Exception e) {
			assertTrue("illegal state", e instanceof IllegalStateException );
			s.connection().commit();
			s.close();
			return;
		}
		assertTrue("serialization should have failed", false);
		
	}
	
	public void testUpdateLazyCollections() throws Exception {
		
		if (getDialect() instanceof HSQLDialect) return;
		
		Session s = openSession();
		Master m = new Master();
		Detail d1 = new Detail();
		Detail d2 = new Detail();
		d2.setX(14);
		Serializable mid = s.save(m);
		//s.flush();
		d1.setMaster(m);
		d2.setMaster(m);
		m.addDetail(d1);
		m.addDetail(d2);
		if (getDialect() instanceof SybaseDialect) {
			s.save(d1);	s.save(d2);
		}
		else {
			s.save( d1, new Long(666) );
			s.save( d2, new Long(667) );
		}
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		m = (Master) s.load(Master.class, mid);
		s.connection().commit();
		s.close();
		m.setName("New Name");
		s = openSession();
		s.update(m, mid);
		Iterator iter = m.getDetails().iterator();
		int i=0;
		while ( iter.hasNext() ) {
			assertTrue( iter.next()!=null );
			i++;
		}
		assertTrue(i==2);
		iter = m.getDetails().iterator();
		while ( iter.hasNext() ) s.delete( iter.next() );
		s.delete(m);
		s.flush();
		s.connection().commit();
		s.close();
		
	}
	
	public void testMultiLevelCascade() throws Exception {
		
		if (getDialect() instanceof HSQLDialect) return;
		
		Session s = openSession();
		Detail detail = new Detail();
		SubDetail subdetail = new SubDetail();
		Master m = new Master();
		Master m0 = new Master();
		Serializable m0id = s.save(m0);
		m0.addDetail(detail);
		detail.setMaster(m0);
		m.getMoreDetails().add(detail);
		detail.setSubDetails( new HashSet() );
		detail.getSubDetails().add(subdetail);
		Serializable mid = s.save(m);
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		m = (Master) s.load( Master.class, mid );
		assertTrue( ( (Detail) m.getMoreDetails().iterator().next() ).getSubDetails().size()!=0 );
		s.delete(m);
		assertTrue( s.find("from sd in class SubDetail").size()==0 );
		assertTrue( s.find("from d in class Detail").size()==0 );
		s.delete( s.load(Master.class, m0id) );
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testMixNativeAssigned() throws Exception {
		
		if (getDialect() instanceof HSQLDialect) return;
		
		Session s = openSession();
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
		s.delete(assn);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCollectionReplaceOnUpdate() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Category c = new Category();
		List list = new ArrayList();
		c.setSubcategories(list);
		list.add( new Category() );
		s.save(c);
		t.commit();
		s.close();
		c.setSubcategories(list);
		
		s = openSession();
		t = s.beginTransaction();
		s.update(c);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		c = (Category) s.load( Category.class, new Long( c.getId() ), LockMode.UPGRADE );
		List list2 = c.getSubcategories();
		t.commit();
		s.close();		

		assertTrue( !Hibernate.isInitialized( c.getSubcategories() ) );

		c.setSubcategories(list2);
		s = openSession();
		t = s.beginTransaction();
		s.update(c);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		c = (Category) s.load( Category.class, new Long( c.getId() ), LockMode.UPGRADE );
		assertTrue( c.getSubcategories().size()==1 );
		s.delete(c);
		t.commit();
		s.close();
	}
	
	public void testCollectionReplace2() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Category c = new Category();
		List list = new ArrayList();
		c.setSubcategories(list);
		list.add( new Category() );
		Category c2 = new Category();
		s.save(c2);
		s.save(c);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		c = (Category) s.load( Category.class, new Long( c.getId() ), LockMode.UPGRADE );
		List list2 = c.getSubcategories();
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		c2 = (Category) s.load( Category.class, new Long( c2.getId() ), LockMode.UPGRADE );
		c2.setSubcategories(list2);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		c2 = (Category) s.load( Category.class, new Long( c2.getId() ), LockMode.UPGRADE );
		assertTrue( c2.getSubcategories().size()==1 );
		s.delete(c2);
		s.delete( s.load( Category.class, new Long( c.getId() ) ) );
		t.commit();
		s.close();
	}
	
	public void testCollectionReplace() throws Exception {
		Session s = openSession();
		Transaction t = s.beginTransaction();
		Category c = new Category();
		List list = new ArrayList();
		c.setSubcategories(list);
		list.add( new Category() );
		s.save(c);
		t.commit();
		s.close();
		
		s = openSession();
		t = s.beginTransaction();
		c = (Category) s.load( Category.class, new Long( c.getId() ), LockMode.UPGRADE );
		c.setSubcategories(list);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		c = (Category) s.load( Category.class, new Long( c.getId() ), LockMode.UPGRADE );
		List list2 = c.getSubcategories();
		t.commit();
		s.close();		

		assertTrue( !Hibernate.isInitialized( c.getSubcategories() ) );

		s = openSession();
		t = s.beginTransaction();
		c = (Category) s.load( Category.class, new Long( c.getId() ), LockMode.UPGRADE );
		c.setSubcategories(list2);
		t.commit();
		s.close();

		s = openSession();
		t = s.beginTransaction();
		c = (Category) s.load( Category.class, new Long( c.getId() ), LockMode.UPGRADE );
		assertTrue( c.getSubcategories().size()==1 );
		s.delete(c);
		t.commit();
		s.close();
	}
	
	public void testCategories() throws Exception {
		Session s = openSession();
		Category c = new Category();
		c.setName(Category.ROOT_CATEGORY);
		Category c1 = new Category();
		Category c2 = new Category();
		Category c3 = new Category();
		c.getSubcategories().add(c1);
		c.getSubcategories().add(c2);
		c2.getSubcategories().add(null);
		c2.getSubcategories().add(c3);
		s.save(c);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		s.lock(c, LockMode.UPGRADE);
		Category loaded = (Category) s.load( Category.class, new Long( c3.getId() ) );
		assertTrue(loaded==c3);
		assertTrue( s.getCurrentLockMode(c3)==LockMode.UPGRADE );
		assertTrue( s.getCurrentLockMode(c)==LockMode.UPGRADE );
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		loaded = (Category) s.load( Category.class, new Long( c.getId() ) );
		assertFalse( Hibernate.isInitialized( loaded.getSubcategories() ) );
		s.connection().commit();
		s.close();
		s = openSession();
		s.lock(loaded, LockMode.NONE);
		assertTrue( loaded.getSubcategories().size()==2 );
		s.connection().commit();
		s.close();
		
		
		s = openSession();
		c = (Category) s.load( Category.class, new Long( c.getId() ) );
		System.out.println( c.getSubcategories() );
		assertTrue( c.getSubcategories().get(0)!=null && c.getSubcategories().get(1)!=null );
		List list = ( (Category) c.getSubcategories().get(1) ).getSubcategories();
		assertTrue( list.get(1)!=null && list.get(0)==null );
		
		assertTrue(
			s.iterate("from c in class Category where c.name = org.hibernate.test.Category.ROOT_CATEGORY").hasNext()
		);
		s.connection().commit();
		s.close();
		
	}
	
	public void testCollectionRefresh() throws Exception {
		Session s = openSession();
		Category c = new Category();
		List list = new ArrayList();
		c.setSubcategories(list);
		list.add( new Category() );
		c.setName("root");
		Serializable id = s.save(c);
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		c = (Category) s.load(Category.class, id);
		s.refresh(c);
		s.flush();
		assertTrue( c.getSubcategories().size()==1 );
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		c = (Category) s.load(Category.class, id);
		assertTrue( c.getSubcategories().size()==1 );
		s.delete(c);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCachedCollectionRefresh() throws Exception {
		Session s = openSession();
		Category c = new Category();
		List list = new ArrayList();
		c.setSubcategories(list);
		list.add( new Category() );
		c.setName("root");
		Serializable id = s.save(c);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		c = (Category) s.load(Category.class, id);
		c.getSubcategories().size(); //force load
		
		Session ss = openSession();
		Category c2 = (Category) ss.load(Category.class, id);
		ss.delete( c2.getSubcategories().get(0) );
		c2.getSubcategories().clear();
		ss.flush();
		ss.connection().commit();
		ss.close();
		
		s.refresh(c);
		assertTrue( c.getSubcategories().size()==0 );

		ss = openSession();
		c2 = (Category) ss.load(Category.class, id);
		c2.getSubcategories().add( new Category() );
		c2.getSubcategories().add( new Category() );
		ss.flush();
		ss.connection().commit();
		ss.close();
		
		s.refresh(c);
		assertTrue( c.getSubcategories().size()==2 );

		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		c = (Category) s.load(Category.class, id);
		assertTrue( c.getSubcategories().size()==2 );
		s.delete(c);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCustomPersister() throws Exception {
		Session s = openSession();
		Custom c = new Custom();
		c.name = "foo";
		c.id=100;
		Long id = (Long) s.save(c);
		assertTrue( c==s.load(Custom.class, id) );
		s.flush();
		s.close();
		s = openSession();
		c = (Custom) s.load(Custom.class, id);
		assertTrue( c.name.equals("foo") );
		c.name = "bar";
		s.flush();
		s.close();
		s = openSession();
		c = (Custom) s.load(Custom.class, id);
		assertTrue( c.name.equals("bar") );
		s.delete(c);
		s.flush();
		s.close();
		s = openSession();
		boolean none = false;
		try {
			s.load(Custom.class, id);
		}
		catch (ObjectNotFoundException onfe) {
			none=true;
		}
		assertTrue(none);
		s.close();
		
	}
	
	public void testInterface() throws Exception {
		Session s = openSession();
		Serializable id = s.save( new BasicNameable() );
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		Nameable n = (Nameable) s.load(Nameable.class, id);
		s.delete(n);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testNoUpdateManyToOne() throws Exception {
		Session s = openSession();
		W w1 = new W();
		W w2 = new W();
		Z z = new Z();
		z.setW(w1);
		s.save(z);
		s.flush();
		z.setW(w2);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		s.update(z);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testQueuedBagAdds() throws Exception {
		Session s = openSession();
		Assignable a = new Assignable();
		a.setId("foo");
		a.setCategories( new ArrayList() );
		Category c = new Category();
		c.setAssignable(a);
		a.getCategories().add(c);
		s.save(a);
		s.flush();
		s.connection().commit();
		s.close();
		
		getSessions().evictCollection("org.hibernate.test.Assignable.categories");

		s = openSession();
		a = (Assignable) s.get(Assignable.class, "foo");
		c = new Category();
		c.setAssignable(a);
		a.getCategories().add(c);
		assertFalse( Hibernate.isInitialized( a.getCategories() ) );
		assertTrue( a.getCategories().size()==2 );
		s.flush();
		s.connection().commit();
		s.close();
		
		getSessions().evictCollection("org.hibernate.test.Assignable.categories");
		
		s = openSession();
		a = (Assignable) s.get(Assignable.class, "foo");
		c = new Category();
		c.setAssignable(a);
		a.getCategories().add(c);
		assertFalse( Hibernate.isInitialized( a.getCategories() ) );
		s.flush();
		assertFalse( Hibernate.isInitialized( a.getCategories() ) );
		assertTrue( a.getCategories().size()==3 );
		s.connection().commit();
		s.close();

		getSessions().evictCollection("org.hibernate.test.Assignable.categories");

		s = openSession();
		a = (Assignable) s.get(Assignable.class, "foo");
		assertTrue( a.getCategories().size()==3 );
		s.delete(a);
		s.flush();
		s.connection().commit();
		s.close();
		
	}

	public String[] getMappings() {
		return new String[] {
			"MasterDetail.hbm.xml",
			"Custom.hbm.xml",
			"Category.hbm.xml",
			"Nameable.hbm.xml",
			"SingleSeveral.hbm.xml",
			"WZ.hbm.xml",
			"UpDown.hbm.xml",
			"Eye.hbm.xml",
			"MN.hbm.xml"
		};
	}
	
	public static Test suite() {
		return new TestSuite(MasterDetailTest.class);
	}

	public static void main(String[] args) throws Exception {
		TestRunner.run( suite() );
	}

}






