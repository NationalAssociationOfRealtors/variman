/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.math.NumberUtils;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.RetsConfig;

public class ConfigurationPanel extends JPanel
{
    public ConfigurationPanel()
    {
        mInConstructor = true;
        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(new HeaderPanel("RETS"));
        ConfigChangeListener changeListener = new ConfigChangeListener();

        TextValuePanel retsConfig = new TextValuePanel();
        mPort = new JTextField(5);

        // Not sure why the min size needs to be set, but if it's not, then the
        // field will shrink when mMetadataDir has lots of text.  I'm not
        // at all sure why the two fields are even linked, but whatever, this
        // fixes it.
        mPort.setMinimumSize(mPort.getPreferredSize());
        mPort.getDocument().addDocumentListener(changeListener);
        retsConfig.addRow("Listening Port:", mPort, GridBagConstraints.NONE);

        RetsConfig config = Admin.getRetsConfig();

        JPanel box = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        String webappRoot = Admin.getWebAppRoot();
        mMetadataDir = new JTextField(IOUtils.resolve(webappRoot,
                                              config.getMetadataDir()));
        mMetadataDir.getDocument().addDocumentListener(changeListener);
        box.add(mMetadataDir, c);
        c.weightx = 0.0;
        box.add(Box.createHorizontalStrut(5));
        box.add(new JButton(new ChooseMetadataAction()), c);
        retsConfig.addRow("Metadatata Directory:", box);

        box = new JPanel(new GridBagLayout());
        c.weightx = 1.0;
        mImageRootDir = new JTextField(config.getGetObjectRoot());
        mImageRootDir.getDocument().addDocumentListener(changeListener);
        box.add(mImageRootDir, c);
        c.weightx = 0.0;
        box.add(Box.createHorizontalStrut(5));
        box.add(new JButton(new ChooseImageRootAction()), c);
        retsConfig.addRow("Image Root:", box);

        mImagePattern = new JTextField(config.getGetObjectPattern());
        mImagePattern.getDocument().addDocumentListener(changeListener);
        retsConfig.addRow("Image Pattern:", mImagePattern);

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

        mInConstructor = false;
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
    }

    public int getPort()
    {
        return NumberUtils.stringToInt(mPort.getText());
    }

    public String getMetadataDir()
    {
        return mMetadataDir.getText();
    }

    public String getImageRootDir()
    {
        return mImageRootDir.getText();
    }

    public String getImagePattern()
    {
        return mImagePattern.getText();
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
            mMetadataDir.setText(metadataDir);
            Admin.setRetsConfigChanged(true);
        }
    }

    private class ChooseImageRootAction extends AbstractAction
    {
        public ChooseImageRootAction()
        {
            super("Choose...");
        }

        public void actionPerformed(ActionEvent event)
        {
            RetsConfig config = Admin.getRetsConfig();
            String webappRoot = Admin.getWebAppRoot();
            String getObjectRoot = config.getGetObjectRoot("");
            getObjectRoot = IOUtils.resolve(webappRoot, getObjectRoot);

            JFileChooser dirDialog = new JFileChooser();
            dirDialog.setSelectedFile(new File(getObjectRoot));
            dirDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (dirDialog.showOpenDialog(ConfigurationPanel.this) !=
                JFileChooser.APPROVE_OPTION)
            {
                return;
            }

            getObjectRoot = dirDialog.getSelectedFile().getPath();
            getObjectRoot = IOUtils.relativize(webappRoot, getObjectRoot);
            mImageRootDir.setText(getObjectRoot);
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

    private class ConfigChangeListener implements DocumentListener
    {
        public void insertUpdate(DocumentEvent event)
        {
            updateEdited();
        }

        public void removeUpdate(DocumentEvent event)
        {
            updateEdited();
        }

        public void changedUpdate(DocumentEvent event)
        {
            updateEdited();
        }

        private void updateEdited()
        {
            if (!mInConstructor)
            {
                // Events may occur during construction and trigger false
                // changes, so ignore changes during construction.
                Admin.setRetsConfigChanged(true);
            }
        }
    }

    private JTextField mPort;
    private JTextField mMetadataDir;
    private JLabel mDatabaseType;
    private JLabel mHostName;
    private JLabel mDatabaseName;
    private JLabel mUsername;
    private JTextField mImageRootDir;
    private JTextField mImagePattern;
    private boolean mInConstructor;
}
