//$Id: ReplicationMode.java,v 1.4 2004/06/04 01:27:36 steveebersole Exp $
package net.sf.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sf.hibernate.type.VersionType;

/**
 * Represents a replication strategy.
 *
 * @see Session#replicate(Object, ReplicationMode)
 * @author Gavin King
 */
public abstract class ReplicationMode implements Serializable {
	private final int code;
	private final String name;
	private static final Map INSTANCES = new HashMap();
	
	public ReplicationMode(int level, String name) {
		this.code=level;
		this.name=name;
	}
	public String toString() {
		return name;
	}
	public abstract boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType);
	/**
	 * Throw an exception when a row already exists.
	 */
	public static final ReplicationMode EXCEPTION = new ReplicationMode(0, "EXCEPTION") {
		public boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType) {
			throw new AssertionFailure("should not be called");
		}
	};
	/**
	 * Ignore replicated entities when a row already exists.
	 */
	public static final ReplicationMode IGNORE = new ReplicationMode(1, "IGNORE") {
		public boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType) {
			return false;
		}
	};
	/**
	 * Overwrite existing rows when a row already exists.
	 */
	public static final ReplicationMode OVERWRITE = new ReplicationMode(3, "OVERWRITE") {
		public boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType) {
			return true;
		}
	};
	/**
	 * When a row already exists, choose the latest version.
	 */
	public static final ReplicationMode LATEST_VERSION = new ReplicationMode(2, "LATEST_VERSION") {
		public boolean shouldOverwriteCurrentVersion(Object entity, Object currentVersion, Object newVersion, VersionType versionType) {
			if (versionType==null) return true; //always overwrite nonversioned data
			return ( (Comparable) currentVersion ).compareTo(newVersion)<=0;
		}
	};
	
	static {
		INSTANCES.put( new Integer(LATEST_VERSION.code), LATEST_VERSION );
		INSTANCES.put( new Integer(IGNORE.code), IGNORE );
		INSTANCES.put( new Integer(OVERWRITE.code), OVERWRITE );
		INSTANCES.put( new Integer(EXCEPTION.code), EXCEPTION );
	}
	
	private Object readResolve() {
		return INSTANCES.get( new Integer(code) );
	}
	
}






