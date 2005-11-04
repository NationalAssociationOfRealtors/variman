//$Id: ColumnMetadata.java,v 1.4 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.tool.hbm2ddl;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC column metadata
 * @author Christoph Sturm
 */
public class ColumnMetadata {
	private final String name;
	private final String typeName;
	private final int columnSize;
	private final int decimalDigits;
	private final String isNullable;
	
	ColumnMetadata(ResultSet rs) throws SQLException {
		name = rs.getString("COLUMN_NAME");
		typeName = rs.getString("TYPE_NAME");
		columnSize = rs.getInt("COLUMN_SIZE");
		decimalDigits = rs.getInt("DECIMAL_DIGITS");
		isNullable = rs.getString("IS_NULLABLE");
	}
	
	public String getName() {
		return name;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public int getColumnSize() {
		return columnSize;
	}
	
	public int getDecimalDigits() {
		return decimalDigits;
	}
	
	public String getNullable() {
		return isNullable;
	}
	
	public String toString() {
		return name;
	}
}






