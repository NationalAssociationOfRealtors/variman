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

import org.realtors.rets.server.User;

public class EditUserDialog extends wxDialog
{
    public EditUserDialog(wxWindow parent, User user)
    {
        super(parent, -1, "Edit User: " + user.getUsername());
        wxSize textSize = new wxSize(175, -1);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);
        wxFlexGridSizer grid = new wxFlexGridSizer(0, 2, 0, 0);
        wxStaticText label;

        label = new wxStaticText(this, -1, "First Name:");
        grid.Add(label, 0, wxALIGN_RIGHT | wxRIGHT | wxBOTTOM, 5);
        mFirstName = new wxTextCtrl(this, -1, "", wxDefaultPosition,
                                        textSize);
        grid.Add(mFirstName, 0, wxALIGN_LEFT | wxBOTTOM, 5);

        label = new wxStaticText(this, -1, "Last Name:");
        grid.Add(label, 0, wxALIGN_RIGHT | wxRIGHT | wxBOTTOM, 5);
        mLastName = new wxTextCtrl(this, -1, "", wxDefaultPosition,
                                       textSize);
        grid.Add(mLastName, 0, wxALIGN_LEFT, 5);

        wxBoxSizer buttonBox = new wxBoxSizer(wxHORIZONTAL);
        wxButton change = new wxButton(this, wxID_OK, "Save Changes");
        change.SetDefault();
        buttonBox.Add(change, 0);
        wxButton cancel = new wxButton(this, wxID_CANCEL, "Cancel");
        buttonBox.Add(cancel, 0, wxLEFT, 15);

        box.Add(grid, 0, wxALIGN_CENTER | wxALL, 15);
        box.Add(new wxStaticLine(this, -1), 0,
                wxALIGN_CENTER | wxEXPAND | wxLEFT | wxBOTTOM | wxRIGHT, 15);
        box.Add(buttonBox, 0, wxALIGN_RIGHT | wxLEFT | wxBOTTOM | wxRIGHT, 15);
        SetSizer(box);
        box.Fit(this);
        CenterOnParent();

        mFirstName.SetValue(user.getFirstName());
        mLastName.SetValue(user.getLastName());
    }

    public String getFirstName()
    {
        return mFirstName.GetValue();
    }

    public String getLastName()
    {
        return mLastName.GetValue();
    }

    private wxTextCtrl mFirstName;
    private wxTextCtrl mLastName;
}
