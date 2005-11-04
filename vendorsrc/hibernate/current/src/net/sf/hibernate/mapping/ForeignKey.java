//$Id: ForeignKey.java,v 1.10 2004/06/04 01:27:42 steveebersole Exp $
package net.sf.hibernate.mapping;

import java.util.Iterator;

import net.sf.hibernate.MappingException;
import net.sf.hibernate.dialect.Dialect;

/**
 * A foreign key constraint
 * @author Gavin King
 */
public class ForeignKey extends Constraint {
	
	//private PrimaryKey referencedPrimaryKey;
	private Table referencedTable;
	private Class referencedClass;
	
	public String sqlConstraintString(Dialect dialect, String constraintName) {
		String[] cols = new String[ getColumnSpan() ];
		String[] refcols = new String[ getColumnSpan() ];
		int i=0;
		Iterator refiter = referencedTable.getPrimaryKey().getColumnIterator();
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			cols[i] = ( (Column) iter.next() ).getQuotedName(dialect);
			refcols[i] = ( (Column) refiter.next() ).getQuotedName(dialect);
			i++;
		}
		return dialect.getAddForeignKeyConstraintString(
			constraintName, cols, referencedTable.getQualifiedName(dialect), refcols
		);
	}

	public Table getReferencedTable() {
		return referencedTable;
	}
	
	private void appendColumns(StringBuffer buf, Iterator columns) {
		while( columns.hasNext() ) {
			Column column = (Column) columns.next();
			buf.append( column.getName() );
			if ( columns.hasNext() ) buf.append(",");
		}	
	}

	public void setReferencedTable(Table referencedTable) throws MappingException {
		if ( referencedTable.getPrimaryKey().getColumnSpan()!=getColumnSpan() ) {
			StringBuffer sb = new StringBuffer();
			sb.append("Foreign key (")
				.append( getTable().getName() )
				.append(" [");
			appendColumns( sb, getColumnIterator() );
			sb.append("])")
				.append(") must have same number of columns as the referenced primary key (")
				.append( referencedTable.getName() )
				.append(" [");
			appendColumns( sb, referencedTable.getPrimaryKey().getColumnIterator() );
			sb.append("])");
			throw new MappingException( sb.toString() );
		}
		
		Iterator fkCols = getColumnIterator();
		Iterator pkCols = referencedTable.getPrimaryKey().getColumnIterator();
		while ( pkCols.hasNext() ) {
			( (Column) fkCols.next() ).setLength( ( (Column) pkCols.next() ).getLength() );
		}
		
		this.referencedTable = referencedTable;
	}
	
	public Class getReferencedClass() {
		return referencedClass;
	}

	public void setReferencedClass(Class referencedClass) {
		this.referencedClass = referencedClass;
	}
	
	public ForeignKey() {}
	
}







