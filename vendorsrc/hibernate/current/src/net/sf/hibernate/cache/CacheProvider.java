//$Id: CacheProvider.java,v 1.6 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.cache;

import java.util.Properties;

/**
 * Support for pluggable caches.
 * 
 * @author Gavin King
 */
public interface CacheProvider {

	/**
	 * Configure the cache
	 * 
	 * @param regionName the name of the cache region
	 * @param properties configuration settings
	 * @throws CacheException
	 */
	public Cache buildCache(String regionName, Properties properties) throws CacheException;

	/**
	 * Generate a timestamp
	 */
	public long nextTimestamp();
	
}
