/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.wxwindows.wxApp;
import org.wxwindows.wxPoint;
import org.wxwindows.wxSize;

import org.apache.log4j.xml.DOMConfigurator;

public class Main extends wxApp
{
    public Main()
    {
        DOMConfigurator.configure(
            getClass().getResource("/rex-admin-log4j.xml"));
    }

    public boolean OnInit()
    {
        wxInitAllImageHandlers();
        AdminFrame frame = new AdminFrame("Rex Administration",
                                    new wxPoint(50, 50), new wxSize(640, 480));
        Admin.setAdminFrame(frame);
        frame.Show(true);
        new InitDatabaseCommand().execute();
        return true;
    }

    public static void main(String[] args)
    {
        Admin.findRexHome();
        Main main = new Main();
        main.MainLoop();
    }
}
