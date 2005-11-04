//$Id: AliasToEntityMapResultTransformer.java,v 1.7 2004/07/18 04:48:51 oneovthafew Exp $
package net.sf.hibernate.transform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gavin King
 */
public class AliasToEntityMapResultTransformer implements ResultTransformer {

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map result = new HashMap();
		for ( int i=0; i<tuple.length; i++ ) {
			String alias = aliases[i];
			if ( !alias.endsWith("_") ) { //TODO: Incredibly dodgy!! what if the user defines an alias ending in "_"
				result.put( alias, tuple[i] );
			}
		}
		//result.put( Criteria.ROOT_ALIAS, tuple[tuple.length-1] );
		return result;
	}

	public List transformList(List collection) {
		return collection;
	}

}
