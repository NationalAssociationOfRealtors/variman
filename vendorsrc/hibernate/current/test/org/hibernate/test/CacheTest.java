//$Id: CacheTest.java,v 1.4 2004/06/04 01:27:35 steveebersole Exp $
package org.hibernate.test;

import junit.framework.TestCase;
import net.sf.hibernate.cache.Cache;
import net.sf.hibernate.cache.CacheConcurrencyStrategy;
import net.sf.hibernate.cache.CacheProvider;
import net.sf.hibernate.cache.JCSCacheProvider;
import net.sf.hibernate.cache.ReadWriteCache;
import net.sf.hibernate.cache.CacheConcurrencyStrategy.SoftLock;

public class CacheTest extends TestCase {
	
	public CacheTest(String arg0) {
		super(arg0);
	}
	
	public void testCaches() throws Exception {
		//doTestCache( new CoherenceCacheProvider() );
		doTestCache( new JCSCacheProvider() );
	}
	
	public void doTestCache(CacheProvider cacheProvider) throws Exception {
		
		Cache cache = cacheProvider.buildCache( String.class.getName(), System.getProperties() );
		
		long longBefore = cache.nextTimestamp();
		
		Thread.sleep(15);
		
		long before = cache.nextTimestamp();
		
		Thread.sleep(15);
		
		//cache.setTimeout(1000);
		CacheConcurrencyStrategy ccs = new ReadWriteCache();
		ccs.setCache(cache);
		
		// cache something
		
		assertTrue( ccs.put("foo", "foo", before) );
		
		Thread.sleep(15);
		
		long after = cache.nextTimestamp();
		
		assertTrue( ccs.get("foo", longBefore)==null );
		assertTrue( ccs.get("foo", after).equals("foo") );
		
		assertTrue( !ccs.put("foo", "foo", before) );
		
		// update it:
		
		SoftLock lock = ccs.lock("foo");
		
		assertTrue( ccs.get("foo", after)==null );
		assertTrue( ccs.get("foo", longBefore)==null );
		
		assertTrue( !ccs.put("foo", "foo", before) );
		
		Thread.sleep(15);
		
		long whileLocked = cache.nextTimestamp();
		
		assertTrue( !ccs.put("foo", "foo", whileLocked) );
		
		Thread.sleep(15);
		
		ccs.release("foo", lock);
		
		assertTrue( ccs.get("foo", after)==null );
		assertTrue( ccs.get("foo", longBefore)==null );
		
		assertTrue( !ccs.put("foo", "bar", whileLocked) );
		assertTrue( !ccs.put("foo", "bar", after) );
		
		Thread.sleep(15);
		
		long longAfter = cache.nextTimestamp();
		
		assertTrue( ccs.put("foo", "baz", longAfter) );
		
		assertTrue( ccs.get("foo", after)==null );
		assertTrue( ccs.get("foo", whileLocked)==null );
		
		Thread.sleep(15);
		
		long longLongAfter = cache.nextTimestamp();
		
		assertTrue( ccs.get("foo", longLongAfter).equals("baz") );
		
		// update it again, with multiple locks:
		
		SoftLock lock1 = ccs.lock("foo");
		SoftLock lock2 = ccs.lock("foo");
		
		assertTrue( ccs.get("foo", longLongAfter)==null );
		
		Thread.sleep(15);
		
		whileLocked = cache.nextTimestamp();
		
		assertTrue( !ccs.put("foo", "foo", whileLocked) );
		
		Thread.sleep(15);
		
		ccs.release("foo", lock2);
		
		Thread.sleep(15);
		
		long betweenReleases = cache.nextTimestamp();
		
		assertTrue( !ccs.put("foo", "bar", betweenReleases) );
		assertTrue( ccs.get("foo", betweenReleases)==null );
		
		Thread.sleep(15);
		
		ccs.release("foo", lock1);
		
		assertTrue( !ccs.put("foo", "bar", whileLocked) );
		
		Thread.sleep(15);
		
		longAfter = cache.nextTimestamp();
		
		assertTrue( ccs.put("foo", "baz", longAfter) );
		assertTrue( ccs.get("foo", whileLocked)==null );
		
		Thread.sleep(15);
		
		longLongAfter = cache.nextTimestamp();
		
		assertTrue( ccs.get("foo", longLongAfter).equals("baz") );
		
	}
	
}






