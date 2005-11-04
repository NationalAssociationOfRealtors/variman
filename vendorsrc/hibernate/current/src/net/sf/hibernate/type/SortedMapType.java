//$Id: SortedMapType.java,v 1.13 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.util.Comparator;

import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.collection.SortedMap;
import net.sf.hibernate.engine.SessionImplementor;


public class SortedMapType extends MapType {
	
	private final Comparator comparator;
	
	public SortedMapType(String role, Comparator comparator) {
		super(role);
		this.comparator = comparator;
	}
	
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) {
		SortedMap map = new SortedMap(session);
		map.setComparator(comparator);
		return map;
	}
	
	public Class getReturnedClass() {
		return java.util.SortedMap.class;
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return new SortedMap( session, (java.util.SortedMap) collection );
	}

}






