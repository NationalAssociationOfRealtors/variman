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
import org.realtors.rets.server.metadata.Lookup;

public class LookupPanel extends AbstractSubPanel
{
    public LookupPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mLookupName = new wxTextCtrl(this, -1);
        grid.addRow("Lookup Name:", mLookupName, wxEXPAND);

        mVisibleName = new wxTextCtrl(this, -1);
        grid.addRow("Visible Name:", mVisibleName, wxEXPAND);
    }

    public void setModel(Lookup lookup)
    {
        AdminUtils.setValue(mLookupName, lookup.getLookupName());
        AdminUtils.setValue(mVisibleName, lookup.getVisibleName());
    }

    private wxTextCtrl mLookupName;
    private wxTextCtrl mVisibleName;
}
