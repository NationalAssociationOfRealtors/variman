//$Id: NewPerformanceTest.java,v 1.4 2004/06/04 01:27:36 steveebersole Exp $
package org.hibernate.test;

import java.io.Serializable;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.hibernate.Session;

public class NewPerformanceTest extends TestCase {
	
	public NewPerformanceTest(String arg0) {
		super(arg0);
	}
	
	public void testPerformance() throws Exception {
		
		for ( int n=2; n<4000; n*=2 ) {
			
			Simple[] simples = new Simple[n];
			Serializable[] ids = new Serializable[n];
			for ( int i=0; i<n; i++ ) {
				simples[i] = new Simple();
				simples[i].init();
				simples[i].setCount(i);
				ids[i] = new Long(i);
			}
			
			Session s = openSession();
			prepare(s, simples, ids, n);
			s.close();
			
			long find = 0;
			long flush = 0;
			
			for ( int i=0; i<100; i++ ) {
				
				s = openSession();
				long time = System.currentTimeMillis();
				List list = s.find("from s in class Simple where not s.name='osama bin laden' and s.other is null");
				find += System.currentTimeMillis() - time;
				assertTrue( list.size()==n );
				time = System.currentTimeMillis();
				s.flush();
				flush += System.currentTimeMillis() - time;
				time = System.currentTimeMillis();
				s.connection().commit();
				find += System.currentTimeMillis() - time;
				s.close();
				
			}
			
			System.out.println( "Objects: " + n + " - find(): " + find + "ms / flush(): " + flush + "ms / Ratio: " + ( (float) flush )/find );
			System.out.println( "Objects: " + n + " flush time per object: " + flush / 100.0 / n );
			System.out.println( "Objects: " + n + " load time per object: " + find / 100.0 / n );
			s = openSession();
			delete(s);
			s.close();
			
		}
	}
	
	private void prepare(Session s, Simple[] simples, Serializable[] ids, int N) throws Exception {
		for ( int i=0; i<N; i++ ) {
			s.save( simples[i], ids[i] );
		}
		s.flush();
		s.connection().commit();
	}
	
	private void delete(Session s) throws Exception {
		s.delete("from s in class Simple");
		s.flush();
		s.connection().commit();
	}
	
	public String[] getMappings() {
		return new String[] { "Simple.hbm.xml" };
	}
	
	public static Test suite() throws Exception {
		return new TestSuite(NewPerformanceTest.class);
	}

	public static void main(String[] args) throws Exception {
		TestRunner.run( suite() );
	}

}
