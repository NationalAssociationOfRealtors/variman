/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.File;

import org.wxwindows.wxTextCtrl;

import org.apache.log4j.Logger;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import org.realtors.rets.server.Util;
import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.RetsServer;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.RetsConfig;

public class AdminUtils
{
    public static void setValue(wxTextCtrl textCtrl, Object text)
    {
        if (text == null)
        {
            textCtrl.SetValue("");
        }
        else
        {
            textCtrl.SetValue(text.toString());
        }
    }

    public static void setValue(wxTextCtrl textCtrl, int number)
    {
        textCtrl.SetValue(Integer.toString(number));
    }

    public static void setValue(wxTextCtrl textCtrl, boolean b)
    {
        textCtrl.SetValue(Util.toString(b));
    }

    public static void initConfig() throws RetsServerException
    {
        File configFile = new File(Admin.getRexHome() +
                                   "/webapp/WEB-INF/rex/rets-config.xml");
        Admin.setConfigFile(configFile.getAbsolutePath());
        Admin.setRetsConfig(
            RetsConfig.initFromXmlFile(Admin.getConfigFile()));
    }

    public static void initDatabase() throws HibernateException
    {
        LOG.debug("Initializing Hibernate configuration");
        RetsConfig retsConfig = Admin.getRetsConfig();
        Configuration config = new Configuration()
            .addJar("rex-hbm-xml.jar")
            .setProperties(retsConfig.createHibernateProperties());
        SessionFactory sessionFactory =
            config.buildSessionFactory();
        PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1,
                                        PasswordMethod.RETS_REALM);
        LOG.debug("Hibernate initialized");
        Admin.setHibernateConfiguration(config);
        RetsServer.setSessions(sessionFactory);
    }

    private static final Logger LOG =
        Logger.getLogger(AdminUtils.class);
}
