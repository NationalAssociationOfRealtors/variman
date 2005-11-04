//$Id: NamedSQLQuery.java,v 1.5 2004/06/04 01:27:42 steveebersole Exp $
package net.sf.hibernate.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.hibernate.util.ArrayHelper;

/**
 * Simple value holder for named sql queries.
 * 
 * @author Max Andersen
 */
public class NamedSQLQuery {
	
	private String query;
	private java.util.Map aliasedClasses;
	private List synchronizedTables;

	public NamedSQLQuery(String query) {
		this.aliasedClasses = new HashMap();
		this.query = query;
		this.synchronizedTables = new ArrayList();
	}

	public String[] getReturnAliases() {
		return (String[]) aliasedClasses.keySet().toArray(ArrayHelper.EMPTY_STRING_ARRAY);
	}

	public Class[] getReturnClasses() {
		return (Class[]) aliasedClasses.values().toArray(ArrayHelper.EMPTY_CLASS_ARRAY);
	}

	public String getQueryString() {
		return query;
	}
	
	public void addSynchronizedTable(String table) {
		synchronizedTables.add(table);
	}
	
	public void addAliasedClass(String alias, Class clazz) {
		aliasedClasses.put(alias, clazz);
	}
	
	public List getSynchronizedTables() {
		return synchronizedTables;
	}
}
