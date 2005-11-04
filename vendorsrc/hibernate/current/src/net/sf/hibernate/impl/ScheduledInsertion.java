//$Id: ScheduledInsertion.java,v 1.9 2004/06/04 01:27:41 steveebersole Exp $
package net.sf.hibernate.impl;

import java.io.Serializable;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.SessionImplementor;
import net.sf.hibernate.persister.ClassPersister;

final class ScheduledInsertion extends ScheduledEntityAction implements SessionImpl.Executable, Serializable {
	private final Object[] state;
	private CacheEntry cacheEntry;

	public ScheduledInsertion(Serializable id, Object[] state, Object instance, ClassPersister persister, SessionImplementor session)
	throws HibernateException {
		super(session, id, instance, persister);
		this.state = state;
	}

	public void execute() throws HibernateException {
		ClassPersister persister = getPersister();
		SessionImplementor session = getSession();
		Object object = getInstance();
		Serializable id = getId();

		// Don't need to lock the cache here, since if someone
		// else inserted the same pk first, the insert would fail

		persister.insert(id, state, object, session);
		session.postInsert(object);

		if ( persister.hasCache() && !persister.isCacheInvalidationRequired() ) {
			cacheEntry = new CacheEntry(object, persister, session);
			persister.getCache().insert(id, cacheEntry);
		}
	}

	//Make 100% certain that this is called before any subsequent ScheduledUpdate.afterTransactionCompletion()!!
	public void afterTransactionCompletion(boolean success) throws HibernateException {
		ClassPersister persister = getPersister();
		if ( success && persister.hasCache() && !persister.isCacheInvalidationRequired() ) {
			persister.getCache().afterInsert( getId(), cacheEntry );
		}
	}

}







