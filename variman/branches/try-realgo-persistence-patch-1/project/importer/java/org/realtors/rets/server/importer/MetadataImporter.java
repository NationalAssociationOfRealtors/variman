/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.log4j.Logger;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.client.RetsSession;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.TableStandardName;
import org.xml.sax.InputSource;


public class MetadataImporter extends MetadataLoader
{
    /**
     * Creates a new <code>MetadataImporter</code> instance.
     *
     */
    public MetadataImporter()
        throws Exception
    {
        super();
        initHibernate();
        mFilename = null;
    }

    public void closeRetsSession()
        throws RetsException
    {
        mRetsSession.logout();
    }

    protected void save(Object object)
    {
        try
        {
            mSession.saveOrUpdate(object);
        }
        catch (HibernateException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void doIt()
        throws Exception
    {
        if (mFilename == null)
        {
            getRetsSession();
        }
        else
        {
            InputStream in = new FileInputStream(mFilename);
            JDomCompactBuilder builder = new JDomCompactBuilder();
            builder.setStrict(RetsConfig.getInstance().getStrictParsing());
            mMetadata = builder.build(new InputSource(in));
        }
        
        parseMetadata();

        if (mFilename == null)
        {
            closeRetsSession();
        }
    }

    public void getRetsSession()
        throws RetsException
    {
        mRetsSession = new RetsSession(mConnectionURL);
        mRetsSession.login(mUsername, mPassword);
        mMetadata = mRetsSession.getMetadata();
    }

    private void initHibernate()
        throws MappingException, HibernateException
    {
        Configuration cfg = new Configuration();
        File mappingJar = new File("rex-hbm-xml.jar");
        cfg.addJar(mappingJar);
        mSessions = cfg.buildSessionFactory();
    }
    
    protected TableStandardName lookupTableStandardName(String standardName)
    {
        TableStandardName name = super.lookupTableStandardName(standardName);
        if (name == null)
        {            
            SessionHelper helper = new SessionHelper(mSessions);
            try
            {
                Session session = helper.beginTransaction();
                String hql = 
                "SELECT name " +
                "  FROM TableStandardName name " +
                " WHERE name.name = :standardName ";
                Query query = session.createQuery(hql);
                query.setString("standardName", standardName);
                List results = query.list();
                if (results.size() == 1)
                {
                    name = (TableStandardName) results.get(0);
                }
            }
            catch (HibernateException e)
            {
                helper.rollback(System.err);
            }
            finally
            {
                helper.close(System.err);
            }
            if (name != null)
            {
                mTableStandardNames.put(standardName, name);
            }
        }
        return name;
    }

    private void parseMetadata()
        throws Exception
    {
        mSession = null;
        Transaction tx = null;

        try
        {
            mSession = mSessions.openSession();

            tx = mSession.beginTransaction();
            deleteExistingMetadata();

            MSystem hSystem = doSystem();
            doResource(hSystem);
            doClasses();
            doObjects();
            doSearchHelp();
            doEditMask();
            doLookup();
            doLookupTypes();
            doUpdateHelp();
            doValidationExternal();
            doUpdate();
            doTable();
            doValidationLookup();
            doValidationLookupType();
            doValidationExternalType();
            doValidationExpression();
            doUpdateType();
            doForeignKey(hSystem);

            tx.commit();
            mSession.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                tx.rollback();
                mSession.close();
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
        finally
        {
            mSession = null;
        }
    }

    private void deleteExistingMetadata()
        throws HibernateException, SQLException
    {
        // The ordering of the tables is crucial. If deletion is done in the
        // wrong order, database constraints will be violated. This is
        // basically walks the metadata tree from the bottom up.
        String[] tables = new String[] {
            "rets_metadata_foreignkey",
            "rets_metadata_lookuptype",
            "rets_metadata_object",
            "rets_metadata_table_editmasks",
            "rets_metadata_table_standard_name",
            "rets_metadata_updatetype_attributes",
            "rets_metadata_updatetype_validationexpressions",
            "rets_metadata_validationexpression",
            "rets_metadata_validationexternaltype_displayfield",
            "rets_metadata_validationexternaltype_resultfields",
            "rets_metadata_validationexternaltype_searchfield",
            "rets_metadata_validationlookuptype",
            "rets_metadata_editmask",
            "rets_metadata_updatetype",
            "rets_metadata_validationexternaltype",
            "rets_metadata_validationlookup",
            "rets_metadata_table",
            "rets_metadata_update",
            "rets_metadata_updatehelp",
            "rets_metadata_validationexternal",
            "rets_metadata_class",
            "rets_metadata_lookup",
            "rets_metadata_searchhelp",
            "rets_metadata_resource",
            "rets_metadata_system",
        };

        Session session = null;
        Statement statement = null;
        Connection connection = null;
        try
        {
            session = mSessions.openSession();
            connection = session.connection();
            statement = connection.createStatement();
            for (int i = 0; i < tables.length; i++)
            {
                String table = tables[i];
                System.out.println("Deleting " + table);
                statement.executeUpdate("DELETE FROM " + table);
                statement.close();
            }
            connection.commit();
        }
        catch (SQLException e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
        }
        finally
        {
            close(statement);
            close(session);
        }
    }

    private void close(Session session)
    {
        try
        {
            if (session != null)
            {
                session.close();
            }
        }
        catch (HibernateException e)
        {
            LOG.error("Caught", e);
        }
    }

    private void close(Statement statement)
    {
        try
        {
            if (statement != null)
            {
                statement.close();
            }
        }
        catch (SQLException e)
        {
            LOG.error("Caught", e);
        }
    }

    /**
     * 
     * @param string
     */
    public void setConnectionURL(String string)
    {
        mConnectionURL = string;
    }

    /**
     * 
     * @param filename
     */
    private void setFile(String filename)
    {
        mFilename = filename;
    }

    /**
     * 
     * @param string
     */
    public void setPassword(String string)
    {
        mPassword = string;
    }

    /**
     * 
     * @param string
     */
    public void setUsername(String string)
    {
        mUsername = string;
    }

    /**
     * 
     * @return
     */
    private static Options getOptions()
    {
        Options ops = new Options();
        ops.addOption("c", "url", true, "The URL to connect with");
        ops.addOption("u", "username", true, "username to connect with");
        ops.addOption("p", "password", true, "password to connect with");
        ops.addOption("f", "file", true, "get metadata from this file");
        return ops;
    }

    public static final void main(String args[])
        throws Exception
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
        
        MetadataImporter mi = new MetadataImporter();
        mi.setConnectionURL(
            cmdl.getOptionValue('c',
                                "http://demo.crt.realtors.org:6103/login"));
        mi.setUsername(cmdl.getOptionValue('u', "Joe"));
        mi.setPassword(cmdl.getOptionValue('p', "Schmoe"));
        if (cmdl.hasOption('f'))
        {
            mi.setFile(cmdl.getOptionValue('f'));
        }
        mi.doIt();
    }

    /**
     * 
     * @param opt
     */
    private static void printHelp(Options opt)
    {
        HelpFormatter fs = new HelpFormatter();
        fs.printHelp("MetadataImporter [options]", opt);
    }

    private String mConnectionURL;
    private String mFilename;
    private String mPassword;
    private RetsSession mRetsSession;
    private SessionFactory mSessions;
    private Session mSession;
    private String mUsername;
    static final String CVSID =
        "$Id: MetadataImporter.java,v 1.43 2004/03/31 15:37:03 dribin Exp $";

    private static final Logger LOG = Logger.getLogger(MetadataImporter.class);

}
