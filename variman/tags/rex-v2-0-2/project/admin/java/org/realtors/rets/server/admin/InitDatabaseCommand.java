/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.wxwindows.wx;
import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxDialog;
import org.wxwindows.wxJWorker;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxWindow;
import org.wxwindows.wxWindowDisabler;

import org.apache.log4j.Logger;

public class InitDatabaseCommand extends wx
{
    public void execute()
    {
        wxBeginBusyCursor();
        final wxWindowDisabler disabler = new wxWindowDisabler();
        final AdminFrame frame = Admin.getAdminFrame();
        final InitDatabaseDialog dialog = new InitDatabaseDialog(frame);
        dialog.Show();
        frame.SetStatusText("Initializing database...");
        wxJWorker worker = new wxJWorker()
        {
            public Object construct()
            {
                try
                {
                    AdminUtils.initDatabase();
                }
                catch (Throwable e)
                {
                    LOG.error("Caught", e);
                    return e;
                }
                return null;
            }

            public void finished()
            {
                dialog.Destroy();
                disabler.delete();
                wxEndBusyCursor();
                Object o = get();
                if (o instanceof Throwable)
                {
                    Throwable t = (Throwable) o;
                    frame.SetStatusText("Database initialization failed: " +
                                        t.getMessage());
                }
                else
                {
                    frame.SetStatusText("Database initialized successfully");
                }
            }
        };
        worker.start();
    }

    private class InitDatabaseDialog extends wxDialog
    {
        public InitDatabaseDialog(wxWindow parent)
        {
            super(parent, -1, "Status");
            wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
            wxStaticText label =
                new wxStaticText(this, -1, "Initializing database...");
            box.Add(label, 0, wxALIGN_CENTER | wxALL, 35);
            SetSizer(box);
            box.Fit(this);
            CenterOnParent();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(InitDatabaseCommand.class);
}
