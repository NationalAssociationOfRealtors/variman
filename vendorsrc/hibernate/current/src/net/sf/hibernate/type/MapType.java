//$Id: MapType.java,v 1.13 2004/06/04 01:28:52 steveebersole Exp $
package net.sf.hibernate.type;

import java.util.Iterator;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.collection.CollectionPersister;
import net.sf.hibernate.collection.Map;
import net.sf.hibernate.collection.PersistentCollection;
import net.sf.hibernate.engine.SessionImplementor;


public class MapType extends PersistentCollectionType {
	
	public MapType(String role) {
		super(role);
	}
	
	public PersistentCollection instantiate(SessionImplementor session, CollectionPersister persister) {
		return new Map(session);
	}
	
	public Class getReturnedClass() {
		return java.util.Map.class;
	}
	
	public Iterator getElementsIterator(Object collection) {
		return ( (java.util.Map) collection ).values().iterator();
	}
	
	public PersistentCollection wrap(SessionImplementor session, Object collection) {
		return new Map( session, (java.util.Map) collection );
	}

	public Object copy(
		Object original,
		Object target,
		SessionImplementor session, Object owner)
		throws HibernateException {
		
		if (original==null) return null;
		if (original==target) return target;
		
		CollectionPersister cp = session.getFactory().getCollectionPersister( getRole() );
		
		java.util.Map result = (java.util.Map) target;
		result.clear();
		Iterator iter = ( (java.util.Map) original ).entrySet().iterator();
		while ( iter.hasNext() ) {
			java.util.Map.Entry me = (java.util.Map.Entry) iter.next();
			Object key = cp.getIndexType().copy( me.getKey(), null, session, owner );
			Object value = cp.getElementType().copy( me.getValue(), null, session, owner );
			result.put(key, value);
		}
		return result;
	}

}





