/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.admin.AdminUtils;
import org.realtors.rets.server.admin.swingui.metadata.MetadataModel;
import org.realtors.rets.server.config.RetsConfig;
import org.realtors.rets.server.config.RetsConfigDao;
import org.realtors.rets.server.metadata.MetadataDao;

public class AdminFrame extends JFrame
{
    public static AdminFrame getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new AdminFrame(Admin.ADMIN_NAME);
        }
        return sInstance;
    }

    private AdminFrame(String title)
    {
        super(title);
        /*
         * Enable default text component actions (copy, paste, etc).
         * Do this before any text components have been created.
         */
        SwingUtils.addTextComponentActions();
        
        SwingUtils.setAdminFrame(this);
        initConfig();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new OnClose());
        Container content = getContentPane();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        mMenuShortcutKeyMask =
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        menu.add(new SaveAction());
        menu.add(new InstallJarAction());
        if (!Admin.isMacOS())
        {
            menu.addSeparator();
            menu.add(new QuitAction());
        }

        JMenu editMenu = SwingUtils.createEditMenu();
        menuBar.add(editMenu);

        menu = new JMenu("Database");
        menuBar.add(menu);
        menu.add(new InitDatabaseAction());
        menu.add(new CreateSchemaAction());

        setJMenuBar(menuBar);
        JMenu userMenu = new JMenu("User");
        menuBar.add(userMenu);
        JMenu groupMenu = new JMenu("Group");
        menuBar.add(groupMenu);
        
        JMenu metadataMenu = new JMenu("Metadata");

        menuBar.add(metadataMenu);

        mAdminTabs = new ArrayList();
        mTabbedPane = new JTabbedPane();
        mConfigurationPanel = new ConfigurationPanel();
        addTab("Configuration", mConfigurationPanel);
        mUsersPanel = new UsersPanel(userMenu);
        addTab("Users", mUsersPanel);
        mGroupsPanel = new GroupsPanel(groupMenu);
        addTab("Groups", mGroupsPanel);
        mLogPanel = new LogPanel();
        addTab("Logging", mLogPanel);
        mMetadataPanel = new MetadataPanel(metadataMenu);
        addTab("Metadata", mMetadataPanel);
        mMetadataModel = new MetadataModel(metadataMenu);
        JCheckBoxMenuItem metadataModelMenuItem = new JCheckBoxMenuItem("Metadata Model");
        metadataModelMenuItem.setSelected(false);
        metadataModelMenuItem.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.DESELECTED)
                {
                    if (mMetadataModelTab > -1)
                    {
                       mTabbedPane.removeTabAt(mMetadataModelTab);                    
                    }
                    mMetadataModelTab = -1;
                }
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    addTab("Metadata Model", mMetadataModel);
                   mMetadataModelTab = mTabbedPane.getTabCount() - 1;                      
                }
            }
        });
        metadataMenu.add(metadataModelMenuItem);
        
        mCurrentAdminTab = mUsersPanel;
        mTabbedPane.addChangeListener(new OnTabChanged());
        content.add(mTabbedPane, BorderLayout.CENTER);
 
        if (!Admin.isMacOS())
        {
            menu = new JMenu("Help");
            menuBar.add(menu);
            menu.add(new AboutAction());
        }

        mStatusBar = new JLabel("Status bar");
        mStatusBar.setBorder(
            BorderFactory.createEtchedBorder());
        content.add(mStatusBar, BorderLayout.SOUTH);

        setSize(640, 480);
        SwingUtils.centerOnScreen(this);
        if  (Admin.isMacOS())
        {
            MacUtils.registerApplicationListeners();
        }
    }

    public void addTab(String name, AdminTab tab)
    {
        mAdminTabs.add(tab);
        mTabbedPane.add(name, tab);
    }

    private void initConfig()
    {
        try
        {
            AdminUtils.initConfig();
        }
        catch (RetsServerException e)
        {
            LOG.error("Caught exception", e);
        }
    }

    private void saveConfig()
    {
        try
        {
            RetsConfig retsConfig = Admin.getRetsConfig();
            retsConfig.setAddress(mConfigurationPanel.getAddresss());
            retsConfig.setPort(mConfigurationPanel.getPort());
            retsConfig.setMetadataDir(mConfigurationPanel.getMetadataDir());
            retsConfig.setGetObjectRoot(mConfigurationPanel.getImageRootDir());
            retsConfig.setPhotoPattern(
                mConfigurationPanel.getPhotoPattern());
            retsConfig.setObjectSetPattern(
                mConfigurationPanel.getObjectSetPattern());

            RetsConfigDao configDao = Admin.getRetsConfigDao();
            configDao.saveRetsConfig(retsConfig);
            /*
             * Metadata is no longer stored as a singleton. Fetch the current metadata 
             * from the Metadata Panel. If it exists, use it.
             */
            Metadata metadata = mMetadataPanel.getMetadata();
            if (metadata != null)
            {
                MetadataDao metadataDao = Admin.getMetadataDao();
                metadataDao.saveMetadata(metadata);
            }
            Admin.setRetsConfigChanged(false);
        }
        catch (RetsServerException e)
        {
            LOG.error("Caught", e);
        }
    }

    public boolean quit()
    {
        if (Admin.isRetsConfigChanged())
        {
            int result = JOptionPane.showConfirmDialog(
                this, "Save changes?", "Confirm Save",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.CANCEL_OPTION)
            {
                return false;
            }
            if (result == JOptionPane.YES_OPTION)
            {
                saveConfig();
            }
        }

        dispose();
        System.exit(0);
        return true;
    }

    public void showAboutBox()
    {
        AboutBox dialog = new AboutBox(AdminFrame.this);
        dialog.setVisible(true);
        dialog.dispose();
    }

    public void setStatusText(String text)
    {
        mStatusBar.setText(text);
    }
    
    public void setDefaultCursor()
    {
        setCursor(Cursor.getDefaultCursor());
    }

    public void setWaitCursor()
    {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    public void refreshUsers()
    {
        mUsersPanel.refreshTab();
    }

    public void refreshGroups()
    {
        mGroupsPanel.refreshTab();
    }

    private class OnClose extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            quit();
        }
    }

    private class SaveAction extends AbstractAction
    {
        public SaveAction()
        {
            super("Save");
            putValue(ACCELERATOR_KEY,
                     KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                            mMenuShortcutKeyMask));
        }

        public void actionPerformed(ActionEvent event)
        {
            saveConfig();
        }

    }

    private class AboutAction extends AbstractAction
    {
        public AboutAction()
        {
            super("About " + Admin.ADMIN_NAME);
        }

        public void actionPerformed(ActionEvent event)
        {
            showAboutBox();
        }
    }

    private class QuitAction extends AbstractAction
    {
        public QuitAction()
        {
            super("Quit");
            putValue(ACCELERATOR_KEY,
                     KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                            mMenuShortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e)
        {
            quit();
        }
    }

    // Returns just the class name -- no package info.
    protected String getClassName(Object o)
    {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }

    private class OnTabChanged implements ChangeListener
    {
        public void stateChanged(ChangeEvent event)
        {
            int tab = mTabbedPane.getSelectedIndex();
            mCurrentAdminTab.tabDeselected();
            mCurrentAdminTab = (AdminTab) mAdminTabs.get(tab);
            mCurrentAdminTab.tabSelected();
            mCurrentAdminTab.refreshTab();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(AdminFrame.class);

    private static AdminFrame sInstance;
    private int mMenuShortcutKeyMask;
    private JLabel mStatusBar;

    private JTabbedPane mTabbedPane;
    private List mAdminTabs;
    private AdminTab mCurrentAdminTab;
    
    private ConfigurationPanel mConfigurationPanel;
    private UsersPanel mUsersPanel;
    private GroupsPanel mGroupsPanel;
    private LogPanel mLogPanel;
    
    private MetadataPanel mMetadataPanel;
    private MetadataModel mMetadataModel;
    private int           mMetadataModelTab = -1;
}
