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
import org.realtors.rets.server.metadata.LookupType;

public class LookupTypePanel extends AbstractSubPanel
{
    public LookupTypePanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mShortValue = new wxTextCtrl(this, -1);
        grid.addRow("Short Value:", mShortValue, wxEXPAND);

        mLongValue = new wxTextCtrl(this, -1);
        grid.addRow("Long Value:", mLongValue, wxEXPAND);

        mValue = new wxTextCtrl(this, -1);
        grid.addRow("Value:", mValue, wxEXPAND);
    }

    public void setModel(LookupType lookupType)
    {
        AdminUtils.setValue(mShortValue, lookupType.getShortValue());
        AdminUtils.setValue(mLongValue, lookupType.getLongValue());
        AdminUtils.setValue(mValue, lookupType.getValue());
    }

    private wxTextCtrl mShortValue;
    private wxTextCtrl mLongValue;
    private wxTextCtrl mValue;
}
