//$Id: TestCase.java,v 1.4 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

import java.util.Iterator;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cache.NonstrictReadWriteCache;
import net.sf.hibernate.cache.ReadWriteCache;
import net.sf.hibernate.cache.TransactionalCache;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.cfg.Environment;
import net.sf.hibernate.dialect.Dialect;
import net.sf.hibernate.mapping.Collection;
import net.sf.hibernate.mapping.PersistentClass;

public abstract class TestCase extends junit.framework.TestCase {
	
	private static SessionFactory sessions;
	private static Configuration cfg;
	private static Dialect dialect;
	private static Class lastTestClass;
	private Session session;
	
	public TestCase(String x) {
		super(x);
	}
	
	private void buildSessionFactory(String[] files) throws Exception {
		
		if ( getSessions()!=null ) getSessions().close();
		
		try {
		
			setCfg( new Configuration() );
			
			cfg.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
			
			//cfg.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);

			for (int i=0; i<files.length; i++) {
				if ( !files[i].startsWith("net/") ) files[i] = "org/hibernate/test/" + files[i];
				getCfg().addResource( files[i], TestCase.class.getClassLoader() );
			}
			
			Iterator iter = cfg.getClassMappings();
			while ( iter.hasNext() ) {
				PersistentClass clazz = (PersistentClass) iter.next();
				if ( !clazz.isInherited() ) {
					cfg.setCacheConcurrencyStrategy( clazz.getMappedClass(), new NonstrictReadWriteCache() );
				}
			}
			
			iter = cfg.getCollectionMappings();
			while ( iter.hasNext() ) {
				Collection coll = (Collection) iter.next();
				cfg.setCacheConcurrencyStrategy( coll.getRole(), new NonstrictReadWriteCache() );
			}
			
			setDialect( Dialect.getDialect() );
			
			setSessions( getCfg().buildSessionFactory( /*new TestInterceptor()*/ ) );
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

	protected void setUp() throws Exception {
		if ( getSessions()==null || lastTestClass!=getClass() ) {
			buildSessionFactory( getMappings() );
			lastTestClass = getClass();
		}
	}
	
	protected void runTest() throws Throwable {
		try {
			super.runTest();
			if ( session!=null && session.isOpen() ) {
				if ( session.isConnected() ) session.connection().rollback();
				session.close();
				session = null;
				fail("unclosed session");
			}
			else {
				session=null;
			}
		}
		catch (Throwable e) {
			try {
				if ( session!=null && session.isOpen() ) {
					if ( session.isConnected() ) session.connection().rollback();
					session.close();
				}
			}
			catch (Exception ignore) {}
			try {
				if (sessions!=null) {
					sessions.close();
					sessions=null;
				}
			}
			catch (Exception ignore) {}
			throw e;
		}
	}
	
	public Session openSession() throws HibernateException {
		session = sessions.openSession();
		return session;
	}
	
	protected abstract String[] getMappings();

	private void setSessions(SessionFactory sessions) {
		TestCase.sessions = sessions;
	}

	protected SessionFactory getSessions() {
		return sessions;
	}

	private void setDialect(Dialect dialect) {
		TestCase.dialect = dialect;
	}

	protected Dialect getDialect() {
		return dialect;
	}

	protected static void setCfg(Configuration cfg) {
		TestCase.cfg = cfg;
	}

	protected static Configuration getCfg() {
		return cfg;
	}

}
