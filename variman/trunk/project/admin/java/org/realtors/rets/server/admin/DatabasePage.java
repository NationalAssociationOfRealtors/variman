/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.apache.commons.lang.math.NumberUtils;
import org.realtors.rets.server.IOUtils;
import org.realtors.rets.server.config.DatabaseConfig;
import org.realtors.rets.server.config.RetsConfig;
import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxButton;
import org.wxwindows.wxCommandEvent;
import org.wxwindows.wxCommandListener;
import org.wxwindows.wxDirDialog;
import org.wxwindows.wxPanel;
import org.wxwindows.wxStaticLine;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

public class DatabasePage extends wxPanel
{
    public DatabasePage(wxWindow parent)
    {
        super(parent);

        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxBoxSizer hBox;
        wxBoxSizer hBox2;
        wxBoxSizer vBox;

        hBox = new wxBoxSizer(wxHORIZONTAL);
        hBox.Add(new wxStaticText(this, -1, "RETS"), 0, wxALL, 5);
        hBox.Add(new wxStaticLine(this, -1), 1,
                 wxALIGN_CENTER_VERTICAL | wxALL, 5);
        box.Add(hBox, 0, wxEXPAND, 0);

        hBox = new wxBoxSizer(wxHORIZONTAL);
        hBox.Add(20, 20);

        TwoColumnGridSizer grid = new TwoColumnGridSizer(this);
        mPort = new wxTextCtrl(this, -1);
        grid.addRow("Listening Port:", mPort);

        hBox2 = new wxBoxSizer(wxHORIZONTAL);
        mMetadataDir = new wxTextCtrl(this, -1, "", wxDefaultPosition,
                                      wxDefaultSize, wxTE_READONLY);
        hBox2.Add(mMetadataDir, 1, wxEXPAND);
        hBox2.Add(new wxButton(this, CHOOSE_METADATA_DIR, "Choose..."), 0,
                  wxLEFT, 10);
        grid.addRow("Metadata Directory:", hBox2, wxEXPAND);
        hBox.Add(grid, 1, wxALL | wxEXPAND, 10);

        box.Add(hBox, 0, wxEXPAND, 0);

        hBox = new wxBoxSizer(wxHORIZONTAL);
        hBox.Add(new wxStaticText(this, -1, "Database"), 0, wxALL, 5);
        hBox.Add(new wxStaticLine(this, -1), 1,
                 wxALIGN_CENTER_VERTICAL | wxALL, 5);
        box.Add(hBox, 0, wxEXPAND, 0);

        hBox = new wxBoxSizer(wxHORIZONTAL);
        hBox.Add(20, 20);
        vBox = new wxBoxSizer(wxVERTICAL);

        grid = new TwoColumnGridSizer(this);

        mDatabaseType = new wxStaticText(this, -1, "");
        grid.addRow("Type:", mDatabaseType, wxEXPAND);

        mHostName = new wxStaticText(this, -1, "");
        grid.addRow("Host Name:", mHostName, wxEXPAND);

        mDatabaseName = new wxStaticText(this, -1, "");
        grid.addRow("Database Name:", mDatabaseName, wxEXPAND);

        mUsername = new wxStaticText(this, -1, "");
        grid.addRow("Username:", mUsername, wxEXPAND);

        vBox.Add(grid, 1, wxEXPAND, 0);
        vBox.Add(new wxButton(this, EDIT_PROPERTIES, "Edit..."), 0);
        hBox.Add(vBox, 1, wxALL | wxEXPAND, 10);
        box.Add(hBox, 0, wxEXPAND, 0);


        updateLabels();
        SetSizer(box);

        EVT_BUTTON(EDIT_PROPERTIES, new OnEditProperties());
        EVT_BUTTON(CHOOSE_METADATA_DIR, new OnChooseMetadata());
    }

    private void updateLabels()
    {
        RetsConfig config = Admin.getRetsConfig();
        DatabaseConfig dbConfig = config.getDatabase();
        mDatabaseType.SetLabel(dbConfig.getDatabaseType().getLongName());
        mHostName.SetLabel(dbConfig.getHostName());
        mDatabaseName.SetLabel(dbConfig.getDatabaseName());
        mUsername.SetLabel(dbConfig.getUsername());
        mPort.SetValue("" + config.getPort());
        String webappRoot = Admin.getWebAppRoot();
        mMetadataDir.SetValue(IOUtils.resolve(webappRoot,
                                              config.getMetadataDir()));
    }

    public int getPort()
    {
        return NumberUtils.stringToInt(mPort.GetValue());
    }

    private class OnEditProperties implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            DatabasePropertiesDialog dialog = null;
            try
            {
                DatabaseConfig dbConfig = Admin.getRetsConfig().getDatabase();
                dialog = new DatabasePropertiesDialog(Admin.getAdminFrame(),
                                                      dbConfig);
                if (dialog.ShowModal() != wxID_OK)
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
                Destroy(dialog);
            }
        }
    }

    private class OnChooseMetadata implements wxCommandListener
    {
        public void handleEvent(wxCommandEvent event)
        {
            wxDirDialog dirDialog = null;
            try
            {
                RetsConfig config = Admin.getRetsConfig();
                String webappRoot = Admin.getWebAppRoot();
                String metadataDir = config.getMetadataDir();
                metadataDir = IOUtils.resolve(webappRoot, metadataDir);
                dirDialog =
                    new wxDirDialog(Admin.getAdminFrame(),
                                    "Choose Metadata Directory", metadataDir);
                if (dirDialog.ShowModal() != wxID_OK)
                {
                    return;
                }

                metadataDir = dirDialog.GetPath();
                metadataDir = IOUtils.relativize(webappRoot, metadataDir);
                config.setMetadataDir(metadataDir);
                updateLabels();
            }
            finally
            {
                Destroy(dirDialog);
            }
        }
    }

    private static final int EDIT_PROPERTIES = wxNewId();
    private static final int CHOOSE_METADATA_DIR = wxNewId();

    private wxStaticText mUsername;
    private wxTextCtrl mPort;
    private wxTextCtrl mMetadataDir;
    private wxStaticText mDatabaseType;
    private wxStaticText mHostName;
    private wxStaticText mDatabaseName;
}
