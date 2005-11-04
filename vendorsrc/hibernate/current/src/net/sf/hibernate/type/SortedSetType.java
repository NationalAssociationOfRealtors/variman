//$Id: SortedSetType.java,v 1.13 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.util.Comparator;

import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.collection.SortedSet;
import net.sf.hibernate.engine.SessionImplementor;

public class SortedSetType extends SetType {
	
	private final Comparator comparator;
	
	public SortedSetType(String role, Comparator comparator) {
		super(role);
		this.comparator = comparator;
	}
	
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) {
		SortedSet set = new SortedSet(session);
		set.setComparator(comparator);
		return set;
	}
	
	public Class getReturnedClass() {
		return java.util.SortedSet.class;
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return new SortedSet( session, (java.util.SortedSet) collection );
	}
}






