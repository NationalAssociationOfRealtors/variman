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
import org.realtors.rets.server.metadata.EditMask;

public class EditMaskPanel extends AbstractSubPanel
{
    public EditMaskPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mEditMaskId = new wxTextCtrl(this, -1);
        grid.addRow("Edit Mask ID:", mEditMaskId, wxEXPAND);

        mValue = new wxTextCtrl(this, -1);
        grid.addRow("Value:", mValue, wxEXPAND);
    }

    public void setModel(EditMask editMask)
    {
        AdminUtils.setValue(mEditMaskId, editMask.getEditMaskID());
        AdminUtils.setValue(mValue, editMask.getValue());
    }

    private wxTextCtrl mEditMaskId;
    private wxTextCtrl mValue;
}
