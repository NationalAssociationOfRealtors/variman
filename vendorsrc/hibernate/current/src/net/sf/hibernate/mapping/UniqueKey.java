//$Id: UniqueKey.java,v 1.10 2004/08/07 00:17:51 oneovthafew Exp $
package net.sf.hibernate.mapping;

import java.util.Iterator;

import net.sf.hibernate.util.StringHelper;
import net.sf.hibernate.dialect.Dialect;

/**
 * A relational unique key constraint
 * @author Gavin King
 */
public class UniqueKey extends Constraint {
	
	public String sqlConstraintString(Dialect dialect) {
		StringBuffer buf = new StringBuffer("unique (");
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			buf.append( ( (Column) iter.next() ).getQuotedName(dialect) );
			if ( iter.hasNext() ) buf.append(StringHelper.COMMA_SPACE);
		}
		return buf.append(StringHelper.CLOSE_PAREN).toString();
	}
	
	public String sqlConstraintString(Dialect dialect, String constraintName) {
		StringBuffer buf = new StringBuffer(
			dialect.getAddPrimaryKeyConstraintString(constraintName)
		).append('(');
		Iterator iter = getColumnIterator();
		while ( iter.hasNext() ) {
			buf.append( ( (Column) iter.next() ).getQuotedName(dialect) );
			if ( iter.hasNext() ) buf.append(StringHelper.COMMA_SPACE);
		}
		return StringHelper.replace( buf.append(StringHelper.CLOSE_PAREN).toString(), "primary key", "unique" ); //TODO: improve this hack!
	}
}







