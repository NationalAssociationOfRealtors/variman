/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.swingui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.admin.Admin;
import org.realtors.rets.server.admin.AdminUtils;
import org.realtors.rets.server.config.RetsConfig;

public class AdminFrame extends JFrame
{
    public static AdminFrame getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new AdminFrame("Rex Administration");
        }
        return sInstance;
    }

    private AdminFrame(String title)
    {
        super(title);
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
        menu.addSeparator();
        menu.add(new QuitAction());

        menu = new JMenu("Database");
        menuBar.add(menu);
        menu.add(new InitDatabaseAction());
        menu.add(new CreateSchemaAction());

        setJMenuBar(menuBar);

        mTabbedPane = new JTabbedPane();
        mConfigurationPanel = new ConfigurationPanel();
        mTabbedPane.addTab("Configuration", mConfigurationPanel);
        mUsersPanel = new UsersPanel();
        mTabbedPane.addTab("Users", mUsersPanel);
        mGroupsPanel = new GroupsPanel();
        mTabbedPane.addTab("Groups", mGroupsPanel);
        mTabbedPane.addChangeListener(new OnTabChanged());
        content.add(mTabbedPane, BorderLayout.CENTER);

        mUserMenu = new JMenu("User");
        menuBar.add(mUserMenu);
        mUserMenu.add(mUsersPanel.getAddUserAction());
        mUserMenu.add(mUsersPanel.getRemoveUserAction());
        mUserMenu.add(mUsersPanel.getChangePasswordAction());
        mUserMenu.add(mUsersPanel.getEditUserAction());
        mUserMenu.setEnabled(false);

        mGroupMenu = new JMenu("Group");
        menuBar.add(mGroupMenu);
        mGroupMenu.add(mGroupsPanel.getAddGroupAction());
        mGroupMenu.setEnabled(false);

        mStatusBar = new JLabel("Status bar");
        mStatusBar.setBorder(
            BorderFactory.createEtchedBorder());
        content.add(mStatusBar, BorderLayout.SOUTH);

        setSize(640, 480);
        SwingUtils.centerOnScreen(this);
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
            retsConfig.setPort(mConfigurationPanel.getPort());
            retsConfig.setMetadataDir(mConfigurationPanel.getMetadataDir());
            retsConfig.setGetObjectRoot(mConfigurationPanel.getImageRootDir());
            retsConfig.setGetObjectPattern(
                mConfigurationPanel.getImagePattern());
            retsConfig.toXml(Admin.getConfigFile());
            mConfigurationPanel.setChanged(false);
        }
        catch (RetsServerException e)
        {
            LOG.error("Caught", e);
        }
    }

    private void quit()
    {
        if (mConfigurationPanel.isChanged())
        {
            int result = JOptionPane.showConfirmDialog(
                this, "Save changes?", "Confirm Save",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.CANCEL_OPTION)
            {
                return;
            }
            if (result == JOptionPane.YES_OPTION)
            {
                saveConfig();
            }
        }


        dispose();
        System.exit(0);
    }

    public void setStatusText(String text)
    {
        mStatusBar.setText(text);
    }

    public void refreshUsers()
    {
        mUsersPanel.populateList();
    }

    public void refreshGroups()
    {
        mGroupsPanel.populateList();
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
            mUserMenu.setEnabled(false);
            mGroupMenu.setEnabled(false);

            int tab = mTabbedPane.getSelectedIndex();
            if (tab == USERS_TAB)
            {
                mUsersPanel.populateList();
                mUserMenu.setEnabled(true);
            }
            else if (tab == GROUPS_TAB)
            {
                mGroupsPanel.populateList();
                mGroupMenu.setEnabled(true);
            }
        }
    }

    private static final int USERS_TAB = 1;
    private static final int GROUPS_TAB = 2;

    private static final Logger LOG =
        Logger.getLogger(AdminFrame.class);

    private static AdminFrame sInstance;
    private JTabbedPane mTabbedPane;
    private JLabel mStatusBar;
    private UsersPanel mUsersPanel;
    private JMenu mUserMenu;
    private int mMenuShortcutKeyMask;
    private ConfigurationPanel mConfigurationPanel;
    private GroupsPanel mGroupsPanel;
    private JMenu mGroupMenu;
}
