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
import org.realtors.rets.server.metadata.MObject;

public class MObjectPanel extends AbstractSubPanel
{
    public MObjectPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mObjectType = new wxTextCtrl(this, -1);
        grid.addRow("Object Type:", mObjectType, wxEXPAND);

        mMimeType = new wxTextCtrl(this, -1);
        grid.addRow("MIME Type:", mMimeType, wxEXPAND);

        mVisibleName = new wxTextCtrl(this, -1);
        grid.addRow("Visible Name:", mVisibleName, wxEXPAND);

        mDescription = new wxTextCtrl(this, -1);
        grid.addRow("Description:", mDescription, wxEXPAND);
    }

    public void setModel(MObject object)
    {
        AdminUtils.setValue(mObjectType, object.getObjectType());
        AdminUtils.setValue(mMimeType, object.getMimeType());
        AdminUtils.setValue(mVisibleName, object.getVisibleName());
        AdminUtils.setValue(mDescription, object.getDescription());
    }

    private wxTextCtrl mObjectType;
    private wxTextCtrl mMimeType;
    private wxTextCtrl mVisibleName;
    private wxTextCtrl mDescription;
}
