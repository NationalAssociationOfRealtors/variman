//$Id: SetType.java,v 1.13 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.collection.Set;
import net.sf.hibernate.engine.SessionImplementor;

public class SetType extends PersistentCollectionType {
	
	public SetType(String role) {
		super(role);
	}
	
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) {
		return new Set(session);
	}
	
	public Class getReturnedClass() {
		return java.util.Set.class;
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return new Set( session, (java.util.Set) collection );
	}
	
}
