/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 * Created on Jul 28, 2003
 *
 */
package org.realtors.rets.server.importer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.dialect.Dialect;

import org.realtors.rets.server.metadata.InterpretationEnum;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Table;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;

/**
 * Creates a database schema based on the metadata currently in the database.
 * Currently optimized for postgres, but should be fairly portable.
 * 
 * @author kgarner
 */
public class CreateSchema extends RetsHelpers
{
    /**
     * Constructor that inits hibernate and loads the type mappings.
     * 
     * @throws HibernateException if hibernate gets ticked
     */
    public CreateSchema() throws HibernateException
    {
        super();
        mLs = System.getProperty("line.separator");
    }

    public String createTables()
        throws InstantiationException, IllegalAccessException,
               ClassNotFoundException, HibernateException
    {
        Dialect dialect = (Dialect)
            getClass().getClassLoader().loadClass(mDialectClass).newInstance();
        Iterator i = mClasses.values().iterator();
        StringBuffer sb = new StringBuffer();
        while (i.hasNext())
        {
            MClass clazz = (MClass) i.next();

            String sqlTableName = clazz.getDbTable();

            sb.append("CREATE TABLE ").append(sqlTableName);
            sb.append(" (").append(mLs);
            sb.append("\tid ").append(dialect.getTypeName(Types.BIGINT));
            sb.append(" NOT NULL,").append(mLs);
            
            boolean needsLookupMultiTable = false;
            boolean firstDone = false;

            Set needsIndex = new HashSet();
            Iterator j = clazz.getTables().iterator();
            while (j.hasNext())
            {
                Table table = (Table) j.next();
                if (table.getInterpretation() ==
                    InterpretationEnum.LOOKUPMULTI)
                {
                    needsLookupMultiTable = true;
                }
                else
                {
                    if (firstDone)
                    {
                        sb.append(",").append(mLs);
                    }
                    else
                    {
                        firstDone = true;
                    }
                    sb.append("\t").append(table.getDbName()).append(" ");
                    switch (table.getDataType().toInt())
                    {
                        case 0 :
                            sb.append(dialect.getTypeName(Types.BOOLEAN));
                            break;
                        case 1 :
                            sb.append(
                                dialect.getTypeName(Types.VARCHAR,
                                                    table.getMaximumLength()));
                            break;
                        case 2 :
                            sb.append(dialect.getTypeName(Types.DATE));
                            break;
                        case 3 :
                            sb.append(dialect.getTypeName(Types.TIMESTAMP));
                            break;
                        case 4 :
                            sb.append(dialect.getTypeName(Types.TIME));
                            break;
                        case 5 :
                            sb.append(dialect.getTypeName(Types.TINYINT));
                            break;
                        case 6 :
                            sb.append(dialect.getTypeName(Types.SMALLINT));
                            break;
                        case 7 :
                            sb.append(dialect.getTypeName(Types.INTEGER));
                            break;
                        case 8 :
                            sb.append(dialect.getTypeName(Types.BIGINT));
                            break;
                        case 9 :
                            /* Not sure if this should be DECIMAL or NUMERIC */
                            //sb.append(dialect.getTypeName(Types.DECIMAL));
                            sb.append(dialect.getTypeName(Types.NUMERIC));
                            break;
                    }
                    
                    if (table.isUnique() && dialect.supportsUnique())
                    {
                        sb.append(" unique");
                    }
    
                    if (table.getIndex() > 0)
                    {
                        needsIndex.add(table);
                    }
                }
            }
            
            sb.append(mLs).append(");").append(mLs);
            sb.append("alter table ").append(sqlTableName);
            sb.append(dialect.getAddPrimaryKeyConstraintString(
                      sqlTableName + "_pk_id"));
            sb.append("(id);").append(mLs);

            j = needsIndex.iterator();
            while (j.hasNext())
            {
                Table table = (Table) j.next();
                String dbName = table.getDbName();
                sb.append("create index ").append(sqlTableName).append("_");
                sb.append(dbName).append("_index on ").append(sqlTableName);
                sb.append("(").append(dbName).append(");").append(mLs);
            }
            
            if (needsLookupMultiTable)
            {
                String bigint = dialect.getTypeName(Types.BIGINT);
                String lmTable = sqlTableName + "_lm";
                sb.append("create table ").append(lmTable).append(" (");
                sb.append(mLs);
                sb.append("\tid ").append(bigint).append(" NOT NULL,");
                sb.append(mLs);
                sb.append("\tparent_id ").append(bigint).append(" NOT NULL, ");
                sb.append(mLs);
                sb.append("\tlookup_name ");
                sb.append(dialect.getTypeName(Types.VARCHAR, 32));
                sb.append(" NOT NULL, ").append(mLs);
                sb.append("\tvalue ");
                sb.append(dialect.getTypeName(Types.VARCHAR, 32)).append(mLs);
                sb.append(");").append(mLs);
                
                // Do the primary key
                sb.append("alter table ").append(lmTable);
                sb.append(dialect.getAddPrimaryKeyConstraintString(
                    lmTable + "_pk_id"));
                sb.append("(id);").append(mLs);
                
                // do the foreign key
                sb.append("alter table ").append(lmTable);
                sb.append(
                    dialect.getAddForeignKeyConstraintString(lmTable + "_fk",
                        new String[] { "parent_id" }, sqlTableName,
                        new String[] { "id" })).append(";").append(mLs);
            }
        }

        return sb.toString();
    }

