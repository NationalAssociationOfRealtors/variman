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
import org.realtors.rets.server.metadata.SearchHelp;

public class SearchHelpPanel extends AbstractSubPanel
{
    public SearchHelpPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mSearchHelpId = new wxTextCtrl(this, -1);
        grid.addRow("Search Help ID:", mSearchHelpId, wxEXPAND);

        mValue = new wxTextCtrl(this, -1);
        grid.addRow("Value:", mValue, wxEXPAND);
    }

    public void setModel(SearchHelp searchHelp)
    {
        AdminUtils.setValue(mSearchHelpId, searchHelp.getSearchHelpID());
        AdminUtils.setValue(mValue, searchHelp.getValue());
    }

    private wxTextCtrl mSearchHelpId;
    private wxTextCtrl mValue;
}
