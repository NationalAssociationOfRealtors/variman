//$Id: DatabaseMetadata.java,v 1.4 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.tool.hbm2ddl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.dialect.Dialect;

/**
 * JDBC database metadata
 * @author Christoph Sturm, Teodor Danciu
 */
public class DatabaseMetadata {
	private final Map tables = new HashMap();
	private final Set sequences = new HashSet();
	
	private DatabaseMetaData meta;
	
	public DatabaseMetadata(Connection connection, Dialect dialect) throws SQLException {
		meta = connection.getMetaData();
		initSequences(connection, dialect);
	}
	
	public TableMetadata getTableMetadata(String name) throws HibernateException {
		TableMetadata table = null;
		
		if (name!=null) {
			
			table = (TableMetadata) tables.get( name.toUpperCase() );
			if (table==null) {
				String[] types = {"TABLE"};
				ResultSet rs = null;
		
				try {
					try {
						rs = meta.getTables(null, "%", name.toUpperCase(), types);
				
						while ( rs.next() ) {
							if ( name.equalsIgnoreCase( rs.getString("TABLE_NAME") ) ) {
								table = new TableMetadata(rs, meta);
								tables.put( name.toUpperCase(), table );
								break;
							}
						}
					}
					finally {
						if (rs!=null) rs.close();
					}
				}
				catch(SQLException e) {
					throw new HibernateException(e);
				}
			}
		}
		
		return table;
	}

	private void initSequences(Connection connection, Dialect dialect) throws SQLException {
		String sql = dialect.getQuerySequencesString();
		
		if (sql==null) return;
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			
			while ( rs.next() ) sequences.add( rs.getString(1).toUpperCase() );
		} 
		finally {
			if (rs!=null) rs.close();
			if (statement!=null) statement.close();
		}
	}
	
	public boolean isSequence(Object key) {
		return key instanceof String && sequences.contains( ( (String) key ).toUpperCase() );
	}

	public boolean isTable(Object key) throws HibernateException {
		return key instanceof String && ( getTableMetadata( (String) key ) != null );
	}
}





