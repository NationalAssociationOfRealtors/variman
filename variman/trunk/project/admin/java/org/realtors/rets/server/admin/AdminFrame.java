/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.io.File;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.config.RetsConfig;

import org.apache.log4j.Logger;

import org.wxwindows.wxCloseEvent;
import org.wxwindows.wxCloseListener;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxFrame;
import org.wxwindows.wxMenu;
import org.wxwindows.wxMenuBar;
import org.wxwindows.wxNotebook;
import org.wxwindows.wxNotebookEvent;
import org.wxwindows.wxNotebookListener;
import org.wxwindows.wxPoint;
import org.wxwindows.wxSize;

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

        wxMenu userMenu = new wxMenu();
        userMenu.Append(ADD_USER, "&Add User...", "Add a new user");
        userMenu.Append(REMOVE_USER, "&Remove User...", "Add a new user");

        wxMenu databaseMenu = new wxMenu();
        databaseMenu.Append(INIT_DATABASE, "Re-&initialize Database...\tCtrl-I",
                            "Re-initialize database configuration");
        databaseMenu.Append(CREATE_SCHEMA, "&Create Schema...",
                            "Create metadata schema");


        wxMenuBar menuBar = new wxMenuBar();
        menuBar.Append(fileMenu, "&File");
        menuBar.Append(databaseMenu, "&Database");
        menuBar.Append(userMenu, "&User");
        if (Admin.isDebugEnabled())
        {
            wxMenu debugMenu = new wxMenu();
            debugMenu.Append(CREATE_DATA_SCHEMA, "Create Data Schema...");
            debugMenu.Append(CREATE_PROPERTIES, "Create Properties...");
            menuBar.Append(debugMenu, "Debug");
            EVT_MENU(CREATE_DATA_SCHEMA, new OnCreateDataSchema());
            EVT_MENU(CREATE_PROPERTIES, new OnCreateProperties());
        }
        SetMenuBar(menuBar);

        initConfig();

        mNotebook = new wxNotebook(this, NOTEBOOK);

        mDatabasePage = new DatabasePage(mNotebook);
        mNotebook.AddPage(mDatabasePage, "Configuration");

        mUsersPage = new UsersPage(mNotebook);
        mNotebook.AddPage(mUsersPage, "Users");

        mMetadataPage = new MetadataPage(mNotebook);
        mNotebook.AddPage(mMetadataPage, "Metadata");

        CreateStatusBar();

        EVT_MENU(SAVE, new OnSave());
        EVT_MENU(QUIT, new OnQuit());
        EVT_MENU(INIT_DATABASE, new OnInitDatabase());
        EVT_MENU(CREATE_SCHEMA, new OnCreateSchema());

        EVT_MENU(ADD_USER, new OnAddUser());
        EVT_MENU(REMOVE_USER, new OnRemoveUser());

        EVT_NOTEBOOK_PAGE_CHANGED(NOTEBOOK, new OnPageChanged());
        EVT_CLOSE(new OnClose());
    }

    private void initConfig()
    {
        try
        {
            File configFile = new File(Admin.getRexHome() +
                                       "/webapp/WEB-INF/rex/rets-config.xml");
            Admin.setConfigFile(configFile.getAbsolutePath());
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

    public void refreshUsers()
    {
        if (mNotebook.GetSelection() == USERS_PAGE)
        {
            mUsersPage.populateList();
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

    private class OnPageChanged implements wxNotebookListener
    {
        public void handleEvent(wxNotebookEvent event)
        {
            int selection = event.GetSelection();
            if (selection == USERS_PAGE)
            {
                mUsersPage.populateList();
            }
            else if (selection == METADATA_PAGE)
            {
                mMetadataPage.populateTree();
            }
        }
    }

    private class OnCreateDataSchema implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            new CreateDataSchemaCommand().execute();
        }
    }

    private class OnCreateProperties implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            new CreatePropertiesCommand(100).execute();
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

    public static final int NOTEBOOK = wxNewId();

    private static final int CREATE_DATA_SCHEMA = wxNewId();
    private static final int CREATE_PROPERTIES = wxNewId();

    private static final int USERS_PAGE = 1;
    private static final int METADATA_PAGE = 2;

    private DatabasePage mDatabasePage;
    private UsersPage mUsersPage;
    private wxNotebook mNotebook;
    private MetadataPage mMetadataPage;
}
