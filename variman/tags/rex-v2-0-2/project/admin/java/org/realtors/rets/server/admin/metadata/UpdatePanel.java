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

import org.realtors.rets.server.admin.AdminUtils;
import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.Update;

public class UpdatePanel extends AbstractSubPanel
{
    public UpdatePanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mUpdateName = new wxTextCtrl(this, -1);
        grid.addRow("Update Name:", mUpdateName, wxEXPAND);

        mDescription = new wxTextCtrl(this, -1);
        grid.addRow("Description:", mDescription, wxEXPAND);

        mKeyField = new wxTextCtrl(this, -1);
        grid.addRow("Key Field:", mKeyField, wxEXPAND);
    }

    public void setModel(Update update)
    {
        AdminUtils.setValue(mUpdateName, update.getUpdateName());
        AdminUtils.setValue(mDescription, update.getDescription());
        AdminUtils.setValue(mKeyField, update.getKeyField());
    }

    private wxTextCtrl mUpdateName;
    private wxTextCtrl mDescription;
    private wxTextCtrl mKeyField;
}
