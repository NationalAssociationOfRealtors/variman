//$Id: QueryImpl.java,v 1.23 2004/06/04 01:27:41 steveebersole Exp $
package net.sf.hibernate.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.ScrollableResults;
import net.sf.hibernate.engine.SessionImplementor;

/**
 * default implementation of the <tt>Query</tt> interface,
 * for "ordinary" HQL queries (not collection filters)
 * @see FilterImpl
 * @author Gavin King
 */
public class QueryImpl extends AbstractQueryImpl {
	
	public QueryImpl(String queryString, SessionImplementor session) {
		super(queryString, session);				
	}
	
	public Iterator iterate() throws HibernateException {
		verifyParameters();
		Map namedParams = getNamedParams();
		return getSession().iterate( bindParameterLists(namedParams), getQueryParameters(namedParams) );
	}
	
	public ScrollableResults scroll() throws HibernateException {
		verifyParameters();
		Map namedParams = getNamedParams();
		return getSession().scroll( bindParameterLists(namedParams), getQueryParameters(namedParams) );
	}
	
	public List list() throws HibernateException {
		verifyParameters();
		Map namedParams = getNamedParams();
		return getSession().find( bindParameterLists(namedParams), getQueryParameters(namedParams) );
	}
	
}






