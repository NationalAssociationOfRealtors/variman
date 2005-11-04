//$Id: BagType.java,v 1.13 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.collection.Bag;
import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.engine.SessionImplementor;

public class BagType extends PersistentCollectionType {
	
	public BagType(String role) {
		super(role);
	}
	
	public PersistentCollection instantiate(
		SessionImplementor session,
		CollectionPersister persister)
		throws HibernateException {
		
		return new Bag(session);
	}
		
	public Class getReturnedClass() {
		return java.util.Collection.class;
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return new Bag( session, (java.util.Collection) collection );
	}
	
}
	
	
	
	
	
	
