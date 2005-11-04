//$Id: HashtableCacheProvider.java,v 1.6 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.cache;

import java.util.Properties;

/**
 * @author Gavin King
 */
public class HashtableCacheProvider implements CacheProvider {

	public Cache buildCache(String regionName, Properties properties)
		throws CacheException {
		return new HashtableCache();
	}

	public long nextTimestamp() {
		return Timestamper.next();
	}

}
