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
import org.realtors.rets.server.metadata.ValidationExternal;

public class ValidationExternalPanel extends AbstractSubPanel
{
    public ValidationExternalPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mName = new wxTextCtrl(this, -1);
        grid.addRow("Name:", mName, wxEXPAND);

        mSearchResource = new wxTextCtrl(this, -1);
        grid.addRow("Search Resource:", mSearchResource, wxEXPAND);

        mSearchClass = new wxTextCtrl(this, -1);
        grid.addRow("Search Class:", mSearchClass, wxEXPAND);
    }

    public void setModel(ValidationExternal validationExternal)
    {
        AdminUtils.setValue(mName,
                            validationExternal.getValidationExternalName());
        AdminUtils.setValue(mSearchResource,
                            validationExternal.getSearchResource());
        AdminUtils.setValue(mSearchClass, validationExternal.getSearchClass());
    }

    private wxTextCtrl mName;
    private wxTextCtrl mSearchResource;
    private wxTextCtrl mSearchClass;
}
