/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.swingui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.IOUtils;
import org.apache.commons.lang.math.NumberUtils;

public class ConfigurationPanel extends JPanel
{
    public ConfigurationPanel()
    {
        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(new HeaderPanel("RETS"));

        TextValuePanel retsConfig = new TextValuePanel();
        mPort = new JTextField(5);

        // Not sure why the min size needs to be set, but if it's not, then the
        // field will shrink when mMetadataDir has lots of text.  I'm not
        // at all sure why the two fields are even linked, but whatever, this
        // fixes it.
        mPort.setMinimumSize(mPort.getPreferredSize());
        retsConfig.addRow("Listening Port:", mPort, GridBagConstraints.NONE);

        JPanel box = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        mMetadataDir = new JTextField("blah");
        box.add(mMetadataDir, c);
        c.weightx = 0.0;
        box.add(new JButton(new ChooseMetadataAction()), c);
        retsConfig.addRow("Metadatata Directory:", box);

        retsConfig.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 5));
        content.add(retsConfig);

        content.add(new HeaderPanel("Database"));

        TextValuePanel tvp = new TextValuePanel();

        mDatabaseType = new JLabel();
        tvp.addRow("Type:", mDatabaseType);

        mHostName = new JLabel();
        tvp.addRow("Host Name:", mHostName);

        mDatabaseName = new JLabel();
        tvp.addRow("Database Name:", mDatabaseName);

        mUsername = new JLabel();
        tvp.addRow("Username:", mUsername);

        tvp.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 5));
        content.add(tvp);

        Box panel = Box.createHorizontalBox();
        panel.add(new JButton(new EditPropertiesAction()));
        panel.add(Box.createHorizontalGlue());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 5, 5));
        content.add(panel);

        add(content, BorderLayout.NORTH);

        updateLabels();
    }

    private void updateLabels()
    {
        RetsConfig config = Admin.getRetsConfig();
        DatabaseConfig dbConfig = config.getDatabase();
        mDatabaseType.setText(dbConfig.getDatabaseType().getLongName());
        mHostName.setText(dbConfig.getHostName());
        mDatabaseName.setText(dbConfig.getDatabaseName());
        mUsername.setText(dbConfig.getUsername());
        mPort.setText("" + config.getPort());
        String webappRoot = Admin.getWebAppRoot();
        mMetadataDir.setText(IOUtils.resolve(webappRoot,
                                              config.getMetadataDir()));
    }

    public int getPort()
    {
        return NumberUtils.stringToInt(mPort.getText());
    }

    private class ChooseMetadataAction extends AbstractAction
    {
        public ChooseMetadataAction()
        {
            super("Choose...");
        }

        public void actionPerformed(ActionEvent event)
        {
            RetsConfig config = Admin.getRetsConfig();
            String webappRoot = Admin.getWebAppRoot();
            String metadataDir = config.getMetadataDir();
            metadataDir = IOUtils.resolve(webappRoot, metadataDir);

            JFileChooser dirDialog = new JFileChooser();
            dirDialog.setSelectedFile(new File(metadataDir));
            dirDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (dirDialog.showOpenDialog(ConfigurationPanel.this) !=
                JFileChooser.APPROVE_OPTION)
            {
                return;
            }

            metadataDir = dirDialog.getSelectedFile().getPath();
            metadataDir = IOUtils.relativize(webappRoot, metadataDir);
            config.setMetadataDir(metadataDir);
            updateLabels();
        }
    }

    private class EditPropertiesAction extends AbstractAction
    {
        public EditPropertiesAction()
        {
            super("Edit...");
        }

        public void actionPerformed(ActionEvent event)
        {
            DatabasePropertiesDialog dialog = null;
            try
            {
                DatabaseConfig dbConfig = Admin.getRetsConfig().getDatabase();
                dialog = new DatabasePropertiesDialog(dbConfig);
                if (dialog.showDialog() != JOptionPane.OK_OPTION)
                {
                    return;
                }
                dialog.updateConfig(dbConfig);
                updateLabels();
                new InitDatabaseAction().execute();
            }
            finally
            {
                if (dialog != null)
                {
                    dialog.dispose();
                }
            }
        }

    }


    private JTextField mPort;
    private JTextField mMetadataDir;
    private JLabel mDatabaseType;
    private JLabel mHostName;
    private JLabel mDatabaseName;
    private JLabel mUsername;
}
