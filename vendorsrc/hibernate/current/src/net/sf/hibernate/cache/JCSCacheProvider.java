//$Id: JCSCacheProvider.java,v 1.3 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.cache;

import java.util.Properties;

/**
 * @author Gavin King
 * @deprecated JCS support will be removed in version 2.1.1
 */
public class JCSCacheProvider implements CacheProvider {

	public Cache buildCache(String regionName, Properties properties)
		throws CacheException {
		return new JCSCache(regionName, properties);
	}

	public long nextTimestamp() {
		return Timestamper.next();
	}

}
