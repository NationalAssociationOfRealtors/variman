/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.RetsConfig;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxButton;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxFlexGridSizer;
import org.wxwindows.wxPanel;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxWindow;
import org.wxwindows.wxStaticLine;
import org.wxwindows.wxTextCtrl;

import org.apache.commons.lang.math.NumberUtils;

public class DatabasePage extends wxPanel
{
    public DatabasePage(wxWindow parent)
    {
        super(parent);

        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxStaticText label;
        wxFlexGridSizer grid;
        wxBoxSizer hBox;

        hBox = new wxBoxSizer(wxHORIZONTAL);
        hBox.Add(new wxStaticText(this, -1, "Networking"), 0, wxALL, 5);
        hBox.Add(new wxStaticLine(this, -1), 1,
                 wxALIGN_CENTER_VERTICAL | wxALL, 5);
        box.Add(hBox, 0, wxEXPAND, 0);

        hBox = new wxBoxSizer(wxHORIZONTAL);
        hBox.Add(20, 20);

        grid = new wxFlexGridSizer(0, 3, 0, 0);
        grid.AddGrowableCol(2);
        label = new wxStaticText(this, -1, "Port:");
        grid.Add(label, 0, wxALIGN_LEFT | wxRIGHT | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1);
        mPort = new wxTextCtrl(this, -1, "");
        grid.Add(mPort, 0, wxBOTTOM, 5);
        hBox.Add(grid, 1, wxALL | wxEXPAND, 10);
        box.Add(hBox, 0, wxEXPAND, 0);

        hBox = new wxBoxSizer(wxHORIZONTAL);
        hBox.Add(new wxStaticText(this, -1, "Database"), 0, wxALL, 5);
        hBox.Add(new wxStaticLine(this, -1), 1,
                 wxALIGN_CENTER_VERTICAL | wxALL, 5);
        box.Add(hBox, 0, wxEXPAND, 0);

        hBox = new wxBoxSizer(wxHORIZONTAL);
        hBox.Add(20, 20);
        grid = new wxFlexGridSizer(0, 3, 0, 0);
        grid.AddGrowableCol(2);

        label = new wxStaticText(this,  -1, "Driver class:");
        grid.Add(label, 0, wxALIGN_LEFT | wxRIGHT | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1);
        mDriverClass = new wxStaticText(this, -1, "");
        grid.Add(mDriverClass, 0, wxEXPAND | wxALIGN_LEFT | wxBOTTOM, 5);

        label = new wxStaticText(this,  -1, "JDBC URL:");
        grid.Add(label, 0, wxALIGN_LEFT | wxRIGHT | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, 0);
        mUrl = new wxStaticText(this, -1, "");
        grid.Add(mUrl, 0, wxEXPAND | wxALIGN_LEFT | wxBOTTOM,  5);

        label = new wxStaticText(this,  -1, "Username:");
        grid.Add(label, 0, wxALIGN_LEFT | wxBOTTOM | wxRIGHT, 5);
        grid.Add(SPACER_WIDTH, -1);
        mUsername = new wxStaticText(this, -1, "");
        grid.Add(mUsername, 0, wxEXPAND | wxALIGN_LEFT | wxBOTTOM, 5);

        grid.Add(-1, -1, 0);
        grid.Add(SPACER_WIDTH, -1);
        grid.Add(new wxButton(this, EDIT_PROPERTIES, "Edit Properties..."), 0);

        hBox.Add(grid, 1, wxALL | wxEXPAND, 10);
        box.Add(hBox, 0, wxEXPAND, 0);


        updateLabels();
        SetSizer(box);

        EVT_BUTTON(EDIT_PROPERTIES, new OnEditProperties());
    }

    private void updateLabels()
    {
        RetsConfig config = Admin.getRetsConfig();
        DatabaseConfig dbConfig = config.getDatabase();
        mDriverClass.SetLabel(dbConfig.getDriver());
        mUrl.SetLabel(dbConfig.getUrl());
        mUsername.SetLabel(dbConfig.getUsername());
        mPort.SetValue("" + config.getPort());
    }

    public int getPort()
    {
        return NumberUtils.stringToInt(mPort.GetValue());
    }

    class OnEditProperties implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            DatabaseConfig dbConfig = Admin.getRetsConfig().getDatabase();
            DatabasePropertiesDialog dialog =
                new DatabasePropertiesDialog(Admin.getAdminFrame(), dbConfig);
            try
            {
                int response = dialog.ShowModal();
                if (response != wxID_OK)
                {
                    return;
                }
                dialog.updateConfig(dbConfig);
                updateLabels();
                // Re-initialize Hibernate with new parameters
                new InitDatabaseCommand().execute();
            }
            finally
            {
                dialog.Destroy();
            }
        }
    }

    private static final int EDIT_PROPERTIES = wxNewId();
    private static final int SPACER_WIDTH = 10;

    private wxStaticText mDriverClass;
    private wxStaticText mUrl;
    private wxStaticText mUsername;
    private wxTextCtrl mPort;
}
