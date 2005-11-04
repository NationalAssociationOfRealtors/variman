//$Id: Constraint.java,v 1.9 2004/06/04 01:27:42 steveebersole Exp $
package net.sf.hibernate.mapping;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.hibernate.dialect.Dialect;
import net.sf.hibernate.engine.Mapping;

/**
 * A relational constraint.
 * @author Gavin King
 */
public abstract class Constraint implements RelationalModel {
	
	private String name;
	private final ArrayList columns = new ArrayList();
	private Table table;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Iterator getColumnIterator() {
		return columns.iterator();
	}
	public void addColumn(Column column) {
		if ( !columns.contains(column) ) columns.add(column);
	}
	public void addColumns(Iterator columns) {
		while ( columns.hasNext() ) addColumn( (Column) columns.next() );
	}
	public int getColumnSpan() {
		return columns.size();
	}
	public Iterator columnIterator() {
		return columns.iterator();
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	
	public String sqlDropString(Dialect dialect) {
		return "alter table " + getTable().getQualifiedName(dialect) + " drop constraint " + getName();
	}
	
	public String sqlCreateString(Dialect dialect, Mapping p) {
		StringBuffer buf = new StringBuffer("alter table ")
		.append( getTable().getQualifiedName(dialect) )
		.append( sqlConstraintString( dialect, getName() ) );
		return buf.toString();
	}
	
	public abstract String sqlConstraintString(Dialect d, String constraintName);
}







