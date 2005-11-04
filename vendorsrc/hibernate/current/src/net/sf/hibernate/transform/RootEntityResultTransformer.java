//$Id: RootEntityResultTransformer.java,v 1.6 2004/06/04 05:43:49 steveebersole Exp $
package net.sf.hibernate.transform;

import java.util.List;

/**
 * @author Gavin King
 */
public class RootEntityResultTransformer implements ResultTransformer {

	public Object transformTuple(Object[] tuple, String[] aliases) {
		return tuple[ tuple.length-1 ];
	}

	public List transformList(List collection) {
		return collection;
	}

}
