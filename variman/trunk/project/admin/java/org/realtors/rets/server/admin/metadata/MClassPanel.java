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

import org.realtors.rets.server.admin.AdminUtils;
import org.realtors.rets.server.admin.TwoColumnGridSizer;
import org.realtors.rets.server.metadata.MClass;

public class MClassPanel extends AbstractSubPanel
{
    public MClassPanel(wxWindow parent)
    {
        super(parent);
    }

    protected void addContent(TwoColumnGridSizer grid)
    {
        mClassName = new wxTextCtrl(this, -1);
        grid.addRow("Class Name:", mClassName, wxEXPAND);

        mStandardName = new wxTextCtrl(this, -1);
        grid.addRow("Standard Name:", mStandardName, wxEXPAND);

        mVisibleName = new wxTextCtrl(this, -1);
        grid.addRow("Visible Name:", mVisibleName, wxEXPAND);
    }

    public void updateFrom(MClass clazz)
    {
        mClassName.SetValue(clazz.getClassName());
        AdminUtils.setValue(mStandardName, clazz.getStandardName());
        mVisibleName.SetValue(clazz.getVisibleName());
    }

    private wxTextCtrl mClassName;
    private wxTextCtrl mStandardName;
    private wxTextCtrl mVisibleName;
}
