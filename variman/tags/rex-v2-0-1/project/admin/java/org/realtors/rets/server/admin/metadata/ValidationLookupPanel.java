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
import org.realtors.rets.server.metadata.ValidationLookup;

public class ValidationLookupPanel extends AbstractSubPanel
{
    public ValidationLookupPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mName = new wxTextCtrl(this, -1);
        grid.addRow("Name:", mName, wxEXPAND);

        mParent1Field = new wxTextCtrl(this, -1);
        grid.addRow("Parent 1 Field:", mParent1Field, wxEXPAND);

        mParent2Field = new wxTextCtrl(this, -1);
        grid.addRow("Parent 1 Field:", mParent2Field, wxEXPAND);
    }

    public void setModel(ValidationLookup validationLookup)
    {
        AdminUtils.setValue(mName, validationLookup.getValidationLookupName());
        AdminUtils.setValue(mParent1Field, validationLookup.getParent1Field());
        AdminUtils.setValue(mParent2Field, validationLookup.getParent2Field());
    }

    private wxTextCtrl mName;
    private wxTextCtrl mParent1Field;
    private wxTextCtrl mParent2Field;
}
