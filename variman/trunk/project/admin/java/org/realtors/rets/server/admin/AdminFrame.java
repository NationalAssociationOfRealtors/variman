/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.cfg.Configuration;
import net.sf.hibernate.cfg.Environment;
import net.sf.hibernate.tool.hbm2ddl.SchemaExport;

import org.realtors.rets.server.PasswordMethod;
import org.realtors.rets.server.SessionHelper;
import org.realtors.rets.server.User;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxFrame;
import org.wxwindows.wxJWorker;
import org.wxwindows.wxMenu;
import org.wxwindows.wxMenuBar;
import org.wxwindows.wxPoint;
import org.wxwindows.wxSize;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxWindow;
import org.wxwindows.wxWindowDisabler;
import org.wxwindows.wxDialog;

public class AdminFrame extends wxFrame
{
    public AdminFrame(String title, wxPoint pos, wxSize size)
    {
        super(null, -1, title, pos, size, wxDEFAULT_FRAME_STYLE);
        Centre(wxBOTH);

        wxMenu fileMenu = new wxMenu();
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
        databaseMenu.Append(TEST_DATABASE, "&Test Connection...",
                         "Tests database connection");

        wxMenuBar menuBar = new wxMenuBar();
        menuBar.Append(fileMenu, "&File");
        menuBar.Append(usersMenu, "&Users");
        menuBar.Append(databaseMenu, "&Database");
        SetMenuBar(menuBar);

        CreateStatusBar();

        EVT_MENU(QUIT, new OnQuit());
        EVT_MENU(INIT_DATABASE, new OnInitDatabase());
        EVT_MENU(CREATE_SCHEMA, new OnCreateSchema());
        EVT_MENU(TEST_DATABASE, new OnTestDatabase());

        EVT_MENU(ADD_USER, new OnAddUser());
        EVT_MENU(REMOVE_USER, new OnRemoveUser());
    }

    public void initDatabase()
    {
        final wxWindowDisabler disabler = new wxWindowDisabler();
        SetStatusText("Initializing database...");
        wxJWorker worker = new wxJWorker()
        {
            public Object construct()
            {
                String message = "";
                try
                {
                    LOG.info("Initializing Hibernate configuration");
                    mCfg = new Configuration()
                        .addJar("rex-hbm-xml.jar");
                    mSessionFactory = mCfg.buildSessionFactory();
                    PasswordMethod.setDefaultMethod(PasswordMethod.DIGEST_A1,
                                                    PasswordMethod.RETS_REALM);
                    LOG.info("Hibernate initialized");
                }
                catch (Throwable e)
                {
                    LOG.error("Caught", e);
                    message = e.getMessage();
                }
                return message;
            }

            public void finished()
            {
                disabler.delete();
                SetStatusText("Database initialized successfully");
            }
        };
        worker.start();
    }

    private void testDatabase()
    {
        Connection connection = null;
        boolean connectionSucceeded = false;
        String errorMessage = "Database connection failed.";
        try
        {
            String driver = mCfg.getProperty(Environment.DRIVER);
            String url = mCfg.getProperty(Environment.URL);
            String user = mCfg.getProperty(Environment.USER);
            String password = mCfg.getProperty(Environment.PASS);

            LOG.debug("driver: " + driver);
            LOG.debug("url: " + url);
            LOG.debug("user: " + user);
            LOG.debug("password = " + password);
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            connectionSucceeded = !connection.isClosed();
        }
        catch (Exception e)
        {
            LOG.error("Caught exception", e);
            errorMessage = errorMessage + "\n\n" + e.getMessage();
        }
        finally
        {
            closeConnection(connection);
        }

        if (connectionSucceeded)
        {
            wxMessageBox("Database connection succeeded.", "Test Database",
                         wxOK | wxICON_INFORMATION, AdminFrame.this);
        }
        else
        {
            wxMessageBox(errorMessage, "Test Database",
                         wxOK | wxICON_EXCLAMATION, AdminFrame.this);
        }
    }

