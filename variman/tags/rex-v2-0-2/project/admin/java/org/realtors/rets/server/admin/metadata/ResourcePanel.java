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
import org.wxwindows.wxChoice;

import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ResourceStandardNameEnum;

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

        mStandardName = createStandardName();
        grid.addRow("Standard Name:", mStandardName);
    }

    private wxChoice createStandardName()
    {
        String[] standardNames = {
            "<none>",
            ResourceStandardNameEnum.ACTIVE_AGENT.toString(),
            ResourceStandardNameEnum.AGENT.toString(),
            ResourceStandardNameEnum.HISTORY.toString(),
            ResourceStandardNameEnum.OFFICE.toString(),
            ResourceStandardNameEnum.OPEN_HOUSE.toString(),
            ResourceStandardNameEnum.PROPERTY.toString(),
            ResourceStandardNameEnum.PROSPECT.toString(),
            ResourceStandardNameEnum.TAX.toString(),
            ResourceStandardNameEnum.TOUR.toString()
        };
        return new wxChoice(this, -1, wxDefaultPosition, wxDefaultSize,
                            standardNames);
    }

    public void populateFrom(Resource resource)
    {
        setValue(mResourceId, resource.getResourceID());
        setValue(mVisibleName, resource.getVisibleName());
        setValue(mDescription, resource.getDescription());
        setValue(mStandardName, resource.getStandardName());
    }

    private wxTextCtrl mResourceId;
    private wxTextCtrl mVisibleName;
    private wxTextCtrl mDescription;
    private wxChoice mStandardName;
}
