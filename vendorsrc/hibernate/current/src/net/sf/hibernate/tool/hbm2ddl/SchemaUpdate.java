//$Id: SchemaUpdate.java,v 1.13 2004/06/04 01:28:51 steveebersole Exp $
package net.sf.hibernate.tool.hbm2ddl;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.cfg.NamingStrategy;
import net.sf.hibernate.connection.ConnectionProvider;
import net.sf.hibernate.connection.ConnectionProviderFactory;
import net.sf.hibernate.dialect.Dialect;
import net.sf.hibernate.util.ReflectHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A commandline tool to update a database schema. May also be called from
 * inside an application.
 * 
 * @author Christoph Sturm
 */
public class SchemaUpdate {

	private static final Log log = LogFactory.getLog(SchemaUpdate.class);
	private ConnectionProvider connectionProvider;
	private Configuration configuration;
	private Dialect dialect;

	public SchemaUpdate(Configuration cfg) throws HibernateException {
		this( cfg, cfg.getProperties() );
	}

	public SchemaUpdate(Configuration cfg, Properties connectionProperties) throws HibernateException {
		this.configuration = cfg;
		dialect = Dialect.getDialect(connectionProperties);
		Properties props = new Properties();
		props.putAll( dialect.getDefaultProperties() );
		props.putAll(connectionProperties);
		connectionProvider = ConnectionProviderFactory.newConnectionProvider(props);
	}

	public static void main(String[] args) {
		try {
			Configuration cfg = new Configuration();

			boolean script = true;
			// If true then execute db updates, otherwise just generate and display updates
			boolean doUpdate = true;
			String propFile = null;

			for ( int i=0; i<args.length; i++ )  {
				if( args[i].startsWith("--") ) {
					if( args[i].equals("--quiet") ) {
						script = false;
					}
					else if( args[i].startsWith("--properties=") ) {
						propFile = args[i].substring(13);
					}
					else if ( args[i].startsWith("--config=") ) {
						cfg.configure( args[i].substring(9) );
					}
					else if ( args[i].startsWith("--text") ) {
						doUpdate = false;
					}
					else if ( args[i].startsWith("--naming=") ) {
						cfg.setNamingStrategy( 
							(NamingStrategy) ReflectHelper.classForName( args[i].substring(9) ).newInstance() 
						);
					}
				}
				else {
					cfg.addFile(args[i]);
				}

			}
			if (propFile!=null) {
				Properties props = new Properties();
				props.load( new FileInputStream(propFile) );
				new SchemaUpdate(cfg, props).execute(script, doUpdate);
			}
			else {
				new SchemaUpdate(cfg).execute(script, doUpdate);
			}
		}
		catch (Exception e) {
			log.error( "Error running schema update", e );
			e.printStackTrace();
		}
	}
	
	/**
	 * Execute the schema updates
	 * @param script print all DDL to the console
	 */
	public void execute(boolean script, boolean doUpdate) {
		
		log.info("Running hbm2ddl schema update");
		
		Connection connection=null;
		DatabaseMetadata meta;
		Statement stmt=null;
		try {

			try {
				log.info("fetching database metadata");
				connection = connectionProvider.getConnection();
				if ( !connection.getAutoCommit() ) {
					connection.commit();
					connection.setAutoCommit(true);
				}
				meta = new DatabaseMetadata(connection, dialect);
				stmt = connection.createStatement();
			}
			catch (SQLException sqle) {
				log.error("could not get database metadata", sqle);
				throw sqle;
			}
			
			log.info("updating schema");
			
			String[] createSQL = configuration.generateSchemaUpdateScript(dialect, meta);
			for (int j = 0; j < createSQL.length; j++) {
	
				final String sql = createSQL[j];
				try {
					if (script) System.out.println(sql);
					if(doUpdate) {
						log.debug(sql);
						stmt.executeUpdate(sql);
					}
				}
				catch (SQLException e) {
					log.error( "Unsuccessful: " + sql );
					log.error( e.getMessage() );
				}
			}

			log.info("schema update complete");

		}
		catch (Exception e) {
			log.error("could not complete schema update", e);
		}
		finally {
			
			try {
				if (stmt!=null) stmt.close();
				if (connection!=null) connection.close();
				if (connectionProvider!=null) connectionProvider.close();
			}
			catch (Exception e) {
				log.error("Error closing connection", e);
			}

		}		
	}
}






