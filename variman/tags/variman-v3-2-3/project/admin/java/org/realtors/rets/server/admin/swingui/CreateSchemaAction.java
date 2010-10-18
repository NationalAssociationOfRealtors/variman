/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.swingui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import org.apache.log4j.Logger;
import org.realtors.rets.server.admin.Admin;

public class CreateSchemaAction extends AbstractAction
{
    public CreateSchemaAction()
    {
        super("Create Schema...");
    }

    public void actionPerformed(ActionEvent event)
    {
        int response = JOptionPane.showConfirmDialog(
            AdminFrame.getInstance(),
            "This will delete all your user information.\n\n" +
            "Are you sure you would like to continue?",
            "Create Schema",
            JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION)
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
        final AdminFrame frame = AdminFrame.getInstance();
        final CreateSchemaDialog dialog = new CreateSchemaDialog(frame);
        frame.setStatusText("Creating schema...");
        SwingWorker worker = new SwingWorker()
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
                dialog.dispose();
                boolean success = ((Boolean) get()).booleanValue();
                if (success)
                {
                    frame.setStatusText("Schema successfully created");
                }
                else
                {
                    frame.setStatusText("Schema create error");
                    JOptionPane.showMessageDialog(
                        frame, "Schema create error", "Create Schema",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.start();
        dialog.setVisible(true);
    }

    private class CreateSchemaDialog extends JDialog
    {
        public CreateSchemaDialog(Frame frame)
        {
            super(frame, true);
            setResizable(false);
            Container content = getContentPane();
            JLabel label = new JLabel("Creating Schema...");
            label.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
            content.add(label);
            pack();
            SwingUtils.centerOnFrame(this, frame);
        }
    }

    private static final Logger LOG =
        Logger.getLogger(CreateSchemaAction.class);
}
