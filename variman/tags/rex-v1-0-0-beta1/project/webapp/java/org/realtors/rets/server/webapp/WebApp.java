/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.webapp;

import javax.servlet.ServletContext;

import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Session;
import net.sf.hibernate.HibernateException;

import org.realtors.rets.server.metadata.MetadataManager;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.webapp.auth.NonceTable;
import org.realtors.rets.server.webapp.auth.NonceReaper;

import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class WebApp
{
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

    public static void setSessions(SessionFactory sessionFactory)
    {
        sSessions = sessionFactory;
    }

    public static SessionHelper createHelper()
    {
        return new SessionHelper(sSessions);
    }

    public static SessionFactory getSessions()
    {
        return sSessions;
    }

    public static Session openSession() throws HibernateException
    {
        return sSessions.openSession();
    }

    public static void setGetObjectRoot(String getObjectRoot)
    {
        sGetObjectRoot = getObjectRoot;
    }

    public static String getGetObjectRoot()
    {
        return sGetObjectRoot;
    }

    public static void setGetObjectPattern(String getObjectPattern)
    {
        sGetObjectPattern = getObjectPattern;
    }

    public static String getGetObjectPattern()
    {
        return sGetObjectPattern;
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

    private static ServletContext sServletContext;
    private static MetadataManager sMetadataManager;
    private static SessionFactory sSessions;
    private static String sLog4jFile;
    private static String sGetObjectRoot;
    private static String sGetObjectPattern;
    private static NonceTable sNonceTable;
    private static NonceReaper sReaper;
}
