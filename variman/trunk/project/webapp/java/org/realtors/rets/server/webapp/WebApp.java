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

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.RetsDTD;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.RetsConfigDao;
import org.realtors.rets.server.config.XmlRetsConfigDao;
import org.realtors.rets.server.webapp.auth.NonceReaper;
import org.realtors.rets.server.webapp.auth.NonceTable;

public class WebApp
{
    private WebApp() {
        // Prevents instantiation.
    }
    
    public static void setServletContext(ServletContext servletContext)
    {
        sServletContext = servletContext;
    }

    public static ServletContext getServletContext()
    {
        synchronized (sLock) {
            if (sServletContext == null) {
                throw new IllegalStateException("Must set the servlet context via the load method before calling.");
            }
            return sServletContext;
        }
    }

    public static void setLog4jFile(String log4jFile)
    {
        sLog4jFile = log4jFile;
    }

    public static void loadLog4j()
    {
        if (sLog4jFile.endsWith(".xml"))
        {
            DOMConfigurator.configure(sLog4jFile);
        }
        else
        {
            PropertyConfigurator.configure(sLog4jFile);
        }
    }
    
    public static NonceTable getNonceTable()
    {
        synchronized (sLock) {
            return sNonceTable;
        }
    }

    public static NonceReaper getReaper()
    {
        synchronized (sLock) {
            return sReaper;
        }
    }

    public static void initReleaseProperties() throws IOException
    {
        ClassLoader classLoader =
            Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        properties.load(classLoader.getResourceAsStream(
            PROJECT_NAME + "-webapp.properties"));
        sVersion = properties.getProperty("version");
        sBuildDate = properties.getProperty("build-date");
    }

    public static String getVersion()
    {
        synchronized (sLock) {
            return sVersion;
        }
    }

    public static String getBuildDate()
    {
        synchronized (sLock) {
            return sBuildDate;
        }
    }

    // This mode added by RealGo to allow a certain non-compliant client to log on,
    // namely HP Real Estate Marketing Assistant (HPMA or HP REMA)
    private static boolean sHPMAMode = false;
    public static void setHPMAMode(boolean mode)
    {
        sHPMAMode = mode;
    }

    public static boolean getHPMAMode()
    {
        return sHPMAMode;
    }
    
    private static void initNonceTable(RetsConfig config)
    {
        NonceTable nonceTable = new NonceTable();
        int initialTimeout = config.getNonceInitialTimeout();
        if (initialTimeout != -1)
        {
            nonceTable.setInitialTimeout(
                initialTimeout * DateUtils.MILLIS_PER_MINUTE);
            LOG.info("Set initial nonce timeout to " + initialTimeout +
            " minutes");
        }

        int successTimeout = config.getNonceSuccessTimeout();
        if (successTimeout != -1)
        {
            nonceTable.setSuccessTimeout(
                successTimeout * DateUtils.MILLIS_PER_MINUTE);
            LOG.info("Set success nonce timeout to " + successTimeout +
            " minutes");
        }
        sNonceTable = nonceTable;
        sReaper = new NonceReaper(nonceTable);
    }

    public static void setHPMALoginPath(String path)
    {
        sHPMALoginPath = path;
    }
    
    public static boolean isHPMALoginPath(String path)
    {
        return sHPMAMode && sHPMALoginPath.equals(path);
    }
    
    public static void loadConfiguration() throws RetsServerException
    {
        synchronized (sLock)
        {
            try
            {
                initReleaseProperties();
                RetsConfig retsConfig = initRetsConfiguration();
                RetsServer.setRetsConfiguration(retsConfig);
            }
            catch (IOException e)
            {
                throw new RetsServerException(e);
            }
        }
    }

    public static void loadConfiguration(ServletContext servletContext) throws RetsServerException
    {
        synchronized (sLock)
        {
            setServletContext(servletContext);
            loadConfiguration();
        }
    }

    private static RetsConfig initRetsConfiguration() throws RetsServerException
    {
        RetsConfigDao configDao = RetsServer.getRetsConfigDao();
        if (configDao == null)
        {
            // TODO - long-term would be good to have it be spring injected
            // but will need to migrate users from using web.xml to define
            // the location of the rets-config file
            String configFile = getContextInitParameter("rets-config-file", "WEB-INF/rets/rets-config.xml");
            configFile = IOUtils.resolve(getServletContext().getRealPath("/"), configFile);
            configDao = new XmlRetsConfigDao(configFile);
        }
        RetsConfig config = configDao.loadRetsConfig();
        LOG.info(config);

        String runInHPMAModeParameter = getContextInitParameter("runInHpmaMode", "false");
        WebApp.setHPMAMode(Boolean.valueOf(runInHPMAModeParameter).booleanValue());
        LOG.info("HPMA compatibility mode: " + sHPMAMode);

        String HPMALoginPath = getContextInitParameter("HpmaLoginPath", "/server/login");
        WebApp.setHPMALoginPath(HPMALoginPath);
        LOG.info("HPMA mode path: " + sHPMALoginPath);

        initNonceTable(config);
        RetsDTD.setBasePath(getServletContext().getRealPath("/"));
        
        return config;
    }

    private static String getContextInitParameter(String param, String defaultValue) {
        String value = getServletContext().getInitParameter(param);
        return (value == null) ? defaultValue : value;
    }

    private static final Logger LOG =
        Logger.getLogger(WebApp.class);
    public static final String PROJECT_NAME = "variman";
    public static final String SERVER_NAME = "Variman";

    private static ServletContext sServletContext;
    private static String sLog4jFile;
    private static NonceTable sNonceTable;
    private static NonceReaper sReaper;
    private static String sVersion;
    private static String sBuildDate;
    private static String sHPMALoginPath = "/server/login";
    
    private static final Object sLock = new Object();
}
