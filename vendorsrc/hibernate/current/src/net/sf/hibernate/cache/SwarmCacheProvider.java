//$Id: SwarmCacheProvider.java,v 1.6 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.cache;

import net.sf.swarmcache.CacheConfiguration;
import net.sf.swarmcache.CacheConfigurationManager;
import net.sf.swarmcache.CacheFactory;

import java.util.Properties;

/**
 * Support for SwarmCache replicated cache. SwarmCache does not support
 * locking, so strict "read-write" semantics are unsupported.
 * @author Jason Carreira
 */
public class SwarmCacheProvider implements CacheProvider {

    public Cache buildCache(String s, Properties properties) throws CacheException {
        CacheConfiguration config = CacheConfigurationManager.getConfig(properties);
        CacheFactory factory = new CacheFactory(config);
        return new SwarmCache( factory.createCache(s) );
    }

    public long nextTimestamp() {
        return System.currentTimeMillis() / 100;
    }

}
