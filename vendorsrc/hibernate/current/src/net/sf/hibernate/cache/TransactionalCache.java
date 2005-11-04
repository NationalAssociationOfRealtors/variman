//$Id: TransactionalCache.java,v 1.6 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.cache;

import net.sf.hibernate.HibernateException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Support for fully transactional cache implementations like
 * JBoss TreeCache. Note that this might be a less scalable
 * concurrency strategy than <tt>ReadWriteCache</tt>. This is
 * a "synchronous" concurrency strategy.
 * 
 * @author Gavin King
 */
public class TransactionalCache implements CacheConcurrencyStrategy {
	
	private static final Log log = LogFactory.getLog(TransactionalCache.class);

	private Cache cache;

	private boolean minimalPuts;

	public Object get(Object key, long txTimestamp) throws CacheException {
		if ( log.isDebugEnabled() ) log.debug("cache lookup: " + key);
		Object result = cache.get(key);
		if ( log.isDebugEnabled() ) {
			log.debug( result==null ? "cache miss" : "cache hit" );
		}
		return result;
	}

	public boolean put(Object key, Object value, long txTimestamp)
		throws CacheException {
		
		if ( minimalPuts && cache.get(key)!=null ) {
			if ( log.isDebugEnabled() ) log.debug("item already cached: " + key);
			return false;
		}
		if ( log.isDebugEnabled() ) log.debug("caching: " + key);
		cache.put(key, value);
		return true;
	}

	/**
	 * Do nothing, returning null.
	 */
	public SoftLock lock(Object key) throws CacheException {
		//noop
		return null;
	}

	/**
	 * Do nothing.
	 */
	public void release(Object key, SoftLock clientLock) throws CacheException {
		//noop
	}

	public void update(Object key, Object value) throws CacheException {
		if ( log.isDebugEnabled() ) log.debug("updating: " + key);
		cache.put(key, value);
	}

	public void insert(Object key, Object value) throws CacheException {
		if ( log.isDebugEnabled() ) log.debug("inserting: " + key);
		cache.put(key, value);
	}

	public void evict(Object key) throws CacheException {
		cache.remove(key);
	}

	public void remove(Object key) throws CacheException {
		if ( log.isDebugEnabled() ) log.debug("removing: " + key);
		cache.remove(key);
	}

	public void clear() throws CacheException {
		log.debug("clearing");
		cache.clear();
	}

	public void destroy() {
		try {
			cache.destroy();
		}
		catch (Exception e) {
			log.warn("could not destroy cache", e);
		}
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	/**
	 * Do nothing.
	 */
	public void afterInsert(Object key, Object value) throws CacheException {
		// noop
	}

	/**
	 * Do nothing.
	 */
	public void afterUpdate(Object key, Object value, SoftLock clientLock) throws CacheException {
		// noop
	}

	public void setMinimalPuts(boolean minimalPuts) throws HibernateException {
		this.minimalPuts = minimalPuts;
	}

}
