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

import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.Resource;

public class ResourcePanel extends AbstractSubPanel
{
    public ResourcePanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mResourceId = new wxTextCtrl(this, -1);
        grid.addRow("Resource ID:", mResourceId, wxEXPAND);

        mVisibleName = new wxTextCtrl(this, -1);
        grid.addRow("Visible Name:", mVisibleName, wxEXPAND);

        mDescription = new wxTextCtrl(this, -1);
        grid.addRow("Description:", mDescription, wxEXPAND);

        mStandardName = new wxTextCtrl(this, -1);
        grid.addRow("Standard Name:", mStandardName, wxEXPAND);
    }

    public void populateFrom(Resource resource)
    {
        mResourceId.SetValue(resource.getResourceID());
        mVisibleName.SetValue(resource.getVisibleName());
        mDescription.SetValue(resource.getDescription());
        mStandardName.SetValue(resource.getStandardName().toString());
    }

    private wxTextCtrl mResourceId;
    private wxTextCtrl mVisibleName;
    private wxTextCtrl mDescription;
    private wxTextCtrl mStandardName;
}
