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
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.realtors.rets.server.ConnectionHelper;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.LogPropertiesUtils;
import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.HibernateUtils;
import org.realtors.rets.server.config.RetsConfig;

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
            WebApp.loadConfiguration(getServletContext());
            PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1, PasswordMethod.RETS_REALM);
            initHibernate();
            LOG.info("Init servlet completed successfully");
        }
        catch (RetsServerException e)
        {
            LOG.fatal("Caught exception when loading RETS configuration", e);
            throw new ServletException(e.getMessage());
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
        // Use Tomcat logging since log4j not fully initialized.
        log("log4jInitFile: " + log4jInitFile);
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
                    logHome = System.getProperty("catalina.home");
                    if (logHome == null)
                    {
                        logHome = System.getProperty("user.dir");
                    }
                    else
                    {
                        logHome = logHome + File.separator + "logs";
                    }
                }
                else 
                {
                    logHome = logHome + File.separator + "logs";
                }
            }
        }
        log("Log Home: " + logHome);
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
                log("Set logging property: " + name + " to " + value);
                System.setProperty(name, value);
            }
        }
    }

    private void initHibernate() throws ServletException {
        RetsConfig retsConfig = RetsServer.getRetsConfiguration();
        try {
            DatabaseConfig databaseConfig = retsConfig.getDatabase();
            if (databaseConfig != null)
            {
                // TODO - The database setup and hibernate initialization 
                // should really be done in spring config outside of rets config
                LOG.debug("Initializing hibernate");
                Configuration cfg = new Configuration();
                File hbmXmlFile = new File(resolveFromContextRoot("WEB-INF/classes/" + WebApp.PROJECT_NAME + "-hbm-xml.jar"));
                LOG.debug("HBM file: " + hbmXmlFile);
                cfg.addJar(hbmXmlFile);
                LOG.info("JDBC URL: " + databaseConfig.getUrl());
                Properties hibernateProperties = HibernateUtils.createHibernateProperties(databaseConfig);
                cfg.setProperties(hibernateProperties);
                RetsServer.setSessions(cfg.buildSessionFactory());
                logDatabaseInfo();
            }
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
    
}
