/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import javax.swing.UIManager;

import org.realtors.rets.server.admin.swingui.AdminFrame;

public class SwingMain
{
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI()
    {
        setNativeLookAndFeel();

        AdminFrame adminFrame = AdminFrame.getInstance();
        adminFrame.setVisible(true);
        if (!Admin.isDebugEnabled())
        {
            new org.realtors.rets.server.admin.swingui.
                InitDatabaseAction().execute();
        }
    }

    public static void setNativeLookAndFeel()
    {
        try
        {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        }
        catch (Exception e)
        {
            System.out.println("Error setting native LAF: " + e);
        }
    }

    public static void main(String[] args)
    {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name",
                           Admin.ADMIN_NAME);
        Admin.initSystemProperties();

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
}
