//$Id: TreeCache.java,v 1.6 2004/06/04 05:43:45 steveebersole Exp $
package net.sf.hibernate.cache;

import java.util.Properties;

import javax.transaction.TransactionManager;

import org.jboss.cache.Fqn;
import org.jboss.cache.PropertyConfigurator;
import net.sf.hibernate.transaction.TransactionManagerLookup;
import net.sf.hibernate.transaction.TransactionManagerLookupFactory;

/**
 * @author Gavin King
 */
public class TreeCache implements Cache {
	
	private static final String ITEM = "item";
	
	private static org.jboss.cache.TreeCache cache;
	private final String regionName;
	
	static final class TransactionManagerLookupAdaptor implements org.jboss.cache.TransactionManagerLookup {
		private final TransactionManagerLookup tml;
		private final Properties props;
		TransactionManagerLookupAdaptor(TransactionManagerLookup tml, Properties props) {
			this.tml=tml;
			this.props=props;
		}
		public TransactionManager getTransactionManager() throws Exception {
			return tml.getTransactionManager(props);
		}
	}
	
	public TreeCache(String regionName, Properties props) throws CacheException {
		this.regionName = '/' + regionName.replace('.', '/');
		try {
			synchronized (TreeCache.class) {
				if (cache==null) {
					cache = new org.jboss.cache.TreeCache();
					PropertyConfigurator config = new PropertyConfigurator();
					config.configure(cache, "treecache.xml");
					TransactionManagerLookup tml = TransactionManagerLookupFactory.getTransactionManagerLookup(props);
					if (tml!=null) cache.setTransactionManagerLookup( 
						new TransactionManagerLookupAdaptor(tml, props)
					);
					cache.start();
				}
			}
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public Object get(Object key) throws CacheException {
		try {
			return cache.get( new Fqn( new Object[] { regionName, key } ), ITEM );
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void put(Object key, Object value) throws CacheException {
		try {
			cache.put( new Fqn( new Object[] { regionName, key } ), ITEM, value );
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void remove(Object key) throws CacheException {
		try {
			cache.remove( new Fqn( new Object[] { regionName, key } ) );
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void clear() throws CacheException {
		try {
			cache.remove( new Fqn(regionName) );
		}
		catch (Exception e) {
			throw new CacheException(e);
		}
	}

	public void destroy() throws CacheException {
		cache.destroy();
	}

	public void lock(Object key) throws CacheException {
		throw new UnsupportedOperationException("TreeCache is a fully transactional cache" + regionName);
	}

	public void unlock(Object key) throws CacheException {
		throw new UnsupportedOperationException("TreeCache is a fully transactional cache: " + regionName);
	}

	public long nextTimestamp() {
		return System.currentTimeMillis() / 100;
	}

	public int getTimeout() {
		return 600; //60 seconds
	}

}
