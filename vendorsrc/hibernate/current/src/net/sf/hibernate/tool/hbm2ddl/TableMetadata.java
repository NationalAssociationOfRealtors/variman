//$Id: TableMetadata.java,v 1.6 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.tool.hbm2ddl;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC table metadata
 * @author Christoph Sturm
 */
public class TableMetadata {
	private final String schema;
	private final String name;
	private final Map columns = new HashMap();
	private final Map foreignKeys = new HashMap();
	private final Map indexes = new HashMap();
	
	TableMetadata(ResultSet rs, DatabaseMetaData meta) throws SQLException {
		schema = rs.getString("TABLE_SCHEM");
		name = rs.getString("TABLE_NAME");
		initColumns(meta);
		initForeignKeys(meta);
		initIndexes(meta);
	}
	
	private void initForeignKeys(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;
		
		try {
			rs = meta.getImportedKeys(null, schema, name);
			
			while ( rs.next() ) addForeignKey(rs);
		} 
		finally {
			if (rs != null) rs.close();
		}
	}
	
	private void initIndexes(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;
		
		try {
			rs = meta.getIndexInfo(null, schema, name, false, true);
			
			while ( rs.next() ) {
				if (rs.getShort("TYPE") == DatabaseMetaData.tableIndexStatistic) continue;
				
				addIndex(rs);
			}
		} 
		finally {
			if (rs != null) rs.close();
		}
	}
	
	public ColumnMetadata getColumnMetadata(String columnName) {
		return (ColumnMetadata)columns.get( columnName.toLowerCase() );
	}
	
	public void addColumn(ResultSet rs) throws SQLException {
		String column = rs.getString("COLUMN_NAME");
		
		if (column==null) return;
		
		if ( getColumnMetadata(column) == null ) {
			ColumnMetadata info = new ColumnMetadata(rs);
			
			columns.put( info.getName().toLowerCase(), info );
		}
	}
	
	public ForeignKeyMetadata getForeignKeyMetadata(String keyName) {
		return (ForeignKeyMetadata) foreignKeys.get( keyName.toUpperCase() );
	}
	
	private void addForeignKey(ResultSet rs) throws SQLException {
		String fk = rs.getString("FK_NAME");
		
		if (fk == null) return;
		
		ForeignKeyMetadata info = getForeignKeyMetadata(fk);
		
		if (info == null) {
			info = new ForeignKeyMetadata(rs);
			
			foreignKeys.put(info.getName().toUpperCase(), info);
		}
		
		info.addColumn( getColumnMetadata("FKCOLUMN_NAME") );
	}
	
	public IndexMetadata getIndexMetadata(String indexName) {
		return (IndexMetadata) indexes.get( indexName.toUpperCase() );
	}
	
	private void addIndex(ResultSet rs) throws SQLException {
		String index = rs.getString("INDEX_NAME");
		
		if (index == null) return;
		
		IndexMetadata info = getIndexMetadata(index);
		
		if (info == null) {
			info = new IndexMetadata(rs);
			
			indexes.put(info.getName().toUpperCase(), info);
		}
		
		info.addColumn( getColumnMetadata("COLUMN_NAME") );
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}

	private void initColumns(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;
		
		try {
			rs = meta.getColumns(null, "%", getName(), "%");
			while ( rs.next() ) addColumn(rs);
		} 
		finally  {
			if (rs != null) rs.close();
		}
	}
}






