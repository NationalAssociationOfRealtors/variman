/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.wxwindows.wxWindowDisabler;
import org.wxwindows.wxJWorker;

import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.SessionFactory;

import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.config.RetsConfig;

import org.apache.log4j.Logger;

public class InitDatabaseCommand
{
    public void execute()
    {
        final wxWindowDisabler disabler = new wxWindowDisabler();
        final AdminFrame frame = Admin.getAdminFrame();
        frame.SetStatusText("Initializing database...");
        wxJWorker worker = new wxJWorker()
        {
            public Object construct()
            {
                String message = "";
                try
                {
                    LOG.info("Initializing Hibernate configuration");
                    RetsConfig retsConfig = Admin.getRetsConfig();
                    Configuration config = new Configuration()
                        .addJar("rex-hbm-xml.jar")
                        .setProperties(retsConfig.createHibernateProperties());
                    SessionFactory sessionFactory =
                        config.buildSessionFactory();
                    PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1,
                                                    PasswordMethod.RETS_REALM);
                    LOG.info("Hibernate initialized");
                    Admin.setHibernateConfiguration(config);
                    Admin.setSessionFactory(sessionFactory);
                }
                catch (Throwable e)
                {
                    LOG.error("Caught", e);
                    message = e.getMessage();
                }
                return message;
            }

            public void finished()
            {
                disabler.delete();
                frame.SetStatusText("Database initialized successfully");
            }
        };
        worker.start();
    }

    private static final Logger LOG =
        Logger.getLogger(InitDatabaseCommand.class);
}
