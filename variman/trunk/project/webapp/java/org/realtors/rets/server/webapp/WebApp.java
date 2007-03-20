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

import java.util.Properties;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.webapp.auth.NonceReaper;
import org.realtors.rets.server.webapp.auth.NonceTable;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

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
        return sServletContext;
    }

    public static MetadataManager getMetadataManager()
    {
        return sMetadataManager;
    }

    public static void setMetadataManager(MetadataManager metadataManager)
    {
        sMetadataManager = metadataManager;
    }

    public static String getLog4jFile()
    {
        return sLog4jFile;
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

    public static SessionHelper createHelper()
    {
        return RetsServer.createSessionHelper();
    }

    public static void setGetObjectRoot(String getObjectRoot)
    {
        sGetObjectRoot = getObjectRoot;
    }

    public static String getGetObjectRoot()
    {
        return sGetObjectRoot;
    }

    public static void setPhotoPattern(String photoPattern)
    {
        sPhotoPattern = photoPattern;
    }

    public static String getPhotoPattern()
    {
        return sPhotoPattern;
    }

    public static void setObjectSetPattern(String objectSetPattern)
    {
        sObjectSetPattern = objectSetPattern;
    }

    public static String getObjectSetPattern()
    {
        return sObjectSetPattern;
    }

    public static void setNonceTable(NonceTable nonceTable)
    {
        sNonceTable = nonceTable;
    }

    public static NonceTable getNonceTable()
    {
        return sNonceTable;
    }

    public static void setNonceReaper(NonceReaper reaper)
    {
        sReaper = reaper;
    }

    public static NonceReaper getReaper()
    {
        return sReaper;
    }

    public static void initProperties() throws IOException
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
        return sVersion;
    }

    public static String getBuildDate()
    {
        return sBuildDate;
    }

    public static final String PROJECT_NAME = "variman";
    public static final String SERVER_NAME = "Variman";

    private static ServletContext sServletContext;
    private static MetadataManager sMetadataManager;
    private static String sLog4jFile;
    private static String sGetObjectRoot;
    private static String sPhotoPattern;
    private static String sObjectSetPattern;
    private static NonceTable sNonceTable;
    private static NonceReaper sReaper;
    private static String sVersion;
    private static String sBuildDate;
    
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
    
}
