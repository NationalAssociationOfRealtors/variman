/*
 * Variman RETS Server
 *
 * Author: Dave Dribin and Danny Hurlburt
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.SessionHelper;

public class WebAppLifecycleListener implements ServletContextListener
{
    public static final String INIT_PARAM_HPMA_MODE = "runInHpmaMode";

    private static final Log LOGGER = LogFactory.getLog(WebAppLifecycleListener.class);
    
    private ServletContext m_servletContext;

    public void contextInitialized(final ServletContextEvent sce)
    {
        try {
            m_servletContext = sce.getServletContext();
            WebApp.load(m_servletContext);
            final boolean runServerInHpmaMode = runServerInHpmaMode();
            WebApp.setHPMAMode(runServerInHpmaMode);
            logWebAppInfo();
            logDatabaseInfo();
            PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1, PasswordMethod.RETS_REALM);
        } catch (Throwable e) {
            LOGGER.fatal("Caught", e);
            throw new RuntimeException(e);
        }
    }

    public void contextDestroyed(final ServletContextEvent sce)
    {
        WebApp.getNonceReaper().stop();
        m_servletContext = null;
    }
    
    private void logWebAppInfo()
    {
        logWebAppProperties();
        logRetsConfigInfo();
    }

    private void logWebAppProperties()
    {
        LOGGER.info(WebApp.SERVER_NAME + " version " + WebApp.getVersion());
        LOGGER.info("Build date " + WebApp.getBuildDate());
        LOGGER.info("Java version " + SystemUtils.JAVA_VERSION);
        LOGGER.info(SystemUtils.JAVA_RUNTIME_NAME + ", version " + SystemUtils.JAVA_RUNTIME_VERSION);
        LOGGER.info(SystemUtils.JAVA_VM_NAME + ", version " + SystemUtils.JAVA_VM_VERSION);
        LOGGER.info(SystemUtils.OS_NAME + ", version " + SystemUtils.OS_VERSION);
    }
    
    private void logRetsConfigInfo()
    {
        LOGGER.info("GetObject root: " + WebApp.getGetObjectRoot());
        LOGGER.info("GetObject photo pattern: " + WebApp.getPhotoPattern());
    }
    
    private void logDatabaseInfo() throws ServletException
    {
        SessionHelper helper = RetsServer.createHelper();
        try {
            Session session = helper.beginSession();
            Connection connection = session.connection();
            DatabaseMetaData metaData = connection.getMetaData();
            LOGGER.info("JDBC Driver info: " + metaData.getDriverName() + " version " + metaData.getDriverVersion());
            LOGGER.info("JDBC DB info: " + metaData.getDatabaseProductName() + " version " + metaData.getDatabaseProductVersion());
        } catch (SQLException e) {
            throw new ServletException("Caught", e);
        } catch (HibernateException e) {
            throw new ServletException("Caught", e);
        } finally {
            try {
                helper.close();
            } catch (HibernateException e) {
                throw new ServletException(e);
            }
        }

    }
    
    private boolean runServerInHpmaMode()
    {
        String runServerInHpmaModeInitParam = m_servletContext.getInitParameter(INIT_PARAM_HPMA_MODE);
        return Boolean.valueOf(runServerInHpmaModeInitParam).booleanValue();
    }
    
}
