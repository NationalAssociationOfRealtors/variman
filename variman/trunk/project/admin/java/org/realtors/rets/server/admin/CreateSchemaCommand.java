/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

import org.apache.log4j.Logger;

import org.wxwindows.wx;
import org.wxwindows.wxJWorker;
import org.wxwindows.wxWindowDisabler;

public class CreateSchemaCommand extends wx
{
    public void execute()
    {
        int response = wxMessageBox("This will delete all your metadata and user " +
                                    "information.\n\n" +
                                    "Are you sure you would like to continue?",
                                    "Create Schema",
                                    wxYES_NO | wxNO_DEFAULT |
                                    wxICON_EXCLAMATION,
                                    Admin.getAdminFrame());
        if (response == wxYES)
        {
            createSchemaInBg();
        }
    }

    /**
     * Creates a schema in a background thread, while disabling the UI.
     * Since there is no way to cancel the operation, there's no use in
     * displaying a dialog box. However, running it in the main thread
     * blocks the GUI thread, making the app looked locked up.
     */
    private void createSchemaInBg()
    {
        final wxWindowDisabler disabler = new wxWindowDisabler();
        final AdminFrame frame = Admin.getAdminFrame();
        frame.SetStatusText("Creating schema...");
        wxJWorker worker = new wxJWorker()
        {
            public Object construct()
            {
                Boolean success = Boolean.FALSE;
                try
                {
                    Configuration config =
                        Admin.getHibernateConfiguration();
                    new SchemaExport(config).create(false, true);
                    success = Boolean.TRUE;
                }
                catch (Throwable e)
                {
                    LOG.error("Caught exception", e);
                }
                return success;
            }

            public void finished()
            {
                disabler.delete();
                boolean success = ((Boolean) get()).booleanValue();
                if (success)
                {
                    frame.SetStatusText("Schema successfully created");
                }
                else
                {
                    frame.SetStatusText("Schema create error");
                    wxMessageBox("Schema create error", "Create Schema",
                                 wxOK | wxICON_ERROR, frame);
                }
            }
        };
        worker.start();
    }

    private static final Logger LOG =
        Logger.getLogger(CreateSchemaCommand.class);
}
