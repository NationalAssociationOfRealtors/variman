/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.DatabaseType;

import org.apache.log4j.Logger;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxButton;
import org.wxwindows.wxChoice;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxDialog;
import org.wxwindows.wxFlexGridSizer;
import org.wxwindows.wxJUtil;
import org.wxwindows.wxSize;
import org.wxwindows.wxStaticLine;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

public class DatabasePropertiesDialog extends wxDialog
{
    public DatabasePropertiesDialog(wxWindow parent, DatabaseConfig config)
    {
        super(parent, -1, "Database Properties");

        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxFlexGridSizer grid = new wxFlexGridSizer(0, 3, 0, 0);
        grid.AddGrowableCol(2);
        wxSize textSize = new wxSize(300, -1);

        grid.Add(new wxStaticText(this, -1, "Database Type:"), 0,
                 wxALIGN_CENTER_VERTICAL | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        createDatabaseTypes(config);
        grid.Add(mDatabaseTypes, 0, wxEXPAND | wxBOTTOM, 5);

        grid.Add(new wxStaticText(this, -1, "Host Name:"), 0,
                 wxALIGN_CENTER_VERTICAL | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        mHostName = new wxTextCtrl(this, -1, config.getHostName(),
                                   wxDefaultPosition, textSize);
        grid.Add(mHostName, 0, wxEXPAND | wxBOTTOM, 5);

        grid.Add(new wxStaticText(this, -1, "Database Name:"), 0,
                 wxALIGN_CENTER_VERTICAL | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        mDatabaseName = new wxTextCtrl(this, -1, config.getDatabaseName(),
                                       wxDefaultPosition, textSize);
        grid.Add(mDatabaseName, 0, wxEXPAND | wxBOTTOM, 5);

        grid.Add(new wxStaticText(this, -1, "Username:"), 0,
                 wxALIGN_CENTER_VERTICAL | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        mUsername = new wxTextCtrl(this, -1, config.getUsername(),
                                   wxDefaultPosition, textSize);
        grid.Add(mUsername, 0, wxEXPAND | wxBOTTOM, 5);

        grid.Add(new wxStaticText(this, -1, "Password:"), 0,
                 wxALIGN_CENTER_VERTICAL | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        mPassword = new wxTextCtrl(this, -1, config.getPassword(),
                                   wxDefaultPosition, textSize,
                                   wxTE_PASSWORD);
        grid.Add(mPassword, 0, wxEXPAND | wxBOTTOM, 5);

        wxBoxSizer buttonBox = new wxBoxSizer(wxHORIZONTAL);
        wxButton button = new wxButton(this, wxID_OK, "Ok");
        buttonBox.Add(button, 0);
        mCancel = new wxButton(this, wxID_CANCEL, "Cancel");
        mCancel.SetDefault();
        buttonBox.Add(mCancel, 0, wxLEFT, 15);
        button = new wxButton(this, TEST_CONNECTION, "Test Connection...");
        buttonBox.Add(button, 0, wxLEFT, 15);

        box.Add(grid, 0, wxEXPAND | wxALL, 15);
        box.Add(new wxStaticLine(this, -1), 0,
                wxEXPAND | wxLEFT | wxBOTTOM | wxRIGHT, 15);
        box.Add(buttonBox, 0, wxALIGN_RIGHT | wxLEFT | wxBOTTOM | wxRIGHT, 15);

        SetSizer(box);
        box.Fit(this);
        CenterOnParent();

        EVT_BUTTON(TEST_CONNECTION, new OnTestConnection());
    }

    private wxChoice createDatabaseTypes(DatabaseConfig config)
    {
        mDatabaseTypes = new wxChoice(this, -1, wxDefaultPosition);
        mDatabaseTypeObjects = new DatabaseType[]{
            DatabaseType.POSTGRESQL,
            DatabaseType.SQLSERVER_JSQL
        };
        for (int i = 0; i < mDatabaseTypeObjects.length; i++)
        {
            DatabaseType databaseType = mDatabaseTypeObjects[i];
            mDatabaseTypes.Append(databaseType.getLongName());
        }
        mDatabaseTypes.SetStringSelection(
            config.getDatabaseType().getLongName());
        return mDatabaseTypes;
    }

    private DatabaseType getDatabaseType()
    {
        return mDatabaseTypeObjects[mDatabaseTypes.GetSelection()];
    }

    public void updateConfig(DatabaseConfig config)
    {
        config.setDatabaseType(getDatabaseType());
        config.setHostName(mHostName.GetValue());
        config.setDatabaseName(mDatabaseName.GetValue());
        config.setUsername(mUsername.GetValue());
        config.setPassword(mPassword.GetValue());
    }

    private class OnTestConnection implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            testConnection();
            mCancel.SetDefault();
        }
    }

    private void testConnection()
    {
        Connection connection = null;
        try
        {
            DatabaseType type = getDatabaseType();
            String driver = type.getDriverClass();
            String hostName = mHostName.GetValue();
            String databaseName = mDatabaseName.GetValue();
            String url = type.getUrl(hostName, databaseName);
            String username = mUsername.GetValue();
            String password = mPassword.GetValue();

            LOG.debug("driver: " + driver);
            LOG.debug("url: " + url);
            LOG.debug("user: " + username);
            LOG.debug("password = " + password);
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            if (!connection.isClosed())
            {
                wxMessageBox("Database connection succeeded.", "Test Database",
                             wxOK | wxICON_INFORMATION, this);
            }
            else
            {
                // Most likely, an exception will get thrown before we ever get
                // here, but just in case...
                wxMessageBox("Database connection failed.", "Test Database",
                             wxOK | wxICON_EXCLAMATION, this);
            }
        }
        catch (Exception e)
        {
            LOG.error("Caught exception", e);
            wxJUtil.logError("Database connection failed.\n" +
                             e.getMessage(), e);
        }
        finally
        {
            closeConnection(connection);
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

    private static final Logger LOG =
        Logger.getLogger(DatabasePropertiesDialog.class);
    private static final int SPACER_WIDTH = 10;
    private static final int TEST_CONNECTION = wxNewId();

    private wxChoice mDatabaseTypes;
    private wxTextCtrl mHostName;
    private wxTextCtrl mDatabaseName;
    private wxTextCtrl mUsername;
    private wxTextCtrl mPassword;
    private wxButton mCancel;
    private DatabaseType[] mDatabaseTypeObjects;
}
