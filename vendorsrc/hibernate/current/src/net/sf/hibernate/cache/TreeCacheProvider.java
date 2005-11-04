//$Id: TreeCacheProvider.java,v 1.6 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.cache;

import java.util.Properties;

/**
 * Support for JBoss TreeCache
 * @author Gavin King
 */
public class TreeCacheProvider implements CacheProvider {

	public Cache buildCache(String regionName, Properties properties)
		throws CacheException {
		return new TreeCache(regionName, properties);
	}

	public long nextTimestamp() {
		return System.currentTimeMillis() / 100;
	}

}
