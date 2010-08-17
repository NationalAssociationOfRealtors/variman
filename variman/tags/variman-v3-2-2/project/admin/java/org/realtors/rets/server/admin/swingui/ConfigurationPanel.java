/*
 * Variman RETS Server
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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.RetsConfig;

public class ConfigurationPanel extends AdminTab
{
    public ConfigurationPanel()
    {
        mInConstructor = true;
        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(new HeaderPanel("RETS"));
        ConfigChangeListener changeListener = new ConfigChangeListener();
        FocusListener focusListener = new DirectoryChangedListener();

        TextValuePanel retsConfig = new TextValuePanel();

        JPanel box;
        GridBagConstraints c;

        mAddressPanel = new AddressPanel();
        mAddressPanel.getDocument().addDocumentListener(changeListener);
        retsConfig.addRow("Listening IP Address:", mAddressPanel);

        mPort = new JTextField(5);

        // Not sure why the min size needs to be set, but if it's not, then the
        // field will shrink when mMetadataDir has lots of text.  I'm not
        // at all sure why the two fields are even linked, but whatever, this
        // fixes it.
        mPort.setMinimumSize(mPort.getPreferredSize());
        mPort.getDocument().addDocumentListener(changeListener);
        retsConfig.addRow("Listening Port:", mPort, GridBagConstraints.NONE);

        RetsConfig config = Admin.getRetsConfig();

        box = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        String webappRoot = Admin.getWebAppRoot();
        mMetadataDir = new JTextField(IOUtils.resolve(webappRoot,
                                              config.getMetadataDir()));
        mMetadataDir.getDocument().addDocumentListener(changeListener);
        mMetadataDir.addFocusListener(focusListener);
        box.add(mMetadataDir, c);
        c.weightx = 0.0;
        box.add(Box.createHorizontalStrut(5));
        box.add(new JButton(new ChooseMetadataAction()), c);
        
        retsConfig.addRow("Metadatata Directory:", box);

        box = new JPanel(new GridBagLayout());
        c.weightx = 1.0;
        mImageRootDir = new JTextField(config.getGetObjectRoot());
        mImageRootDir.getDocument().addDocumentListener(changeListener);
        mImageRootDir.addFocusListener(focusListener);
        box.add(mImageRootDir, c);
        c.weightx = 0.0;
        box.add(Box.createHorizontalStrut(5));
        box.add(new JButton(new ChooseImageRootAction()), c);
        retsConfig.addRow("Get Object Root:", box);

        mPhotoPattern = new JTextField(config.getPhotoPattern());
        mPhotoPattern.getDocument().addDocumentListener(changeListener);
        retsConfig.addRow("Photo Pattern:", mPhotoPattern);

        mObjectSetPattern = new JTextField(config.getObjectSetPattern());
        mObjectSetPattern.getDocument().addDocumentListener(changeListener);
        retsConfig.addRow("Object Set Pattern:", mObjectSetPattern);

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
        mAddressPanel.setAddress(config.getAddress());
        mPort.setText("" + config.getPort());
    }

    public String getAddresss()
    {
        return mAddressPanel.getAddress();
    }

    public int getPort()
    {
        return NumberUtils.toInt(mPort.getText());
    }

    public String getMetadataDir()
    {
        return mMetadataDir.getText();
    }

    public String getImageRootDir()
    {
        return mImageRootDir.getText();
    }

    public String getPhotoPattern()
    {
        return mPhotoPattern.getText();
    }

    public String getObjectSetPattern()
    {
        return mObjectSetPattern.getText();
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
            // Save to the configuration so we can retrieve it from MetadataPanel.
            config.setMetadataDir(metadataDir);
            mMetadataDir.setText(metadataDir);
            Admin.setRetsConfigChanged(true);
        }
    }

    private class DirectoryChangedListener implements FocusListener
    {
       	public void focusGained()
    	{
    	}
    	
    	public void focusLost()
    	{
    	}
    	
    	public void focusGained(FocusEvent e)
    	{
    	}
    	
    	public void focusLost(FocusEvent e)
    	{
    		/*
    		 * See if the other component involved in the focus change is one
    		 * of the JButtons that invoke the JFileChooser. If so, we do
    		 * not want to process the directory name right now.
    		 */
    		if (e.getOppositeComponent() instanceof JButton)
    			return;
    		
    		JTextField textField = (JTextField)e.getSource();
    		String text = textField.getText();
    		if (text != null && text.length() > 0)
    		{
    			File file = new File(text);
    			if (!file.exists())
    			{
					int answer = (int) JOptionPane.showConfirmDialog(
				                            SwingUtils.getAdminFrame(),
				                            "Create the Directory?",
				                            "Directory Doesn't Exist",
				                            JOptionPane.OK_CANCEL_OPTION,
				                            JOptionPane.QUESTION_MESSAGE);
					if (answer == JOptionPane.OK_OPTION)
					{
						try
						{
							textField.setText(file.getCanonicalPath());
							file.mkdirs();
						}
						catch (Exception f)
						{
                            JOptionPane.showMessageDialog(
                                    SwingUtils.getAdminFrame(),
                                    "Unable to create " + text + "\n" + f.getMessage());
						}
					}
    			}
    		}
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

    private void updateEdited()
    {
        if (!mInConstructor)
        {
            // Events may occur during construction and trigger false
            // changes, so ignore changes during construction.
            Admin.setRetsConfigChanged(true);
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
    }

    private class AddressPanel extends JPanel
    {
        public AddressPanel()
        {
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.0;
            String[] options = {"All", "Specified:"};
            mAddressMenu = new JComboBox(options);
            mAddressMenu.setAction(new AddressChangedAction());
            add(mAddressMenu, c);

            add(Box.createHorizontalStrut(5));

            c.weightx = 1.0;
            mListeningAddress = new JTextField();
            add(mListeningAddress, c);
            setAddress(null);
        }

        public void setAddress(String address)
        {
            if (StringUtils.isBlank(address))
            {
                mAddressMenu.setSelectedIndex(0);
                mListeningAddress.setText(null);
            }
            else
            {
                mAddressMenu.setSelectedIndex(1);
                mListeningAddress.setText(address);
            }
            syncAddressComponents();
        }

        private void syncAddressComponents()
        {
            if (mAddressMenu.getSelectedIndex() == 0)
                mListeningAddress.setEnabled(false);
            else
                mListeningAddress.setEnabled(true);
        }

        public String getAddress()
        {
            if (mAddressMenu.getSelectedIndex() == 0)
                return null;
            else
                return mListeningAddress.getText();
        }

        public Document getDocument()
        {
            return mListeningAddress.getDocument();
        }

        class AddressChangedAction extends AbstractAction
        {
            public void actionPerformed(ActionEvent event)
            {
                updateEdited();
                syncAddressComponents();
            }
        }

        private JComboBox mAddressMenu;
        private JTextField mListeningAddress;
    }

    private AddressPanel mAddressPanel;
    private JTextField mPort;
    private JTextField mMetadataDir;
    private JLabel mDatabaseType;
    private JLabel mHostName;
    private JLabel mDatabaseName;
    private JLabel mUsername;
    private JTextField mImageRootDir;
    private JTextField mPhotoPattern;
    private JTextField mObjectSetPattern;
    private boolean mInConstructor;
}
