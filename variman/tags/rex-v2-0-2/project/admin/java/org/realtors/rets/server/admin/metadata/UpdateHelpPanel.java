/*
 * Rex RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.admin.metadata;

import org.wxwindows.wxWindow;
import org.wxwindows.wxTextCtrl;

import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.admin.AdminUtils;
import org.realtors.rets.server.metadata.UpdateHelp;

public class UpdateHelpPanel extends AbstractSubPanel
{
    public UpdateHelpPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mUpdateHelpId = new wxTextCtrl(this, -1);
        grid.addRow("Update Help ID:", mUpdateHelpId, wxEXPAND);

        mValue = new wxTextCtrl(this, -1);
        grid.addRow("Value:", mValue, wxEXPAND);
    }

    public void setModel(UpdateHelp updateHelp)
    {
        AdminUtils.setValue(mUpdateHelpId, updateHelp.getUpdateHelpID());
        AdminUtils.setValue(mValue, updateHelp.getValue());
    }

    private wxTextCtrl mUpdateHelpId;
    private wxTextCtrl mValue;
}
