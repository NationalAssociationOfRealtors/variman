/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxTextCtrl;
import org.wxwindows.wxWindow;

import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.MSystem;

public class MSystemPanel extends AbstractSubPanel
{
    public MSystemPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mSystemId = new wxTextCtrl(this, -1, "");
        grid.addRow("System ID:", mSystemId, wxEXPAND);

        mDescription = new wxTextCtrl(this, -1, "");
        grid.addRow("Description:", mDescription, wxEXPAND);

        mComments = new wxTextCtrl(this, -1, "");
        grid.addRow("Comments:", mComments, wxEXPAND);
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
