//$Id: Main.java,v 1.2 2004/06/04 01:28:53 steveebersole Exp $
package org.hibernate.perf;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import net.sf.hibernate.AssertionFailure;
import net.sf.hibernate.FlushMode;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * @author Gavin King
 */
public class Main {
	
	private static final String SQL_UPDATE = "update Record set name=?, description=?, timestamp=?, parent=?, price=?, quantity=?, serialNumber=?, version=? where id = ? and version = ?";
	private static final String SQL_GET = "select id, name, description, timestamp, parent, price, quantity, serialNumber, version from Record where id = ?";
	private static final String SQL_QUERY = "select id, name, description, timestamp, parent, price, quantity, serialNumber, version from Record where description like ?";
	private static final String HQL_QUERY = "from Record rec where rec.description like ?";
	private static final String HQL_PROJECTION = "select new Record(rec.id, rec.name, rec.description, rec.timestamp, rec.quantity, rec.price, rec.serialNumber, rec.version) from Record rec where rec.description like ?";

	public static void main(String[] args) throws Exception {
		Configuration cfg = new Configuration()
			.addClass(Record.class);
		SessionFactory sf = cfg.buildSessionFactory();
		new SchemaExport(cfg).create(true, true);
		
		
		Session s = sf.openSession();
		for ( int i=0; i<10000; i++ ) {
			Record rec = new Record();
			if (i % 100 == 0) {
				rec.setDescription("a long description yadayadayada foo bar fee fi fo fum" + i);
			}
			else {
				rec.setDescription("a description yadayadayada foo bar baz" + i);
			}
			rec.setName("Record " + i);
			rec.setTimestamp( new Date() );
			rec.setQuantity(i/10+i/50);
			rec.setPrice( new BigDecimal(i/2.45) );
			rec.setSerialNumber( Integer.toString(i*100) );
			s.save(rec);
		}
		s.flush();
		s.connection().commit();
		s.close();
		
		//warm up
		for ( int i=0; i<100; i++) testJDBC(sf);
		for ( int i=0; i<100; i++) testJDBCGet(sf, i+5);
		for ( int i=0; i<100; i++) testHibernateProjection(sf);
		for ( int i=0; i<100; i++) testHibernate(sf);
		for ( int i=0; i<100; i++) testHibernateWithFlush(sf);
		for ( int i=0; i<100; i++) testHibernateGet(sf, i+5);
		
		//test JDBC
		long start1 = System.currentTimeMillis();
		for ( int i=0; i<200; i++) testJDBC(sf);
		long jdbc = System.currentTimeMillis() - start1;
		System.out.println("JDBC query (pure data access): " + jdbc);
		
		//test Hibernate with projection
		long start3 = System.currentTimeMillis();
		for ( int i=0; i<200; i++) testHibernateProjection(sf);
		long hibernateProjection = System.currentTimeMillis() - start3;
		System.out.println("Hibernate projection: " + hibernateProjection);
		
		//test Hibernate
		long start2 = System.currentTimeMillis();
		for ( int i=0; i<200; i++) testHibernate(sf);
		long hibernate = System.currentTimeMillis() - start2;
		System.out.println("Hibernate query (overhead is cost of session cache): " + hibernate);
		
		//test Hibernate including flush
		long start4 = System.currentTimeMillis();
		for ( int i=0; i<200; i++) testHibernateWithFlush(sf);
		long hibernateWithFlush = System.currentTimeMillis() - start4;
		System.out.println("Hibernate query, flush (overhead is cost of session cache plus cost of dirty check): " + hibernateWithFlush);
		

		//test JDBC get
		long start5 = System.currentTimeMillis();
		for ( int j=0; j<4; j++ )
		for ( int i=0; i<500; i++) testJDBCGet(sf, i*5+6);
		long jdbcGet = System.currentTimeMillis() - start5;
		System.out.println("JDBC query by id (pure data access): " + jdbcGet);
		
		//test Hibernate get
		long start6 = System.currentTimeMillis();
		for ( int j=0; j<4; j++ )
		for ( int i=0; i<500; i++) testHibernateGet(sf, i*5+5);
		long hibernateGet = System.currentTimeMillis() - start6;
		System.out.println("Hibernate get (overhead is cost of session cache): " + hibernateGet);
		
		//warm up
		for ( int i=0; i<100; i++) testJDBCGetUpdate(sf, i+10);
		for ( int i=0; i<100; i++) testHibernateGetUpdate(sf, i+5);

		//test JDBC get/update
		long start7 = System.currentTimeMillis();
		for ( int j=0; j<4; j++ )
		for ( int i=0; i<500; i++) testJDBCGetUpdate(sf, i*6+2222);
		long jdbcGetUpdate = System.currentTimeMillis() - start7;
		System.out.println("JDBC get, update (pure data access): " + jdbcGetUpdate);
		
		//test Hibernate get/update
		long start8 = System.currentTimeMillis();
		for ( int j=0; j<4; j++ )
		for ( int i=0; i<500; i++) testHibernateGetUpdate(sf, i*6+1111);
		long hibernateGetUpdate = System.currentTimeMillis() - start8;
		System.out.println("Hibernate get, update (overhead is cost of session cache plus cost of dirty check): " + hibernateGetUpdate);		
		
		sf.close();
		
	}
	
