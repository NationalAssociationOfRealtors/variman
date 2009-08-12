/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.persistence.Persistence;

import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.EntityManagerHelper;
import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.DatabaseType;
import org.realtors.rets.server.config.RetsConfig;


public class AdminUtils
{

    public static void initConfig() throws RetsServerException
    {
        initAdminProperties();
        File configFile = new File(Admin.getHomeDirectory() +
                                   "/variman/WEB-INF/rets/rets-config.xml");
        Admin.setConfigFile(configFile.getAbsolutePath());
        if (configFile.exists())
        {
            Admin.setRetsConfig(
                RetsConfig.initFromXmlFile(Admin.getConfigFile()));
            Admin.setRetsConfigChanged(false);
        }
        else
        {
            RetsConfig retsConfig = RetsConfig.getInstance();
            String defaultDirectory = SystemUtils.USER_DIR + File.separator;
            retsConfig.setMetadataDir(defaultDirectory + "metadata");
            retsConfig.setGetObjectRoot(defaultDirectory + "pictures");
            retsConfig.setPhotoPattern("%k-%i.jpg");
            DatabaseConfig databaseConfig = new DatabaseConfig();
            databaseConfig.setDatabaseType(DatabaseType.POSTGRESQL);
            databaseConfig.setHostName("localhost");
            retsConfig.setDatabase(databaseConfig);
            retsConfig.setSecurityConstraints(new ArrayList());
            Admin.setRetsConfig(retsConfig);
            Admin.setRetsConfigChanged(true);
        }

        File logConfigFile =
            new File(Admin.getHomeDirectory() +
                     "/variman/WEB-INF/rets/rets-logging.properties");
        Admin.setLogConfigFile(logConfigFile.getAbsolutePath());
    }

    public static void initAdminProperties() throws RetsServerException
    {
        try
        {
            Admin.initProperties();
            LOG.info(Admin.ADMIN_NAME + " version " + Admin.getVersion());
            LOG.info("Build date " + Admin.getBuildDate());
            LOG.info("Java version " + SystemUtils.JAVA_VERSION);
            LOG.info(SystemUtils.JAVA_RUNTIME_NAME + ", version " +
                     SystemUtils.JAVA_RUNTIME_VERSION);
            LOG.info(SystemUtils.JAVA_VM_NAME + ", version " +
                     SystemUtils.JAVA_VM_VERSION);
            LOG.info(SystemUtils.OS_NAME + ", version " +
                     SystemUtils.OS_VERSION);
        }
        catch (IOException e)
        {
            throw new RetsServerException(e);
        }
    }

    public static void initDatabase()
        throws HibernateException, RetsServerException
    {
        LOG.debug("Initializing Hibernate configuration");
        RetsConfig retsConfig = Admin.getRetsConfig();
        LOG.info("JDBC URL: " + retsConfig.getDatabase().getUrl());
        String hibernateConfig = Admin.getHomeDirectory() + 
                                    "/variman/WEB-INF/classes/persistence.xml";
 
        AnnotationConfiguration config = new AnnotationConfiguration()
            .addFile(new File(hibernateConfig))
            .setProperties(retsConfig.createHibernateProperties());
        SessionFactory sessionFactory =
            config.buildSessionFactory();
        PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1,
                                        PasswordMethod.RETS_REALM);
        LOG.debug("Hibernate initialized");
        Admin.setHibernateConfiguration(config);
        
        Map properties = retsConfig.createHibernateProperties();
        RetsServer.setEntityManagerFactory(
                Persistence.createEntityManagerFactory(
                        Admin.PROJECT_NAME, properties));
        logDatabaseInfo();
    }

    private static void logDatabaseInfo() throws RetsServerException
    {
        EntityManagerHelper helper = RetsServer.createEntityManagerHelper();
        try
        {
            Connection connection = helper.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            
            LOG.info("JDBC Driver info: " + metaData.getDriverName() +
                     " version " + metaData.getDriverVersion());
            LOG.info("JDBC DB info: " + metaData.getDatabaseProductName() +
                     " version " + metaData.getDatabaseProductVersion());
        }
        catch (SQLException e)
        {
            throw new RetsServerException("Caught", e);
        }
        catch (HibernateException e)
        {
            throw new RetsServerException("Caught", e);
        }
        finally
        {
            helper.close(LOG);
        }

    }

    private static final Logger LOG =
        Logger.getLogger(AdminUtils.class);
}
