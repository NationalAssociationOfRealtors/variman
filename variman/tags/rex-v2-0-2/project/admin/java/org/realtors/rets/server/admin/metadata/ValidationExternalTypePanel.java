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
import org.realtors.rets.server.metadata.ValidationExternalType;

public class ValidationExternalTypePanel extends AbstractSubPanel
{
    public ValidationExternalTypePanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mSearchField = new wxTextCtrl(this, -1);
        grid.addRow("Search Field:", mSearchField, wxEXPAND);

        mDisplayField = new wxTextCtrl(this, -1);
        grid.addRow("Display Field", mDisplayField, wxEXPAND);

        // Todo: ValidationExternalTypePanel.addContent ResultFields
    }

    public void setModel(ValidationExternalType validationExternalType)
    {
        AdminUtils.setValue(mSearchField,
                            validationExternalType.getSearchField());
        AdminUtils.setValue(mDisplayField,
                            validationExternalType.getDisplayField());
    }

    private wxTextCtrl mSearchField;
    private wxTextCtrl mDisplayField;
}
