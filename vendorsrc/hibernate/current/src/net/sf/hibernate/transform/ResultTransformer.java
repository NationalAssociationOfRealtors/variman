//$Id: ResultTransformer.java,v 1.6 2004/06/04 05:43:49 steveebersole Exp $
package net.sf.hibernate.transform;

import java.util.List;

/**
 * Implementors define a strategy for transforming criteria query
 * results into the actual application-visible query result list.
 * @see net.sf.hibernate.Criteria#setResultTransformer(ResultTransformer)
 * @author Gavin King
 */
public interface ResultTransformer {
	public Object transformTuple(Object[] tuple, String[] aliases);
	public List transformList(List collection);
}
