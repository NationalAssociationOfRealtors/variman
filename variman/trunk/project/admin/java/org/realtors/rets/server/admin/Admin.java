/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.SessionFactory;

import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.SessionHelper;

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

    public static SessionFactory getSessionFactory()
    {
        return sSessionFactory;
    }

    public static void setSessionFactory(SessionFactory sessionFactory)
    {
        sSessionFactory = sessionFactory;
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

    public static SessionHelper createSessionHelper()
    {
        return new SessionHelper(sSessionFactory);
    }

    private static Configuration sHibernateConfiguration;
    private static SessionFactory sSessionFactory;
    private static String sConfigFile;
    private static RetsConfig sRetsConfig;
    private static AdminFrame sAdminFrame;
}
