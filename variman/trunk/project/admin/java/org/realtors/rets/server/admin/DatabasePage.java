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

public class DatabasePage extends wxPanel
{
    public DatabasePage(wxWindow parent, RetsConfig config)
    {
        super(parent);
        mConfig = config.getDatabase();
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxFlexGridSizer grid = new wxFlexGridSizer(0, 3, 0, 0);
        grid.AddGrowableCol(2);
        wxStaticText label;

        label = new wxStaticText(this,  -1, "Driver class:");
        grid.Add(label, 0, wxALIGN_LEFT | wxRIGHT | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, 0);
        mDriverClass = new wxStaticText(this, -1, "");
        grid.Add(mDriverClass, 0, wxEXPAND | wxALIGN_LEFT | wxBOTTOM, 5);

        label = new wxStaticText(this,  -1, "JDBC URL:");
        grid.Add(label, 0, wxALIGN_LEFT | wxRIGHT | wxBOTTOM, 5);
        grid.Add(SPACER_WIDTH, -1, 0);
        mUrl = new wxStaticText(this, -1, "");
        grid.Add(mUrl, 0, wxEXPAND | wxALIGN_LEFT | wxBOTTOM,  5);

        label = new wxStaticText(this,  -1, "Username:");
        grid.Add(label, 0, wxALIGN_LEFT | wxBOTTOM | wxRIGHT, 5);
        grid.Add(SPACER_WIDTH, -1, 0);
        mUsername = new wxStaticText(this, -1, "");
        grid.Add(mUsername, 0, wxEXPAND | wxALIGN_LEFT | wxBOTTOM, 5);

        grid.Add(-1, -1, 0);
        grid.Add(SPACER_WIDTH, -1, 0);
        grid.Add(new wxButton(this, EDIT_PROPERTIES, "Edit Properties..."), 0);

        updateLabels();

        box.Add(grid, 1, wxALL | wxEXPAND, 10);

        SetSizer(box);

        EVT_BUTTON(EDIT_PROPERTIES, new OnEditProperties());
    }

    private void updateLabels()
    {
        mDriverClass.SetLabel(mConfig.getDriver());
        mUrl.SetLabel(mConfig.getUrl());
        mUsername.SetLabel(mConfig.getUsername());
    }

    class OnEditProperties implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            DatabasePropertiesDialog dialog =
                new DatabasePropertiesDialog(null, mConfig);
            try
            {
                int response = dialog.ShowModal();
                if (response != wxID_OK)
                {
                    return;
                }
                dialog.updateConfig(mConfig);
                updateLabels();
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
    private DatabaseConfig mConfig;
}
