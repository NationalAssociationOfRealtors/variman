/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.sf.hibernate.cfg.Configuration;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.realtors.rets.common.metadata.JDomCompactBuilder;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.types.MSystem;

import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.JdomUtils;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.metadata.MetadataLoader;
//import org.realtors.rets.server.metadata.MSystem;

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

    public static void setHomeDirectory(String homeDirectory)
    {
        sHomeDirectory = homeDirectory;
    }

    public static String getHomeDirectory()
    {
        return sHomeDirectory;
    }

    public static void initSystemProperties()
    {
        findHomeDirectory();
        initDebugEnabled();
    }

    public static void findHomeDirectory()
    {
        sHomeDirectory = System.getProperty(PROJECT_NAME + ".home");
        if (sHomeDirectory == null)
        {
            sHomeDirectory = System.getProperty("user.dir");
            System.setProperty(PROJECT_NAME + ".home", sHomeDirectory);
        }
    }

    private static void initDebugEnabled()
    {
        if (System.getProperty(PROJECT_NAME + ".debug") != null)
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
        return sHomeDirectory + File.separator + "webapp";
    }

    public static boolean isDebugEnabled()
    {
        return sDebugEnabled;
    }

    public static void initProperties() throws IOException
    {
        ClassLoader classLoader =
            Thread.currentThread().getContextClassLoader();
        Properties appProperties = new Properties();
        appProperties.load(
            classLoader.getResourceAsStream(
                PROJECT_NAME + "-admin.properties"));
        sVersion = appProperties.getProperty("version");
        sBuildDate = appProperties.getProperty("build-date");
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

    public static void setLogConfigFile(String logConfigFile)
    {
        sLogConfigFile = logConfigFile;
    }

    public static String getLogConfigFile()
    {
        return sLogConfigFile;
    }
    
    public static MetadataLoader getMetadataLoader()
    {
    	if (sMetadataLoader == null)
    		sMetadataLoader = new MetadataLoader();
    	
    	return sMetadataLoader;
    }
    
//    public static MSystem getMetadata()
//    	throws RetsServerException
//    {
//        String metadataDir = Admin.getRetsConfig().getMetadataDir();
//        metadataDir = IOUtils.resolve(Admin.getWebAppRoot(), metadataDir);
//        File file = new File(metadataDir);
//        if (!file.isDirectory())
//        	return null;
//        
//        MetadataLoader loader = new MetadataLoader();
//        MSystem system = getMetadataLoader().parseMetadataDirectory(metadataDir);
//        return system;
//    }

    public static final String PROJECT_NAME = "variman";
    public static final String ADMIN_NAME = "Variman Admin";
    public static final String SERVER_NAME = "Variman";

    private static Configuration sHibernateConfiguration;
    private static String sConfigFile;
    private static RetsConfig sRetsConfig;
    private static boolean sRetsConfigChanged;
    private static String sHomeDirectory;
    private static boolean sDebugEnabled;
    private static String sVersion;
    private static String sBuildDate;
    private static String sLogConfigFile;
    private static MetadataLoader sMetadataLoader = null;

}
