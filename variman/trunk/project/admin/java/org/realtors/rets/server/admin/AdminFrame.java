/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.RetsConfig;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.wxwindows.wxCloseEvent;
import org.wxwindows.wxCloseListener;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxFrame;
import org.wxwindows.wxMenu;
import org.wxwindows.wxMenuBar;
import org.wxwindows.wxNotebook;
import org.wxwindows.wxPanel;
import org.wxwindows.wxPoint;
import org.wxwindows.wxSize;
import org.wxwindows.wxStaticText;

public class AdminFrame extends wxFrame
{
    public AdminFrame(String title, wxPoint pos, wxSize size)
    {
        super(null, -1, title, pos, size, wxDEFAULT_FRAME_STYLE);
        Centre(wxBOTH);

        wxMenu fileMenu = new wxMenu();
        fileMenu.Append(SAVE, "&Save\tCtrl-S", "Save configuration file");
        fileMenu.AppendSeparator();
        fileMenu.Append(QUIT, "E&xit\tCtrl-Q", "Quit this program");

        wxMenu usersMenu = new wxMenu();
        usersMenu.Append(ADD_USER, "&Add User...", "Add a new user");
        usersMenu.Append(REMOVE_USER, "&Remove User...", "Add a new user");
        usersMenu.Append(LIST_USERS, "&List Users...", "List all users");

        wxMenu databaseMenu = new wxMenu();
        databaseMenu.Append(INIT_DATABASE, "Re-&initialize Database...\tCtrl-I",
                         "Re-initialize database configuration");
        databaseMenu.Append(CREATE_SCHEMA, "&Create Schema...",
                         "Create metadata schema");

        wxMenuBar menuBar = new wxMenuBar();
        menuBar.Append(fileMenu, "&File");
        menuBar.Append(usersMenu, "&Users");
        menuBar.Append(databaseMenu, "&Database");
        SetMenuBar(menuBar);

        initConfig();

        wxNotebook notebook = new wxNotebook(this, -1);

        mDatabasePage = new DatabasePage(notebook);
        notebook.AddPage(mDatabasePage, "Configuration");

        wxPanel userPage = new wxPanel(notebook);
        new wxStaticText(userPage, -1, "User Page");
        notebook.AddPage(userPage, "Users");

        CreateStatusBar();

        EVT_MENU(SAVE, new OnSave());
        EVT_MENU(QUIT, new OnQuit());
        EVT_MENU(INIT_DATABASE, new OnInitDatabase());
        EVT_MENU(CREATE_SCHEMA, new OnCreateSchema());

        EVT_MENU(ADD_USER, new OnAddUser());
        EVT_MENU(REMOVE_USER, new OnRemoveUser());
        EVT_CLOSE(new OnClose());
    }

    private void initConfig()
    {
        try
        {
            Admin.setConfigFile(
                Admin.getRexHome() + "/webapp/WEB-INF/classes/rets-config.xml");
            Admin.setRetsConfig(
                RetsConfig.initFromXmlFile(Admin.getConfigFile()));
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
            retsConfig.setPort(mDatabasePage.getPort());
            retsConfig.toXml(Admin.getConfigFile());
        }
        catch (RetsServerException e)
        {
            LOG.error("Caught exception", e);
        }
    }

    class OnQuit implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            Close(true);
        }
    }

    private class OnClose implements wxCloseListener
    {
        public void handleEvent(wxCloseEvent event)
        {
            saveConfig();
            Destroy();
        }
    }

    private class OnSave implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            saveConfig();
            SetStatusText("Saved configuration: " + Admin.getConfigFile());
        }
    }

    class OnInitDatabase implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            new InitDatabaseCommand().execute();
        }
    }

    class OnCreateSchema implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            new CreateSchemaCommand().execute();
        }
    }

    class OnAddUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            new AddUserCommand().execute();
        }
    }

    class OnRemoveUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            new RemoveUserCommand().execute();
        }
    }

    private static final Logger LOG =
        Logger.getLogger(AdminFrame.class);
    private static final int SAVE = wxNewId();
    private static final int QUIT = wxNewId();
    private static final int CREATE_SCHEMA = wxNewId();
    private static final int INIT_DATABASE = wxNewId();

    private static final int ADD_USER = wxNewId();
    private static final int REMOVE_USER = wxNewId();
    private static final int LIST_USERS = wxNewId();

    private DatabasePage mDatabasePage;
}
