/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.dialect.Dialect;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.InterpretationEnum;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataLoader;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.Table;

public class CreateDataSchemaCommand
{
    public CreateDataSchemaCommand()
    {
        mClasses = new HashMap();
        mTables = new HashMap();
        mLs = System.getProperty("line.separator");
        mSessions = RetsServer.getSessions();
    }

    public void execute()
    {
        try
        {
            loadMetadata();
            String schema = createTables();
            writeToDB(schema);
        }
        catch (Exception e)
        {
            LOG.error("Caught exception", e);
        }
    }

    private void loadMetadata()
        throws RetsServerException
    {
        String metadataDir = Admin.getRetsConfig().getMetadataDir();
        metadataDir = IOUtils.resolve(Admin.getWebAppRoot(), metadataDir);
        MetadataLoader loader = new MetadataLoader();
        MSystem system = loader.parseMetadataDirectory(metadataDir);
        System.out.println("Got system" + system.getId());
        Iterator j = system.getResources().iterator();
        while (j.hasNext())
        {
            Resource res = (Resource) j.next();
            Iterator k = res.getClasses().iterator();
            while (k.hasNext())
            {
                MClass clazz = (MClass) k.next();
                mClasses.put(clazz.getPath(), clazz);
                Iterator l = clazz.getTables().iterator();
                while (l.hasNext())
                {
                    Table table = (Table) l.next();
                    mTables.put(table.getPath(), table);
                }
            }
        }
    }

    public String createTables()
        throws HibernateException
    {
        Properties properties =
            Admin.getHibernateConfiguration().getProperties();
        Dialect dialect = Dialect.getDialect(properties);
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
                        case 0:
                            sb.append(dialect.getTypeName(Types.BIT));
                            break;
                        case 1:
                            sb.append(
                                dialect.getTypeName(Types.VARCHAR,
                                                    table.getMaximumLength()));
                            break;
                        case 2:
                            sb.append(dialect.getTypeName(Types.DATE));
                            break;
                        case 3:
                            sb.append(dialect.getTypeName(Types.TIMESTAMP));
                            break;
                        case 4:
                            sb.append(dialect.getTypeName(Types.TIME));
                            break;
                        case 5:
                            sb.append(dialect.getTypeName(Types.TINYINT));
                            break;
                        case 6:
                            sb.append(dialect.getTypeName(Types.SMALLINT));
                            break;
                        case 7:
                            sb.append(dialect.getTypeName(Types.INTEGER));
                            break;
                        case 8:
                            sb.append(dialect.getTypeName(Types.BIGINT));
                            break;
                        case 9:
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
                sb.append(dialect.getAddForeignKeyConstraintString(
                    lmTable + "_fk",
                    new String[]{"parent_id"}, sqlTableName,
                    new String[]{"id"})).append(";").append(mLs);
            }
        }

        return sb.toString();
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

    private static final Logger LOG =
        Logger.getLogger(CreateDataSchemaCommand.class);
    private Map mClasses;
    private Map mTables;
    private String mLs;
    private SessionFactory mSessions;
}
