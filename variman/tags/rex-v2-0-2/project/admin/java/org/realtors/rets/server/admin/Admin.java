/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.File;

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

    public static AdminFrame getAdminFrame()
    {
        return sAdminFrame;
    }

    public static void setAdminFrame(AdminFrame adminFrame)
    {
        sAdminFrame = adminFrame;
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

    private static Configuration sHibernateConfiguration;
    private static String sConfigFile;
    private static RetsConfig sRetsConfig;
    private static AdminFrame sAdminFrame;
    private static String sRexHome;
    private static boolean sDebugEnabled;
}
