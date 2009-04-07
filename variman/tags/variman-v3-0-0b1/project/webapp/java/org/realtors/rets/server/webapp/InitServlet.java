/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.cfg.Configuration;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.realtors.rets.server.ConnectionHelper;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.LogPropertiesUtils;
import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.QueryCountTable;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.GroupRules;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.MetadataLoader;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.protocol.ConditionRuleSet;
import org.realtors.rets.server.protocol.TableGroupFilter;
import org.realtors.rets.server.webapp.auth.NonceReaper;
import org.realtors.rets.server.webapp.auth.NonceTable;

/**
 * FIXME: Change to a ServletContextListener. (This will still maintain
 * Servlet 2.3 compatibility.)
 * 
 * @web.servlet name="init-servlet"
 *   load-on-startup="1"
 */
public class InitServlet extends RetsServlet
{
    public void init() throws ServletException
    {
        initLog4J();
        try
        {
            LOG.info("Running init servlet");
            initWebAppProperties();
            WebApp.setServletContext(getServletContext());
            PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1,
                                            PasswordMethod.RETS_REALM);
            initRetsConfiguration();
            initHibernate();
            initMetadata();
            initNonceTable();
            initGroupFilter();
            initConditionRuleSet();
            LOG.info("Init servlet completed successfully");
        }
        catch (ServletException e)
        {
            LOG.fatal("Caught", e);
            Throwable rootCause = e.getRootCause();
            if (rootCause != null)
            {
                LOG.fatal("Caused by", rootCause);
            }
            throw e;
        }
        catch (RuntimeException e)
        {
            LOG.fatal("Caught", e);
            throw e;
        }
        catch (Error e)
        {
            LOG.fatal("Caught", e);
            throw e;
        }
    }

    private void initWebAppProperties() throws ServletException
    {
        try
        {
            WebApp.initProperties();
            LOG.info(WebApp.SERVER_NAME + " version " + WebApp.getVersion());
            LOG.info("Build date " + WebApp.getBuildDate());
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
            throw new ServletException(e);
        }
    }

    private String resolveFromContextRoot(String file)
    {
        return IOUtils.resolve(getServletContext().getRealPath("/"), file);
    }

    /**
     * Initialize log4j. First, the application's context is checked for the
     * file name, and then the servlet context is checked.
     */
    private void initLog4J()
    {
        initLogHome();
        initLogLevels();
        String log4jInitFile =
            getContextInitParameter(
                "log4j-init-file", "WEB-INF/classes/" +
                WebApp.PROJECT_NAME + "-webapp-log4j.properties");
        log4jInitFile = resolveFromContextRoot(log4jInitFile);
        WebApp.setLog4jFile(log4jInitFile);
        WebApp.loadLog4j();
    }

    /**
     * Sets the <code>variman.log.home</code> system property so it can be used
     * in the log4j config file.
     */
    private void initLogHome()
    {
        String prefix = WebApp.PROJECT_NAME;
        String logHome = getContextInitParameter(prefix + "-log4j-home", null);
        if (logHome == null)
        {
            logHome = System.getProperty(prefix + ".log.home");
            if (logHome == null)
            {
                logHome = System.getProperty(prefix + ".home");
                if (logHome == null)
                {
                    logHome = System.getProperty("user.dir");
                }
                else
                {
                    logHome = logHome + File.separator + "logs";
                }
            }
        }
        System.setProperty(prefix + ".log.home", logHome);
    }

    private void initLogLevels()
    {
        String logPropertiesFileName =
            getContextInitParameter("rets-config-file",
                                    "WEB-INF/rets/rets-logging.properties");
        logPropertiesFileName = resolveFromContextRoot(logPropertiesFileName);
        Properties logProperties =
            LogPropertiesUtils.createDefaultLoggingProperties();
        try
        {
            File logPropertiesFile = new File(logPropertiesFileName);
            if (logPropertiesFile.exists() && logPropertiesFile.canRead())
            {
                    logProperties.load(new FileInputStream(logPropertiesFile));
            }
        }
        catch (IOException e)
        {
            // No logging setup, so just ignore
        }
        Enumeration e = logProperties.propertyNames();
        while (e.hasMoreElements())
        {
            String name = (String) e.nextElement();
            if (name.startsWith(WebApp.PROJECT_NAME + "."))
            {
                String value = logProperties.getProperty(name);
                log("Set loging property: " + name + " to " + value);
                System.setProperty(name, value);
            }
        }
    }

    private void initRetsConfiguration() throws ServletException
    {
        try
        {
            String configFile =
                getContextInitParameter("rets-config-file",
                                        "WEB-INF/rets/rets-config.xml");
            configFile = resolveFromContextRoot(configFile);
            mRetsConfig = RetsConfig.initFromXml(new FileReader(configFile));

            String address = mRetsConfig.getAddress();
            if  (address == null)
                address = "all IP addresses";
            LOG.info("Listening on " + address + ", port " +
                     mRetsConfig.getPort());

            String getObjectRoot = getGetObjectRoot();
            WebApp.setGetObjectRoot(getObjectRoot);
            LOG.info("GetObject root: " + getObjectRoot);

            String photoPattern = mRetsConfig.getPhotoPattern();
            WebApp.setPhotoPattern(photoPattern);
            LOG.info("GetObject photo pattern: " + photoPattern);

            String objectSetPattern = mRetsConfig.getObjectSetPattern();
            WebApp.setObjectSetPattern(objectSetPattern);
            LOG.info("GetObject object set pattern: " + objectSetPattern);

            RetsServer.setSecurityConstraints(
                mRetsConfig.getSecurityConstraints());

            RetsServer.setQueryCountTable(new QueryCountTable());
        }
        catch (IOException e)
        {
            throw new ServletException(e);
        }
        catch (RetsServerException e)
        {
            throw new ServletException(e);
        }
    }

    private String getGetObjectRoot()
    {
        ServletContext context = getServletContext();
        String getObjectRoot =
            mRetsConfig.getGetObjectRoot(context.getRealPath("/"));
        File getObjectRootFile = new File(getObjectRoot);
        if (!getObjectRootFile.exists())
        {
            LOG.warn("GetObject root does not exist: " + getObjectRoot);
       }
        else if (!getObjectRootFile.isDirectory())
        {
            LOG.warn("GetObject root is not a directory: " +
                     getObjectRoot);
       }
        else if (!getObjectRootFile.canRead())
        {
            LOG.warn("GetObject root is not readable: " + getObjectRoot);
       }
        return getObjectRoot;
    }

    private void initHibernate() throws ServletException
    {
        try
        {
            LOG.debug("Initializing hibernate");
            Configuration cfg = new Configuration();
            File hbmXmlFile = new File(resolveFromContextRoot(
	            "WEB-INF/classes/" + WebApp.PROJECT_NAME + "-hbm-xml.jar"));
	        LOG.debug("HBM file: " + hbmXmlFile);
            cfg.addJar(hbmXmlFile);
            LOG.info("JDBC URL: " + mRetsConfig.getDatabase().getUrl());
            cfg.setProperties(mRetsConfig.createHibernateProperties());
            RetsServer.setSessions(cfg.buildSessionFactory());
            logDatabaseInfo();
        }
        catch (HibernateException e)
        {
            throw new ServletException("Could not initialize hibernate", e);
        }
    }

    private void logDatabaseInfo() throws ServletException
    {
        ConnectionHelper helper = RetsServer.createHelper();
        try
        {
            Connection connection = helper.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            LOG.info("JDBC Driver info: " + metaData.getDriverName() +
                     " version " + metaData.getDriverVersion());
            LOG.info("JDBC DB info: " + metaData.getDatabaseProductName() +
                     " version " + metaData.getDatabaseProductVersion());
        }
        catch (Exception e)
        {
            throw new ServletException("Caught", e);
        }
        finally
        {
            helper.close(LOG);
        }

    }

    private void initMetadata() throws ServletException
    {
        LOG.debug("Initializing metadata");
        MSystem system = findSystem();
        LOG.debug("Creating metadata manager");
        MetadataManager manager = new MetadataManager();
        manager.addRecursive(system);
        LOG.debug("Adding metadata to servlet context");
        WebApp.setMetadataManager(manager);
    }

    private MSystem findSystem() throws ServletException
    {
        try
        {
            String metadataDir = mRetsConfig.getMetadataDir();
            metadataDir = resolveFromContextRoot(metadataDir);
            LOG.info("Reading metadata from: " + metadataDir);
            MetadataLoader loader = new MetadataLoader();
            return loader.parseMetadataDirectory(metadataDir);
        }
        catch (RetsServerException e)
        {
            throw new ServletException(e);
        }
    }

    private void initNonceTable()
    {
        NonceTable nonceTable = new NonceTable();
        int initialTimeout = mRetsConfig.getNonceInitialTimeout();
        if (initialTimeout != -1)
        {
            nonceTable.setInitialTimeout(
                initialTimeout * DateUtils.MILLIS_PER_MINUTE);
            LOG.info("Set initial nonce timeout to " + initialTimeout +
                      " minutes");
        }

        int successTimeout = mRetsConfig.getNonceSuccessTimeout();
        if (successTimeout != -1)
        {
            nonceTable.setSuccessTimeout(
                successTimeout * DateUtils.MILLIS_PER_MINUTE);
            LOG.info("Set success nonce timeout to " + successTimeout +
                      " minutes");
        }
        WebApp.setNonceTable(nonceTable);
        WebApp.setNonceReaper(new NonceReaper(nonceTable));
    }

    private void initGroupFilter()
    {
        LOG.debug("Initializing group filter");
        TableGroupFilter groupFilter = new TableGroupFilter();
        MetadataManager metadataManager = WebApp.getMetadataManager();
        MSystem system =
            (MSystem) metadataManager.findUnique(MSystem.TABLE, "");
        Set resources = system.getResources();
        for (Iterator i = resources.iterator(); i.hasNext();)
        {
            Resource resource = (Resource) i.next();
            String resourceID = resource.getResourceID();
            Set classes = resource.getClasses();
            for (Iterator j = classes.iterator(); j.hasNext();)
            {
                MClass aClass = (MClass) j.next();
                String className = aClass.getClassName();
                LOG.debug("Setting tables for " + resourceID + ":" + className);
                groupFilter.setTables(resourceID, className,
                                      aClass.getTables());
            }
        }
        List securityConstraints = mRetsConfig.getAllGroupRules();
        for (int i = 0; i < securityConstraints.size(); i++)
        {
            GroupRules rules = (GroupRules) securityConstraints.get(i);
            LOG.debug("Adding rules for " + rules.getGroupName());
            groupFilter.addRules(rules);
        }

        RetsServer.setTableGroupFilter(groupFilter);
    }

    private void initConditionRuleSet()
    {
        LOG.debug("Initializing condition rule set");
        ConditionRuleSet ruleSet = new ConditionRuleSet();
        List securityConstraints = mRetsConfig.getAllGroupRules();
        for (int i = 0; i < securityConstraints.size(); i++)
        {
            GroupRules rules = (GroupRules) securityConstraints.get(i);
            LOG.debug("Adding condition rules for " + rules.getGroupName());
            ruleSet.addRules(rules);
        }
        RetsServer.setConditionRuleSet(ruleSet);
    }

    protected void doRets(RetsServletRequest request, RetsServletResponse response)
            throws RetsServerException, IOException
    {
        // Does nothing.
    }
    
    public void destroy()
    {
        WebApp.getReaper().stop();
    }
    
    private static final Logger LOG = Logger.getLogger(InitServlet.class);
    private RetsConfig mRetsConfig;
    
}