    /**
     * Sets the name of the dialect class to use.
     * @param string
     */
    public void setDialectClass(String string)
    {
        mDialectClass = string;
    }

    /**
     * Write the schema out to the database.
     * 
     * @param schema the schema to put into the db
     */
    private void writeToDB(String schema)
        throws HibernateException, SQLException
    {
        Session session = null;
        Connection con = null;
        try
        {
            session = mSessions.openSession();
            con = session.connection();
            Statement stmt = con.createStatement();
            stmt.execute(schema);
            con.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            if (con != null)
            {
                con.rollback();
            }
            if (session != null)
            {
                session.close();
            }
            throw e;
        }
        catch (SQLException e)
        {
            if (con != null)
            {
                con.rollback();
            }
            if (session != null)
            {
                session.close();
            }
            throw e;
        }
    }

    private static Options getOptions()
    {
        Options ops = new Options();
        ops.addOption("d", "database", false,
                      "just create right into the database");
        ops.addOption("D", "dialect", true, "The dialect class to use");
        ops.addOption("f", "file", true, "output file");
        ops.addOption("v", "verbose", false, "spew to the screen anyway");
        return ops;
    }

    public static void main(String[] args)
        throws IOException, HibernateException, SQLException,
               InstantiationException, IllegalAccessException,
               ClassNotFoundException
    {
        Parser parser = new GnuParser();
        Options opts = getOptions();
        CommandLine cmdl = null;
        try
        {
            cmdl = parser.parse(opts, args);
        }
        catch (Exception e)
        {
            printHelp(opts);
            System.exit(1);
        }
        CreateSchema cs = new CreateSchema();
        
        cs.setDialectClass(cmdl.getOptionValue('D',
            "net.sf.hibernate.dialect.PostgreSQLDialect"));

        cs.loadMetadata();
        String schema = cs.createTables();

        if (cmdl.hasOption('f'))
        {
            writeFile(cmdl.getOptionValue('f', "schema.out"), schema);
        }

        if (cmdl.hasOption('d'))
        {
            cs.writeToDB(schema);
        }

        if (cmdl.hasOption('v') ||
            !(cmdl.hasOption('f') || cmdl.hasOption('d')))
        {
            System.out.print(schema);
        }
    }

    private static void printHelp(Options opt)
    {
        HelpFormatter fs = new HelpFormatter();
        fs.printHelp("CreateSchema [options]", opt);
    }

    /**
     * Write the schema out to a file.
     * 
     * @param fileName the output filename
     * @param schema the schema string
     */
    private static void writeFile(String fileName, String schema)
        throws IOException
    {
        PrintWriter pw = new PrintWriter(new FileWriter(fileName));
        pw.print(schema);
        pw.close();
    }

    private String mDialectClass;
    /** The Line Seperator */
    private String mLs;
}
