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
import org.realtors.rets.server.metadata.ValidationLookupType;

public class ValidationLookupTypePanel extends AbstractSubPanel
{
    public ValidationLookupTypePanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mValidText = new wxTextCtrl(this, -1);
        grid.addRow("Valid Text:", mValidText, wxEXPAND);

        mParent1Value = new wxTextCtrl(this, -1);
        grid.addRow("Parent 1 Value:", mParent1Value, wxEXPAND);

        mParent2Value = new wxTextCtrl(this, -1);
        grid.addRow("Parent 2 Value:", mParent2Value, wxEXPAND);
    }

    public void setModel(ValidationLookupType validationLookupType)
    {
        AdminUtils.setValue(mValidText, validationLookupType.getValidText());
        AdminUtils.setValue(mParent1Value,
                            validationLookupType.getParent1Value());
        AdminUtils.setValue(mParent2Value,
                            validationLookupType.getParent2Value());
    }

    private wxTextCtrl mValidText;
    private wxTextCtrl mParent1Value;
    private wxTextCtrl mParent2Value;
}
