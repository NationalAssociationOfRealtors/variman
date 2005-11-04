//$Id: FumTest.java,v 1.6 2004/06/04 06:42:41 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.hibernate.Criteria;
import net.sf.hibernate.FetchMode;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.FlushMode;
import net.sf.hibernate.dialect.HSQLDialect;
import net.sf.hibernate.dialect.MckoiDialect;
import net.sf.hibernate.dialect.MySQLDialect;
import net.sf.hibernate.dialect.PointbaseDialect;
import net.sf.hibernate.dialect.SQLServerDialect;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.MatchMode;
import net.sf.hibernate.type.DateType;
import net.sf.hibernate.type.EntityType;
import net.sf.hibernate.type.StringType;
import net.sf.hibernate.type.Type;

public class FumTest extends TestCase {
	
	private static short fumKeyShort = 1;
	
	public FumTest(String arg) {
		super(arg);
	}
	
	public void testCriteriaCollection() throws Exception {
		if ( getDialect() instanceof HSQLDialect ) return;
		if ( getDialect() instanceof SQLServerDialect ) return;
		Session s = openSession();
		Fum fum = new Fum( fumKey("fum") );
		fum.setFum("a value");
		fum.getMapComponent().getFummap().put("self", fum);
		fum.getMapComponent().getStringmap().put("string", "a staring");
		fum.getMapComponent().getStringmap().put("string2", "a notha staring");
		fum.getMapComponent().setCount(1);
		s.save(fum);
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		Fum b = (Fum) s.createCriteria(Fum.class).add( 
			Expression.in("fum", new String[] { "a value", "no value" } )
		)
		.uniqueResult();
		//assertTrue( Hibernate.isInitialized( b.getMapComponent().getFummap() ) );
		assertTrue( Hibernate.isInitialized( b.getMapComponent().getStringmap() ) );
		assertTrue( b.getMapComponent().getFummap().size()==1 );
		assertTrue( b.getMapComponent().getStringmap().size()==2 );
		
		/*int none = s.createCriteria(Fum.class).add( 
			Expression.in( "fum", new String[0] )
		).list().size();
		assertTrue(none==0);*/
		s.delete(b);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCriteria() throws Exception {
		if ( getDialect() instanceof SQLServerDialect ) return;
		Session s = openSession();
		Fum fum = new Fum( fumKey("fum") );
		fum.setFo( new Fum( fumKey("fo") ) );
		fum.setFum("fo fee fi");
		fum.getFo().setFum("stuff");
		Fum fr = new Fum( fumKey("fr") );
		fr.setFum("goo");
		Fum fr2 = new Fum( fumKey("fr2") );
		fr2.setFum("soo");
		fum.setFriends( new HashSet() );
		fum.getFriends().add(fr);
		fum.getFriends().add(fr2);
		s.save(fr);
		s.save(fr2);
		s.save( fum.getFo() );
		s.save(fum);

		Criteria base = s.createCriteria(Fum.class)
			.add( Expression.like("fum", "f", MatchMode.START) );
		base.createCriteria("fo")
			.add( Expression.isNotNull("fum") );
		base.createCriteria("friends")
			.add( Expression.like("fum", "g%") );
		List list = base.list();
		assertTrue( list.size()==1 && list.get(0)==fum );

		base = s.createCriteria(Fum.class)
			.add( Expression.like("fum", "f%") )
			.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		base.createCriteria("fo", "fo")
			.add( Expression.isNotNull("fum") );
		base.createCriteria("friends", "fum")
			.add( Expression.like("fum", "g", MatchMode.START) );
		Map map = (Map) base.uniqueResult();
		
		assertTrue( 
			map.get("this")==fum && 
			map.get("fo")==fum.getFo() && 
			fum.getFriends().contains( map.get("fum") ) &&
			map.size()==3
		);

		base = s.createCriteria(Fum.class)
			.add( Expression.like("fum", "f%") )
			.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
			.setFetchMode("friends", FetchMode.EAGER);
		base.createCriteria("fo", "fo")
			.add( Expression.eq( "fum", fum.getFo().getFum() ) );
		map = (Map) base.list().get(0);
		
		assertTrue( 
			map.get("this")==fum && 
			map.get("fo")==fum.getFo() &&
			map.size()==2
		);

		list = s.createCriteria(Fum.class)
			.createAlias("friends", "fr")
			.createAlias("fo", "fo")
			.add( Expression.like("fum", "f%") )
			.add( Expression.isNotNull("fo") )
			.add( Expression.isNotNull("fo.fum") )
			.add( Expression.like("fr.fum", "g%") )
			.add( Expression.eqProperty("fr.id.short", "id.short") )
			.list();
		assertTrue( list.size()==1 && list.get(0)==fum );
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		base = s.createCriteria(Fum.class)
			.add( Expression.like("fum", "f%") );
		base.createCriteria("fo")
			.add( Expression.isNotNull("fum") );
		base.createCriteria("friends")
			.add( Expression.like("fum", "g%") );
		fum = (Fum) base.list().get(0);
		assertTrue(  fum.getFriends().size()==2 );
		s.delete(fum);
		s.delete( fum.getFo() );
		Iterator iter = fum.getFriends().iterator();
		while ( iter.hasNext() ) s.delete( iter.next() );
		s.flush();
		s.connection().commit();
		s.close();
	}
			
	public void testListIdentifiers() throws Exception {
		if ( getDialect() instanceof SQLServerDialect ) return;
		Session s = openSession();
		Fum fum = new Fum( fumKey("fum") );
		fum.setFum("fo fee fi");
		s.save(fum);
		fum = new Fum( fumKey("fi") );
		fum.setFum("fee fi fo");
		s.save(fum);
		List list = s.find("select fum.id from fum in class Fum where not fum.fum='FRIEND'");
		assertTrue( "list identifiers", list.size()==2);
		Iterator iter = s.iterate("select fum.id from fum in class Fum where not fum.fum='FRIEND'");
		int i=0;
		while ( iter.hasNext() ) {
			assertTrue( "iterate identifiers",  iter.next() instanceof FumCompositeID);
			i++;
		}
		assertTrue(i==2);
		
		s.delete( s.load(Fum.class, (Serializable) list.get(0) ) );
		s.delete( s.load(Fum.class, (Serializable) list.get(1) ) );
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	
	public FumCompositeID fumKey(String str) {
		
		return fumKey(str,false);
	}
	
	private FumCompositeID fumKey(String str, boolean aCompositeQueryTest) {
		FumCompositeID id = new FumCompositeID();
		if ( getDialect() instanceof MckoiDialect ) {
			GregorianCalendar now = new GregorianCalendar();
			GregorianCalendar cal = new GregorianCalendar( 
				now.get(java.util.Calendar.YEAR),
				now.get(java.util.Calendar.MONTH),
				now.get(java.util.Calendar.DATE) 
			);
			id.setDate( cal.getTime() );
		}
		else {
			id.setDate( new Date() );
		}
		id.setString( new String(str) );
		
		if (aCompositeQueryTest) {
			id.setShort( fumKeyShort++ );
		}
		else {
			id.setShort( (short) 12 );
		}
		
		return id;
	}
	
	public void testCompositeID() throws Exception {
		if ( getDialect() instanceof SQLServerDialect ) return;
		if ( getDialect() instanceof HSQLDialect ) return;
		Session s = openSession();
		Fum fum = new Fum( fumKey("fum") );
		fum.setFum("fee fi fo");
		s.save(fum);
		assertTrue( "load by composite key", fum==s.load( Fum.class, fumKey("fum") ) );
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		fum = (Fum) s.load( Fum.class, fumKey("fum"), LockMode.UPGRADE );
		assertTrue( "load by composite key", fum!=null );
		
		Fum fum2 = new Fum( fumKey("fi") );
		fum2.setFum("fee fo fi");
		fum.setFo(fum2);
		s.save(fum2);
		assertTrue(
			"find composite keyed objects",
			s.find("from fum in class Fum where not fum.fum='FRIEND'").size()==2
		);
		assertTrue(
			"find composite keyed object",
			s.find("select fum from fum in class Fum where fum.fum='fee fi fo'").get(0)==fum
		);
		fum.setFo(null);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		Iterator iter = s.iterate("from fum in class Fum where not fum.fum='FRIEND'");
		int i = 0;
		while ( iter.hasNext() ) {
			fum = (Fum) iter.next();
			//iter.remove();
			s.delete(fum);
			i++;
		}
		assertTrue( "iterate on composite key", i==2 );
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCompositeIDOneToOne() throws Exception {
		if ( getDialect() instanceof SQLServerDialect ) return;
		if ( getDialect() instanceof HSQLDialect ) return;
		Session s = openSession();
		Fum fum = new Fum( fumKey("fum") );
		fum.setFum("fee fi fo");
		//s.save(fum);
		Fumm fumm = new Fumm();
		fumm.setFum(fum);
		s.save(fumm);
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		fumm = (Fumm) s.load( Fumm.class, fumKey("fum") );
		//s.delete( fumm.getFum() );
		s.delete(fumm);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCompositeIDQuery() throws Exception {
		if ( getDialect() instanceof SQLServerDialect ) return;
		if ( getDialect() instanceof HSQLDialect ) return;
		Session s = openSession();
		Fum fee = new Fum( fumKey("fee",true) );
		fee.setFum("fee");
		s.save(fee);
		Fum fi = new Fum( fumKey("fi",true) );
		fi.setFum("fi");
		short fiShort = fi.getId().getShort();
		s.save(fi);
		Fum fo = new Fum( fumKey("fo",true) );
		fo.setFum("fo");
		s.save(fo);
		Fum fum = new Fum( fumKey("fum",true) );
		fum.setFum("fum");
		s.save(fum);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		// Try to find the Fum object "fo" that we inserted searching by the string in the id
		List vList = s.find("from fum in class Fum where fum.id.string='fo'"  );
		assertTrue( "find by composite key query (find fo object)", vList.size() == 1 );
		fum = (Fum)vList.get(0);
		assertTrue( "find by composite key query (check fo object)", fum.getId().getString().equals("fo") );
		
		// Try to find the Fum object "fi" that we inserted searching by the date in the id
		vList = s.find("from fum in class Fum where fum.id.short = ?",new Short(fiShort),Hibernate.SHORT);
		assertTrue( "find by composite key query (find fi object)", vList.size() == 1 );
		fi = (Fum)vList.get(0);
		assertTrue( "find by composite key query (check fi object)", fi.getId().getString().equals("fi") );
		
		// Make sure we can return all of the objects by searching by the date id
		assertTrue(
			"find by composite key query with arguments",
			s.find("from fum in class Fum where fum.id.date <= ? and not fum.fum='FRIEND'",new Date(),Hibernate.DATE).size()==4
		);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		assertTrue(
			s.iterate("select fum.id.short, fum.id.date, fum.id.string from fum in class Fum").hasNext()
		);
		assertTrue(
			s.iterate("select fum.id from fum in class Fum").hasNext()
		);
		Query qu = s.createQuery("select fum.fum, fum , fum.fum, fum.id.date from fum in class Fum");
		Type[] types = qu.getReturnTypes();
		assertTrue(types.length==4);
		for ( int k=0; k<types.length; k++) {
			assertTrue( types[k]!=null );
		}
		assertTrue(types[0] instanceof StringType);
		assertTrue(types[1] instanceof EntityType);
		assertTrue(types[2] instanceof StringType);
		assertTrue(types[3] instanceof DateType);
		Iterator iter = qu.iterate();
		int j = 0;
		while ( iter.hasNext() ) {
			j++;
			assertTrue( ( (Object[]) iter.next() )[1] instanceof Fum );
		}
		assertTrue( "iterate on composite key", j==8 );
		
		fum = (Fum) s.load( Fum.class, fum.getId() );
		s.filter( fum.getQuxArray(), "where this.foo is null" );
		s.filter( fum.getQuxArray(), "where this.foo.id = ?", "fooid", Hibernate.STRING );
		Query f = s.createFilter( fum.getQuxArray(), "where this.foo.id = :fooId" );
		f.setString("fooId", "abc");
		assertFalse( f.iterate().hasNext() );

		iter = s.iterate("from fum in class Fum where not fum.fum='FRIEND'");
		int i = 0;
		while ( iter.hasNext() ) {
			fum = (Fum) iter.next();
			//iter.remove();
			s.delete(fum);
			i++;
		}
		assertTrue( "iterate on composite key", i==4 );
		s.flush();
		
		s.iterate("from fu in class Fum, fo in class Fum where fu.fo.id.string = fo.id.string and fo.fum is not null");
		
		s.find("from Fumm f1 inner join f1.fum f2");
		
		s.connection().commit();
		s.close();
	}
	
	
	public void testCompositeIDCollections() throws Exception {
		if ( getDialect() instanceof SQLServerDialect ) return;
		if ( getDialect() instanceof HSQLDialect ) return;
		Session s = openSession();
		Fum fum1 = new Fum( fumKey("fum1") );
		Fum fum2 = new Fum( fumKey("fum2") );
		fum1.setFum("fee fo fi");
		fum2.setFum("fee fo fi");
		s.save(fum1);
		s.save(fum2);
		Qux q = new Qux();
		s.save(q);
		Set set = new HashSet();
		List list = new ArrayList();
		set.add(fum1); set.add(fum2);
		list.add(fum1);
		q.setFums(set);
		q.setMoreFums(list);
		fum1.setQuxArray( new Qux[] {q} );
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		q = (Qux) s.load( Qux.class, q.getKey() );
		assertTrue( "collection of fums", q.getFums().size()==2 );
		assertTrue( "collection of fums", q.getMoreFums().size()==1 );
		assertTrue( "unkeyed composite id collection", ( (Fum) q.getMoreFums().get(0) ).getQuxArray()[0]==q );
		Iterator iter = q.getFums().iterator();
		iter.hasNext();
		Fum f = (Fum) iter.next();
		s.delete(f);
		iter.hasNext();
		f = (Fum) iter.next();
		s.delete(f);
		s.delete(q);
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	
	public void testDeleteOwner() throws Exception {
		if ( getDialect() instanceof SQLServerDialect ) return;
		Session s = openSession();
		Qux q = new Qux();
		s.save(q);
		Fum f1 = new Fum( fumKey("f1") );
		Fum f2 = new Fum( fumKey("f2") );
		Set set = new HashSet();
		set.add(f1);
		set.add(f2);
		List list = new LinkedList();
		list.add(f1);
		list.add(f2);
		f1.setFum("f1");
		f2.setFum("f2");
		q.setFums(set);
		q.setMoreFums(list);
		s.save(f1);
		s.save(f2);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		q = (Qux) s.load( Qux.class, q.getKey(), LockMode.UPGRADE );
		s.lock( q, LockMode.UPGRADE );
		s.delete(q);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		list = s.find("from fum in class Fum where not fum.fum='FRIEND'");
		assertTrue( "deleted owner", list.size()==2 );
		s.lock( list.get(0), LockMode.UPGRADE );
		s.lock( list.get(1), LockMode.UPGRADE );
		Iterator iter = list.iterator();
		while ( iter.hasNext() ) {
			s.delete( iter.next() );
		}
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	
	public void testCompositeIDs() throws Exception {
		Session s = openSession();
		Fo fo = Fo.newFo();
		Properties props = new Properties();
		props.setProperty("foo", "bar");
		props.setProperty("bar", "foo");
		fo.setSerial(props);
		fo.setBuf( "abcdefghij1`23%$*^*$*\n\t".getBytes() );
		s.save( fo, fumKey("an instance of fo") );
		s.flush();
		props.setProperty("x", "y");
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		fo = (Fo) s.load( Fo.class, fumKey("an instance of fo") );
		props = (Properties) fo.getSerial();
		assertTrue( props.getProperty("foo").equals("bar") );
		assertTrue( props.getProperty("x").equals("y") );
		assertTrue( fo.getBuf()[0]=='a' );
		fo.getBuf()[1]=(byte)126;
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		fo = (Fo) s.load( Fo.class, fumKey("an instance of fo") );
		assertTrue( fo.getBuf()[1]==126 );
		assertTrue(
			s.iterate("from fo in class Fo where fo.id.string like 'an instance of fo'").next()==fo
		);
		s.delete(fo);
		s.flush();
		try {
			s.save( Fo.newFo() );
			assertTrue(false);
		}
		catch (Exception e) {
			//System.out.println( e.getMessage() );
		}
		s.connection().commit();
		s.close();
	}
		
	public void testKeyManyToOne() throws Exception {
		Session s = openSession();
		Inner sup = new Inner();
		InnerKey sid = new InnerKey();
		sup.setDudu("dudu");
		sid.setAkey("a");
		sid.setBkey("b");
		sup.setId(sid);
		Middle m = new Middle();
		MiddleKey mid = new MiddleKey();
		mid.setOne("one");
		mid.setTwo("two");
		mid.setSup(sup);
		m.setId(mid);
		m.setBla("bla");
		Outer d = new Outer();
		OuterKey did = new OuterKey();
		did.setMaster(m);
		did.setDetailId("detail");
		d.setId(did);
		d.setBubu("bubu");
		s.save(sup);
		s.save(m);
		s.save(d);
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		Inner in = (Inner) s.find("from Inner").get(0);
		assertTrue( in.getMiddles().size()==1 );
		s.flush();
		s.connection().commit();
		s.close();
		s = openSession();
		assertTrue( s.find("from Inner _inner join _inner.middles middle").size()==1 );
		s.flush();
		s.connection().commit();
		s.close();
		
		s = openSession();
		d = (Outer) s.load(Outer.class, did);
		assertTrue( d.getId().getMaster().getId().getSup().getDudu().equals("dudu") );
		s.delete(d);
		s.delete( d.getId().getMaster() );
		s.save( d.getId().getMaster() );
		s.save(d);
		s.flush();
		s.connection().commit();
		s.close();

		s = openSession();
		d = (Outer) s.find("from Outer o where o.id.detailId = ?", d.getId().getDetailId(), Hibernate.STRING ).get(0);
		s.find("from Outer o where o.id.master.id.sup.dudu is not null");
		s.find("from Outer o where o.id.master.id.sup.id.akey is not null");
		if ( !(getDialect() instanceof HSQLDialect) ) s.find("from Inner i where i.backOut.id.master.id.sup.id.akey = i.id.bkey");
		s.find("select o.id.master.id.sup.dudu from Outer o where o.id.master.id.sup.dudu is not null");
		s.find("select o.id.master.id.sup.id.akey from Outer o where o.id.master.id.sup.id.akey is not null");
		if ( !(getDialect() instanceof HSQLDialect) ) s.find("select i.backOut.id.master.id.sup.id.akey from Inner i where i.backOut.id.master.id.sup.id.akey = i.id.bkey");
		s.find("from Outer o where o.id.master.bla = ''");
		s.find("from Outer o where o.id.master.id.one = ''");
		s.find("from Inner inn where inn.id.bkey is not null and inn.backOut.id.master.id.sup.id.akey > 'a'");
		s.find("from Outer as o left join o.id.master m left join m.id.sup where o.bubu is not null");
		s.find("from Outer as o left join o.id.master.id.sup s where o.bubu is not null");
		s.find("from Outer as o left join o.id.master m left join o.id.master.id.sup s where o.bubu is not null");
		s.delete(d);
		s.delete( d.getId().getMaster() );
		s.delete( d.getId().getMaster().getId().getSup() );
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	public void testCompositeKeyPathExpressions() throws Exception {
		Session s = openSession();
		s.find("select fum1.fo from fum1 in class Fum where fum1.fo.fum is not null");
		s.find("from fum1 in class Fum where fum1.fo.fum is not null order by fum1.fo.fum");
		if ( !(getDialect() instanceof MySQLDialect) && !(getDialect() instanceof HSQLDialect) && !(getDialect() instanceof MckoiDialect) && !(getDialect() instanceof PointbaseDialect) ) {
			s.find("from fum1 in class Fum where exists elements(fum1.friends)");
			s.find("from fum1 in class Fum where size(fum1.friends) = 0");
		}
		s.find("select fum1.friends.elements from fum1 in class Fum");
		s.find("from fum1 in class Fum, fr in elements( fum1.friends )");
		s.connection().commit();
		s.close();
	}

	public void testUnflushedSessionSerialization() throws Exception {

		///////////////////////////////////////////////////////////////////////////
		// Test insertions across serializations
		Session s = getSessions().openSession();
		s.setFlushMode(FlushMode.NEVER);

		Simple simple = new Simple();
		simple.setAddress("123 Main St. Anytown USA");
		simple.setCount(1);
		simple.setDate( new Date() );
		simple.setName("My UnflushedSessionSerialization Simple");
		simple.setPay( new Float(5000) );
		s.save( simple, new Long(10) );

		// Now, try to serialize session without flushing...
		s.disconnect();
		Session s2 = spoofSerialization(s);
		s.close();
		s = s2;
		s.reconnect();

		simple = (Simple) s.load( Simple.class, new Long(10) );
		Simple other = new Simple();
		other.init();
		s.save( other, new Long(11) );

		simple.setOther(other);
		s.flush();

		s.connection().commit();
		s.close();
		Simple check = simple;

		///////////////////////////////////////////////////////////////////////////
		// Test updates across serializations
		s = getSessions().openSession();
		s.setFlushMode(FlushMode.NEVER);

		simple = (Simple) s.get( Simple.class, new Long(10) );
		assertTrue("Not same parent instances", check.getName().equals( simple.getName() ) );
		assertTrue("Not same child instances", check.getOther().getName().equals( other.getName() ) );

		simple.setName("My updated name");

		s.disconnect();
		s2 = spoofSerialization(s);
		s.close();
		s = s2;
		s.reconnect();
		s.flush();

		s.connection().commit();
		s.close();
		check = simple;

		///////////////////////////////////////////////////////////////////////////
		// Test deletions across serializations
		s = getSessions().openSession();
		s.setFlushMode(FlushMode.NEVER);

		simple = (Simple) s.get( Simple.class, new Long(10) );
		assertTrue("Not same parent instances", check.getName().equals( simple.getName() ) );
		assertTrue("Not same child instances", check.getOther().getName().equals( other.getName() ) );

		// Now, lets delete across serialization...
		s.delete(simple);

		s.disconnect();
		s2 = spoofSerialization(s);
		s.close();
		s = s2;
		s.reconnect();
		s.flush();

		s.connection().commit();
		s.close();
	}

	private Session spoofSerialization(Session session) throws IOException {
		try {
			// Serialize the incoming out to memory
			ByteArrayOutputStream serBaOut = new ByteArrayOutputStream();
			ObjectOutputStream serOut = new ObjectOutputStream(serBaOut);

			serOut.writeObject(session);

			// Now, re-constitue the model from memory
			ByteArrayInputStream serBaIn =
			        new ByteArrayInputStream(serBaOut.toByteArray());
			ObjectInputStream serIn = new ObjectInputStream(serBaIn);

			Session outgoing = (Session) serIn.readObject();

			return outgoing;
		}
		catch (ClassNotFoundException cnfe) {
			throw new IOException("Unable to locate class on reconstruction");
		}
	}
	
	public String[] getMappings() {
		return new String[] { 
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
			"Simple.hbm.xml",
			"Middle.hbm.xml"
		};
	}
	
	public static Test suite() {
		return new TestSuite(FumTest.class);
	}
	public static void main(String[] args) throws Exception {
		TestRunner.run( suite() );
	}
	
}







