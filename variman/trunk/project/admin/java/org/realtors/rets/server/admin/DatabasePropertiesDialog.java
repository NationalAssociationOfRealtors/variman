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

import org.wxwindows.wxWindow;
import org.wxwindows.wxDialog;
import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxFlexGridSizer;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxButton;
import org.wxwindows.wxStaticLine;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxSize;

import org.realtors.rets.server.config.DatabaseConfig;

import org.apache.log4j.Logger;

public class DatabasePropertiesDialog extends wxDialog
{
    public DatabasePropertiesDialog(wxWindow parent, DatabaseConfig config)
    {
        super(parent, -1, "Database Properties");

        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxFlexGridSizer grid = new wxFlexGridSizer(0, 3, 0, 0);
        grid.AddGrowableCol(2);
        wxSize textSize = new wxSize(300, -1);

        grid.Add(new wxStaticText(this, -1, "Driver Class:"), 0, wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        mDriverClass = new wxTextCtrl(this, -1, config.getDriver(),
                                      wxDefaultPosition, textSize);
        grid.Add(mDriverClass, 0, wxEXPAND | wxBOTTOM,  5);

        grid.Add(new wxStaticText(this, -1, "JDBC URL:"), 0, wxBOTTOM,  5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        mUrl = new wxTextCtrl(this, -1, config.getUrl(), wxDefaultPosition,
                              textSize);
        grid.Add(mUrl, 0, wxEXPAND | wxBOTTOM,  5);

        grid.Add(new wxStaticText(this, -1, "Username:"), 0, wxBOTTOM,  5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        mUsername = new wxTextCtrl(this, -1, config.getUsername(),
                                   wxDefaultPosition, textSize);
        grid.Add(mUsername, 0, wxEXPAND | wxBOTTOM,  5);

        grid.Add(new wxStaticText(this, -1, "Password:"), 0, wxBOTTOM,  5);
        grid.Add(SPACER_WIDTH, -1, wxBOTTOM, 5);
        mPassword = new wxTextCtrl(this, -1, config.getPassword(),
                                   wxDefaultPosition, textSize,
                                   wxTE_PASSWORD);
        grid.Add(mPassword, 0, wxEXPAND | wxBOTTOM,  5);

        wxBoxSizer buttonBox = new wxBoxSizer(wxHORIZONTAL);
        wxButton button = new wxButton(this, wxID_OK, "Ok");
        buttonBox.Add(button, 0);
        mCancel = new wxButton(this, wxID_CANCEL, "Cancel");
        mCancel.SetDefault();
        buttonBox.Add(mCancel, 0, wxLEFT, 15);
        button = new wxButton(this, TEST_CONNECTION, "Test Connection...");
        buttonBox.Add(button,  0, wxLEFT, 15);

        box.Add(grid, 0, wxEXPAND | wxALL, 15);
        box.Add(new wxStaticLine(this, -1), 0,
                wxEXPAND | wxLEFT | wxBOTTOM | wxRIGHT, 15);
        box.Add(buttonBox, 0, wxALIGN_RIGHT | wxLEFT | wxBOTTOM | wxRIGHT, 15);

        SetSizer(box);
        box.Fit(this);
        CenterOnParent();

        EVT_BUTTON(TEST_CONNECTION, new OnTestConnection());
    }

    public void updateConfig(DatabaseConfig config)
    {
        config.setDriver(mDriverClass.GetValue());
        config.setUrl(mUrl.GetValue());
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
        boolean connectionSucceeded = false;
        String errorMessage = "Database connection failed.";
        try
        {
            String driver = mDriverClass.GetValue();
            String url = mUrl.GetValue();
            String username = mUsername.GetValue();
            String password = mPassword.GetValue();

            LOG.debug("driver: " + driver);
            LOG.debug("url: " + url);
            LOG.debug("user: " + username);
            LOG.debug("password = " + password);
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
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
                         wxOK | wxICON_INFORMATION, this);
        }
        else
        {
            wxMessageBox(errorMessage, "Test Database",
                         wxOK | wxICON_EXCLAMATION, this);
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

    private wxTextCtrl mDriverClass;
    private wxTextCtrl mUrl;
    private wxTextCtrl mUsername;
    private wxTextCtrl mPassword;
    private wxButton mCancel;
}
