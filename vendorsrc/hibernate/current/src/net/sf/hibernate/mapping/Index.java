//$Id: Index.java,v 1.11 2004/06/04 01:27:42 steveebersole Exp $
package net.sf.hibernate.mapping;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.engine.Mapping;
import net.sf.hibernate.util.StringHelper;
import net.sf.hibernate.dialect.Dialect;

/**
 * A relational table index
 * @author Gavin King
 */
public class Index implements RelationalModel {
	
	private Table table;
	private ArrayList columns = new ArrayList();
	private String name;
	
	public String sqlCreateString(Dialect dialect, Mapping mapping) throws HibernateException {
		StringBuffer buf = new StringBuffer("create index ")
			.append( dialect.qualifyIndexName() ? name : StringHelper.unqualify(name) )
			.append(" on ")
			.append( table.getQualifiedName(dialect) )
			.append(" (");
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			buf.append( ( (Column) iter.next() ).getQuotedName(dialect) );
			if ( iter.hasNext() ) buf.append(StringHelper.COMMA_SPACE);
		}
		buf.append(StringHelper.CLOSE_PAREN);
		return buf.toString();
	}
	
	public String sqlConstraintString(Dialect dialect) {
		StringBuffer buf = new StringBuffer(" index (");
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			buf.append( ( (Column) iter.next() ).getQuotedName(dialect) );
			if ( iter.hasNext() ) buf.append(StringHelper.COMMA_SPACE);
		}
		return buf.append(StringHelper.CLOSE_PAREN).toString();
	}
	
	public String sqlDropString(Dialect dialect) {
		return "drop index " + StringHelper.qualify( table.getQualifiedName(dialect), name);
	}
	
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public Iterator getColumnIterator() {
		return columns.iterator();
	}
	public void addColumn(Column column) {
		if ( !columns.contains(column) ) columns.add(column);
	}
	public void addColumns(Iterator extraColumns) {
		while ( extraColumns.hasNext() ) addColumn( (Column) extraColumns.next() );
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}






