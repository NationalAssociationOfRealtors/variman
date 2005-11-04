//$Id: ListType.java,v 1.13 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.List;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.engine.SessionImplementor;

public class ListType extends PersistentCollectionType {
	
	public ListType(String role) {
		super(role);
	}
	
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) {
		return new List(session);
	}
	
	public Class getReturnedClass() {
		return java.util.List.class;
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return new List( session, (java.util.List) collection );
	}

}





