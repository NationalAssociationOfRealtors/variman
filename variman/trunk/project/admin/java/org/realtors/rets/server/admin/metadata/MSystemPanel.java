/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.MSystem;
import org.wxwindows.wxBoxSizer;
import org.wxwindows.wxPanel;
import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

public class MSystemPanel extends wxPanel
{
    public MSystemPanel(wxWindow parent)
    {
        super(parent);
        wxBoxSizer box = new wxBoxSizer(wxVERTICAL);

        TwoColumnGridSizer grid = new TwoColumnGridSizer(this);
        mSystemId = new wxTextCtrl(this, -1, "");
        grid.addRow("System ID:", mSystemId, wxEXPAND);
        mDescription = new wxTextCtrl(this, -1, "");
        grid.addRow("Description:", mDescription, wxEXPAND);
        mComments = new wxTextCtrl(this, -1, "");
        grid.addRow("Comments:", mComments, wxEXPAND);

        box.Add(grid, 1, wxEXPAND | wxALL, 5);
        SetSizer(box);
    }

    public void populateFrom(MSystem system)
    {
        mSystemId.SetValue(system.getSystemID());
        mDescription.SetValue(system.getDescription());
        mComments.SetValue(system.getComments());
    }

    private wxTextCtrl mSystemId;
    private wxTextCtrl mDescription;
    private wxTextCtrl mComments;
}
