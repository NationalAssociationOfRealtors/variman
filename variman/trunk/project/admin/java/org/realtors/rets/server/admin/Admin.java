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
import java.util.Properties;

import net.sf.hibernate.cfg.Configuration;

import org.realtors.rets.server.config.RetsConfig;

public class Admin
{
    public static void setHibernateConfiguration(Configuration configuration)
    {
        sHibernateConfiguration = configuration;
    }

    public static Configuration getHibernateConfiguration()
    {
        return sHibernateConfiguration;
    }

    public static void setConfigFile(String configFile)
    {
        sConfigFile = configFile;
    }

    public static String getConfigFile()
    {
        return sConfigFile;
    }

    public static RetsConfig getRetsConfig()
    {
        return sRetsConfig;
    }

    public static void setRetsConfig(RetsConfig retsConfig)
    {
        sRetsConfig = retsConfig;
    }

    public static boolean isRetsConfigChanged()
    {
        return sRetsConfigChanged;
    }

    public static void setRetsConfigChanged(boolean retsConfigChanged)
    {
        sRetsConfigChanged = retsConfigChanged;
    }

    public static void setRexHome(String rexHome)
    {
        sRexHome = rexHome;
    }

    public static String getRexHome()
    {
        return sRexHome;
    }

    public static void initSystemProperties()
    {
        findRexHome();
        initDebugEnabled();
    }

    public static void findRexHome()
    {
        sRexHome = System.getProperty("rex.home");
        if (sRexHome == null)
        {
            sRexHome = System.getProperty("user.dir");
            System.setProperty("rex.home", sRexHome);
        }
    }

    private static void initDebugEnabled()
    {
        if (System.getProperty("rex.debug") != null)
        {
            sDebugEnabled = true;
        }
        else
        {
            sDebugEnabled = false;
        }
    }

    public static String getWebAppRoot()
    {
        return sRexHome + File.separator + "webapp";
    }

    public static boolean isDebugEnabled()
    {
        return sDebugEnabled;
    }

    public static void initProperties() throws IOException
    {
        ClassLoader classLoader =
            Thread.currentThread().getContextClassLoader();
        Properties rexProperties = new Properties();
        rexProperties.load(
            classLoader.getResourceAsStream("rex-admin.properties"));
        sVersion = rexProperties.getProperty("version");
        sBuildDate = rexProperties.getProperty("build-date");
    }

    public static String getBuildDate()
    {
        return sBuildDate;
    }

    public static String getVersion()
    {
        return sVersion;
    }

    public static boolean isMacOS()
    {
        return (System.getProperty("mrj.version") != null);
    }

    private static Configuration sHibernateConfiguration;
    private static String sConfigFile;
    private static RetsConfig sRetsConfig;
    private static boolean sRetsConfigChanged;
    private static String sRexHome;
    private static boolean sDebugEnabled;
    private static String sVersion;
    private static String sBuildDate;
}
