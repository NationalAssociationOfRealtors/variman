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
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.UpdateType;

public class UpdateTypePanel extends AbstractSubPanel
{
    public UpdateTypePanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mSystemName = new wxTextCtrl(this, -1);
        grid.addRow("System Name:", mSystemName, wxEXPAND);

        mSequence = new wxTextCtrl(this, -1);
        grid.addRow("Sequence:", mSequence, wxEXPAND);

        mDefault = new wxTextCtrl(this, -1);
        grid.addRow("Default:", mDefault, wxEXPAND);

        mUpdateHelp = new wxTextCtrl(this, -1);
        grid.addRow("Update Help:", mUpdateHelp, wxEXPAND);

        mValidationLookup = new wxTextCtrl(this, -1);
        grid.addRow("Validtion Lookup:", mValidationLookup, wxEXPAND);

        mValidationExternal = new wxTextCtrl(this, -1);
        grid.addRow("Validation External:", mValidationExternal, wxEXPAND);

        // Todo: UpdateTypePanel.UpdateTypePanel Attributes

        // Todo: UpdateTypePanel.UpdateTypePanel Validation Expressions
    }

    public void setModel(UpdateType updateType)
    {
        setValue(mSystemName, updateType.getTable());
        AdminUtils.setValue(mSequence, updateType.getSequence());
        AdminUtils.setValue(mDefault, updateType.getDefault());
        AdminUtils.setValue(mUpdateHelp, updateType.getUpdateHelp());
        AdminUtils.setValue(mValidationLookup,
                            updateType.getValidationLookup());
        AdminUtils.setValue(mValidationExternal,
                            updateType.getValidationExternal());
    }

    private void setValue(wxTextCtrl textCtrl, Table table)
    {
        AdminUtils.setValue(textCtrl, table.getSystemName());
    }

    private wxTextCtrl mSystemName;
    private wxTextCtrl mSequence;
    private wxTextCtrl mDefault;
    private wxTextCtrl mUpdateHelp;
    private wxTextCtrl mValidationLookup;
    private wxTextCtrl mValidationExternal;
}