    private void addUser()
    {
        SessionHelper helper = new SessionHelper(mSessionFactory);
        try
        {
            AddUserDialog addUserDialog = new AddUserDialog(this);
            if (addUserDialog.ShowModal() == wxID_CANCEL)
            {
                return;
            }
            addUserDialog.Destroy();

            User user = new User();
            user.setFirstName(addUserDialog.getFirstName());
            user.setLastName(addUserDialog.getLastName());
            user.setUsername(addUserDialog.getUsername());
            user.changePassword(addUserDialog.getPassword());
            Session session = helper.beginTransaction();
            session.save(user);
            helper.commit();
            SetStatusText("User " + user.getName() + " added");
            LOG.debug("New user: " + user);
        }
        catch (HibernateException e)
        {
            helper.rollback(LOG);
            LOG.error("Caught exception", e);
            StringWriter stackTraceWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stackTraceWriter));
            String stackTrace = stackTraceWriter.toString();
            stackTrace = StringUtils.replace(stackTrace, "\t", "    ");
            wxLogMessage(stackTrace);
            wxLogError("Could not add user.");
        }
        finally
        {
            helper.close(LOG);
        }
    }

    private void closeConnection(Connection connection)
    {
        try
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        catch (SQLException e)
        {
            LOG.error("Caught exception", e);
        }
    }

    class InitializeHibernateDialog extends wxDialog
    {
        public InitializeHibernateDialog(wxWindow parent, int id, String title)
        {
            super(parent, id, title, wxDefaultPosition, wxDefaultSize,
                  wxFRAME_FLOAT_ON_PARENT);
            wxBoxSizer sizer = new wxBoxSizer(wxVERTICAL);
            wxStaticText label =
                new wxStaticText(this, -1, "Initializing hibernate...");
            sizer.Add(label, 0, wxALIGN_LEFT | wxALL, 35);
            SetSizer(sizer);
            sizer.Fit(this);
            CenterOnParent(wxBOTH);
        }
    }

    class OnQuit implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            Close(true);
        }
    }

    class OnInitDatabase implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            initDatabase();
        }
    }

    class OnCreateSchema implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            int response = wxMessageBox(
                "This will delete all your metadata and user " +
                "information.\n\n" +
                "Are you sure you would like to continue?",
                "Create Schema",
                wxYES_NO | wxNO_DEFAULT |
                wxICON_EXCLAMATION,
                AdminFrame.this);
            if (response == wxYES)
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
            final wxWindowDisabler disabler = new wxWindowDisabler();
            SetStatusText("Creating schema...");
            wxJWorker worker = new wxJWorker()
            {
                public Object construct()
                {
                    Boolean success = Boolean.FALSE;
                    try
                    {
                        new SchemaExport(mCfg).create(false, true);
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
                    disabler.delete();
                    boolean success = ((Boolean) get()).booleanValue();
                    if (success)
                    {
                        SetStatusText("Schema successfully created");
                    }
                    else
                    {
                        SetStatusText("Schema create error");
                        wxMessageBox("Schema create error", "Create Schema",
                                     wxOK | wxICON_ERROR, AdminFrame.this);
                    }
                }
            };
            worker.start();
        }
    }

    class OnTestDatabase implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            testDatabase();
        }
    }

    class OnAddUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            addUser();
        }
    }

    class OnRemoveUser implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            SessionHelper helper = new SessionHelper(mSessionFactory);
            try
            {
                RemoveUserDialog dialog =
                    new RemoveUserDialog(AdminFrame.this);
                if (dialog.ShowModal() == wxID_CANCEL)
                {
                    return;
                }
                dialog.Destroy();

                String username = dialog.getUsername();
                User user = null;
                Session session = helper.beginTransaction();
                List users = session.find(
                    "SELECT user " +
                    "  FROM User user " +
                    " WHERE user.username = ?",
                    username, Hibernate.STRING);
                if (users.size() == 1)
                {
                    user = (User) users.get(0);
                    session.delete(user);
                    LOG.debug("User deleted: " + user);
                    SetStatusText("User " + user.getName() + " removed");
                }
                else
                {
                    LOG.debug("Expecting 1 user, found: " + users.size());
                    wxMessageBox("User not found: " + username,
                                 "User Not Found", wxOK | wxICON_EXCLAMATION,
                                 AdminFrame.this);
                }
                helper.commit();
            }
            catch (HibernateException e)
            {
                helper.rollback(LOG);
                LOG.error("Caught exception", e);
                StringWriter stackTraceWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTraceWriter));
                String stackTrace = stackTraceWriter.toString();
                stackTrace = StringUtils.replace(stackTrace, "\t", "    ");
                wxLogMessage(stackTrace);
                wxLogError("Could not add user.");
            }
            finally
            {
                helper.close(LOG);
            }
        }
    }

    private static final Logger LOG =
        Logger.getLogger(AdminFrame.class);
    public static final int QUIT = wxNewId();
    public static final int CREATE_SCHEMA = wxNewId();
    public static final int INIT_DATABASE = wxNewId();
    public static final int TEST_DATABASE = wxNewId();

    public static final int ADD_USER = wxNewId();
    public static final int REMOVE_USER = wxNewId();
    public static final int LIST_USERS = wxNewId();

    private Configuration mCfg;
    private SessionFactory mSessionFactory;
}