	private static void testHibernateProjection(SessionFactory sf) throws Exception {
		Session s = sf.openSession();
		s.setFlushMode(FlushMode.NEVER);
		List list = s.find(HQL_PROJECTION, "a long%", Hibernate.STRING);
		assertTrue( list.size()==100 );
		s.connection().commit();
		s.close();
	}
	
	private static void testHibernate(SessionFactory sf) throws Exception {
		Session s = sf.openSession();
		s.setFlushMode(FlushMode.NEVER);
		List list = s.find(HQL_QUERY, "a long%", Hibernate.STRING);
		assertTrue( list.size()==100 );
		s.connection().commit();
		s.close();
	}
	
	private static void testHibernateWithFlush(SessionFactory sf) throws Exception {
		Session s = sf.openSession();
		s.setFlushMode(FlushMode.NEVER);
		List list = s.find(HQL_QUERY, "a long%", Hibernate.STRING);
		s.flush();
		assertTrue( list.size()==100 );
		s.connection().commit();
		s.close();
	}
	
	private static void testJDBC(SessionFactory sf) throws Exception {
		Session s = sf.openSession();
		PreparedStatement ps = s.connection()
			.prepareStatement(SQL_QUERY);
		ps.setString(1, "a long%");
		ResultSet rs = ps.executeQuery();
		List list = new ArrayList();
		while ( rs.next() ) {
			list.add( getRecord(rs) );
		}
		rs.close();
		ps.close();
		assertTrue( list.size()==100 );
		s.connection().commit();
		s.close();
	}
	
	private static void testJDBCGet(SessionFactory sf, int id) throws Exception {
		Session s = sf.openSession();
		PreparedStatement ps = s.connection()
			.prepareStatement(SQL_GET);
		ps.setLong(1, id);
		
		ResultSet rs = ps.executeQuery();
		assertTrue( rs.next() );
		Record rec = getRecord(rs);
		rs.close();
		
		ps.close();
		s.connection().commit();
		s.close();
	}
	
	private static void testJDBCGetUpdate(SessionFactory sf, int id) throws Exception {
		Session s = sf.openSession();
		
		PreparedStatement ps = s.connection()
			.prepareStatement(SQL_GET);
		ps.setLong(1, id+3);
		ResultSet rs = ps.executeQuery();
		assertTrue( rs.next() );
		Record rec = getRecord(rs);
		rs.close();
		ps.close();
		
		rec.setName( rec.getName() + " dirty" );
		rec.setTimestamp( new Date() );
		
		ps = s.connection()
			.prepareStatement(SQL_UPDATE);
		ps.setString( 1, rec.getName() );
		ps.setString( 2, rec.getDescription() );
		ps.setTimestamp( 3, new Timestamp( rec.getTimestamp().getTime() ) );
		ps.setNull(4, Types.BIGINT);
		ps.setBigDecimal( 5, rec.getPrice() );
		ps.setInt( 6, rec.getQuantity() );
		ps.setString( 7, rec.getSerialNumber() );
		ps.setInt( 8, rec.getVersion()+1 );
		ps.setLong( 9, rec.getId().longValue() );
		ps.setInt( 10, rec.getVersion() );
		assertTrue( ps.executeUpdate()==1 );
		ps.close();
		
		s.connection().commit();
		s.close();
	}
	
	private static Record getRecord(ResultSet rs) throws SQLException {
		Record rec = new Record();
		rec.setDescription( rs.getString("description") );
		rec.setName( rs.getString("name") );
		rec.setId( new Long( rs.getLong("id") ) );
		rec.setTimestamp( rs.getTimestamp("timestamp") );
		rec.setPrice( rs.getBigDecimal("price") );
		rec.setQuantity( rs.getInt("quantity") );
		rec.setSerialNumber( rs.getString("serialNumber") );
		rec.setVersion( rs.getInt("version") );
		return rec;
	}
	
	private static void testHibernateGet(SessionFactory sf, int id) throws Exception {
		Session s = sf.openSession();
		s.setFlushMode(FlushMode.NEVER);
		Record r = (Record) s.get( Record.class, new Long(id) );
		assertTrue(r!=null);
		s.connection().commit();
		s.close();
	}
	
	private static void testHibernateGetUpdate(SessionFactory sf, int id) throws Exception {
		Session s = sf.openSession();
		s.setFlushMode(FlushMode.NEVER);
		Record r = (Record) s.get( Record.class, new Long(id+1) );
		r.setName( r.getName() + " dirty" );
		r.setTimestamp( new Date() );
		s.flush();
		s.connection().commit();
		s.close();
	}
	
	private static void assertTrue(boolean value) {
		if (!value) throw new AssertionFailure("assertion failed");
	}
}
