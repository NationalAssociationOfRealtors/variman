/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin;

import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxButton;
import org.wxwindows.wxDialog;
import org.wxwindows.wxFlexGridSizer;
import org.wxwindows.wxSize;
import org.wxwindows.wxStaticLine;
import org.wxwindows.wxStaticText;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

public class RemoveUserDialog extends wxDialog
{
    public RemoveUserDialog(wxWindow parent)
    {
        super(parent, -1, "Remove User");

        wxSize textSize = new wxSize(175, -1);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxFlexGridSizer grid = new wxFlexGridSizer(0, 2, 0, 0);
        wxStaticText label;

        label = new wxStaticText(this, -1, "Username:");
        grid.Add(label, 0, wxALIGN_RIGHT | wxRIGHT, 5);
        mUsername = new wxTextCtrl(this, -1, "", wxDefaultPosition,
                                   textSize);
        grid.Add(mUsername, 0, wxALIGN_LEFT, 0);


        wxBoxSizer buttonBox = new wxBoxSizer(wxHORIZONTAL);
        wxButton add = new wxButton(this, wxID_OK, "Remove User");
        add.SetDefault();
        buttonBox.Add(add, 0);
        wxButton cancel = new wxButton(this, wxID_CANCEL, "Cancel");
        buttonBox.Add(cancel, 0, wxLEFT, 15);

        box.Add(grid, 0, wxALIGN_CENTER | wxALL, 15);
        box.Add(new wxStaticLine(this, -1), 0,
                wxALIGN_CENTER | wxEXPAND | wxLEFT | wxBOTTOM | wxRIGHT, 15);
        box.Add(buttonBox, 0, wxALIGN_RIGHT | wxLEFT | wxBOTTOM | wxRIGHT, 15);
        SetSizer(box);
        box.Fit(this);
        CenterOnParent();

        CenterOnParent();
    }

    public String getUsername()
    {
        return mUsername.GetValue();
    }

    private wxTextCtrl mUsername;
}
